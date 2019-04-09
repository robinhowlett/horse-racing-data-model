package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.robinhowlett.data.Breed.isTBOrArabian;

/**
 * Parses and stores the race type (e.g. "STAKES"), short code (e.g. "STK"), name (e.g. "Kentucky
 * Derby"), grade (e.g. 1), black type (e.g. "Grade 1"), and for what {@link Breed}
 */
@JsonPropertyOrder({"breed", "type", "code", "name", "grade", "blackType"})
@EqualsAndHashCode
@ToString
public class RaceTypeNameBlackTypeBreed {

    public static final Map<String, String> RACE_TYPE_CODES;
    public static final String CAN = "CAN";

    static final String SPEED_INDEX_OPTIONAL_CLAIMING = "SPEED INDEX OPTIONAL CLAIMING";
    static final String ALLOWANCE_OPTIONAL_CLAIMING = "ALLOWANCE OPTIONAL CLAIMING";
    static final String CLAIMING_STAKES_TRIAL = "CLAIMING STAKES/TRIAL";
    static final String STARTER_ALLOWANCE = "STARTER ALLOWANCE";
    static final String UNKNOWN_RACE_TYPE = "UNKNOWN RACE TYPE";
    static final String MATCH_RACE = "MATCH RACE";
    static final String CANCELLED = "CANCELLED";
    static final String STAKES = "STAKES";
    static final String TRIALS = "TRIALS";
    static final String TRIAL = "TRIAL";
    static final String CLT = "CLT";
    static final String MCH = "MCH";
    static final String STA = "STA";
    static final String STK = "STK";
    static final String UNK = "UNK";

    static {
        Map<String, String> raceCodes = new LinkedHashMap<>();
        raceCodes.put(SPEED_INDEX_OPTIONAL_CLAIMING, "AOC");
        raceCodes.put("INVITATIONAL HANDICAP STAKES", "IHS");
        raceCodes.put(ALLOWANCE_OPTIONAL_CLAIMING, "AOC");
        raceCodes.put("OPTIONAL CLAIMING HANDICAP", "OCH");
        raceCodes.put("STARTER OPTIONAL CLAIMING", "SOC");
        raceCodes.put("MAIDEN OPTIONAL CLAIMING", "MOC");
        raceCodes.put("MAIDEN STARTER ALLOWANCE", "MSA");
        raceCodes.put("OPTIONAL CLAIMING STAKES", "OCS");
        raceCodes.put("SPEED INDEX CONSOLATION", "SPC");
        raceCodes.put("WAIVER MAIDEN CLAIMING", "WMC");
        raceCodes.put("CLAIMING STAKES TRIAL", "CLT");
        raceCodes.put(CLAIMING_STAKES_TRIAL, CLT);
        raceCodes.put("INVITATIONAL HANDICAP", "INH");
        raceCodes.put("MAIDEN SPECIAL WEIGHT", "MSW");
        raceCodes.put("FUTURITY CONSOLATION", "FCN");
        raceCodes.put("MATURITY CONSOLATION", "MCN");
        raceCodes.put("INVITATIONAL STAKES", "INS");
        raceCodes.put("STAKES CONSOLATION", "SCN");
        raceCodes.put("CLAIMING HANDICAP", "CLH");
        raceCodes.put("DERBY CONSOLATION", "DCN");
        raceCodes.put("OPTIONAL CLAIMING", "OCL");
        raceCodes.put("SPEED INDEX FINAL", "SPF");
        raceCodes.put("SPEED INDEX TRIAL", "SPT");
        raceCodes.put(STARTER_ALLOWANCE, STA);
        raceCodes.put(UNKNOWN_RACE_TYPE, UNK);
        raceCodes.put("SPEED INDEX RACE", "SPI");
        raceCodes.put("STARTER HANDICAP", "SHP");
        raceCodes.put("ALLOWANCE FINAL", "ALWFL");
        raceCodes.put("ALLOWANCE TRIAL", "AWT");
        raceCodes.put("CLAIMING STAKES", "CST");
        raceCodes.put("HANDICAP STAKES", "HDS");
        raceCodes.put("MAIDEN CLAIMING", "MCL");
        raceCodes.put("WAIVER CLAIMING", "WCL");
        raceCodes.put("CANCELLED RACE", "CAN");
        raceCodes.put("FUTURITY FINAL", "FUTFL");
        raceCodes.put("FUTURITY TRIAL", "FTR");
        raceCodes.put("MATURITY FINAL", "MATFL");
        raceCodes.put("MATURITY TRIAL", "MTR");
        raceCodes.put("STARTER STAKES", "SST");
        raceCodes.put("MAIDEN STAKES", "MST");
        raceCodes.put("CHAMPIONSHIP", "CHM");
        raceCodes.put("INVITATIONAL", "INV");
        raceCodes.put("MAIDEN TRIAL", "MDT");
        raceCodes.put("STAKES TRIAL", "STR");
        raceCodes.put("CONSOLATION", "CON");
        raceCodes.put("DERBY FINAL", "DBYFL");
        raceCodes.put("DERBY TRIAL", "DTR");
        raceCodes.put("EXHIBITION", "EXH");
        raceCodes.put(MATCH_RACE, MCH);
        raceCodes.put("ALLOWANCE", "ALW");
        raceCodes.put(CANCELLED, CAN);
        raceCodes.put("CLAIMING", "CLM");
        raceCodes.put("FUTURITY", "FUT");
        raceCodes.put("HANDICAP", "HCP");
        raceCodes.put("MATURITY", "MAT");
        raceCodes.put("MAIDEN", "MDN");
        raceCodes.put(STAKES, STK);
        raceCodes.put(TRIALS, "TRL");
        raceCodes.put("DERBY", "DBY");
        raceCodes.put("FINAL", "FNL");
        raceCodes.put("MATCH", "MCH");
        raceCodes.put("STAKE", "STK");
        raceCodes.put(TRIAL, "TRL");
        RACE_TYPE_CODES = Collections.unmodifiableMap(raceCodes);
    }

