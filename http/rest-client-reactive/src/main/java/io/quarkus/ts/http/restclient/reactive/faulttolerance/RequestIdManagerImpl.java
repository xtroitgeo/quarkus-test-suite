package io.quarkus.ts.http.restclient.reactive.faulttolerance;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class RequestIdManagerImpl implements RequestIdManager {

    private int requestID;

    public int currentRequestId() {
        return requestID;
    }

    public void overrideRequestId(int inboundRequestId) {
        this.requestID = inboundRequestId;
    }
}
