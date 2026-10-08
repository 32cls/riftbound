package org.acme.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardSetDto {
    @JsonProperty("set_id")
    public String setId;
}
