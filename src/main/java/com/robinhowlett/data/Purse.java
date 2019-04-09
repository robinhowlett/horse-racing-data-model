package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Parses and stores the value amount, textual description, and any other additional information
 * specific to or enhancing the value
 */
@JsonPropertyOrder({"value", "text", "availableMoney", "enhancements", "valueOfRace"})
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Purse {

    @JsonIgnore
    public List<PurseEnhancement> enhancementsList = new ArrayList<>();
    @Getter
    @Setter
    private Integer value;
    @Getter
    @Setter
    private String text;
    @Getter
    @Setter
    private String availableMoney;
    @Getter
    @Setter
    private String valueOfRace;

    @JsonCreator
    public Purse(Integer value, String text, String availableMoney, String valueOfRace,
            List<PurseEnhancement> enhancementsList) {
        this.value = value;
        this.text = text;
        this.availableMoney = availableMoney;
        this.valueOfRace = valueOfRace;
        this.enhancementsList = enhancementsList;
    }

    @JsonProperty("enhancements")
    public String getEnhancements() {
        return (!enhancementsList.isEmpty() ? enhancementsList.stream()
                .map(purseEnhancement -> String.format("%s: %s",
                        purseEnhancement.getType().getChartValue(), purseEnhancement.getText()))
                .collect(Collectors.joining(", ")) : null);
    }

    public List<PurseEnhancement> getEnhancements(EnhancementType enhancementType) {
        return enhancementsList.stream()
                .filter(enhancement -> enhancement.getType().equals(enhancementType))
                .collect(Collectors.toList());
    }

    /**
     * Enum for types of value enhancementsList e.g. Plus or Includes
     */
    @ToString
    public enum EnhancementType {
        PLUS("Plus"),
        INCLUDES("Includes");

        @Getter
        private final String chartValue;

        EnhancementType(String chartValue) {
            this.chartValue = chartValue;
        }

        public static EnhancementType forChartValue(String chartValue) {
            for (EnhancementType enhancementType : values()) {
                if (enhancementType.getChartValue().equals(chartValue)) {
                    return enhancementType;
                }
            }
            return null; // throw an exception perhaps...
        }
    }

    /**
     * Stores the value {@link EnhancementType} and its textual description
     */
    @Data
    public static class PurseEnhancement {
        private final EnhancementType type;
        private final String text;
    }
}
