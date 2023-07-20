Works only with Java 11 

```shell
keytool -keystore server-keystore.bcfks -alias localhost -validity 3000 -genkeypair -sigalg SHA512withRSA -keyalg RSA -keysize 2048 -storepass password -keypass password -storetype BCFKS -providername BCFIPS -providerpath providers/bc-fips-*.jar -providerclass org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider -provider org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider -dname "cn=Redhat, ou=QuarkusQE, o=Redhat,L=Madrid, st=MA, c=ES" -ext SAN="DNS:localhost,IP:127.0.0.1" -J-Djava.security.properties=/tmp/kc.keystore-create.java.security

openssl req -new -x509 -keyout ca-key -out ca-cert -days 3000

keytool -keystore client-truststore.bcfks -storetype BCFKS -alias CARoot -import -file ca-cert -providername BCFIPS -providerpath providers/bc-fips-*.jar -provider org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider -providerclass org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider

keytool -keystore server-truststore.bcfks -storetype BCFKS -alias CARoot -importcert -file ca-cert -providername BCFIPS -providerpath providers/bc-fips-*.jar -provider org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider -providerclass org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider

keytool -keystore server-keystore.bcfks -alias localhost -storepass password -keypass password -storetype BCFKS -providername BCFIPS -certreq -file cert-file -providerpath providers/bc-fips-*.jar -providerclass org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider -provider org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider -J-Djava.security.properties=/tmp/kc.keystore-create.java.security

openssl x509 -req -CA ca-cert -CAkey ca-key -in cert-file -out cert-signed -days 3000 -CAcreateserial -passin pass:password

keytool -keystore server-keystore.bcfks -storetype BCFKS -alias CARoot -import -file ca-cert -storepass password -providername BCFIPS -providerpath providers/bc-fips-*.jar -providerclass org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider -provider org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider -J-Djava.security.properties=/tmp/kc.keystore-create.java.security

cat ca-cert cert-signed > cert

keytool -keystore server-keystore.bcfks -storetype BCFKS -storepass password -alias localhost -import -file cert -providername BCFIPS -providerpath providers/bc-fips-*.jar -providerclass org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider -provider org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider -J-Djava.security.properties=/tmp/kc.keystore-create.java.security
```