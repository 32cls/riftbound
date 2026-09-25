package org.acme;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;

import io.quarkus.arc.Unremovable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ConfigBean {

    Client client;

    @ConfigProperty(name = "MEILI_MASTER_KEY")
    String masterKey;

    @ConfigProperty(name = "meili.endpoint")
    String meiliEndpoint;

    @Produces
    @Unremovable
    Client getMeiliClient() {
        if (this.client == null) {
            this.client = new Client(new Config(this.meiliEndpoint, this.masterKey));
        }
        return this.client;
    }

}
