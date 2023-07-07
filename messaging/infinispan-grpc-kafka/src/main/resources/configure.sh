#!/usr/bin/env bash


function generate-certificate() {
  echo "OPENSSL VERSION: $(openssl version)"
  openssl req -new -newkey rsa:4096 -days 365 -nodes -x509 \
      -subj "/C=BR/ST=DF/L=Brasilia/O=Red Hat/CN=www.redhat.com" \
      -keyout mykey.pem -out cert.pem
}

function generate-keystore() {
  openssl pkcs12 -export -aes256 -out keystore.p12 -inkey mykey.pem -in cert.pem -nodes -passout pass: -certpbe AES-256-CBC -keypbe AES-256-CBC
}

function set-keystore-infinispan-server() {
  sed 's/<security-realm name="default">/<security-realm name="default"><server-identities><ssl><keystore path="/keystore.p12" relative-to="infinispan.server.config.path" password="" \/><\/ssl><\/server-identities>/' -i ./resources/infinispan-config.xml
}

function generate-truststore() {
  keytool -import -alias 1 -file cert.pem -keystore truststore.p12 -storetype PKCS12 -storepass password
}

function delete_certificate() {
    rm cert.pem mykey.pem
}

generate-certificate
generate-keystore
generate-truststore
