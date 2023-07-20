package io.quarkus.ts.security.oidcclient.mtls;

import static io.quarkus.ts.security.oidcclient.mtls.MutualTlsKeycloakService.KC_DEV_MODE_PKCS12_FIPS_CMD;
import static io.quarkus.ts.security.oidcclient.mtls.MutualTlsKeycloakService.newKeycloakInstance;

import io.quarkus.test.bootstrap.KeycloakService;
import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.services.KeycloakContainer;
import io.quarkus.test.services.QuarkusApplication;

@QuarkusScenario
public class Pkcs12FipsOidcMtlsIT extends KeycloakMtlsAuthN {

    @KeycloakContainer(image = "quay.io/keycloak/keycloak:latest", command = KC_DEV_MODE_PKCS12_FIPS_CMD, port = KEYCLOAK_PORT)
    static KeycloakService keycloak = newKeycloakInstance(REALM_FILE_PATH, REALM_DEFAULT, "realms")
            .withProperty("HTTPS_KEYSTORE",
                    "resource_with_destination::/etc/|server-keystore." + PKCS12_FIPS_KEYSTORE_FILE_EXTENSION)
            .withProperty("HTTPS_TRUSTSTORE",
                    "resource_with_destination::/etc/|server-truststore." + PKCS12_FIPS_KEYSTORE_FILE_EXTENSION);

    /**
     * Keystore file type is automatically detected by file extension by quarkus-oidc.
     */
    @QuarkusApplication
    static RestService app = createRestService(PKCS12_FIPS_KEYSTORE_FILE_TYPE, PKCS12_FIPS_KEYSTORE_FILE_EXTENSION,
            keycloak::getRealmUrl)
            .withProperty("quarkus.oidc.tls.trust-store-file", "client-truststore." + PKCS12_FIPS_KEYSTORE_FILE_EXTENSION)
            .withProperty("quarkus.oidc.tls.key-store-file", "client-keystore." + PKCS12_FIPS_KEYSTORE_FILE_EXTENSION);

    @Override
    protected KeycloakService getKeycloakService() {
        return keycloak;
    }

    @Override
    protected String getKeystoreFileExtension() {
        return PKCS12_FIPS_KEYSTORE_FILE_EXTENSION;
    }
}
