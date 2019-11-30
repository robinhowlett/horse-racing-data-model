package com.robinhowlett.data;

import com.robinhowlett.data.PointsOfCall.PointOfCall.RelativePosition.Lengths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class LengthsTest {

    private Double lengths;
    private String expected;

    public LengthsTest(Double lengths, String expected) {
        this.lengths = lengths;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection lengths() {
        return Arrays.asList(new Object[][]{
                {null, null},
                {-1.00, "1"},
                {0.05, "Nose"},
                {0.08, "Head"},
                {0.25, "Neck"},
                {0.30, "Neck"},
                {1.00, "1"},
                {1.25, "1 1/4"},
                {2.50, "2 1/2"},
                {3.66, "3 1/2"},
                {13.99, "13 3/4"},
                {14.01, "14"},
                {1.33333333, "1 1/4"},
                {1000.00, "1000"},
        });
    }

    @Test
    public void convertToText_WithVariousDoubleValues_ConvertsCorrectly() {
        assertThat(Lengths.lengthsToText(lengths), equalTo(expected));
    }
}
