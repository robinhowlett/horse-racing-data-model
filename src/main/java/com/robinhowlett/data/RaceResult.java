package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.robinhowlett.data.FractionalPoint.Fractional;
import com.robinhowlett.data.FractionalPoint.Split;
import com.robinhowlett.data.PointsOfCall.PointOfCall;
import com.robinhowlett.data.PointsOfCall.PointOfCall.RelativePosition;
import com.robinhowlett.data.PointsOfCall.PointOfCall.RelativePosition.LengthsAhead;
import com.robinhowlett.data.PointsOfCall.PointOfCall.RelativePosition.TotalLengthsBehind;
import com.robinhowlett.data.wagering.WagerPayoffPools;
import com.robinhowlett.data.wagering.WagerPayoffPools.WinPlaceShowPayoffPool;
import com.robinhowlett.data.wagering.WagerPayoffPools.WinPlaceShowPayoffPool.WinPlaceShowPayoff;

import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.robinhowlett.data.Race.convertToMonthDayYear;
import static com.robinhowlett.data.Race.is2016ParxOaksDebacle;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Represents a single race result on a chart.
 */
@JsonPropertyOrder({"links", "cancellation", "raceDate", "track", "raceNumber", "conditions",
        "distanceSurfaceTrackRecord", "weather", "postTimeStartCommentsTimer", "deadHeat",
        "numberOfRunners", "finalTime", "finalMillis", "winningMargin", "starters", "scratches",
        "wagering", "fractionals", "splits", "ratings", "footnotes"})
@EqualsAndHashCode
@ToString
public class RaceResult implements Race {

    @JsonInclude(NON_EMPTY)
    @Getter
    private final List<Link> links;
    @JsonProperty("cancellation") // required for property order but unwrapped
    @JsonUnwrapped
    @Getter
    private final Cancellation cancellation;
    @Getter
    private final LocalDate raceDate;
    @Getter
    private final Track track;
    @Getter
    private final Integer raceNumber;
    @JsonProperty("conditions")
    @Getter
    private final RaceConditions raceConditions;
    @JsonProperty("distanceSurfaceTrackRecord") // required for property order but unwrapped
    @JsonUnwrapped
//    @Getter
    private final DistanceSurfaceTrackRecord distanceSurfaceTrackRecord;
    @Getter
    private final Weather weather;
    @JsonProperty("postTimeStartCommentsTimer") // required for property order but unwrapped
    @JsonUnwrapped
    @Getter
    private final PostTimeStartCommentsTimer postTimeStartCommentsTimer;
    @Getter
    private final boolean deadHeat;
    @Getter
    private final List<Starter> starters;
    @Getter
    private final List<Scratch> scratches;
    @Getter
    private final List<Fractional> fractionals;
    @Getter
    private final List<Split> splits;
    @JsonProperty("wagering")
    @Getter
    private final WagerPayoffPools wagerPayoffPools;
    @Getter
    private final String footnotes;
    @Getter
    @Setter
    private List<Rating> ratings;

