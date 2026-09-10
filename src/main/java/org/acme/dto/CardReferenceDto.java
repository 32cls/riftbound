package org.acme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardReferenceDto {

    @JsonProperty("riftbound_id")
    public String riftboundId;
    @JsonProperty("new")
    public boolean isNew;
    public String name;
    public CardSetDto set;
    public MediaDto media;

    @Override
    public String toString() {
	    return this.riftboundId + " " + this.name + " " + this.set + " " + this.media + " " + this.isNew;
    }

}
