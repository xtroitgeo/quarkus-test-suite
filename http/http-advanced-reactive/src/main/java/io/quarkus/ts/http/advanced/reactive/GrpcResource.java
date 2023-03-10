package io.quarkus.ts.http.advanced.reactive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import io.grpc.reflection.v1.MutinyServerReflectionGrpc;
import io.grpc.reflection.v1.ServerReflectionRequest;
import io.grpc.reflection.v1.ServerReflectionResponse;
import io.grpc.reflection.v1.ServiceResponse;
import io.quarkus.example.Greeter;
import io.quarkus.example.HelloReply;
import io.quarkus.example.HelloRequest;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("/grpc")
public class GrpcResource {

    @Inject
    @GrpcClient("hello")
    Greeter client;

    @Inject
    @GrpcClient("reflection-service")
    MutinyServerReflectionGrpc.MutinyServerReflectionStub reflection;

    @GET
    @Path("/reflection/service/info")
    @Produces(MediaType.APPLICATION_JSON)
    public GrpcReflectionResponse reflectionServiceInfo() {
        ServerReflectionRequest request = ServerReflectionRequest.newBuilder().setHost("localhost")
                .setListServices("").build();
        Uni<ServerReflectionResponse> response = invoke(request);

        Uni<Integer> reflectionServiceCount = response.map(item -> item.getListServicesResponse().getServiceCount());
        //  int reflectionServiceCount = response.getListServicesResponse().getServiceCount();
        Uni<List<String>> reflectionServiceList = getReflectionServiceList(response);

        return new GrpcReflectionResponse(reflectionServiceCount, reflectionServiceList);
    }

    @GET
    @Path("/reflection/descriptor/{protoFilename}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<byte[]> reflectionFileDescriptor(@PathParam("protoFilename") String protoFilename) {
        ServerReflectionRequest request = ServerReflectionRequest.newBuilder()
                .setHost("localhost").setFileByFilename(protoFilename + ".proto").build();

        Uni<ServerReflectionResponse> response = invoke(request);
        return response.map(item -> item.getFileDescriptorResponse().toByteArray());
    }

    private Uni<ServerReflectionResponse> invoke(ServerReflectionRequest request) {
        return reflection.serverReflectionInfo(Multi.createFrom().item(request))
                .collect().first();
    }


    private Uni<List<String>> getReflectionServiceList(Uni<ServerReflectionResponse> response) {
        Uni<List<ServiceResponse>> serviceList = response.map(item -> item.getListServicesResponse().getServiceList());

        List<String> serviceNames = new ArrayList<>();
//        for (var service : serviceList) {
//            serviceNames.add(service.getName());
//        }
        serviceList.map(list -> list.forEach(item -> serviceNames.add(item.getName()))); // tady je problem

        return serviceNames;
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello(@PathParam("name") String name) {
        return client.sayHello(HelloRequest.newBuilder().setName(name).build()).onItem().transform(HelloReply::getMessage);
    }

}
