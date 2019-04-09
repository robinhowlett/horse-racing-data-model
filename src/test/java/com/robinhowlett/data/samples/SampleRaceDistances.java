package com.robinhowlett.data.samples;

import com.robinhowlett.data.DistanceSurfaceTrackRecord.RaceDistance;

public class SampleRaceDistances {

    public static RaceDistance oneMile() {
        return new RaceDistance("One Mile", "1 Mile", "1m", true, 5280);
    }

    public static RaceDistance aboutTwoMiles() {
        return new RaceDistance("About Two Miles", "About 2 Miles", "Abt 2m", false, 10560);
    }

    public static RaceDistance sixHundredSixtyYards() {
        return new RaceDistance("Six Hundred And Sixty Yards", "660 Yards", "660y", true, 1980);
    }

    public static RaceDistance fiveAndOneHalfFurlongs() {
        return new RaceDistance("Five And One Half Furlongs", "5 1/2 Furlongs", "5 1/2f", true,
                3630);
    }
}
