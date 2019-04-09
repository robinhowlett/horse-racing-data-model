package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Parses the race conditions text to determine, if possible, the age, gender, and breeding location
 * restrictions imposed for the race
 */
@EqualsAndHashCode
@ToString
public class RaceRestrictions {

    public static final int ALL_SEXES = 31;
    public static final Map<Integer, String> SEXES_CODES;

    static {
        Map<Integer, String> sexesCodes = new LinkedHashMap<>();
        sexesCodes.put(0, null);
        sexesCodes.put(1, "C");
        sexesCodes.put(2, "G");
        sexesCodes.put(3, "C&G");
        sexesCodes.put(4, "H");
        sexesCodes.put(5, "C&H");
        sexesCodes.put(6, "G&H");
        sexesCodes.put(7, "C&G&H");
        sexesCodes.put(8, "F");
        sexesCodes.put(9, "C&F");
        sexesCodes.put(10, "G&F");
        sexesCodes.put(11, "C&G&F");
        sexesCodes.put(12, "F&H");
        sexesCodes.put(13, "C&H&F");
        sexesCodes.put(14, "G&H&F");
        sexesCodes.put(15, "C&G&H&F");
        sexesCodes.put(16, "M");
        sexesCodes.put(17, "C&M");
        sexesCodes.put(18, "G&M");
        sexesCodes.put(19, "C&G&M");
        sexesCodes.put(20, "H&M");
        sexesCodes.put(21, "C&H&M");
        sexesCodes.put(22, "G&H&M");
        sexesCodes.put(23, "C&G&H&M");
        sexesCodes.put(24, "F&M");
        sexesCodes.put(25, "C&F&M");
        sexesCodes.put(26, "G&F&M");
        sexesCodes.put(27, "C&G&F&M");
        sexesCodes.put(28, "H&F&M");
        sexesCodes.put(29, "C&H&F&M");
        sexesCodes.put(30, "G&H&F&M");
        sexesCodes.put(ALL_SEXES, "A");
        SEXES_CODES = Collections.unmodifiableMap(sexesCodes);
    }

    @JsonInclude(NON_NULL)
    @Getter
    private final String code;
    @Getter
    private final Integer minAge;
    @Getter
    private final Integer maxAge;
    @Getter
    private final String ageCode;

    /*
     a bitwise-style value to store the gender restrictions that apply

     1 = colts
     2 = geldings
     4 = horses
     8 = fillies
     16 = mares
     31 = all
      */
    @Getter
    private final int sexes;
    @Getter
    private final String sexesCode;
    @Getter
    private final boolean femaleOnly;
    @Getter
    private final boolean stateBred;

    public RaceRestrictions(RaceRestrictionCodes raceRestrictionCodes, Integer minAge,
            Integer maxAge, int sexes) {
        this((raceRestrictionCodes != null ? raceRestrictionCodes.getCode() : null),
                minAge, maxAge, sexes,
                (raceRestrictionCodes != null && raceRestrictionCodes.isStateBred()));
    }

    public RaceRestrictions(String code, Integer minAge, Integer maxAge, int sexes, boolean
            stateBred) {
        this(code, minAge,
                // maxAge: if no specified max, then assume same as the minimum
                (maxAge != null ? maxAge : minAge),
                // ageCode
                createAgeCode(minAge, (maxAge != null ? maxAge : minAge)),
                sexes,
                // sexesCode
                SEXES_CODES.getOrDefault(sexes, null),
                // femaleOnly: 8 = fillies, 16 = mares, 24 = fillies & mares
                (sexes % 8 == 0),
                stateBred);
    }

    @JsonCreator
    public RaceRestrictions(String code, Integer minAge, Integer maxAge, String ageCode, int
            sexes, String sexesCode, boolean femaleOnly, boolean stateBred) {
        this.code = code;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.ageCode = ageCode;
        this.sexes = sexes;
        this.sexesCode = sexesCode;
        this.femaleOnly = femaleOnly;
        this.stateBred = stateBred;
    }

    static String createAgeCode(Integer minAge, Integer maxAge) {
        if (minAge != null) {
            if (minAge == maxAge) {
                return String.valueOf(minAge);
            } else if (maxAge == -1) {
                return String.valueOf(minAge) + "+";
            } else {
                return String.valueOf(minAge) + "-" + String.valueOf(maxAge);
            }
        } else {
            return null;
        }
    }

    @Data
    public static class RaceRestrictionCodes {
        private final String code;
        private final boolean stateBred;
    }

    @Data
    public static class AgeSexPattern {
        private final int sexOffset;
        private final int ageOffset;
        private final Pattern pattern;
    }
}
