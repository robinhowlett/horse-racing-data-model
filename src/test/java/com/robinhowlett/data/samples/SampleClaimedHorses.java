package com.robinhowlett.data.samples;

import com.robinhowlett.data.ClaimedHorse;
import com.robinhowlett.data.Horse;

import java.util.ArrayList;
import java.util.List;

public class SampleClaimedHorses {

    public static List<ClaimedHorse> threeClaimedHorses() {
        return new ArrayList<ClaimedHorse>() {{
            add(bellaGiornattaClaimedHorse());
            add(new ClaimedHorse(new Horse("Magic Mama"), "Gary Sherlock",
                    "Bernhardt, Charles and Warren, Craig"));
            add(new ClaimedHorse(new Horse("Mavicsa"), "Jeff Bonde", "Mersad Metanovic"));
        }};
    }

    public static ClaimedHorse bellaGiornattaClaimedHorse() {
        return new ClaimedHorse(new Horse("Bella Giornatta"), "Linda Mikus", "Carlo Fisco");
    }
}
