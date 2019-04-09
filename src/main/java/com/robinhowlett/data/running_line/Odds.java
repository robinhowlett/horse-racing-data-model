package com.robinhowlett.data.running_line;

import lombok.Data;

/**
 * The value of the {@link com.robinhowlett.data.Starter} (expressed within a {@link Double} {@code
 * value} field), a boolean to note if the starter was favorite, and (if applicable) the 1-based
 * index of starter in terms of odds order (e.g. 2nd fav has a "choice" value of 2)
 */
@Data
public class Odds {

    private final Double value;
    private final boolean favorite;
    private Integer choice;
}
