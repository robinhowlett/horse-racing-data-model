package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The owner of the {@link Starter}
 */
@JsonPropertyOrder({"name"})
@EqualsAndHashCode
@ToString
public class Trainer {

    @JsonIgnore
    @Getter
    private final String program;
    @Getter
    private final String name;
    @Getter
    private final String firstName;
    @Getter
    private final String lastName;

    public Trainer(String firstName, String lastName) {
        this(null, firstName, lastName);
    }

    public Trainer(String program, String firstName, String lastName) {
        this.program = (program != null ? program.toUpperCase() : null);
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = (firstName != null && !firstName.isEmpty()) ?
                (firstName + " " + lastName) : lastName;
    }
}