    protected RaceResult(Builder builder) {
        this.cancellation = (builder.cancellation != null ? builder.cancellation :
                Cancellation.notCancelled());
        this.raceDate = builder.raceDate;
        this.track = builder.track;
        this.raceNumber = builder.raceNumber;
        this.raceConditions = builder.raceConditions;
        this.distanceSurfaceTrackRecord = builder.distanceSurfaceTrackRecord;
        this.postTimeStartCommentsTimer = builder.postTimeStartCommentsTimer;
        this.deadHeat = builder.deadHeat;
        this.starters = builder.starters;
        this.fractionals = builder.fractionals;
        this.splits = builder.splits;
        this.scratches = builder.scratches;
        this.wagerPayoffPools = builder.wagerPayoffPools;
        this.footnotes = builder.footnotes;

        if (builder.weatherTrackCondition != null) {
            if (distanceSurfaceTrackRecord != null &&
                    builder.weatherTrackCondition.getTrackCondition() != null) {
                distanceSurfaceTrackRecord.setTrackCondition(
                        builder.weatherTrackCondition.getTrackCondition());
            }

            weather = new Weather(builder.weatherTrackCondition.getWeather(),
                    builder.windSpeedDirection);
        } else {
            if (builder.windSpeedDirection != null) {
                weather = new Weather(null, builder.windSpeedDirection);
            } else {
                weather = null;
            }
        }

        if (distanceSurfaceTrackRecord != null &&
                distanceSurfaceTrackRecord.getRaceDistance() != null &&
                builder.runUpTemporaryRail != null) {
            distanceSurfaceTrackRecord.getRaceDistance()
                    .setRunUp(builder.runUpTemporaryRail.getRunUp());
            distanceSurfaceTrackRecord.getRaceDistance()
                    .setTempRail(builder.runUpTemporaryRail.getTempRail());
        }

        if (raceConditions != null && builder.raceTypeNameBlackTypeBreed != null) {
            raceConditions.setRaceTypeNameBlackTypeBreed(builder.raceTypeNameBlackTypeBreed);
        }

        if (raceConditions != null && builder.purse != null) {
            raceConditions.setPurse(builder.purse);
        }

        ratings = new ArrayList<>();

        links = buildLinks(track, raceDate, raceNumber);
    }

    public RaceResult(Cancellation cancellation, LocalDate raceDate, Track track,
            Integer raceNumber) {
        this(cancellation, raceDate, track, raceNumber, null, null, null, null, false, null,
                null, null, null, null, null, null);
    }

    @JsonCreator
    public RaceResult(Cancellation cancellation, LocalDate raceDate, Track track,
            Integer raceNumber, RaceConditions raceConditions,
            DistanceSurfaceTrackRecord distanceSurfaceTrackRecord, Weather weather,
            PostTimeStartCommentsTimer postTimeStartCommentsTimer, boolean deadHeat,
            List<Starter> starters, List<Scratch> scratches, List<Fractional> fractionals,
            List<Split> splits, WagerPayoffPools wagerPayoffPools, String footnotes,
            List<Rating> ratings) {
        this.cancellation = cancellation;
        this.raceDate = raceDate;
        this.track = track;
        this.raceNumber = raceNumber;
        this.raceConditions = raceConditions;
        this.distanceSurfaceTrackRecord = distanceSurfaceTrackRecord;
        this.weather = weather;
        this.postTimeStartCommentsTimer = postTimeStartCommentsTimer;
        this.deadHeat = deadHeat;
        this.starters = starters;
        this.scratches = scratches;
        this.fractionals = fractionals;
        this.splits = splits;
        this.wagerPayoffPools = wagerPayoffPools;
        this.footnotes = footnotes;
        this.ratings = ratings;
        this.links = buildLinks(track, raceDate, raceNumber);
    }

    public static List<Link> buildLinks(Track track, LocalDate raceDate, Integer raceNumber) {
        List<Link> links = new ArrayList<>();

        if (track != null && raceDate != null) {
            String raceDateMDY = convertToMonthDayYear(raceDate);
            String singleChartEmbedded =
                    String.format("https://www.equibase.com/premium/chartEmb.cfm?" +
                                    "track=%s&raceDate=%s&cy=%s&rn=%s", track.getCode(),
                            raceDateMDY,
                            track.getCountry(), raceNumber);
            Link embeddedChart = new Link(singleChartEmbedded, "web");

            String singeChartDirect =
                    String.format("https://www.equibase.com/premium/eqbPDFChartPlus.cfm?" +
                                    "RACE=%d&BorP=P&TID=%s&CTRY=%s&DT=%s&DAY=D&STYLE=EQB",
                            raceNumber, track.getCode(), track.getCountry(), raceDateMDY);
            Link directChart = new Link(singeChartDirect, "pdf");

            String raceDayEmbedded =
                    String.format("https://www.equibase.com/premium/chartEmb.cfm?" +
                                    "track=%s&raceDate=%s&cy=%s", track.getCode(), raceDateMDY,
                            track.getCountry());
            Link embeddedRaceDay = new Link(raceDayEmbedded, "allWeb");

            String raceDayDirect =
                    String.format("https://www.equibase.com/premium/eqbPDFChartPlus.cfm?" +
                                    "RACE=A&BorP=P&TID=%s&CTRY=%s&DT=%s&DAY=D&STYLE=EQB",
                            track.getCode(), track.getCountry(), raceDateMDY);
            Link directRaceDay = new Link(raceDayDirect, "allPdf");

            links.add(embeddedChart);
            links.add(directChart);
            links.add(embeddedRaceDay);
            links.add(directRaceDay);
        }

        return links;
    }

