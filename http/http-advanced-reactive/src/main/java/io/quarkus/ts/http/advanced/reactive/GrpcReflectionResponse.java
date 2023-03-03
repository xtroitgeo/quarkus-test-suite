package io.quarkus.ts.http.advanced.reactive;

import io.grpc.reflection.v1.ServerReflectionResponse;
import io.grpc.reflection.v1.ServiceResponse;

import java.util.ArrayList;
import java.util.List;

public final class GrpcReflectionResponse {

    private final ServerReflectionResponse response;

    public GrpcReflectionResponse(ServerReflectionResponse response) {
        this.response = response;
    }

    public List<String> getServiceList() {
        List<ServiceResponse> serviceList = response.getListServicesResponse().getServiceList();
        List<String> serviceNames = new ArrayList<>();
        for (var service : serviceList) {
            serviceNames.add(service.getName());
        }
        return serviceNames;
    }

    public int getServiceCount() {
        return response.getListServicesResponse().getServiceCount();
    }

    public String getFileDescriptor() {
        return response.getFileDescriptorResponse().toString();
    }

}
