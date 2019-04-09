package com.robinhowlett.data.running_line;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.robinhowlett.data.RaceResult;
import com.robinhowlett.data.Track;

import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * Stores the {@link LocalDate} instance of the last race date, the number of days between this race
 * date and then, and the {@link Track}, race number, and finishing position of the {@link
 * com.robinhowlett.data.Starter}'s last performance (if applicable and they exist)
 */
@EqualsAndHashCode
@ToString
public class LastRaced {

    @JsonInclude(NON_EMPTY)
    @Getter
    private final List<Link> links;
    @Getter
    private final LocalDate raceDate;
    @Getter
    private final Integer daysSince;
    @JsonUnwrapped
    @Getter
    private final LastRacePerformance lastRacePerformance;

    public LastRaced(LocalDate raceDate, Integer daysSince,
            LastRacePerformance lastRacePerformance) {
        this(raceDate, daysSince, lastRacePerformance,
                (lastRacePerformance != null ? RaceResult.buildLinks(lastRacePerformance.getTrack(),
                        raceDate, lastRacePerformance.getRaceNumber()) : null));
    }

    @JsonCreator
    LastRaced(LocalDate raceDate, Integer daysSince,
            LastRacePerformance lastRacePerformance, List<Link> links) {
        this.raceDate = raceDate;
        this.daysSince = daysSince;
        this.lastRacePerformance = lastRacePerformance;
        this.links = links;
    }

    public static LastRaced noLastRace() {
        return null;
    }

    public boolean hasLastRace() {
        return (raceDate != null);
    }

    @JsonIgnore
    public Optional<Link> getLink(String rel) {
        return getLinks().stream()
                .filter(link -> link.getRel() != null &&
                        link.getRel().equalsIgnoreCase(rel)
                ).findAny();
    }

    /**
     * The {@link Track}, race number, and finishing position of the {@link
     * com.robinhowlett.data.Starter}'s last performance (if applicable and they exist)
     */
    @Data
    public static class LastRacePerformance {
        private final Track track;
        private final Integer raceNumber;
        private final Integer officialPosition;
    }


}
