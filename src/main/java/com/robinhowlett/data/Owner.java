package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The owner of the {@link Starter}
 */
@ToString
@EqualsAndHashCode
public class Owner {

    @JsonIgnore
    @Getter
    private final String program;
    @Getter
    private final String name;

    public Owner(String name) {
        this(null, name);
    }

    public Owner(String program, String name) {
        this.program = (program != null ? program.toUpperCase() : null);
        this.name = name;
    }
}
