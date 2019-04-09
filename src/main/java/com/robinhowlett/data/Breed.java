package com.robinhowlett.data;

import com.robinhowlett.exceptions.DataModelException;

import lombok.Getter;
import lombok.ToString;

/**
 * Enum for Thoroughbred (TB), Quarter Horse (QH), Arabian (AR), and Mixed (MX; usually a race
 * containing both TB and QH)
 */
@ToString
public enum Breed {
    THOROUGHBRED("Thoroughbred", "TB"),
    QUARTER_HORSE("Quarter Horse", "QH"),
    ARABIAN("Arabian", "ARAB"),
    MIXED("Mixed", "MIX");

    @Getter
    private final String chartValue;
    @Getter
    private final String code;

    Breed(String chartValue, String code) {
        this.chartValue = chartValue;
        this.code = code;
    }

    // forChartValue("Thoroughbred") returns Breed.THOROUGHBRED
    public static Breed forChartValue(String text) throws NoMatchingBreedException {
        for (Breed breed : values()) {
            if (breed.getChartValue().equals(text)) {
                return breed;
            }
        }
        throw new NoMatchingBreedException(text);
    }

    // forCode("TB") returns Breed.THOROUGHBRED
    public static Breed forCode(String text) throws NoMatchingBreedException {
        for (Breed breed : values()) {
            if (breed.getCode().startsWith(text)) {
                return breed;
            }
        }
        throw new NoMatchingBreedException(text);
    }

    public static boolean isBreed(String text) {
        for (Breed breed : values()) {
            if (breed.getChartValue().equals(text)) {
                return true;
            }
        }
        return false;
    }

    public static class NoMatchingBreedException extends DataModelException {
        public NoMatchingBreedException(String message) {
            super(String.format("Did not match a breed for %s", message));
        }
    }

    public static boolean isTBOrArabian(Breed breed) {
        return breed.equals(Breed.THOROUGHBRED) || breed.equals(Breed.ARABIAN);
    }
}
