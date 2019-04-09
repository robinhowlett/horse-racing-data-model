package com.robinhowlett.data.samples;

import com.robinhowlett.data.RaceRestrictions;

public class SampleRaceRestrictions {

    public static RaceRestrictions threeYearOldAndOlderFilliesAndMares() {
        return new RaceRestrictions(null, 3, -1, 24, false);
    }

    public static RaceRestrictions threeAndOlderAllAgesNonWinnersOfTwoLifetime() {
        return new RaceRestrictions("NW2 L", 4, -1, 31, false);
    }

    public static RaceRestrictions twoYearOldFilliesStateBred() {
        return new RaceRestrictions(null, 2, 2, 8, true);
    }

    public static RaceRestrictions threeAndFourYearOlds() {
        return new RaceRestrictions(null, 3, 4, 31, false);
    }
}
