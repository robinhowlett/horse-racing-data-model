package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.robinhowlett.exceptions.DataModelException;

import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.robinhowlett.data.Breed.isTBOrArabian;
import static com.robinhowlett.data.DistanceSurfaceTrackRecord.Format.FLAT;
import static com.robinhowlett.data.DistanceSurfaceTrackRecord.Format.JUMPS;
import static com.robinhowlett.data.DistanceSurfaceTrackRecord.Surface.DIRT;
import static com.robinhowlett.data.DistanceSurfaceTrackRecord.Surface.SYNTHETIC;
import static com.robinhowlett.data.DistanceSurfaceTrackRecord.Surface.TURF;
import static com.robinhowlett.data.Race.round;

/**
 * Parses the textual description of the race distance and converts it into a {@link RaceDistance}
 * instance, including calculating the race distance in feet, furlongs, and with compact
 * description. The scheduled and actual surface race on is additionally stored. It also parses and
 * stores, in a {@link TrackRecord} instance, the details of the track record for this
 * distance/surface.
 */
@JsonPropertyOrder({"distance", "surface", "course", "trackCondition", "offTurf",
        "scheduledDistance", "scheduledSurface", "scheduledCourse", "format", "trackRecord"})
@EqualsAndHashCode
@ToString
public class DistanceSurfaceTrackRecord {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceSurfaceTrackRecord.class);

    @JsonProperty("distance")
    @Getter
    private final RaceDistance raceDistance;
    @Getter
    private final String surface;
    @Getter
    private final String course;
    @JsonInclude(NON_NULL)
    @Getter
    private final RaceDistance scheduledDistance;
    @JsonInclude(NON_NULL)
    @Getter
    private final String scheduledSurface;
    @JsonInclude(NON_NULL)
    @Getter
    private final String scheduledCourse;
    @Getter
    private final String format;
    @Getter
    private final TrackRecord trackRecord;
    @Setter
    private boolean scheduledDistanceChange;
    @JsonInclude(NON_NULL)
    @Getter
    @Setter
    private TrackCondition trackCondition;

    public DistanceSurfaceTrackRecord(RaceDistance raceDistance, String course, boolean offTurf) {
        this.raceDistance = raceDistance;

        SurfaceCourseFormat courseSurfaceCourseFormat = SurfaceCourseFormat.fromCourse(course);
        this.surface = courseSurfaceCourseFormat.getSurface().getText();
        this.course = courseSurfaceCourseFormat.getCourse();
        this.format = courseSurfaceCourseFormat.getFormat().getText();

        this.scheduledSurface = offTurf ? Surface.TURF.getText() : null;

        this.scheduledCourse = null;
        this.scheduledDistance = null;
        this.trackRecord = null;
    }

    public DistanceSurfaceTrackRecord(RaceDistance raceDistance, String course,
            String scheduledCourse, RaceDistance scheduledDistance, TrackRecord trackRecord) {
        this.raceDistance = raceDistance;

        SurfaceCourseFormat courseSurfaceCourseFormat = SurfaceCourseFormat.fromCourse(course);
        this.surface = courseSurfaceCourseFormat.getSurface().getText();
        this.course = courseSurfaceCourseFormat.getCourse();
        this.format = courseSurfaceCourseFormat.getFormat().getText();

        if (scheduledCourse != null && !scheduledCourse.trim().isEmpty()) {
            SurfaceCourseFormat scheduledCourseSurfaceCourseFormat =
                    SurfaceCourseFormat.fromCourse(scheduledCourse);
            this.scheduledSurface = scheduledCourseSurfaceCourseFormat.getSurface().getText();
            this.scheduledCourse = scheduledCourseSurfaceCourseFormat.getCourse();
        } else {
            this.scheduledSurface = null;
            this.scheduledCourse = null;
        }

        this.scheduledDistance = scheduledDistance;
        this.trackRecord = trackRecord;
    }

    @JsonCreator
    public DistanceSurfaceTrackRecord(RaceDistance raceDistance, String surface, String course,
            String scheduledSurface, String scheduledCourse, RaceDistance scheduledDistance,
            String format, TrackRecord trackRecord, TrackCondition trackCondition) {
        this.raceDistance = raceDistance;
        this.surface = surface;
        this.course = course;
        this.scheduledSurface = scheduledSurface;
        this.scheduledCourse = scheduledCourse;
        this.scheduledDistance = scheduledDistance;
        this.format = format;
        this.trackRecord = trackRecord;
        this.trackCondition = trackCondition;
    }

    public boolean isOffTurf() {
        return (scheduledSurface != null && !surface.equals(scheduledSurface));
    }

    public boolean isScheduledDistanceChange() {
        return (scheduledDistance != null && raceDistance.getFeet() != scheduledDistance.getFeet())
                || scheduledDistanceChange;
    }

    @ToString
    enum Surface {
        DIRT("Dirt"),
        TURF("Turf"),
        SYNTHETIC("Synthetic");

        @Getter
        private String text;

        Surface(String text) {
            this.text = text;
        }

        public static Surface forText(String text) {
            for (Surface surface : values()) {
                if (surface.getText().equals(text)) {
                    return surface;
                }
            }
            return null;
        }
    }

    @ToString
    enum Format {
        FLAT("Flat"),
        JUMPS("Jumps");

        @Getter
        private String text;

        Format(String text) {
            this.text = text;
        }

        public static Format forText(String text) {
            for (Format surface : values()) {
                if (surface.getText().equals(text)) {
                    return surface;
                }
            }
            return null;
        }
    }

    @ToString
    public enum TrackCondition {
        FAST("FT", "Fast"),
        FIRM("FM", "Firm"),
        FROZEN("FZ", "Frozen"),
        GOOD("GD", "Good"),
        HARD("HD", "Hard"),
        HEAVY("HY", "Heavy"),
        MUDDY("MY", "Muddy"),
        SLOPPY("SY", "Sloppy"),
        SLOW("SL", "Slow"),
        SOFT("SF", "Soft"),
        WET_FAST("WF", "Wet Fast"),
        YIELDING("YL", "Yielding");

        public static final String SEALED_SUFFIX = " (Sealed)";
        @Getter
        private String code;
        @Getter
        private String text;
        @Getter
        @Setter
        private boolean sealed;

        TrackCondition(String code, String text) {
            this(code, text, false);
        }

        TrackCondition(String code, String text, boolean sealed) {
            this.code = code;
            this.text = text;
            this.sealed = sealed;
        }

        public static TrackCondition lookup(String code) {
            for (TrackCondition trackCondition : TrackCondition.values()) {
                if (trackCondition.getCode().equals(code)) {
                    return trackCondition;
                }
            }
            LOGGER.error(String.format("Unable to find TrackCondition for code: %s", code));
            return null;
        }

        public static TrackCondition fromText(String text) {
            boolean sealed = false;
            if (text.contains(SEALED_SUFFIX)) {
                sealed = true;
                text = text.substring(0, text.indexOf("(") - 1);
            }
            for (TrackCondition trackCondition : values()) {
                if (trackCondition.getText().equals(text)) {
                    trackCondition.setSealed(sealed);
                    return trackCondition;
                }
            }
            return null;
        }
    }

    /**
     * Stores the textual description of the race distance, the distance expressed in feet and
     * furlongs, a compact description of the race distance and whether the distance is exact or
     * estimated ("About")
     */
    @JsonPropertyOrder({"text", "alt", "compact", "feet", "furlongs", "exact", "runUp"})
    @EqualsAndHashCode
    @ToString
    public static class RaceDistance {
        public static final List<String> NUMERATORS = Arrays.asList("", "one", "two", "three",
                "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve",
                "thirteen", "fourteen", "fifteen");

        public static final List<String> TENS = Arrays.asList("", "ten", "twenty", "thirty",
                "forty", "fifty", "sixty", "seventy", "eighty", "ninety");

        @Getter
        private final String text;
        @Getter
        private final String alt;
        @Getter
        private final String compact;
        @Getter
        private final boolean exact;
        @Getter
        private final int feet;
        @Getter
        private final double furlongs;
        @Getter
        @Setter
        private Integer runUp;
        @Getter
        @Setter
        private Integer tempRail;

        public RaceDistance(String text, String alt, String compact, boolean exact, int feet) {
            this(text, alt, compact, exact, feet, null, null);
        }

        @JsonCreator
        public RaceDistance(String text, String alt, String compact, boolean exact, int feet,
                Integer runUp, Integer tempRail) {
            this.text = text;
            this.alt = alt;
            this.compact = compact;
            this.exact = exact;
            this.feet = feet;
            this.furlongs = round((double) feet / 660, 2).doubleValue();
            this.runUp = runUp;
            this.tempRail = tempRail;
        }

        /**
         * Generate the textual descriptions of a race distance using the provided number of feet,
         * whether the distance is estimated or not, and respecting the standard for the breed in
         * question
         */
        public static RaceDistance fromFeet(int feet, boolean about, Breed breed) {
            // these will be used for the long and short textual descriptions of the race distance
            StringBuilder textBuilder = new StringBuilder();
            StringBuilder altBuilder = new StringBuilder();
            StringBuilder compactBuilder = new StringBuilder();

            // prepend the description if the race distance was estimated rather than exact
            textBuilder.append(about ? "about " : "");
            altBuilder.append(about ? "about " : "");
            compactBuilder.append(about ? "Abt " : "");

            // the race distance expressed as a miles rounded-down to a whole number
            int miles = feet / 5280;

            // the total number of furlongs rounded-down
            int totalFurlongs = feet / 660;
            int milesAsFurlongs = miles * 8;
            // the balance number of furlongs after the miles have been subtracted
            int furlongs = totalFurlongs - milesAsFurlongs;

            // the total number of yards
            int totalYards = feet / 3;
            int milesAsYards = miles * 1760;
            int furlongsAsYards = furlongs * 220;
            // the balance number of yards after the miles and furlongs have been subtracted
            int yards = totalYards - milesAsYards - furlongsAsYards;

            // distance descriptions differ if the race is a mile or longer
            if (miles > 0) {
                String num = NUMERATORS.get(miles);
                textBuilder.append(num);
                altBuilder.append(miles);
                compactBuilder.append(miles);

                if (furlongs == 0 && yards == 0) {
                    // race distance is a whole number of miles
                    textBuilder.append(miles > 1 ? " miles" : " mile");
                    altBuilder.append(miles > 1 ? " miles" : " mile");
                    compactBuilder.append("m");
                } else {
                    // 110 feet is half a furlong - a sixteenth of a mile
                    if (yards == 110) {
                        // e.g. one and three sixteenth miles
                        int sixteenths = (furlongs * 2) + 1;
                        textBuilder.append(" and ")
                                .append(NUMERATORS.get(sixteenths))
                                .append(" sixteenth miles");
                        altBuilder.append(" ").append(sixteenths).append("/16 miles");
                        compactBuilder.append(" ").append(sixteenths).append("/16m");
                    } else if (yards == 0) {
                        // if yards are zero, then the race distance is miles and furlongs
                        // use the modulus to see if we should use halves, eighths, or fourths
                        switch (furlongs % 4) {
                            case 0:
                                textBuilder.append(" and one half miles");
                                altBuilder.append(" 1/2 miles");
                                compactBuilder.append(" 1/2m");
                                break;
                            case 1:
                            case 3:
                                textBuilder.append(" and ")
                                        .append(NUMERATORS.get(furlongs))
                                        .append(" eighth miles");
                                altBuilder.append(" ").append(furlongs).append("/8 miles");
                                compactBuilder.append(" ").append(furlongs).append("/8m");
                                break;
                            case 2:
                                int fourths = furlongs / 2;
                                textBuilder.append(" and ")
                                        .append(NUMERATORS.get(fourths))
                                        .append(" fourth miles");
                                altBuilder.append(" ").append(fourths).append("/4 miles");
                                compactBuilder.append(" ").append(fourths).append("/4m");
                                break;
                        }
                    } else {
                        // the yards cannot be expressed as fractions of a furlong, so use them
                        // e.g. one mile forty yards
                        int yardsInTens = yards / 10;
                        textBuilder.append(" mile")
                                .append(miles > 1 ? "s" : "")
                                .append(" and ")
                                .append(TENS.get(yardsInTens)).append(" yards");
                        altBuilder.append(" mile")
                                .append(miles > 1 ? "s " : " ")
                                .append(yards)
                                .append(" yard")
                                .append(yards > 1 ? "s" : "");
                        compactBuilder.append("m ").append(yards).append("y");
                    }
                }
            } else {
                // race distance is less than a mile
                // favor using furlongs for TB and Arabians
                // favor using yards for QH and Mixed
                if (furlongs > 0 && yards == 55 && isTBOrArabian(breed)) {
                    // 55 yards is a quarter of a furlong, or a thirty-second of a mile
                    String num = NUMERATORS.get(furlongs);
                    textBuilder.append(num).append(" and one fourth furlongs");
                    altBuilder.append(furlongs).append(" 1/4 furlongs");
                    compactBuilder.append(furlongs).append(" 1/4f");
                } else if (furlongs > 0 && yards == 110 && isTBOrArabian(breed)) {
                    // 110 yards is half a furlong, or a sixteenth of a mile
                    String num = NUMERATORS.get(furlongs);
                    textBuilder.append(num).append(" and one half furlongs");
                    altBuilder.append(furlongs).append(" 1/2 furlongs");
                    compactBuilder.append(furlongs).append(" 1/2f");
                } else if (yards == 0 && isTBOrArabian(breed)) {
                    // e.g. for TB/Arabian, use 2 furlongs rather than 440 yards
                    String num = NUMERATORS.get(furlongs);
                    textBuilder.append(num).append(" furlong").append(furlongs > 1 ? "s" : "");
                    altBuilder.append(furlongs).append(" furlong").append(furlongs > 1 ? "s" : "");
                    compactBuilder.append(furlongs).append("f");
                } else if (isTBOrArabian(breed)) {
                    // e.g. for TB/Arabian, use 4 furlongs 40 yards rather than 950 yards
                    String num = NUMERATORS.get(furlongs);
                    int yardsInTens = yards / 10;
                    textBuilder.append(num)
                            .append(" furlong")
                            .append(furlongs > 1 ? "s" : "")
                            .append(" and ")
                            .append(TENS.get(yardsInTens)).append(" yards");
                    altBuilder.append(furlongs)
                            .append(" furlong")
                            .append(furlongs > 1 ? "s " : " ")
                            .append(yards)
                            .append(" yard")
                            .append(yards > 1 ? "s" : "");
                    compactBuilder.append(furlongs).append("f ").append(yards).append("y");
                } else {
                    // if QH or Mixed, use the whole number of yards
                    // e.g. four hundred forty yards
                    // e.g. three hundred and seventy five yards
                    // e.g. one thousand three hundred and twenty yards
                    int thousandYards = totalYards / 1000;
                    int hundredYards = (totalYards - (thousandYards * 1000)) / 100;
                    int tensYards = (totalYards - (thousandYards * 1000) - (hundredYards * 100))
                            / 10;
                    int singleYards = (totalYards - (thousandYards * 1000) - (hundredYards * 100)
                            - (tensYards * 10));

                    if (thousandYards > 0) {
                        textBuilder.append(NUMERATORS.get(thousandYards)).append(" thousand ");
                    }
                    if (hundredYards > 0) {
                        textBuilder.append(NUMERATORS.get(hundredYards)).append(" hundred ");
                    }
                    if (tensYards > 0 || singleYards > 0) {
                        textBuilder.append("and ");

                        if (tensYards == 1) {
                            textBuilder.append(NUMERATORS.get(10 + singleYards).concat(" "));
                        } else {
                            textBuilder
                                    .append(tensYards > 1 ? TENS.get(tensYards).concat(" ") : "")
                                    .append(singleYards > 0 ?
                                            NUMERATORS.get(singleYards).concat(" ") : "");
                        }
                    }

                    textBuilder.append("yards");
                    altBuilder.append(totalYards).append(" yards");
                    compactBuilder.append(totalYards).append("y");
                }
            }

            // capitalize the start of each word to match the PDF standard
            String text = WordUtils.capitalize(textBuilder.toString());
            String alt = WordUtils.capitalize(altBuilder.toString());
            String compact = compactBuilder.toString();
            boolean exact = !about; // if the distance is "about", then it isn't exact

            return new RaceDistance(text, alt, compact, exact, feet);
        }
    }

    /**
     * Stories the name of the track record holder, the track record time (as both a String and in
     * milliseconds) and the date when the record was set
     */
    @Data
    public static class TrackRecord {
        @JsonIgnoreProperties({"color", "sex", "sire", "dam", "damSire", "foalingDate",
                "foalingLocation", "breeder"})
        private final Horse holder;
        private final String time;
        private final Long millis;
        private final LocalDate raceDate;
    }

    @Data
    private static class SurfaceCourseFormat {
        public static final Map<String, SurfaceCourseFormat> SURFACE_COURSE_TYPES;

        static {
            Map<String, SurfaceCourseFormat> surfaceCourseTypes = new LinkedHashMap<>();
            surfaceCourseTypes.put("Dirt", new SurfaceCourseFormat(DIRT, "Dirt", FLAT));
            surfaceCourseTypes.put("Turf", new SurfaceCourseFormat(TURF, "Turf", FLAT));
            surfaceCourseTypes.put("All Weather Track",
                    new SurfaceCourseFormat(SYNTHETIC, "All Weather Track", FLAT));
            surfaceCourseTypes.put("Inner track",
                    new SurfaceCourseFormat(DIRT, "Inner Track", FLAT));
            surfaceCourseTypes.put("Inner turf", new SurfaceCourseFormat(TURF, "Inner Turf",
                    FLAT));
            surfaceCourseTypes.put("Hurdle", new SurfaceCourseFormat(TURF, "Hurdle", JUMPS));
            surfaceCourseTypes.put("Downhill turf",
                    new SurfaceCourseFormat(TURF, "Downhill Turf", FLAT));
            surfaceCourseTypes.put("Outer turf", new SurfaceCourseFormat(TURF, "Outer Turf",
                    FLAT));
            surfaceCourseTypes.put("Timber", new SurfaceCourseFormat(TURF, "Timber", JUMPS));
            surfaceCourseTypes.put("Steeplechase",
                    new SurfaceCourseFormat(TURF, "Steeplechase", JUMPS));
            surfaceCourseTypes.put("Hunt on turf",
                    new SurfaceCourseFormat(TURF, "Hunt On Turf", JUMPS));
            SURFACE_COURSE_TYPES = Collections.unmodifiableMap(surfaceCourseTypes);
        }

        private final Surface surface;
        private final String course;
        private final Format format;

        static SurfaceCourseFormat fromCourse(String course) {
            return SURFACE_COURSE_TYPES.get(course);
        }
    }

    public static class NoRaceDistanceFound extends DataModelException {
        public NoRaceDistanceFound(String distanceSurfaceTrackRecord) {
            super(String.format("Unable to identify a valid race distance, surface, and/or track " +
                    "record: %s", distanceSurfaceTrackRecord));
        }
    }
}
