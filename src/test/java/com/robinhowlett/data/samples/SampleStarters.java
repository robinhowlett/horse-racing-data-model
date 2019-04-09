package com.robinhowlett.data.samples;

import com.robinhowlett.data.FractionalPoint.Fractional;
import com.robinhowlett.data.FractionalPoint.Split;
import com.robinhowlett.data.Horse;
import com.robinhowlett.data.Jockey;
import com.robinhowlett.data.PointsOfCall.PointOfCall;
import com.robinhowlett.data.PointsOfCall.PointOfCall.RelativePosition;
import com.robinhowlett.data.PointsOfCall.PointOfCall.RelativePosition.LengthsAhead;
import com.robinhowlett.data.PointsOfCall.PointOfCall.RelativePosition.TotalLengthsBehind;
import com.robinhowlett.data.Starter;

import java.util.ArrayList;
import java.util.List;

public class SampleStarters {

    public static List<Starter> quarterHorseStarters() {
        return new ArrayList<Starter>() {{
            Starter.Builder first = new Starter.Builder();
            first.horse(new Horse("Perkin Desire")).jockey(new Jockey("Ramiro", "Garcia"));
            first.pointsOfCall(new ArrayList<PointOfCall>() {{
                PointOfCall pointOfCall = new PointOfCall(6, "Fin", "350y", 1050);
                pointOfCall.setRelativePosition(new RelativePosition(1,
                        new LengthsAhead("1 3/4", 1.75)));
                add(pointOfCall);
            }});
            Fractional finFirst = new Fractional(6, "Fin", "350y", 1050, "0:18.015", 18015L);
            first.fractionals(new ArrayList<Fractional>() {{
                add(finFirst);
            }});
            Starter firstStarter = first.build();
            add(firstStarter);

            Starter.Builder second = new Starter.Builder();
            second.horse(new Horse("Ima Cutie Patutie")).jockey(new Jockey("Vince", "Guerra"));
            second.pointsOfCall(new ArrayList<PointOfCall>() {{
                PointOfCall pointOfCall = new PointOfCall(6, "Fin", "350y", 1050);
                RelativePosition relativePosition = new RelativePosition(2,
                        new LengthsAhead("1/2", 0.5));
                relativePosition.setTotalLengthsBehind(new TotalLengthsBehind("1" +
                        " 3/4", 1.75));
                pointOfCall.setRelativePosition(relativePosition);
                add(pointOfCall);
            }});
            Starter secondStarter = second.build();
            Fractional finSecond = new Fractional(6, "Fin", "350y", 1050, "0:18.317", 18317L);
            secondStarter.setFractionals(new ArrayList<Fractional>() {{
                add(finSecond);
            }});
            secondStarter.setSplits(new ArrayList<Split>() {{
                add(Split.calculate(null, finSecond));
            }});
            add(secondStarter);
        }};
    }

}
