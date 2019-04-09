package com.robinhowlett.data;

import java.time.LocalDate;

import lombok.Data;

/**
 * Parses and stores the track name, the race date (as a {@link LocalDate} instance) and the race
 * number
 */
@Data
public class TrackRaceDateRaceNumber {
    private final String trackName;
    private final LocalDate raceDate;
    private final int raceNumber;
}
