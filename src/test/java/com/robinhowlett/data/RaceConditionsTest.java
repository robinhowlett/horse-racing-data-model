package com.robinhowlett.data;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.robinhowlett.data.RaceRestrictions.ALL_SEXES;
import static com.robinhowlett.data.RaceTypeNameBlackTypeBreed.RACE_TYPE_CODES;

import static org.hamcrest.CoreMatchers.equalTo;

public class RaceConditionsTest {

    @Test
    public void isClaimingRace_WithAllRaceTypes_IdentifiesClaimingRaceCountCorrectly() {
        Assert.assertThat(
                RACE_TYPE_CODES.keySet().stream()
                        .filter(RaceConditions::isClaimingRace)
                        .collect(Collectors.toList()),
                equalTo(Arrays.asList(
                        "SPEED INDEX OPTIONAL CLAIMING",
                        "ALLOWANCE OPTIONAL CLAIMING",
                        "OPTIONAL CLAIMING HANDICAP",
                        "STARTER OPTIONAL CLAIMING",
                        "MAIDEN OPTIONAL CLAIMING",
                        "OPTIONAL CLAIMING STAKES",
                        "WAIVER MAIDEN CLAIMING",
                        "CLAIMING STAKES TRIAL",
                        "CLAIMING STAKES/TRIAL",
                        "CLAIMING HANDICAP",
                        "OPTIONAL CLAIMING",
                        "CLAIMING STAKES",
                        "MAIDEN CLAIMING",
                        "WAIVER CLAIMING",
                        "CLAIMING"
                )));
    }

    @Test
    public void buildAgeSexesSummary_WithDefinedAgeRangeAndSexesValue_BuildsSimpleSummary() {
        Assert.assertThat(
                RaceConditions.buildAgeSexesSummary(
                        new RaceRestrictions(null, 3, 4, 24)),
                equalTo("3-4 (F&M)"));
    }

    @Test
    public void buildAgeSexesSummary_WithUndefinedAgeRangeAndAllSexes_BuildsSimpleSummary() {
        Assert.assertThat(
                RaceConditions.buildAgeSexesSummary(
                        new RaceRestrictions("NW2 L", 3, -1, 31, true)),
                equalTo("3+"));
    }

    @Test
    public void buildStateBredSummary_WithStateBredRaces_AddsStateBredPrefix() {
        Assert.assertThat(
                RaceConditions.buildStateBredSummary(
                        "3+ (F&M)", new RaceRestrictions("NW2 L", 3, -1, 24, true)),
                equalTo("3+ (F&M) [S]"));
    }

    @Test
    public void buildStateBredSummary_WithNormalRace_DoesNotAddStateBredPrefix() {
        Assert.assertThat(
                RaceConditions.buildStateBredSummary(
                        "3", new RaceRestrictions(null, 3, 3, ALL_SEXES)),
                equalTo("3"));
    }

    @Test
    public void buildCodeSummary_WithGradedStakes_BuildsSummaryWithGradeSummary() {
        Assert.assertThat(
                RaceConditions.buildCodeSummary(
                        "", new RaceTypeNameBlackTypeBreed("STAKES", "Breeders' Cup Classic", 1,
                                "Grade 1", Breed.THOROUGHBRED)),
                equalTo("G1"));
    }

    @Test
    public void buildCodeSummary_WithNonGradedStakes_BuildsSummaryWithStakesSummary() {
        Assert.assertThat(
                RaceConditions.buildCodeSummary(
                        "", new RaceTypeNameBlackTypeBreed("STAKES", "Aspen S.", null,
                                "Black Type", Breed.THOROUGHBRED)),
                equalTo("STK"));
    }

    @Test
    public void buildPurseSummary_WithPurseValueOfOneThousand_BuildsSummaryWithOneDecimalPoint() {
        Assert.assertThat(
                RaceConditions.buildPurseSummary("ALW", new Purse(1000, null, null, null, null)),
                equalTo("ALW 1.0K"));
    }

    @Test
    public void buildPurseSummary_WithPurseValueOfTwentyThousand_BuildsBasicSummary() {
        Assert.assertThat(
                RaceConditions.buildPurseSummary("ALW", new Purse(20000, null, null, null, null)),
                equalTo("ALW 20K"));
    }

    @Test
    public void buildClaimingPriceSummary_WithHighClaimingPriceRange_BuildsSimpleSummary() {
        Assert.assertThat(
                RaceConditions.buildClaimingPriceSummary("CLM",
                        new RaceConditions.ClaimingPriceRange(90000, 100000)),
                equalTo("CLM 100-90K"));
    }

    @Test
    public void buildClaimingPriceSummary_WithLowClaimingPriceRange_BuildsSimpleSummary() {
        Assert.assertThat(
                RaceConditions.buildClaimingPriceSummary("CLM",
                        new RaceConditions.ClaimingPriceRange(2000, 2500)),
                equalTo("CLM 2.5-2.0K")); // max goes first
    }

    @Test
    public void buildClaimingPriceSummary_WithSingleClaimingPrice_BuildsSimpleSummary() {
        Assert.assertThat(
                RaceConditions.buildClaimingPriceSummary("CLM",
                        new RaceConditions.ClaimingPriceRange(10000, 10000)),
                equalTo("CLM 10K")); // max goes first
    }

    @Test
    public void buildRestrictionsCode_WithDefinedCode_AddsCodeAsSuffix() {
        Assert.assertThat(
                RaceConditions.buildRestrictionsCode("ALW 10K",
                        new RaceRestrictions("NW2 L", 3, -1, 31, true)),
                equalTo("ALW 10K (NW2 L)"));
    }

    @Test
    public void buildRestrictionsCode_WithoutCode_DoesNotAddCodeAsSuffix() {
        Assert.assertThat(
                RaceConditions.buildRestrictionsCode("CLM 5.5K",
                        new RaceRestrictions(null, 3, -1, 24)),
                equalTo("CLM 5.5K"));
    }

    @Test
    public void buildSummary() {
        Assert.assertThat(
                RaceConditions.buildSummary(
                        new RaceRestrictions("NW2 L", 3, -1, 3, true),
                        new RaceTypeNameBlackTypeBreed("CLAIMING", Breed.THOROUGHBRED),
                        new RaceConditions.ClaimingPriceRange(8500, 10000),
                        new Purse(20000, "$20,000", null, null, null)
                ),
                equalTo("3+ (C&G) [S] CLM 10-8.5K (NW2 L)"));
    }
}