    @JsonIgnore
    public Optional<Link> getLink(String rel) {
        return getLinks().stream()
                .filter(link -> link.getRel() != null &&
                        link.getRel().equalsIgnoreCase(rel)
                ).findAny();
    }

    public int getNumberOfRunners() {
        return (starters != null ? starters.size() : 0);
    }

    @JsonIgnore
    public List<Starter> getWinners() {
        if (starters != null) {
            return starters.stream().filter(Starter::isWinner).collect(toList());
        }
        return new ArrayList<>();
    }

    @JsonIgnore
    public List<Starter> firstFinishers() {
        if (starters != null) {
            // may have passed the post first but been disqualified
            return starters.stream().filter(Starter::finishedFirst).collect(toList());
        }
        return new ArrayList<>();
    }

    @JsonProperty("finalTime")
    public String getFinalTime() {
        List<Starter> winners = firstFinishers();
        if (winners != null && !winners.isEmpty()) {
            Fractional finishFractional = winners.get(0).getFinishFractional();
            return (finishFractional != null ? finishFractional.getTime() : null);
        }
        return null;
    }

    @JsonProperty("finalMillis")
    public Long getFinalMillis() {
        List<Starter> winners = firstFinishers();
        if (winners != null && !winners.isEmpty()) {
            Fractional finishFractional = winners.get(0).getFinishFractional();
            return (finishFractional != null ? finishFractional.getMillis() : null);
        }
        return null;
    }

    @JsonIgnore
    public String simpleSummary() {
        return String.format("%s %s R%d", getTrack().getCode(), getRaceDate(), getRaceNumber());
    }

    /**
     * Builder pattern used to construct the {@link RaceResult}
     */
    public static class Builder<T extends Builder<T>> {
        private Cancellation cancellation;
        private LocalDate raceDate;
        private Track track;
        private Integer raceNumber;
        private RaceTypeNameBlackTypeBreed raceTypeNameBlackTypeBreed;
        private RaceConditions raceConditions;
        private Purse purse;
        private DistanceSurfaceTrackRecord distanceSurfaceTrackRecord;
        private RunUpTemporaryRail runUpTemporaryRail;
        private WeatherTrackCondition weatherTrackCondition;
        private PostTimeStartCommentsTimer postTimeStartCommentsTimer;
        private WindSpeedDirection windSpeedDirection;
        private List<Fractional> fractionals;
        private List<Split> splits;
        private List<Scratch> scratches;
        private boolean deadHeat;
        private WagerPayoffPools wagerPayoffPools;
        private List<Starter> starters;
        private String footnotes;

        public Builder cancellation(final Cancellation cancellation) {
            this.cancellation = cancellation;
            return this;
        }

        public Builder track(final Track track) {
            this.track = track;
            return this;
        }

        public Builder raceDate(final LocalDate raceDate) {
            this.raceDate = raceDate;
            return this;
        }

        public Builder raceNumber(final Integer raceNumber) {
            this.raceNumber = raceNumber;
            return this;
        }

        public Builder raceTypeAndRaceNameAndBlackTypeAndBreed(
                final RaceTypeNameBlackTypeBreed raceTypeNameBlackTypeBreed) {
            this.raceTypeNameBlackTypeBreed = raceTypeNameBlackTypeBreed;
            return this;
        }

        public Builder runUpTemporaryRail(final RunUpTemporaryRail runUpTemporaryRail) {
            this.runUpTemporaryRail = runUpTemporaryRail;
            return this;
        }

