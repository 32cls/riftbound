package org.acme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaDto {
    @JsonProperty("image_url")
    public String imageUrl;
}
