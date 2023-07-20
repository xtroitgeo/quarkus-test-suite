PKCS12 keystore and truststore generation
For FIPS compliance need to be generated with keytool util with OpenJDK 17 Red Hat build

1. Create a keystore for the client

keytool -genkey -alias Client -keyalg RSA -keystore client-keystore.pkcs12 -keysize 2048 -storetype PKCS12 -storepass password -dname "cn=localhost, ou=QuarkusQE, o=Redhat, L=Brno, st=BR, c=CZ" 

2. Export the public cert of the client

keytool -export -keystore client-keystore.pkcs12 -alias Client -file client.crt

3. Create a keystore for the server

keytool -genkey -alias Server -keyalg RSA -keystore server-keystore.pkcs12 -keysize 2048 -storetype PKCS12 -storepass password -dname "cn=localhost, ou=QuarkusQE, o=Redhat, L=Brno, st=BR, c=CZ" 

4. Export the public cert of the server

keytool -export -keystore server-keystore.pkcs12 -alias Server -file server.crt

5. Create a truststore for the client

keytool -genkey -alias ClientTrust -keyalg RSA -keystore client-truststore.pkcs12 -keysize 2048 -storetype PKCS12 -storepass password -dname "cn=localhost, ou=QuarkusQE, o=Redhat, L=Brno, st=BR, c=CZ" 

6. Create a truststore for the server

keytool -genkey -alias ServerTrust -keyalg RSA -keystore server-truststore.pkcs12 -keysize 2048 -storetype PKCS12 -storepass password -dname "cn=localhost, ou=QuarkusQE, o=Redhat, L=Brno, st=BR, c=CZ"

7. Import the client public cert into the server truststore

keytool -import -keystore server-truststore.pkcs12 -alias Client -file client.crt

8. Import the server public cert into the client truststore

keytool -import -keystore client-truststore.pkcs12 -alias Server -file server.crt

9. Delete the existing private key of the server truststore

keytool -delete -alias serverTrust -keystore server-truststore.pkcs12 -storepass password

10. Delete the existing private key of the client truststore

keytool -delete -alias clientTrust -keystore client-truststore.pkcs12 -storepass password


-----------------------------------------------
```shell
keytool -keystore server-keystore.pkcs12 -alias keycloak-pkcs12 -validity 3000 -genkeypair -sigalg SHA512withRSA -keyalg RSA -keysize 2048 -storepass password -keypass password -storetype PKCS12 -providername SunJCE -dname "cn=Redhat, ou=QuarkusQE, o=Redhat,L=Madrid, st=MA, c=ES" -ext SAN="DNS:keycloak-pkcs12,IP:127.0.0.1" 

openssl req -new -x509 -keyout ca-key -out ca-cert -days 3000

keytool -keystore client-truststore.pkcs12 -storetype PKCS12 -alias CARoot -import -file ca-cert -providername SunJCE -provider com.sun.crypto.provider.SunJCE -providerclass com.sun.crypto.provider.SunJCE

keytool -keystore server-truststore.pkcs12 -storetype PKCS12 -alias CARoot -importcert -file ca-cert -providername SunJCE  -provider com.sun.crypto.provider.SunJCE -providerclass com.sun.crypto.provider.SunJCE

keytool -keystore server-keystore.pkcs12 -alias keycloak-pkcs12 -storepass password -keypass password -storetype PKCS12 -providername SunJCE -certreq -file cert-file -provider com.sun.crypto.provider.SunJCE -providerclass com.sun.crypto.provider.SunJCE

openssl x509 -req -CA ca-cert -CAkey ca-key -in cert-file -out cert-signed -days 3000 -CAcreateserial -passin pass:password

keytool -keystore server-keystore.pkcs12 -storetype PKCS12 -alias CARoot -import -file ca-cert -storepass password -providername SunJCE -provider com.sun.crypto.provider.SunJCE -providerclass com.sun.crypto.provider.SunJCE 

cat ca-cert cert-signed > cert

keytool -keystore server-keystore.pkcs12 -storetype PKCS12 -storepass password -alias keycloak-pkcs12 -import -file cert -providername SunJCE -provider com.sun.crypto.provider.SunJCE -providerclass com.sun.crypto.provider.SunJCE
```