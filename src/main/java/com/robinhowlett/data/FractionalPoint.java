package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.robinhowlett.data.Race.round;

/**
 * Stores the {@link Fractional}s for a particular race distance
 */
@EqualsAndHashCode
@ToString
public class FractionalPoint {

    @Getter
    private final String distance;
    @Getter
    private final int floor;
    @Getter
    private final List<Fractional> fractionals;

    public FractionalPoint(int floor) {
        this("", floor, new ArrayList<>());
    }

    @JsonCreator
    public FractionalPoint(
            @JsonProperty("distance") String distance,
            @JsonProperty("floor") int floor,
            @JsonProperty("fractionals") List<Fractional> fractionals) {
        this.distance = distance;
        this.floor = floor;
        this.fractionals = fractionals;
    }

    public static String convertToTime(Long millis) {
        if (millis != null) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
            long seconds = (TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(minutes));
            long subSecondMillis = (millis % 1000);

            return String.format("%d:%02d.%03d", minutes, seconds, subSecondMillis);
        } else {
            return null;
        }
    }

    public String printFractionalDistances() {
        List<String> distances = new ArrayList<>();
        for (Fractional fractional : getFractionals()) {
            distances.add(fractional.getText());
        }
        return String.join(",", distances);
    }

    /**
     * A specific fractional point for the {@link FractionalPoint} in question
     */
    @JsonPropertyOrder({"point", "text", "compact", "feet", "furlongs", "time", "millis"})
    @EqualsAndHashCode
    @ToString
    public static class Fractional {
        @Getter
        protected final int point;
        @Getter
        protected final String text;
        @Getter
        @Setter
        protected String compact;
        @Getter
        protected int feet;
        @Getter
        @Setter
        protected double furlongs;
        @Getter
        @Setter
        protected String time;
        @Getter
        @Setter
        protected Long millis;

        public Fractional(int point, String text, String compact, int feet) {
            this(point, text, compact, feet, null, null);
        }

        public Fractional(int point, String text, String compact, int feet, Long millis) {
            this(point, text, compact, feet, convertToTime(millis), millis);
        }

        @JsonCreator
        public Fractional(
                @JsonProperty("point") int point,
                @JsonProperty("text") String text,
                @JsonProperty("compact") String compact,
                @JsonProperty("feet") int feet,
                @JsonProperty("time") String time,
                @JsonProperty("millis") Long millis) {
            this.point = point;
            this.text = text;
            this.compact = compact;
            this.feet = feet;
            this.furlongs = round((double) feet / 660, 2).doubleValue();
            this.time = time;
            this.millis = millis;
        }

        public boolean hasFractionalValue() {
            return (getMillis() != null);
        }

        public void setFeet(int feet) {
            this.feet = feet;
            this.furlongs = round((double) feet / 660, 2).doubleValue();
        }

        public boolean hasTimeAndMillis() {
            return (getTime() != null && !getTime().isEmpty() && getMillis() != null);
        }
    }

    /**
     * A Split is the difference between two particular {@link Fractional}s
     */
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class Split extends Fractional {
        private static final Logger LOGGER = LoggerFactory.getLogger(Split.class);

        @Getter
        private final Fractional from;
        @Getter
        private final Fractional to;

        @JsonCreator
        public Split(int point, String text, String compact, int feet, String time, Long millis,
                Fractional from, Fractional to) {
            super(point, text, compact, feet, time, millis);
            this.from = from;
            this.to = to;
        }

        // calculates the Split - the time taken between fractionals e.g. if a Starter
        // recorded a first quarter time of 22 seconds, and a first half-mile time of 45 seconds,
        // that would be a 2 furlong split of 23 seconds
        public static List<Split> calculateSplitsFromFractionals(List<Fractional> fractionals) {
            List<Split> splits = new ArrayList<>();
            for (int i = 0; i < fractionals.size(); i++) {
                Fractional fractional = fractionals.get(i);
                if (i == 0) {
                    Split split = calculate(null, fractional);
                    if (split != null) {
                        splits.add(split);
                    }
                    continue;
                }

                Fractional next = fractionals.get(i - 1);
                Split split = Split.calculate(next, fractional);
                splits.add(split);
            }
            return splits;
        }

        public static Split calculate(Fractional from, Fractional to) {
            if (from == null && to != null) {
                return new Split(to.getPoint(), "Start to " + to.getText(),
                        "Start to " + to.getCompact(), to.getFeet(), to.getTime(),
                        to.getMillis(), from, to);
            } else {
                if (to == null) {
                    LOGGER.error(String.format("Unable to create a split time for %s to %s",
                            from, to));
                    return null;
                }

                int splitFeet = to.getFeet() - from.getFeet();
                String text = from.getText() + " to " + to.getText();
                String compact = from.getCompact() + " to " + to.getCompact();

                Long splitMillis = null;
                String time = null;
                if (from.getMillis() != null && to.getMillis() != null) {
                    splitMillis = to.getMillis() - from.getMillis();
                    time = convertToTime(splitMillis);
                }

                return new Split(to.getPoint(), text, compact, splitFeet, time, splitMillis,
                        from, to);
            }
        }
    }
}
