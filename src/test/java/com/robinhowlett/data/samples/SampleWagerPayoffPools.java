package com.robinhowlett.data.samples;

import com.robinhowlett.data.Horse;
import com.robinhowlett.data.wagering.WagerPayoffPools;
import com.robinhowlett.data.wagering.WagerPayoffPools.ExoticPayoffPool;
import com.robinhowlett.data.wagering.WagerPayoffPools.WagerNameUnit;
import com.robinhowlett.data.wagering.WagerPayoffPools.WinPlaceShowPayoffPool.WinPlaceShowPayoff;
import com.robinhowlett.data.wagering.WagerPayoffPools.WinningNumbersPayoff;

import java.util.ArrayList;
import java.util.List;

public class SampleWagerPayoffPools {

    public static WagerPayoffPools expectedWagerPayoffPools() {
        List<WinPlaceShowPayoff> winPlaceShowPayoffs = new ArrayList<WinPlaceShowPayoff>() {{
            add(expectedWinPlaceAndShowPayoff());
            add(expectedPlaceAndShowPayoff());
            add(expectedShowPayoff());
        }};

        List<ExoticPayoffPool> exoticPayoffPools = new ArrayList<ExoticPayoffPool>() {{
            add(new ExoticPayoffPool(new WagerNameUnit(2.0, "Exacta"),
                    new WinningNumbersPayoff("7-8", null, 23.4), 2892d, null));
            add(new ExoticPayoffPool(new WagerNameUnit(2.0, "Quinella"),
                    new WinningNumbersPayoff("7-8", null, 17.6), 1239d, null));
            add(new ExoticPayoffPool(new WagerNameUnit(2.0, "Trifecta"),
                    new WinningNumbersPayoff("7-8-3", null, 104.8), 3983d, null));
            add(new ExoticPayoffPool(new WagerNameUnit(2.0, "Superfecta"),
                    new WinningNumbersPayoff("7-8-3-6", null, 1140.6), 1521d, null));
            add(new ExoticPayoffPool(new WagerNameUnit(2.0, "Daily Double"),
                    new WinningNumbersPayoff("11-7", null, 16.4), 882d, null));
        }};
        WagerPayoffPools.WinPlaceShowPayoffPool winPlaceShowPayoffPool =
                new WagerPayoffPools.WinPlaceShowPayoffPool(6334, winPlaceShowPayoffs);
        return new WagerPayoffPools(winPlaceShowPayoffPool, exoticPayoffPools);
    }

    public static WinPlaceShowPayoff expectedWinPlaceAndShowPayoff() {
        return new WinPlaceShowPayoff("7", new Horse("Prater Sixty Four"), 3.8, 2.8, 2.4);
    }

    public static WinPlaceShowPayoff expectedPlaceAndShowPayoff() {
        return new WinPlaceShowPayoff("8", new Horse("Candy Sweetheart"), null, 6.4, 3.8);
    }

    public static WinPlaceShowPayoff expectedShowPayoff() {
        return new WinPlaceShowPayoff("3", new Horse("Midnightwithdrawal"), null, null, 3.4);
    }

    public static WinPlaceShowPayoff expectedShowPayoffNullProgram() {
        return new WinPlaceShowPayoff(null, new Horse("Tiznow"), null, null, 0.2);
    }
}
