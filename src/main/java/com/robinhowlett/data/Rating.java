package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Stores a rating related to a {@link RaceResult} or a {@link Starter} (e.g. speed figure,
 * class/power rating etc.)
 */
@Data
public class Rating {

    protected final String name;
    protected final String text;
    protected final Double value;
    @JsonInclude(NON_NULL)
    protected final String extra;

}
