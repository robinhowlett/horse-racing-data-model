package com.robinhowlett.data.samples;

import com.robinhowlett.data.DistanceSurfaceTrackRecord.TrackCondition;
import com.robinhowlett.data.WeatherTrackCondition;

public class SampleWeatherTrackConditions {

    public static WeatherTrackCondition clearAndFast() {
        return new WeatherTrackCondition("Clear", TrackCondition.FAST);
    }

}
