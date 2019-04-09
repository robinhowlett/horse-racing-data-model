package com.robinhowlett.data;

import com.robinhowlett.data.DistanceSurfaceTrackRecord.TrackCondition;

import lombok.Data;

/**
 * Stores the textual description of the weather (e.g. "Clear") and track conditions (e.g. "Fast")
 */
@Data
public class WeatherTrackCondition {
    private final String weather;
    private final TrackCondition trackCondition;
}
