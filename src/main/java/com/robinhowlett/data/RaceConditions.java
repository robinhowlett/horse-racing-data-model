package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.robinhowlett.data.RaceRestrictions.ALL_SEXES;

import static java.util.Locale.US;

/**
 * Parses and stores the textual description of the race conditions and, if applicable, the minimum
 * and maximum claiming prices that can be availed of
 */
@JsonPropertyOrder({"raceTypeNameBlackTypeBreed", "text", "restrictions", "purse",
        "claimingPriceRange", "summary"})
@EqualsAndHashCode
@ToString
public class RaceConditions {

    @Getter
    private final String text;
    @JsonInclude(NON_NULL)
    @Getter
    private final ClaimingPriceRange claimingPriceRange;
    @Getter
    private final RaceRestrictions restrictions;
    @JsonProperty("raceTypeNameBlackTypeBreed") // required for property order but unwrapped
    @JsonUnwrapped
    @Getter
    @Setter
    private RaceTypeNameBlackTypeBreed raceTypeNameBlackTypeBreed;
    @Getter
    @Setter
    private Purse purse;

    public RaceConditions(String text, ClaimingPriceRange claimingPriceRange,
            RaceRestrictions raceRestrictions) {
        this(text, claimingPriceRange, raceRestrictions, null, null);
    }

    @JsonCreator
    public RaceConditions(String text, ClaimingPriceRange claimingPriceRange,
            RaceRestrictions restrictions, RaceTypeNameBlackTypeBreed raceTypeNameBlackTypeBreed,
            Purse purse) {
        this.text = text;
        this.claimingPriceRange = claimingPriceRange;
        this.restrictions = restrictions;
        this.raceTypeNameBlackTypeBreed = raceTypeNameBlackTypeBreed;
        this.purse = purse;
    }

    @JsonProperty("summary")
    public String getSummary() {
        return buildSummary(restrictions, raceTypeNameBlackTypeBreed, claimingPriceRange, purse);
    }

    static String buildSummary(RaceRestrictions restrictions,
            RaceTypeNameBlackTypeBreed raceTypeNameBlackTypeBreed,
            ClaimingPriceRange claimingPriceRange, Purse purse) {
        String code = buildAgeSexesSummary(restrictions);

        code = buildStateBredSummary(code, restrictions);

        boolean claimingRace = false;
        if (raceTypeNameBlackTypeBreed != null) {
            claimingRace = isClaimingRace(raceTypeNameBlackTypeBreed.getType());
            code = buildCodeSummary(code, raceTypeNameBlackTypeBreed);
        }

        // for Claiming Races, use the claiming price range rather than the purse
        if (claimingRace) {
            code = buildClaimingPriceSummary(code, claimingPriceRange);
        } else {
            code = buildPurseSummary(code, purse);
        }

        code = buildRestrictionsCode(code, restrictions);

        return (!code.isEmpty() ? code : null);
    }

    static String buildStateBredSummary(String code, RaceRestrictions restrictions) {
        if (restrictions != null && restrictions.isStateBred()) {
            if (!code.isEmpty()) {
                code = code.concat(" ").concat("[S]");
            } else {
                code = "[S]";
            }
        }
        return code;
    }

    static String buildAgeSexesSummary(RaceRestrictions restrictions) {
        String code = "";
        if (restrictions != null) {
            if (restrictions.getAgeCode() != null) {
                code = restrictions.getAgeCode();
            }
            if (restrictions.getSexesCode() != null && restrictions.getSexes() != ALL_SEXES) {
                code = (!code.isEmpty() ?
                        code.concat(" (").concat(restrictions.getSexesCode()) :
                        code.concat("(").concat(restrictions.getSexesCode())).concat(")");
            }
        }
        return code;
    }

    static String buildCodeSummary(String code,
            RaceTypeNameBlackTypeBreed raceTypeNameBlackTypeBreed) {
        Integer grade = raceTypeNameBlackTypeBreed.getGrade();
        if (grade != null) {
            String gradeCode = "G".concat(String.valueOf(grade));
            if (!code.isEmpty()) {
                return code.concat(" ").concat(gradeCode);
            } else {
                return gradeCode;
            }
        } else {
            // e.g. ALW, CLM, AOC
            String raceCode = raceTypeNameBlackTypeBreed.getCode();
            if (raceCode != null) {
                if (!code.isEmpty()) {
                    return code.concat(" ").concat(raceCode);
                } else {
                    return raceCode;
                }
            } else {
                return code;
            }
        }
    }