    @Getter
    private final String type;
    @JsonInclude(NON_NULL)
    @Getter
    private final String code;
    @Getter
    private final String name;
    @Getter
    private final Integer grade;
    @Getter
    private final String blackType;
    @Getter
    private final Breed breed;

    public RaceTypeNameBlackTypeBreed(String type, Breed breed) {
        this(type, null, breed);
    }

    public RaceTypeNameBlackTypeBreed(String type, String name, Breed breed) {
        this(type, name, null, breed);
    }

    public RaceTypeNameBlackTypeBreed(String type, String name, String blackType, Breed breed) {
        this(type, name, null, blackType, breed);
    }

    public RaceTypeNameBlackTypeBreed(String type, String name, Integer grade, String blackType,
            Breed breed) {
        this(type,
                // code
                RACE_TYPE_CODES.getOrDefault(type, null),
                name, grade, blackType, breed);
    }

    @JsonCreator
    public RaceTypeNameBlackTypeBreed(String type, String code, String name, Integer grade,
            String blackType, Breed breed) {
        this.type = type;
        this.code = code;
        this.name = name;
        this.grade = grade;
        this.blackType = blackType;
        this.breed = breed;
    }

    public static Map.Entry<String, String> typeFromCode(String code, Breed breed) {
        if (code.equals("AOC")) {
            return isTBOrArabian(breed) ?
                    new AbstractMap.SimpleEntry(ALLOWANCE_OPTIONAL_CLAIMING, code) :
                    new AbstractMap.SimpleEntry(SPEED_INDEX_OPTIONAL_CLAIMING, code);
        } else if (code.equals("TRL")) {
            return isTBOrArabian(breed) ?
                    new AbstractMap.SimpleEntry(TRIAL, code) :
                    new AbstractMap.SimpleEntry(TRIALS, code);
        } else if ((code.equals("MAT") && isTBOrArabian(breed)) || code.equals("MCH")) {
            return new AbstractMap.SimpleEntry(MATCH_RACE, MCH);
        } else if (code.equals("STR") && isTBOrArabian(breed)) {
            return new AbstractMap.SimpleEntry(STARTER_ALLOWANCE, STA);
        } else if (code.equals("CAN") || code.equals("ZCH")) {
            return new AbstractMap.SimpleEntry(CANCELLED, CAN);
        } else if (code.equals("STK")) {
            return new AbstractMap.SimpleEntry(STAKES, STK);
        } else if (code.equals("CLT")) {
            return new AbstractMap.SimpleEntry(STAKES, STK);
        } else {
            return RACE_TYPE_CODES.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(code))
                    .findFirst()
                    .orElse(new AbstractMap.SimpleEntry(UNKNOWN_RACE_TYPE, UNK));
        }
    }
}
