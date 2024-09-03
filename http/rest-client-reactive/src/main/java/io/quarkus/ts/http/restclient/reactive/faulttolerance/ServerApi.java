package io.quarkus.ts.http.restclient.reactive.faulttolerance;

import java.util.concurrent.ExecutionException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
@Path("/server")
public class ServerApi {

    private final ClientResource clientResource;

    @Inject
    public ServerApi(@RestClient ClientResource clientResource) {
        this.clientResource = clientResource;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("async")
    public Response getItemsAsync() throws ExecutionException, InterruptedException {
        return clientResource.getItemsAsync()
                .toCompletableFuture()
                .get();
    }
}
