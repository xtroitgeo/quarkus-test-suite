package io.quarkus.ts.http.restclient.reactive.faulttolerance;

import java.util.concurrent.CompletionStage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@RegisterRestClient(configKey = "client.endpoint")
@Path("client")
public interface ClientResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Retry(retryOn = ProcessingException.class)
    @Path("async")
    CompletionStage<Response> getItemsAsync();
}
