package com.robinhowlett.data;

import com.robinhowlett.data.DistanceSurfaceTrackRecord.RaceDistance;
import com.robinhowlett.data.samples.SampleRaceDistances;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class RaceDistanceTest {

    private int feet;
    private boolean about;
    private Breed breed;
    private RaceDistance expected;

    public RaceDistanceTest(int feet, boolean about, Breed breed, RaceDistance expected) {
        this.feet = feet;
        this.about = about;
        this.breed = breed;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection distances() {
        return Arrays.asList(new Object[][]{
                {8250, true, Breed.THOROUGHBRED,
                        new RaceDistance("About One And Nine Sixteenth Miles", "About 1 9/16 " +
                                "Miles", "Abt 1 9/16m", false, 8250)},
                {5280, false, Breed.THOROUGHBRED,
                        SampleRaceDistances.oneMile()},
                {10560, true, Breed.THOROUGHBRED,
                        SampleRaceDistances.aboutTwoMiles()},
                {11220, false, Breed.THOROUGHBRED,
                        new RaceDistance("Two And One Eighth Miles", "2 1/8 Miles", "2 1/8m",
                                true, 11220)},
                {7920, true, Breed.THOROUGHBRED,
                        new RaceDistance("About One And One Half Miles", "About 1 1/2 Miles",
                                "Abt 1 1/2m", false, 7920)},
                {660, false, Breed.THOROUGHBRED,
                        new RaceDistance("One Furlong", "1 Furlong", "1f", true, 660)},
                {4620, true, Breed.THOROUGHBRED,
                        new RaceDistance("About Seven Furlongs", "About 7 Furlongs", "Abt 7f",
                                false, 4620)},
                {3630, false, Breed.THOROUGHBRED,
                        SampleRaceDistances.fiveAndOneHalfFurlongs()},
                {3465, true, Breed.THOROUGHBRED,
                        new RaceDistance("About Five And One Fourth Furlongs", "About 5 1/4 " +
                                "Furlongs", "Abt 5 1/4f", false, 3465)},
                {600, false, Breed.QUARTER_HORSE,
                        new RaceDistance("Two Hundred Yards", "200 Yards", "200y", true, 600)},
                {3000, false, Breed.QUARTER_HORSE,
                        new RaceDistance("One Thousand Yards", "1000 Yards", "1000y", true, 3000)},
                {1200, true, Breed.QUARTER_HORSE,
                        new RaceDistance("About Four Hundred Yards", "About 400 Yards", "Abt 400y",
                                false, 1200)},
                {1980, false, Breed.QUARTER_HORSE,
                        SampleRaceDistances.sixHundredSixtyYards()},
                {3300, false, Breed.QUARTER_HORSE,
                        new RaceDistance("One Thousand One Hundred Yards", "1100 Yards", "1100y",
                                true, 3300)},
                {660, true, Breed.MIXED,
                        new RaceDistance("About Two Hundred And Twenty Yards", "About 220 Yards",
                                "Abt 220y", false,
                                660)},
                {3960, false, Breed.MIXED,
                        new RaceDistance("One Thousand Three Hundred And Twenty Yards", "1320 " +
                                "Yards", "1320y", true, 3960)},
                {10680, false, Breed.THOROUGHBRED,
                        new RaceDistance("Two Miles And Forty Yards", "2 Miles 40 Yards", "2m " +
                                "40y", true, 10680)},
                {5490, true, Breed.THOROUGHBRED,
                        new RaceDistance("About One Mile And Seventy Yards", "About 1 Mile 70 " +
                                "Yards", "Abt 1m 70y", false, 5490)},
                {2850, false, Breed.ARABIAN,
                        new RaceDistance("Four Furlongs And Seventy Yards", "4 Furlongs 70 " +
                                "Yards", "4f 70y", true, 2850)},
                {1125, false, Breed.QUARTER_HORSE,
                        new RaceDistance("Three Hundred And Seventy Five Yards", "375 Yards",
                                "375y", true, 1125)},
                {945, false, Breed.QUARTER_HORSE,
                        new RaceDistance("Three Hundred And Fifteen Yards", "315 Yards", "315y",
                                true, 945)}
        });
    }

    @Test
    public void fromFeet_WithParameters_ReturnsCorrectRaceDistance() throws Exception {
        assertThat(RaceDistance.fromFeet(feet, about, breed), equalTo(expected));
    }
}
