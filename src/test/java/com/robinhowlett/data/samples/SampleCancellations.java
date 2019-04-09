package com.robinhowlett.data.samples;

import com.robinhowlett.data.Cancellation;

public class SampleCancellations {

    public static Cancellation cancelledDueToTrackConditions() {
        return new Cancellation(true, "Track Conditions");
    }

    public static Cancellation cancelledForUnknownReason() {
        return new Cancellation(Cancellation.NO_REASON_AVAILABLE);
    }
}