    public static boolean isClaimingRace(String type) {
        return (type != null) && (type.contains("CLAIM"));
    }

    static String buildClaimingPriceSummary(String code, ClaimingPriceRange claimingPriceRange) {
        if (claimingPriceRange != null) {
            Integer max = claimingPriceRange.getMax();
            Integer min = claimingPriceRange.getMin();
            if ((max != null) && (max > 0)) {
                String shortClaim;
                // for claiming prices less than 10000, use a single decimal point
                if (max >= 10000) {
                    // e.g. CLM 50K
                    shortClaim = String.valueOf(max / 1000);
                } else {
                    // e.g. MCL 5.5K
                    shortClaim = String.format("%.1f", max / (double) 1000);
                }

                // for claiming races with a range, format it as "max-minK", else "priceK"
                if ((min != null) && (Integer.compare(min, max) != 0) && (min > 0)) {
                    if (min >= 10000) {
                        // e.g. CLM 50-45K
                        shortClaim = shortClaim.concat("-")
                                .concat(String.valueOf(min / 1000));
                    } else {
                        // e.g. CLM 5.5-4.5K
                        shortClaim = shortClaim.concat("-")
                                .concat(String.format("%.1f", min / (double) 1000));
                    }
                }

                code = code.concat(" ").concat(shortClaim).concat("K");
            }
        }
        return code;
    }

    static String buildPurseSummary(String code, Purse purse) {
        if (purse != null && purse.getValue() != null) {
            String shortPurse;
            if (purse.getValue() >= 10000) {
                shortPurse = String.valueOf(purse.getValue() / 1000);
            } else {
                shortPurse = String.format("%.1f", purse.getValue() / (double) 1000);
            }
            code = code.concat(" ").concat(shortPurse).concat("K");
        }
        return code;
    }

    static String buildRestrictionsCode(String code, RaceRestrictions restrictions) {
        if ((restrictions != null) && (restrictions.getCode() != null)) {
            code = code.concat(" (").concat(restrictions.getCode()).concat(")");
        }
        return code;
    }

    /**
     * Stores the range of the claiming prices that apply to a claiming race. Some claiming races
     * allow setting the claim within a particular range, others give weight allowances for lower
     * claim prices, while others use a single value (meaning the minimum and maximum are the same)
     */
    @Data
    public static class ClaimingPriceRange {
        private static final Logger LOGGER = LoggerFactory.getLogger(ClaimingPriceRange.class);
        private static final Pattern CLAIMING_PRICE_PATTERN =
                Pattern.compile("Claiming Price: " +
                        "\\$([0-9]{1,3}(,[0-9]{3})*)( - \\$([0-9]{1,3}(,[0-9]{3})*))?$");

        private final int min;
        private final int max;

        public static ClaimingPriceRange parse(String raceConditions) {
            if (raceConditions == null || raceConditions.isEmpty()) {
                return null;
            }

            Integer maxClaim = null, minClaim = null;
            Matcher matcher = CLAIMING_PRICE_PATTERN.matcher(raceConditions);
            if (matcher.find()) {
                String maxClaimAmount = matcher.group(1);
                if (maxClaimAmount != null) {
                    try {
                        maxClaim =
                                NumberFormat.getNumberInstance(US).parse(maxClaimAmount).intValue();
                    } catch (ParseException e) {
                        LOGGER.error(String.format("Unable to parse a max claim price value from " +
                                "text: %s", maxClaimAmount), e);
                    }
                }

                String minClaimAmount = matcher.group(4);
                if (minClaimAmount != null) {
                    try {
                        minClaim =
                                NumberFormat.getNumberInstance(US).parse(minClaimAmount).intValue();
                    } catch (ParseException e) {
                        LOGGER.error(String.format("Unable to parse a min claim price value from " +
                                "text: %s", minClaimAmount), e);
                    }
                } else if (maxClaim != null) {
                    minClaim = maxClaim;
                }

                // integrity check
                if (minClaim != null && maxClaim != null) {
                    if (minClaim > maxClaim) {
                        int holder = maxClaim;
                        minClaim = holder;
                        maxClaim = minClaim;
                    }
                }

                return new ClaimingPriceRange(minClaim, maxClaim);
            }

            return null;
        }
    }
}
