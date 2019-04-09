package com.robinhowlett.data.samples;

import com.robinhowlett.data.Disqualification;
import com.robinhowlett.data.Horse;

import java.util.ArrayList;
import java.util.List;

public class SampleDisqualifications {

    public static List<Disqualification> threeDisqualifications() {
        return new ArrayList<Disqualification>() {{
            add(nccAnchormanDisqualifiedFrom3rdTo7th());
            add(new Disqualification("5", new Horse("This Wagons Okay"), 4, 10));
            add(new Disqualification("8", new Horse("Carpe Diem Drew"), 9, 11));
            add(imaRabbitDisqualifiedFrom1stTo(13));
        }};
    }

    public static Disqualification imaRabbitDisqualifiedFrom1stTo(int newPosition) {
        return new Disqualification("12f", new Horse("Ima Rabbit"), 1, newPosition);
    }

    public static Disqualification nccAnchormanDisqualifiedFrom3rdTo7th() {
        return new Disqualification("10", new Horse("Ncc Anchorman"), 3, 7);
    }

}
