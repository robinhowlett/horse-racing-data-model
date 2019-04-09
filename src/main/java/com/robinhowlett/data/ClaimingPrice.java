package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Parses and stores the program number, horse name, and claiming price for each {@link Starter} who
 * was available to be claimed
 */
@ToString
@EqualsAndHashCode
public class ClaimingPrice {

    @Getter
    private final String program;
    @JsonIgnoreProperties({"color", "sex", "sire", "dam", "damSire", "foalingDate",
            "foalingLocation", "breeder"})
    @Getter
    private final Horse horse;
    @Getter
    private final int claimingPrice;

    public ClaimingPrice(int claimingPrice) {
        this(null, null, claimingPrice);
    }

    @JsonCreator
    public ClaimingPrice(String program, Horse horse, int claimingPrice) {
        this.program = (program != null ? program.toUpperCase() : null);
        this.horse = horse;
        this.claimingPrice = claimingPrice;
    }
}
