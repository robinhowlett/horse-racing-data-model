package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Parses and stores the {@link Starter}s who were disqualified and given a new finishing position
 */
@Data
public class Disqualification {

    private final String program;
    @JsonIgnoreProperties({"color", "sex", "sire", "dam", "damSire", "foalingDate",
            "foalingLocation", "breeder"})
    private final Horse horse;
    private final int originalPosition;
    private final int newPosition;

    public Disqualification(int newPosition) {
        this(null, null, 0, newPosition);
    }

    public Disqualification(String program, Horse horse, int originalPosition,
            int newPosition) {
        this.program = (program != null ? program.toUpperCase() : null);
        this.horse = horse;
        this.originalPosition = originalPosition;
        this.newPosition = newPosition;
    }
}
