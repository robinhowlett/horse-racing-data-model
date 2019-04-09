package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Parses and stores the horse, trainer, and owner name(s) for {@link Starter}s that were claimed
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ClaimedHorse {

    @JsonIgnoreProperties({"color", "sex", "sire", "dam", "damSire", "foalingDate",
            "foalingLocation", "breeder"})
    private final Horse horse;
    private final String newTrainerName;
    private String newOwnerName;

    public ClaimedHorse(String newTrainerName, String newOwnerName) {
        this.horse = null;
        this.newTrainerName = newTrainerName;
        this.newOwnerName = newOwnerName;
    }
}
