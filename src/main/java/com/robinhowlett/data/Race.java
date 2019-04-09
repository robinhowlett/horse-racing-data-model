package com.robinhowlett.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface Race {

    /**
     * Round half-up and to three decimal places
     *
     * @see #round(double, int)
     */
    static BigDecimal round(double d) {
        return round(d, 3);
    }

    /**
     * Round half-up and up to the defined number of decimal places. Used to try to avoid floating
     * point woes
     */
    static BigDecimal round(double d, int newScale) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(newScale, RoundingMode.HALF_UP);
        return bd;
    }

    static String convertToMonthDayYear(LocalDate isoDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        return dateTimeFormatter.format(isoDate);
    }

    // http://www.drf.com/news/settlement-creates-two-winners-2016-parx-oaks
    static boolean is2016ParxOaksDebacle(Track track, LocalDate raceDate,
            Integer raceNumber) {
        return (track != null && track.getCode() != null && raceDate != null &&
                raceNumber != null &&
                track.getCode().equals("PRX") &&
                raceDate.isEqual(LocalDate.of(2016, 5, 7)) &&
                raceNumber == 8);
    }

    static String ordinal(int i) {
        String[] suffixes =
                new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];
        }
    }
}
