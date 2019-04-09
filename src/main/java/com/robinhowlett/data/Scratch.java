package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Parses and stores the horse(s) scratched from the race and, if available, for what reason. If the
 * scratch still earned part of the purse for simply entering, that value is not stored.
 */
@Data
public class Scratch {

    @JsonIgnoreProperties({"color", "sex", "sire", "dam", "damSire", "foalingDate",
            "foalingLocation", "breeder"})
    private final Horse horse;
    private final String reason;
}
