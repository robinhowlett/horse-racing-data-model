package com.robinhowlett.data;

import lombok.Data;

/**
 * Parses and stores the wind speed (in miles per hour e.g. 5) and direction (e.g. "Cross")
 */
@Data
public class WindSpeedDirection {
    private final Integer speed;
    private final String direction;
}
