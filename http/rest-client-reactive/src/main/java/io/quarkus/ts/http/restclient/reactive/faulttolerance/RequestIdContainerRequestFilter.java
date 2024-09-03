package io.quarkus.ts.http.restclient.reactive.faulttolerance;

import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

/**
 * Filter that extracts the input requestID from the request and passes them along to the request scope data structure.
 */
@Provider
@Produces
@PreMatching
@ConstrainedTo(RuntimeType.SERVER)
public class RequestIdContainerRequestFilter implements ContainerRequestFilter {

    @Inject
    RequestIdManagerImpl requestIdManagerImpl;

    @Override
    @ActivateRequestContext
    public void filter(ContainerRequestContext requestContext) {
        MultivaluedMap<String, String> headers = requestContext.getHeaders();
        if (headers.containsKey("REQUEST_ID")) {
            int requestId = Integer.valueOf(headers.getFirst("REQUEST_ID"));
            requestIdManagerImpl.overrideRequestId(requestId);
        }
    }
}
