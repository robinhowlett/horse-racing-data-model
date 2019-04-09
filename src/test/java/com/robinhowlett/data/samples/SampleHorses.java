package com.robinhowlett.data.samples;

import com.robinhowlett.data.Breeder;
import com.robinhowlett.data.Horse;

import java.time.LocalDate;

public class SampleHorses {

    public static Horse backStopNameOnly() {
        return new Horse("Back Stop");
    }

    public static Horse backStopFullDetails() {
        return new Horse("Back Stop", "Bay", "Filly", new Horse("Blame"), new Horse("Freeroll"),
                new Horse("Touch Gold"), LocalDate.of(2012, 3, 30), "Kentucky",
                new Breeder("Claiborne Farm"));
    }
}
