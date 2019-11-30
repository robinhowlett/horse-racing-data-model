package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.robinhowlett.data.FractionalPoint.Fractional;
import com.robinhowlett.data.FractionalPoint.Split;
import com.robinhowlett.data.PointsOfCall.PointOfCall;
import com.robinhowlett.data.PointsOfCall.PointOfCall.RelativePosition;
import com.robinhowlett.data.PointsOfCall.PointOfCall.RelativePosition.TotalLengthsBehind;
import com.robinhowlett.data.running_line.LastRaced;
import com.robinhowlett.data.running_line.MedicationEquipment;
import com.robinhowlett.data.running_line.Odds;
import com.robinhowlett.data.running_line.Weight;
import com.robinhowlett.data.wagering.WagerPayoffPools.WinPlaceShowPayoffPool.WinPlaceShowPayoff;
import com.robinhowlett.exceptions.DataModelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Everything to do with a participant in a {@link RaceResult}
 */
@JsonPropertyOrder({"lastRaced", "program", "entry", "entryProgram", "horse", "jockey",
        "trainer", "owner", "weight", "medicationEquipment", "claim", "postPosition",
        "finishPosition", "officialPosition", "positionDeadHeat", "wageringPosition", "winner",
        "disqualified", "odds", "choice", "favorite", "wagering", "pointsOfCall", "fractionals",
        "splits", "ratings", "comments"})
