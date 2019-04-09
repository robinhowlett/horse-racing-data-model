package com.robinhowlett.data.running_line;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * Stores the weight carried by the horse (net) and the jockey allowance (due to apprenticeship)
 */
@JsonPropertyOrder({"carried", "jockeyAllowance"})
@Data
public class Weight {

    @JsonProperty("carried")
    private final int weightCarried;
    private final int jockeyAllowance;
}