        public Builder fractionals(final List<Fractional> fractionals) {
            this.splits = Split.calculateSplitsFromFractionals(fractionals);
            this.fractionals = fractionals;
            return this;
        }

        public Builder distanceAndSurfaceAndTrackRecord(
                final DistanceSurfaceTrackRecord distanceSurfaceTrackRecord) {
            this.distanceSurfaceTrackRecord = distanceSurfaceTrackRecord;
            return this;
        }

        public Builder starters(List<Starter> starters) {
            this.starters = starters;
            return this;
        }

        public Builder raceConditionsAndClaimingPricesRange(
                final RaceConditions raceConditions) {
            this.raceConditions = raceConditions;
            return this;
        }

        public Builder purse(final Purse purse) {
            this.purse = purse;
            return this;
        }

        public Builder windSpeedAndDirection(final WindSpeedDirection windSpeedDirection) {
            this.windSpeedDirection = windSpeedDirection;
            return this;
        }

        public Builder weatherAndTrackCondition(final WeatherTrackCondition weatherTrackCondition) {
            this.weatherTrackCondition = weatherTrackCondition;
            return this;
        }

        public Builder postTimeAndStartCommentsAndTimer(
                final PostTimeStartCommentsTimer postTimeStartCommentsTimer) {
            this.postTimeStartCommentsTimer = postTimeStartCommentsTimer;
            return this;
        }

        public Builder scratches(final List<Scratch> scratches) {
            this.scratches = scratches;
            return this;
        }

        public Builder wagerPoolsAndPayoffs(final WagerPayoffPools wagerPayoffPools) {
            this.wagerPayoffPools = wagerPayoffPools;
            return this;
        }

        public Builder footnotes(final String footnotes) {
            this.footnotes = footnotes;
            return this;
        }

        // for looking up suitable point of calls when building a Starter
        public DistanceSurfaceTrackRecord getDistanceSurfaceTrackRecord() {
            return distanceSurfaceTrackRecord;
        }

        // useful for looking up daysSince for a Starter being built
        public LocalDate getRaceDate() {
            return raceDate;
        }

        // for looking up suitable point of calls when building a Starter
        public RaceTypeNameBlackTypeBreed getRaceTypeNameBlackTypeBreed() {
            return raceTypeNameBlackTypeBreed;
        }

        public RaceResult build() {
            markCoupledAndFieldEntries(starters);

            updateStartersWithWinPlaceShowPayoffs(starters, wagerPayoffPools);

            calculateFractionalsAndSplits(starters, fractionals);

            updateStartersWithOddsChoiceIndicies(starters);

            markPositionDeadHeats(starters);

            // whether the race resulted in a dead heat
            // ignore the idiotic 2016 Parx Oaks co-winner decision
            if (!is2016ParxOaksDebacle(track, raceDate, raceNumber)) {
                deadHeat = detectDeadHeat(starters);
            }

            return new RaceResult(this);
        }

        List<Starter> markPositionDeadHeats(List<Starter> starters) {
            if (starters != null && !starters.isEmpty()) {
                starters.stream()
                        // did finish the race
                        .filter(starter -> starter.getFinishPosition() != null)
                        .collect(groupingBy(Starter::getFinishPosition))
                        .entrySet().stream()
                        .filter(entry -> entry.getValue().size() > 1)
                        .flatMap(entry -> entry.getValue().stream())
                        .forEach(starter -> starter.setPositionDeadHeat(true));
            }

            return starters;
        }

        List<Starter> markCoupledAndFieldEntries(List<Starter> starters) {
            if (starters != null) {
                starters.stream()
                        .collect(groupingBy(Starter::getEntryProgram))
                        .entrySet().stream()
                        .filter(entry -> entry.getValue().size() > 1)
                        .flatMap(entry -> entry.getValue().stream())
                        .filter(starter -> !starter.isEntry())
                        .forEach(starter -> starter.setEntry(true));
            }
            return starters;
        }

