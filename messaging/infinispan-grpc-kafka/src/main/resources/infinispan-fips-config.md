```bash
git clone git@github.com:gtroitsk/quarkus-test-suite.git
cd quarkus-test-suite/messaging/infinispan-grpc-kafka/
git switch infinispan-pkcs12-fips
export JAVA_HOME="/qa/tools/opt/x86_64/openjdk17_last/"
export PATH="/qa/tools/opt/x86_64/openjdk17_last/bin:/opt/apache-maven-3.8.6/bin:/home/hudson/.vscode-server/bin/e2816fe719a4026ffa1ee0189dc89bdfdbafb164/bin/remote-cli:/opt/apache-maven-3.8.6/bin:/opt/apache-maven-3.8.6/bin:/home/hudson/.local/bin:/home/hudson/bin:/opt/apache-maven-3.8.6/bin:/opt/apache-maven-3.8.6/bin:/usr/local/bin:/usr/bin:/usr/local/sbin:/usr/sbin"
sed 's/ts.infinispan.log.enable=false/ts.infinispan.log.enable=true/' -i src/test/resources/test.properties
sed 's/ts.app.log.enable=false/ts.app.log.enable=true/' -i src/test/resources/test.properties
mvn clean verify -Dit.test=InfinispanKafkaSaslIT
```

You must see exception:

08:16:26,485 INFO  [app] 08:16:26,266 [Consumer clientId=consumer-test-consumer-1, groupId=test-consumer] Error connecting to node localhost:32942 (id: -1 rack: null): java.io.IOException: Channel could not be created for socket java.nio.channels.SocketChannel[closed]
08:16:26,485 INFO  [app]        at org.apache.kafka.common.network.Selector.buildAndAttachKafkaChannel(Selector.java:348)
08:16:26,485 INFO  [app]        at org.apache.kafka.common.network.Selector.registerChannel(Selector.java:329)
08:16:26,485 INFO  [app]        at org.apache.kafka.common.network.Selector.connect(Selector.java:256)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.NetworkClient.initiateConnect(NetworkClient.java:1032)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.NetworkClient.access$600(NetworkClient.java:73)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.NetworkClient$DefaultMetadataUpdater.maybeUpdate(NetworkClient.java:1203)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.NetworkClient$DefaultMetadataUpdater.maybeUpdate(NetworkClient.java:1091)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.NetworkClient.poll(NetworkClient.java:569)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:280)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:251)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:242)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.awaitMetadataUpdate(ConsumerNetworkClient.java:164)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.consumer.internals.AbstractCoordinator.ensureCoordinatorReady(AbstractCoordinator.java:277)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.consumer.internals.AbstractCoordinator.ensureCoordinatorReady(AbstractCoordinator.java:240)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.consumer.internals.ConsumerCoordinator.coordinatorUnknownAndUnreadySync(ConsumerCoordinator.java:499)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.consumer.internals.ConsumerCoordinator.poll(ConsumerCoordinator.java:531)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.consumer.KafkaConsumer.updateAssignmentMetadataIfNeeded(KafkaConsumer.java:1288)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1247)
08:16:26,485 INFO  [app]        at org.apache.kafka.clients.consumer.KafkaConsumer.poll(KafkaConsumer.java:1227)
08:16:26,485 INFO  [app]        at io.quarkus.ts.messaging.infinispan.grpc.kafka.quickstart.KafkaEndpoint.lambda$initialize$1(KafkaEndpoint.java:29)
08:16:26,485 INFO  [app]        at java.base/java.lang.Thread.run(Thread.java:833)
08:16:26,485 INFO  [app] Caused by: org.apache.kafka.common.KafkaException: org.apache.kafka.common.errors.SaslAuthenticationException: Failed to configure SaslClientAuthenticator
08:16:26,485 INFO  [app]        at org.apache.kafka.common.network.SaslChannelBuilder.buildChannel(SaslChannelBuilder.java:239)
08:16:26,486 INFO  [app]        at org.apache.kafka.common.network.Selector.buildAndAttachKafkaChannel(Selector.java:338)
08:16:26,486 INFO  [app]        ... 20 more
08:16:26,486 INFO  [app] Caused by: org.apache.kafka.common.errors.SaslAuthenticationException: Failed to configure SaslClientAuthenticator
08:16:26,486 INFO  [app] Caused by: org.apache.kafka.common.errors.SaslAuthenticationException: Failed to create SaslClient with mechanism PLAIN
08:16:26,486 INFO  [app] 08:16:26,266 [Consumer clientId=consumer-test-consumer-1, groupId=test-consumer] Bootstrap broker localhost:32942 (id: -1 rack: null) disconnected
08:16:47,918 INFO  [app] Service stopped (Quarkus JVM mode)


