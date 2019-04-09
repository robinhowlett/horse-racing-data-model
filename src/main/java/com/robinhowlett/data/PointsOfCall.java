package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.robinhowlett.data.Race.round;

/**
 * Stores the {@link PointOfCall} instances for a particular race distance
 */
@EqualsAndHashCode
@ToString
public class PointsOfCall {

    @Getter
    private final String distance;
    @Getter
    private final int floor;
    @Getter
    private final List<PointOfCall> calls;

    @JsonCreator
    public PointsOfCall(
            @JsonProperty("distance") String distance,
            @JsonProperty("floor") int floor,
            @JsonProperty("calls") List<PointOfCall> calls) {
        this.distance = distance;
        this.floor = floor;
        this.calls = calls;
    }

    public Optional<PointOfCall> getStretchPointOfCall() {
        return getPointOfCall(5);
    }

    public Optional<PointOfCall> getFinishPointOfCall() {
        return getPointOfCall(6);
    }

    private Optional<PointOfCall> getPointOfCall(int index) {
        for (PointOfCall pointOfCall : getCalls()) {
            if (pointOfCall.getPoint() == index) {
                return Optional.of(pointOfCall);
            }
        }
        return Optional.empty();
    }

    /**
     * A specific point of call for the specified {@link PointsOfCall} for the race distance in
     * question
     */
    @JsonPropertyOrder({"point", "text", "compact", "feet", "furlongs", "relativePosition"})
    @EqualsAndHashCode
    @ToString
    public static class PointOfCall {
        @Getter
        private final int point;
        @Getter
        private final String text;
        @Getter
        @Setter
        private String compact;
        @Getter
        private Integer feet;
        @Getter
        private Double furlongs;
        @Getter
        @Setter
        private RelativePosition relativePosition;

        public PointOfCall(int point, String text, String compact, Integer feet) {
            this(point, text, compact, feet, null);
        }

        @JsonCreator
        public PointOfCall(
                @JsonProperty("point") int point,
                @JsonProperty("text") String text,
                @JsonProperty("compact") String compact,
                @JsonProperty("feet") Integer feet,
                @JsonProperty("relativePosition") RelativePosition relativePosition) {
            this.point = point;
            this.text = text;
            this.compact = compact;
            this.feet = feet;
            this.furlongs = ((feet != null) ?
                    round((double) feet / 660, 2).doubleValue() : null);
            this.relativePosition = relativePosition;
        }

        public boolean hasKnownDistance() {
            return (feet != null);
        }

        public boolean hasRelativePosition() {
            return getRelativePosition() != null;
        }

        public boolean hasLengths() {
            return getRelativePosition() != null && getRelativePosition().getLengthsAhead() != null;
        }

        public void setFeet(Integer feet) {
            this.feet = feet;
            this.furlongs = (feet != null ?
                    round((double) feet / 660, 2).doubleValue() : null);
        }

        /**
         * Stores the position of the {@link Starter} at this point of call, and, if applicable, the
         * details about the number of lengths ahead of the next starter, and the total number of
         * lengths behind the leader at this point. "Wide" tracks the position of the horse versus
         * the rail e.g. "5-wide" would be five horse widths from the inside rail.
         */
        @JsonPropertyOrder({"position", "lengthsAhead", "totalLengthsBehind", "wide"})
        @EqualsAndHashCode
        @ToString
        public static class RelativePosition {
            @Getter
            private final Integer position;
            @Getter
            private final LengthsAhead lengthsAhead;
            @Getter
            @Setter
            private TotalLengthsBehind totalLengthsBehind;
            @JsonInclude(NON_NULL)
            @Getter
            @Setter
            private Integer wide; // reserved for custom use

            public RelativePosition(Integer position, LengthsAhead lengthsAhead) {
                this(position, lengthsAhead, null);
            }

            public RelativePosition(Integer position, LengthsAhead lengthsAhead,
                    TotalLengthsBehind totalLengthsBehind) {
                this(position, lengthsAhead, totalLengthsBehind, null);
            }

            @JsonCreator
            public RelativePosition(
                    @JsonProperty("position") Integer position,
                    @JsonProperty("lengthsAhead") LengthsAhead lengthsAhead,
                    @JsonProperty("totalLengthsBehind") TotalLengthsBehind totalLengthsBehind,
                    @JsonProperty("wide") Integer wide) {
                this.position = position;
                this.lengthsAhead = lengthsAhead;
                this.totalLengthsBehind = totalLengthsBehind;
                this.wide = wide;
            }

            /**
             * Tracks lengths as the chart's textual description and as a Double
             */
            @JsonPropertyOrder({"text", "lengths"})
            @Data
            public abstract static class Lengths {
                protected final String text;
                protected final Double lengths;

                public static String lengthsToText(Double lengths) {
                    if (lengths == null) return null;

                    lengths = Math.abs(lengths);

                    if (lengths > 0 && lengths <= 0.05) {
                        return "Nose";
                    } else if (lengths > 0.05 && lengths <= 0.10) {
                        return "Head";
                    } else if (lengths > 0.10 && lengths < 0.50) {
                        return "Neck";
                    } else {
                        int iLengths = lengths.intValue();
                        double fraction = lengths - iLengths;
                        String sLengths =
                                (iLengths > 0 ? String.valueOf(iLengths).concat(" ") : "");

                        if (fraction >= 0 && fraction < 0.25) {
                            return sLengths.trim();
                        } else if (fraction >= 0.25 && fraction < 0.50) {
                            return sLengths.concat("1/4");
                        } else if (fraction >= 0.50 && fraction < 0.75) {
                            return sLengths.concat("1/2");
                        } else {
                            return sLengths.concat("3/4");
                        }
                    }
                }
            }

            /**
             * The number of lengths ahead of the next starter
             */
            @Data
            public static class LengthsAhead extends Lengths {
                public LengthsAhead(String chart, Double lengths) {
                    super(chart, lengths);
                }
            }

            /**
             * The total number of lengths behind the leader at the particular point of call
             */
            @Data
            public static class TotalLengthsBehind extends Lengths {
                public TotalLengthsBehind(String chart, Double lengths) {
                    super(chart, lengths);
                }
            }
        }
    }
}
