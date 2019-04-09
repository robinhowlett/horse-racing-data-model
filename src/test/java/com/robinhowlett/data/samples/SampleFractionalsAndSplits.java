package com.robinhowlett.data.samples;

import com.robinhowlett.data.FractionalPoint.Fractional;
import com.robinhowlett.data.FractionalPoint.Split;

import java.util.ArrayList;
import java.util.List;

public class SampleFractionalsAndSplits {

    public static Fractional quarterMileFirstFractional() {
        return new Fractional(1, "1/4", "2f", 1320, "0:22.000", 22000L);
    }

    public static Fractional halfMileSecondFractional() {
        return new Fractional(2, "1/2", "4f", 2640, "0:46.000", 46000L);
    }

    public static Fractional mileAndAHalfThirdFractional() {
        return new Fractional(3, "1 1/2", "1 1/2m", 7920, "2:48.980", 168980L);
    }

    public static Fractional finishFourthFractional() {
        return new Fractional(4, "Fin", "1 17/32m", 8085, "2:54.080", 174080L);
    }

    public static Split startToQuarterMileFirstSplit() {
        return new Split(1, "Start to 1/4", "Start to 2f", 1320, "0:22.000",
                22000L, null, quarterMileFirstFractional());
    }

    public static Split quarterMileToHalfMileSecondSplit() {
        return new Split(2, "1/4 to 1/2", "2f to 4f", 1320, "0:24.000", 24000L,
                quarterMileFirstFractional(), halfMileSecondFractional());
    }

    public static Split halfMileToMileAndAHalfThirdSplit() {
        return new Split(3, "1/2 to 1 1/2", "4f to 1 1/2m", 5280, "2:02.980",
                122980L, halfMileSecondFractional(), mileAndAHalfThirdFractional());
    }

    public static Split mileAndAHalfToFinishFourthSplit() {
        return new Split(4, "1 1/2 to Fin", "1 1/2m to 1 17/32m", 165,
                "0:05.100", 5100L, mileAndAHalfThirdFractional(), finishFourthFractional());
    }

    public static List<Fractional> fourFractionals() {
        return new ArrayList<Fractional>() {{
            add(quarterMileFirstFractional());
            add(halfMileSecondFractional());
            add(mileAndAHalfThirdFractional());
            add(finishFourthFractional());
        }};
    }

    public static List<Split> fourSplits() {
        return new ArrayList<Split>() {{
            add(startToQuarterMileFirstSplit());
            add(quarterMileToHalfMileSecondSplit());
            add(halfMileToMileAndAHalfThirdSplit());
            add(mileAndAHalfToFinishFourthSplit());
        }};
    }

}