@EqualsAndHashCode
@ToString
public class Starter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);
    private static final Pattern ENTRY_PROGRAM = Pattern.compile("(\\d+)F|(\\d+)[A-Z]?");

    @Getter
    private final LastRaced lastRaced;
    @Getter
    private final String program;
    @Getter
    private final String entryProgram; // e.g. "1", "1A", and "1X" are all "1"
    @Getter
    private final Horse horse;
    @Getter
    private final Jockey jockey;
    @Getter
    private final Weight weight;
    @Getter
    private final MedicationEquipment medicationEquipment;
    @Getter
    private final Integer postPosition;
    @Getter
    private final Double odds;
    @Getter
    private final Boolean favorite;
    @Getter
    private final String comments;
    @Getter
    private final List<PointOfCall> pointsOfCall;
    @Getter
    @Setter
    private Integer finishPosition; // unofficial finishing position
    @Setter
    private Integer officialPosition; // official finishing position (post DQs etc)
    // Returns true if at least one other Starter dead-heated for the same finishing position
    @Getter
    @Setter
    private boolean positionDeadHeat;
    @JsonInclude(NON_NULL)
    @Getter
    @Setter
    private Integer wageringPosition; // payoff finishing position (Win=1, Place=2, Show=3)
    @Getter
    @Setter
    private Trainer trainer;
    @Getter
    @Setter
    private Owner owner;
    @JsonInclude(NON_NULL)
    @Getter
    @Setter
    private Claim claim;
    private boolean winner;
    @Setter
    private Boolean disqualified;
    @JsonProperty("wagering")
    @JsonIgnoreProperties({"program", "horse"})
    @JsonInclude(NON_NULL)
    @Getter
    private WinPlaceShowPayoff winPlaceShowPayoff;
    @Getter
    @Setter
    private List<Rating> ratings;
    @Getter
    @Setter
    private List<Fractional> fractionals;
    @Getter
    @Setter
    private List<Split> splits;
    // A 1-index based value representing the starter's odds as the n-th betting choice
    @Getter
    @Setter
    private Integer choice;
    @Getter
    @Setter
    private boolean entry;

    protected Starter(Builder builder) {
        lastRaced = builder.lastRaced;
        program = builder.program;
        entryProgram = builder.entryProgram;
        entry = builder.entry;
        horse = builder.horse;
        jockey = builder.jockey;
        weight = builder.weight;
        medicationEquipment = builder.medicationEquipment;
        postPosition = builder.postPosition;
        comments = builder.comments;

        odds = (builder.odds != null ? builder.odds.getValue() : null);
        favorite = (builder.odds != null ? builder.odds.isFavorite() : null);

        pointsOfCall = builder.pointsOfCall;
        if (builder.pointsOfCall != null) {
            updateFinishPosition(builder.pointsOfCall);
        }

        fractionals = builder.fractionals;
        splits = builder.splits;

        ratings = new ArrayList<>();

        trainer = builder.trainer;
        owner = builder.owner;
        claim = builder.claim;
    }

    @JsonCreator
    public Starter(LastRaced lastRaced, String program, String entryProgram, boolean entry,
            Horse horse, Jockey jockey, Weight weight, MedicationEquipment medicationEquipment,
            Integer postPosition, Double odds, Boolean favorite, String comments,
            List<PointOfCall> pointsOfCall, Integer finishPosition, Integer officialPosition,
            boolean positionDeadHeat, Integer wageringPosition, Trainer trainer, Owner owner,
            Claim claim, boolean winner, Boolean disqualified,
            WinPlaceShowPayoff winPlaceShowPayoff, List<Rating> ratings,
            List<Fractional> fractionals, List<Split> splits, Integer choice) {
        this.lastRaced = lastRaced;
        this.program = program;
        this.entryProgram = entryProgram;
        this.entry = entry;
        this.horse = horse;
        this.jockey = jockey;
        this.weight = weight;
        this.medicationEquipment = medicationEquipment;
        this.postPosition = postPosition;
        this.odds = odds;
        this.favorite = favorite;
        this.comments = comments;
        this.pointsOfCall = pointsOfCall;
        this.finishPosition = finishPosition;
        this.officialPosition = officialPosition;
        this.positionDeadHeat = positionDeadHeat;
        this.wageringPosition = wageringPosition;
        this.trainer = trainer;
        this.owner = owner;
        this.claim = claim;
        this.winner = winner;
        this.disqualified = disqualified;
        this.winPlaceShowPayoff = winPlaceShowPayoff;
        this.ratings = ratings;
        this.fractionals = fractionals;
        this.splits = splits;
        this.choice = choice;
    }

    // constructor suitable for pre-race
    public Starter(LastRaced lastRaced, String program, String entryProgram, Horse horse,
            Jockey jockey, Weight weight, MedicationEquipment medicationEquipment,
            Integer postPosition, Trainer trainer, Owner owner, Claim claim, List<Rating> ratings,
            boolean entry) {
        this.lastRaced = lastRaced;
        this.program = program;
        this.entryProgram = entryProgram;
        this.horse = horse;
        this.jockey = jockey;
        this.weight = weight;
        this.medicationEquipment = medicationEquipment;
        this.postPosition = postPosition;
        this.trainer = trainer;
        this.owner = owner;
        this.claim = claim;
        this.ratings = ratings;
        this.entry = entry;

        odds = null;
        favorite = null;
        comments = null;
        pointsOfCall = null;
    }

    /**
     * For a given Program, if it is a Coupled or Field Entry, returns the root Program number,
     * otherwise returns the Program number
     *
     * @param program The program "number" e.g. "1", "1A", "1X"
     * @return The root Program "number" as a string e.g. a Coupled Entry 1, 1A, and 1X should
     * return "1", but a Field Entry of 12F, 13F, and 14F should return "F"
     */
    public static String getEntryProgram(String program) {
        if (program != null) {
            Matcher matcher = ENTRY_PROGRAM.matcher(program);
            if (matcher.find()) {
                return (matcher.group(1) != null ? "F" : matcher.group(2));
            }
        }
        return program;
    }

    public boolean isDisqualified() {
        return (disqualified != null ? disqualified : false);
    }

    public void updateDisqualification(Disqualification disqualification) {
        this.disqualified = (disqualification != null);
        this.officialPosition = (disqualification != null ?
                disqualification.getNewPosition() : null);
    }

    /**
     * Return the position at the last point of call (which should always be the finish)
     *
     * @param pointsOfCall The list of points of call for the race
     */
    public void updateFinishPosition(List<PointOfCall> pointsOfCall) {
        if (pointsOfCall != null && !pointsOfCall.isEmpty()) {
            finishPosition = pointsOfCall.get(pointsOfCall.size() - 1).getRelativePosition()
                    .getPosition();
        }
    }

    /**
     * Returns the official position marked on the chart or, if that is not present, the position
     * from the final point of call (the finish)
     *
     * @return A number representing the official finishing position
     */
    public Integer getOfficialPosition() {
        return (officialPosition != null ? officialPosition : finishPosition);
    }

    public void setWinPlaceShowPayoff(WinPlaceShowPayoff winPlaceShowPayoff) {
        this.winPlaceShowPayoff = winPlaceShowPayoff;
        if (this.winPlaceShowPayoff != null) {
            if (this.winPlaceShowPayoff.getWin() != null) {
                this.wageringPosition = 1;
            } else if (this.winPlaceShowPayoff.getPlace() != null) {
                this.wageringPosition = 2;
            } else if (this.winPlaceShowPayoff.getShow() != null) {
                this.wageringPosition = 3;
            }
        }
    }

    public boolean matchesProgramOrName(String program, String horseName) {
        return (program != null && this.program.equals(program)) ||
                horse.getName().equals(horseName);
    }

    @JsonIgnore
    public Fractional getFinishFractional() {
        if (fractionals != null && !fractionals.isEmpty()) {
            return fractionals.get(fractionals.size() - 1);
        }
        return null;
    }

    @JsonIgnore
    public PointOfCall getFinishPointOfCall() {
        if (pointsOfCall != null && !pointsOfCall.isEmpty()) {
            return pointsOfCall.get(pointsOfCall.size() - 1);
        }
        return null;
    }

    /**
     * Add the total lengths behind to the {@link Starter}'s {@link RelativePosition} for the
     * applicable {@link PointOfCall}
     *
     * @param column           The name of the point of call column header.
     * @param relativePosition The relative position of the Starter at this point of call.
     * @return The {@link Starter} with its calculated total lengths behind at this point of call.
     * @throws PointOfCallNotFoundException If the column header value was not expected.
     */
    public Starter setTotalLengthsBehindAtPointOfCall(String column,
            RelativePosition relativePosition) throws PointOfCallNotFoundException {
        Optional<PointOfCall> pointOfCall = getPointOfCall(column);
        if (pointOfCall.isPresent()) {
            TotalLengthsBehind totalLengthsBehind = createTotalLengthsBehind(relativePosition);
            RelativePosition pocRelPosition = pointOfCall.get().getRelativePosition();
            if (pocRelPosition != null) {
                pocRelPosition.setTotalLengthsBehind(totalLengthsBehind);
            }
        } else {
            throw new PointOfCallNotFoundException(column);
        }
        return this;
    }

    /**
     * For horses that were not the leader at the point of call, the total lengths behind is taken
     * from the Past Performance Running Line Preview
     *
     * @param relativePosition The {@link RelativePosition} of the Starter at this point of call
     */
    private TotalLengthsBehind createTotalLengthsBehind(RelativePosition relativePosition) {
        Integer position = relativePosition.getPosition();
        if (position != null && position != 1) {
            RelativePosition.LengthsAhead runningLinePreviewLengths =
                    relativePosition.getLengthsAhead();
            if (runningLinePreviewLengths != null) {
                String chart = runningLinePreviewLengths.getText();
                Double lengths = runningLinePreviewLengths.getLengths();
                if (chart != null && lengths != null) {
                    return new TotalLengthsBehind(chart, lengths);
                }
            }
        }
        return null;
    }

    /**
     * Get the point of call that matches the specified distance (in feet)
     *
     * @param feet The number of feet to use to look up the point of call the corresponds.
     * @return An {@link Optional} of {@link PointOfCall} corresponding to the feet value provided.
     */
    public Optional<PointOfCall> getPointOfCall(int feet) {
        List<PointOfCall> pointsOfCall = getPointsOfCall();
        if (pointsOfCall != null) {
            for (PointOfCall pointOfCall : pointsOfCall) {
                if (pointOfCall.getFeet() != null && pointOfCall.getFeet() == feet) {
                    return Optional.of(pointOfCall);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Get the point of call that matches the specified description in the chart
     *
     * @param chartPointOfCall The value of the column text header that denotes a certain {@link
     *                         PointOfCall}
     * @return An {@link Optional} of {@link PointOfCall} corresponding to the text column value.
     */
    public Optional<PointOfCall> getPointOfCall(String chartPointOfCall) {
        List<PointOfCall> pointsOfCall = getPointsOfCall();
        if (pointsOfCall != null) {
            for (PointOfCall pointOfCall : pointsOfCall) {
                if (pointOfCall.getText().equals(chartPointOfCall)) {
                    return Optional.of(pointOfCall);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * @return true if this Starter was an official winner of the race
     */
    public boolean isWinner() {
        return (getOfficialPosition() != null && getOfficialPosition() == 1);
    }

    /**
     * Different than {@link #isWinner()} because the horse that finished first may not have been
     * the official winner
     *
     * @return true if this Starter was the unofficial winner of the race (by finishing first)
     */
    @JsonIgnore
    public boolean finishedFirst() {
        return (getFinishPosition() != null && getFinishPosition() == 1);
    }

    /**
     * Builder pattern used to create {@link Starter} instances
     */
    public static class Builder<T extends Builder<T>> {
        private LastRaced lastRaced;
        private String program;
        private String entryProgram;
        private boolean entry;
        private Horse horse;
        private Jockey jockey;
        private Weight weight;
        private MedicationEquipment medicationEquipment;
        private Integer postPosition;
        private Odds odds;
        private String comments;
        private List<PointOfCall> pointsOfCall;
        private List<Fractional> fractionals;
        private List<Split> splits;
        private Trainer trainer;
        private Owner owner;
        private Claim claim;

        public Builder lastRaced(final LastRaced lastRaced) {
            this.lastRaced = lastRaced;
            return this;
        }

        public Builder program(final String program) {
            this.program = (program != null ? program.toUpperCase() : null);
            this.entryProgram = Starter.getEntryProgram(this.program);
            this.entry = (this.program != null && this.program != entryProgram);
            return this;
        }

        public Builder horse(final Horse horse) {
            this.horse = horse;
            return this;
        }

        public Builder jockey(final Jockey jockey) {
            this.jockey = jockey;
            return this;
        }

        public Builder weight(final Weight weight) {
            this.weight = weight;
            return this;
        }

        public Builder medicationAndEquipment(final MedicationEquipment medicationEquipment) {
            this.medicationEquipment = medicationEquipment;
            return this;
        }

        public Builder postPosition(final Integer postPosition) {
            this.postPosition = postPosition;
            return this;
        }

        public Builder odds(final Odds odds) {
            this.odds = odds;
            return this;
        }

        public Builder comments(final String comments) {
            this.comments = comments;
            return this;
        }

        public Builder pointsOfCall(final List<PointOfCall> pointsOfCall) {
            this.pointsOfCall = pointsOfCall;
            return this;
        }

        public Builder fractionals(final List<Fractional> fractionals) {
            this.splits = Split.calculateSplitsFromFractionals(fractionals);
            this.fractionals = fractionals;
            return this;
        }

        public Builder trainer(final Trainer trainer) {
            this.trainer = trainer;
            return this;
        }

        public Builder owner(final Owner owner) {
            this.owner = owner;
            return this;
        }

        public Builder claim(final Claim claim) {
            this.claim = claim;
            return this;
        }

        public Horse getHorse() {
            return horse;
        }

        public Starter build() {
            return new Starter(this);
        }
    }

    public static class InvalidPointsOfCallException extends DataModelException {
        public InvalidPointsOfCallException(String message) {
            super(message);
        }
    }

    @EqualsAndHashCode
    @ToString
    public static class Claim {
        @Getter
        private final int price;
        @Getter
        private boolean claimed;
        @JsonInclude(NON_NULL)
        @Getter
        private String newTrainerName;
        @JsonInclude(NON_NULL)
        @Getter
        private String newOwnerName;

        public Claim(ClaimingPrice claimingPrice, ClaimedHorse claimedHorse) {
            if (claimingPrice != null) {
                price = claimingPrice.getClaimingPrice();
            } else {
                price = 0;
            }

            if (claimedHorse != null) {
                claimed = true;
                newTrainerName = claimedHorse.getNewTrainerName();
                newOwnerName = claimedHorse.getNewOwnerName();
            } else {
                claimed = false;
                newTrainerName = null;
                newOwnerName = null;
            }
        }
    }

    static class PointOfCallNotFoundException extends DataModelException {
        public PointOfCallNotFoundException(String column) {
            super(String.format("Point of call not found for column: %s", column));
        }
    }
}
