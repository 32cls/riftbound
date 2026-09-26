package org.acme;

import org.locationtech.jts.geom.Point;

import jakarta.validation.constraints.NotBlank;

public class LocationInput {
    @NotBlank
    public Point location;

}
