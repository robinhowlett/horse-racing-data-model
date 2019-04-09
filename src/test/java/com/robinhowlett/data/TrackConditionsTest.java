package com.robinhowlett.data;

import com.robinhowlett.data.DistanceSurfaceTrackRecord.TrackCondition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.robinhowlett.data.DistanceSurfaceTrackRecord.TrackCondition.MUDDY;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class TrackConditionsTest {

    private String text;
    private TrackCondition trackCondition;

    public TrackConditionsTest(String text, TrackCondition trackCondition) {
        this.text = text;
        this.trackCondition = trackCondition;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection trackConditions() {
        TrackCondition muddyAndSealed = MUDDY;
        muddyAndSealed.setSealed(true);
        return Arrays.asList(new Object[][]{
                {"Muddy", MUDDY},
                {"Muddy (Sealed)", muddyAndSealed},
                {"Nonsense", null}
        });
    }

    @Test
    public void fromText_WithParameters_ReturnsCorrectTrackCondition() {
        assertThat(TrackCondition.fromText(text), equalTo(trackCondition));
    }
}