        // adds the win, show, and place payoffs to the applicable Starters for easier lookups,
        // also handling coupled/field entries
        List<Starter> updateStartersWithWinPlaceShowPayoffs(List<Starter> starters,
                WagerPayoffPools wagerPayoffPools) {
            if (wagerPayoffPools != null && starters != null) {
                WinPlaceShowPayoffPool payoffPools = wagerPayoffPools.getWinPlaceShowPayoffPools();
                if (payoffPools != null) {
                    List<WinPlaceShowPayoff> winPlaceShowPayoffs =
                            payoffPools.getWinPlaceShowPayoffs();

                    // group Win-Place-Show payoffs by their entry program number
                    Map<Optional<String>, List<WinPlaceShowPayoff>> wpsPayoffsByEntry =
                            winPlaceShowPayoffs.stream()
                                    .collect(groupingBy(
                                            wpsPayoff -> ofNullable(wpsPayoff.getEntryProgram())));

                    // for each unique coupled program number
                    for (Optional<String> entryProgram : wpsPayoffsByEntry.keySet()) {
                        List<WinPlaceShowPayoff> wpsPayoffsForEntry =
                                wpsPayoffsByEntry.get(entryProgram);
                        if (wpsPayoffsForEntry != null) {
                            Optional<WinPlaceShowPayoff> payoff =
                                    wpsPayoffsForEntry.stream().findFirst();

                            // group starters by their entry program number
                            Map<Optional<String>, List<Starter>> startersByEntryProgram =
                                    starters.stream()
                                            .collect(groupingBy(starter ->
                                                    ofNullable(starter.getEntryProgram())));

                            // set the same WPS payoffs for all starters of a coupled/field
                            // entry
                            if (entryProgram.isPresent() && payoff.isPresent() &&
                                    startersByEntryProgram.containsKey(entryProgram)) {
                                startersByEntryProgram.get(entryProgram).stream().forEach(
                                        starter -> starter.setWinPlaceShowPayoff(payoff.get()));
                            } else {
                                // or set the WPS payoffs for the matching starter
                                for (Starter starter : starters) {
                                    if (payoff.isPresent() &&
                                            matchesEntryProgramOrHorseName(payoff.get(), starter)) {
                                        starter.setWinPlaceShowPayoff(payoff.get());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return starters;
        }

        boolean matchesEntryProgramOrHorseName(WinPlaceShowPayoff payoff, Starter starter) {
            return (payoff.getHorse() != null &&
                    payoff.getHorse().getName().equals(starter.getHorse().getName()));
        }

        /**
         * For Thoroughbred and Arabian races, combines the times of the leader at each fractional
         * point and the lengths ahead/behind at each point of call to calculate the (estimated)
         * individual fractional and splits for each {@link Starter}
         *
         * For Quarter Horse and Mixed races, each {@link Starter}'s fractionals and splits are
         * taken from the individual final time. The race's fractional and split times are taken
         * from the winner's individual final time.
         */
        List<Starter> calculateFractionalsAndSplits(List<Starter> starters,
                List<Fractional> raceFractionals) {
            if (starters != null && !starters.isEmpty()) {
                if (raceFractionals != null && !raceFractionals.isEmpty()) {
                    starters = calculateIndividualFractionalsFromRace(starters, raceFractionals);
                } else {
                    calculateRaceFractionalsFromWinner(starters, raceFractionals);
                }
            }

            return starters;
        }

        /**
         * No race fractionals were provided, use the winner's finish fractionals instead
         */
        List<Fractional> calculateRaceFractionalsFromWinner(List<Starter> starters,
                List<Fractional> raceFractionals) {
            Optional<Starter> first = starters.stream()
                    .filter(starter -> starter.getFinishPosition() == 1)
                    .findFirst();

            if (first.isPresent() && (first.get().getFinishFractional() != null)) {
                if (raceFractionals == null) {
                    raceFractionals = new ArrayList<>();
                }
                raceFractionals.addAll(first.get().getFractionals());
                this.fractionals(raceFractionals);
            }

            return raceFractionals;
        }

        List<Starter> calculateIndividualFractionalsFromRace(List<Starter> starters,
                List<Fractional> fractionals) {
            for (Starter starter : starters) {
                List<Fractional> individualFractionals = new ArrayList<>();
                for (Fractional fractional : fractionals) {
                    Optional<PointOfCall> pointOfCallOptional =
                            starter.getPointOfCall(fractional.getFeet());
                    if (pointOfCallOptional.isPresent()) {
                        PointOfCall pointOfCall = pointOfCallOptional.get();
                        Fractional individualFractional =
                                calculateIndividualFractionals(fractional, pointOfCall);

                        individualFractionals.add(individualFractional);
                    }
                }

                starter.setFractionals(individualFractionals);

                List<Split> splits = Split.calculateSplitsFromFractionals(individualFractionals);
                starter.setSplits(splits);
            }

            return starters;
        }

        Fractional calculateIndividualFractionals(Fractional fractional, PointOfCall pointOfCall) {
            RelativePosition relativePosition = pointOfCall.getRelativePosition();
            TotalLengthsBehind totalLengthsBehind = relativePosition.getTotalLengthsBehind();
            LengthsAhead lengthsAhead = relativePosition.getLengthsAhead();

            Double lengths;
            if (totalLengthsBehind != null) {
                lengths = totalLengthsBehind.getLengths();
            } else if (lengthsAhead != null) {
                lengths = 0.0;
            } else {
                lengths = null;
            }

            Long individualMillis = null;
            String time = null;
            if (lengths != null) {
                Long fractionalMillis = fractional.getMillis();
                if (fractionalMillis != null) {
                    double feetPerMillisecond =
                            ((double) fractional.getFeet() / fractionalMillis);
                    double feetBehind = (lengths * 8.75); // 8.75 feet (estimation for a
                    // "length")
                    double additionalMillis = (feetBehind / feetPerMillisecond);
                    individualMillis = (long) (fractionalMillis + additionalMillis);
                    time = FractionalPoint.convertToTime(individualMillis);
                }
            }

            return new Fractional(fractional.getPoint(), fractional.getText(),
                    fractional.getCompact(), fractional.getFeet(), time, individualMillis);
        }

        List<Starter> updateStartersWithOddsChoiceIndicies(List<Starter> starters) {
            if (starters != null) {
                List<Double> odds = new ArrayList<>();

                // for each Starter that has an Odds value, add it to the odds List
                starters.stream()
                        .filter(starter -> (starter.getOdds() != null))
                        .forEach(starter -> odds.add(starter.getOdds()));

                // sort the odds (ascending)
                odds.sort(Comparator.comparingDouble(Double::doubleValue));

                // remove duplicates from the odds list by replacing them with nulls
                List<Double> truncatedOdds = new ArrayList<>();
                for (Double choice : odds) {
                    if (truncatedOdds.contains(choice)) {
                        truncatedOdds.add(null);
                    } else {
                        truncatedOdds.add(choice);
                    }
                }

                // update each starter that has an Odds value with the 1-based choice index
                // e.g. the favorite is 1, the third favorite is 3, the tenth favorite is 10
                starters.stream()
                        .filter(starter -> (starter.getOdds() != null))
                        .forEach(starter -> {
                            int choiceIndex = odds.indexOf(starter.getOdds());
                            if (choiceIndex > -1) {
                                starter.setChoice(choiceIndex + 1); // 1-based
                            }
                        });
            }
            return starters;
        }

        boolean detectDeadHeat(List<Starter> starters) {
            long count = 0;
            if (starters != null) {
                count = starters.stream()
                        .filter(starter -> {
                            Integer officialPosition = starter.getOfficialPosition();
                            return (officialPosition != null ? officialPosition == 1 : false);
                        }).count();
            }

            return count > 1;
        }

        public String summaryText() {
            return String.format("%s (%s), %s, Race %d (%s)", track.getCode(), track.getName(),
                    raceDate, raceNumber, (raceTypeNameBlackTypeBreed != null ?
                            raceTypeNameBlackTypeBreed.getBreed().getCode() : "Failed to parse"));
        }
    }

    @Data
    public static class Weather {
        private final String text;
        @JsonProperty("wind")
        @JsonInclude(NON_NULL)
        private final WindSpeedDirection windSpeedDirection;
    }
}
