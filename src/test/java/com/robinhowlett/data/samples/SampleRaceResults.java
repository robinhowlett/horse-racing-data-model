package com.robinhowlett.data.samples;

import com.robinhowlett.data.RaceResult;

public class SampleRaceResults {

    public static RaceResult cancelledRaceDueToWeather() {
        RaceResult.Builder builder = new RaceResult.Builder();
        builder.cancellation(SampleCancellations.cancelledDueToTrackConditions());
        return builder.build();
    }

    public static RaceResult arapahoePark24July2016Race1() {
        RaceResult.Builder builder = new RaceResult.Builder();
        return builder.build();
    }
}
