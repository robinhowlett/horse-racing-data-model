package com.robinhowlett.data.samples;

import com.robinhowlett.data.ClaimingPrice;
import com.robinhowlett.data.Horse;

import java.util.ArrayList;
import java.util.List;

public class SampleClaimingPrices {

    public static List<ClaimingPrice> nineClaimingPrices() {
        return new ArrayList<ClaimingPrice>() {{
            add(furrTheSouth15000ClaimingPrice());
            add(new ClaimingPrice("7", new Horse("Classic James"), 15000));
            add(new ClaimingPrice("6", new Horse("First Noted"), 15000));
            add(new ClaimingPrice("1", new Horse("Dannys Great Knight"), 15000));
            add(new ClaimingPrice("4", new Horse("Six Tins"), 15000));
            add(new ClaimingPrice("3", new Horse("Brookstone Boy"), 15000));
            add(new ClaimingPrice("2", new Horse("Hardspun"), 15000));
            add(new ClaimingPrice("8", new Horse("Famous Dashn Dully"), 15000));
            add(new ClaimingPrice("5", new Horse("Motorspeedway"), 15000));
        }};
    }

    public static ClaimingPrice furrTheSouth15000ClaimingPrice() {
        return new ClaimingPrice("9", new Horse("Furr the South"), 15000);
    }
}
