package com.robinhowlett.data.samples;

import com.robinhowlett.data.Purse;
import com.robinhowlett.data.Purse.PurseEnhancement;

import java.util.ArrayList;

import static com.robinhowlett.data.Purse.EnhancementType.INCLUDES;
import static com.robinhowlett.data.Purse.EnhancementType.PLUS;

public class SamplePurses {

    public static Purse oneHundredGrandPurseWithAddedAvailableIncludesPlusAndValue() {
        return new Purse(100000, "$100,000 Guaranteed", "$40,000 Added",
                "$9,700 1st $5,820, 2nd $1,940, 3rd $970, 4th $485, 5th $97, 6th $97, 7th $97, " +
                        "8th $97, 9th $97",
                new ArrayList<PurseEnhancement>() {{
                    add(new PurseEnhancement(PLUS, "$40,000 Added"));
                    add(new PurseEnhancement(INCLUDES, "$2,000 Other Sources"));
                }});
    }


}
