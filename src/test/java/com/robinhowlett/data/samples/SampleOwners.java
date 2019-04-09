package com.robinhowlett.data.samples;

import com.robinhowlett.data.Owner;

import java.util.ArrayList;
import java.util.List;

public class SampleOwners {

    public static List<Owner> sevenOwners() {
        return new ArrayList<Owner>() {{
            add(new Owner("3", "D P Racing"));
            add(new Owner("14", "Kretz Racing LLC"));
            add(new Owner("9", "Rockingham Ranch"));
            add(new Owner("2", "Arndt, Michael. J., McFetridge, S. M., Preiss, Daniel, and " +
                    "Metanovic, Mersad"));
            add(longNameOwner());
            add(new Owner("8", "Qatar Racing, Ltd."));
            add(new Owner("6", "Reddam Racing LLC"));
        }};
    }

    public static Owner longNameOwner() {
        return new Owner("7", "Gorman, Mark, Queen Bee Racing, LLC and Sterling Stable LLC");
    }
}
