package io.quarkus.ts.http.restclient.reactive.faulttolerance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * Filter that propagates on all client calls the current requestId.
 */
@Provider
@ApplicationScoped
public class RequestIdClientRequestFilter implements ClientRequestFilter {

    private final RequestIdManager requestIdManager;

    public RequestIdClientRequestFilter(RequestIdManager requestIdManager) {
        this.requestIdManager = requestIdManager;
    }

    @Override
    public void filter(ClientRequestContext requestContext) {
        int requestId = requestIdManager.currentRequestId();
        requestContext.getHeaders().putSingle("REQUEST_ID", requestId);
    }
}
