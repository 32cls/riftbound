package org.acme;

import com.meilisearch.sdk.Client;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.inject.spi.CDI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class CardReferenceClientLookupTest {
    @Test
    void meiliClientResolvesViaCdi() {
        Client client = CDI.current().select(Client.class).get();
        assertNotNull(client);
    }
}
