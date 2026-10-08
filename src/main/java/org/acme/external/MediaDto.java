package org.acme.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaDto {
    @JsonProperty("image_url")
    public String imageUrl;
}
