package com.robinhowlett.data.samples;

import com.robinhowlett.data.PostTimeStartCommentsTimer;

public class SamplePostTimeStartCommentsTimers {

    public static PostTimeStartCommentsTimer pm101GoodForAllNoTimer() {
        return new PostTimeStartCommentsTimer("1:01", "Good for all");
    }

    public static PostTimeStartCommentsTimer pm150GoodForAllWithElectronicTimer() {
        return new PostTimeStartCommentsTimer("1:50", "Good for all", "Electronic");
    }

}
