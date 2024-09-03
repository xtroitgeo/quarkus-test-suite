package io.quarkus.ts.http.restclient.reactive.faulttolerance;

public interface RequestIdManager {

    int currentRequestId();
}
