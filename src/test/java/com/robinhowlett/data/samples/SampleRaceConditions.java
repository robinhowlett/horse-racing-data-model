package com.robinhowlett.data.samples;

import com.robinhowlett.data.RaceConditions;
import com.robinhowlett.data.RaceConditions.ClaimingPriceRange;
import com.robinhowlett.data.RaceRestrictions;

public class SampleRaceConditions {

    public static RaceConditions threeAndOlderFilliesAndMaresConditions() {
        return new RaceConditions("FOR MAIDENS, FILLIES AND MARES THREE YEARS OLD AND UPWARD. " +
                "Three Year Olds, 120 lbs.; Older, 124 lbs.", null,
                new RaceRestrictions(null, 3, -1, "3+", 24, "F&M", true, false));
    }

    public static ClaimingPriceRange twentyTwoFiveToTwentyFiveThousandClaimingPriceRange() {
        return new ClaimingPriceRange(22500, 25000);
    }
}
