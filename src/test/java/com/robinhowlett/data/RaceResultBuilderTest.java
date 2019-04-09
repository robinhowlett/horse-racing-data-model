package com.robinhowlett.data;

import com.robinhowlett.data.FractionalPoint.Fractional;
import com.robinhowlett.data.FractionalPoint.Split;
import com.robinhowlett.data.PointsOfCall.PointOfCall;
import com.robinhowlett.data.PointsOfCall.PointOfCall.RelativePosition;
import com.robinhowlett.data.PointsOfCall.PointOfCall.RelativePosition.LengthsAhead;
import com.robinhowlett.data.PointsOfCall.PointOfCall.RelativePosition.TotalLengthsBehind;
import com.robinhowlett.data.running_line.Odds;
import com.robinhowlett.data.samples.SampleStarters;
import com.robinhowlett.data.samples.SampleWagerPayoffPools;
import com.robinhowlett.data.wagering.WagerPayoffPools;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RaceResultBuilderTest {

    @Test
    public void updateStartersWithWinPlaceShowPayoffs_WithCoupledFieldEntries_UpdatesWPSForEach() {
        List<Starter> expected = new ArrayList<Starter>() {{
            Starter first = new Starter.Builder().program("7")
                    .horse(new Horse("Prater Sixty Four"))
                    .jockey(new Jockey("Karlo", "Lopez")).build();
            first.setWinPlaceShowPayoff(SampleWagerPayoffPools.expectedWinPlaceAndShowPayoff());

            Starter second = new Starter.Builder().program("8")
                    .horse(new Horse("Candy Sweetheart"))
                    .jockey(new Jockey("Dennis", "Collins")).build();
            second.setWinPlaceShowPayoff(SampleWagerPayoffPools.expectedPlaceAndShowPayoff());

            Starter third = new Starter.Builder().program("3")
                    .horse(new Horse("Midnightwithdrawal"))
                    .jockey(new Jockey("Alfredi", "Triana Jr.")).build();
            third.setWinPlaceShowPayoff(SampleWagerPayoffPools.expectedShowPayoff());

            // null program so payoff should be matched by name
            Starter alsoThird = new Starter.Builder().program(null)
                    .horse(new Horse("Tiznow"))
                    .jockey(new Jockey("Chris", "McCarron")).build();
            alsoThird.setWinPlaceShowPayoff(SampleWagerPayoffPools
                    .expectedShowPayoffNullProgram());

            Starter fifth = new Starter.Builder().program("7A")
                    .horse(new Horse("Rachel Alexandra"))
                    .jockey(new Jockey("Calvin", "Borel")).build();
            fifth.setWinPlaceShowPayoff(SampleWagerPayoffPools.expectedWinPlaceAndShowPayoff());

            Starter sixth = new Starter.Builder().program("4F")
                    .horse(new Horse("Zenyatta"))
                    .jockey(new Jockey("Mike", "Smith")).build();
            sixth.setWinPlaceShowPayoff(null);

            Starter seventh = new Starter.Builder().program("7B")
                    .horse(new Horse("Ghostzapper"))
                    .jockey(new Jockey("Javier", "Castellano")).build();
            seventh.setWinPlaceShowPayoff(SampleWagerPayoffPools.expectedWinPlaceAndShowPayoff());

            // null program but should not have any matched payoff
            Starter eighth = new Starter.Builder().program(null)
                    .horse(new Horse("Arazi"))
                    .jockey(new Jockey("Pat", "Valenzuela")).build();
            eighth.setWinPlaceShowPayoff(null);

            add(first);
            add(second);
            add(third);
            add(alsoThird);
            add(fifth);
            add(sixth);
            add(seventh);
            add(eighth);
        }};

        RaceResult.Builder raceBuilder = new RaceResult.Builder();
        List<Starter> starters = new ArrayList<Starter>() {{
            add(new Starter.Builder().program("7")
                    .horse(new Horse("Prater Sixty Four"))
                    .jockey(new Jockey("Karlo", "Lopez")).build());
            add(new Starter.Builder().program("8")
                    .horse(new Horse("Candy Sweetheart"))
                    .jockey(new Jockey("Dennis", "Collins")).build());
            add(new Starter.Builder().program("3")
                    .horse(new Horse("Midnightwithdrawal"))
                    .jockey(new Jockey("Alfredi", "Triana Jr.")).build());
            add(new Starter.Builder().program(null)
                    .horse(new Horse("Tiznow"))
                    .jockey(new Jockey("Chris", "McCarron")).build());
            add(new Starter.Builder().program("7A")
                    .horse(new Horse("Rachel Alexandra"))
                    .jockey(new Jockey("Calvin", "Borel")).build());
            add(new Starter.Builder().program("4F")
                    .horse(new Horse("Zenyatta"))
                    .jockey(new Jockey("Mike", "Smith")).build());
            add(new Starter.Builder().program("7B")
                    .horse(new Horse("Ghostzapper"))
                    .jockey(new Jockey("Javier", "Castellano")).build());
            add(new Starter.Builder().program(null)
                    .horse(new Horse("Arazi"))
                    .jockey(new Jockey("Pat", "Valenzuela")).build());
        }};

        WagerPayoffPools expectedWagerPayoffPools = SampleWagerPayoffPools
                .expectedWagerPayoffPools();
        // add example to handle when starters have missing program numbers
        expectedWagerPayoffPools.getWinPlaceShowPayoffPools().getWinPlaceShowPayoffs()
                .add(SampleWagerPayoffPools.expectedShowPayoffNullProgram());

        // method under test
        starters = raceBuilder.updateStartersWithWinPlaceShowPayoffs(starters,
                expectedWagerPayoffPools);

        assertThat(starters, equalTo(expected));
    }

    @Test
    public void updateStartersWithOddsChoiceIndicies_WithFiveStarters_UpdatesChoiceWhenOdds() {
        List<Starter> expected = new ArrayList<Starter>() {{
            // favorite
            Starter first = new Starter.Builder().odds(new Odds(2.0, true))
                    .horse(new Horse("Prater Sixty Four"))
                    .jockey(new Jockey("Karlo", "Lopez")).build();
            first.setChoice(1);

            // joint-2nd favorite
            Starter second = new Starter.Builder().odds(new Odds(4.0, false))
                    .horse(new Horse("Candy Sweetheart"))
                    .jockey(new Jockey("Dennis", "Collins")).build();
            second.setChoice(2);

            // no odds, therefore no choice
            Starter third = new Starter.Builder()
                    .horse(new Horse("Midnightwithdrawal"))
                    .jockey(new Jockey("Alfredi", "Triana Jr.")).build();

            // 4th favorite
            Starter fourth = new Starter.Builder().odds(new Odds(10.0, false))
                    .horse(new Horse("Prowers County"))
                    .jockey(new Jockey("Carl", "Williams")).build();
            fourth.setChoice(4);

            // joint-2nd favorite
            Starter fifth = new Starter.Builder().odds(new Odds(4.0, false))
                    .horse(new Horse("Al Baz (GB)"))
                    .jockey(new Jockey("Tracy", "Hebert")).build();
            fifth.setChoice(2);

            add(first);
            add(second);
            add(third);
            add(fourth);
            add(fifth);
        }};

        RaceResult.Builder raceBuilder = new RaceResult.Builder();
        List<Starter> starters = new ArrayList<Starter>() {{
            add(new Starter.Builder().odds(new Odds(2.0, true))
                    .horse(new Horse("Prater Sixty Four"))
                    .jockey(new Jockey("Karlo", "Lopez")).build());
            add(new Starter.Builder().odds(new Odds(4.0, false))
                    .horse(new Horse("Candy Sweetheart"))
                    .jockey(new Jockey("Dennis", "Collins")).build());
            add(new Starter.Builder()
                    .horse(new Horse("Midnightwithdrawal"))
                    .jockey(new Jockey("Alfredi", "Triana Jr.")).build());
            add(new Starter.Builder().odds(new Odds(10.0, false))
                    .horse(new Horse("Prowers County"))
                    .jockey(new Jockey("Carl", "Williams")).build());
            add(new Starter.Builder().odds(new Odds(4.0, false))
                    .horse(new Horse("Al Baz (GB)"))
                    .jockey(new Jockey("Tracy", "Hebert")).build());
        }};

        // method under test
        starters = raceBuilder.updateStartersWithOddsChoiceIndicies(starters);

        assertThat(starters, equalTo(expected));
    }

    @Test
    public void calculateIndividualFractionalsAndSplits_WithTBred_CreatesFractionalsAndSplits() {
        List<Starter> expectedStarters = new ArrayList<Starter>() {{
            // Back Stop
            Starter.Builder first = new Starter.Builder();
            first.horse(new Horse("Back Stop")).jockey(new Jockey("Dennis", "Collins"));
            first.pointsOfCall(new ArrayList<PointOfCall>() {{
                PointOfCall first = new PointOfCall(1, "Start",
                        "Start", null);
                first.setRelativePosition(new PointOfCall.RelativePosition(1, null));
                add(first);

                PointOfCall second = new PointOfCall(2, "1/4", "2f",
                        1320);
                second.setRelativePosition(
                        new PointOfCall.RelativePosition(1,
                                new PointOfCall.RelativePosition.LengthsAhead("2",
                                        2.0)));
                add(second);

                PointOfCall third = new PointOfCall(3, "1/2", "4f", 2640);
                third.setRelativePosition(
                        new PointOfCall.RelativePosition(1,
                                new PointOfCall.RelativePosition.LengthsAhead("Head",
                                        0.1)));
                add(third);

                PointOfCall fourth = new PointOfCall(5, "Str", "5f",
                        3300);
                fourth.setRelativePosition(
                        new RelativePosition(1,
                                new RelativePosition.LengthsAhead("1/2",
                                        0.5)));
                add(fourth);

                PointOfCall fifth = new PointOfCall(6, "Fin", "6f", 3960);
                fifth.setRelativePosition(
                        new RelativePosition(1,
                                new LengthsAhead("1 1/2",
                                        1.5)));
                add(fifth);
            }});
            Fractional frac1_1 = new Fractional(1, "1/4", "2f", 1320, "0:22.880", 22880L);
            Fractional frac1_2 = new Fractional(2, "1/2", "4f", 2640, "0:46.500", 46500L);
            Fractional frac1_3 = new Fractional(3, "5/8", "5f", 3300, "0:59.310", 59310L);
            Fractional frac1_6 = new Fractional(6, "Fin", "6f", 3960, "1:12.980", 72980L);
            Starter firstStarter = first.build();
            firstStarter.setFractionals(new ArrayList<Fractional>() {{
                add(frac1_1);
                add(frac1_2);
                add(frac1_3);
                add(frac1_6);
            }});
            firstStarter.setSplits(new ArrayList<Split>() {{
                add(Split.calculate(null, frac1_1));
                add(Split.calculate(frac1_1, frac1_2));
                add(Split.calculate(frac1_2, frac1_3));
                add(Split.calculate(frac1_3, frac1_6));
            }});
            add(firstStarter);

            // Regal Sunset
            Starter.Builder second = new Starter.Builder();
            second.horse(new Horse("Regal Sunset")).jockey(new Jockey("Vince", "Guerra"));
            second.pointsOfCall(new ArrayList<PointOfCall>() {{
                PointOfCall first = new PointOfCall(1, "Start", "Start", null);
                first.setRelativePosition(new RelativePosition(2, null));
                add(first);

                PointOfCall second = new PointOfCall(2, "1/4", "2f", 1320);
                RelativePosition relativePosition2 = new RelativePosition(2,
                        new LengthsAhead("1 1/2", 1.5));
                relativePosition2.setTotalLengthsBehind(new TotalLengthsBehind("2", 2.0));
                second.setRelativePosition(relativePosition2);
                add(second);

                PointOfCall third = new PointOfCall(3, "1/2", "4f", 2640);
                RelativePosition relativePosition3 = new RelativePosition(3,
                        new LengthsAhead("4", 4.0));
                relativePosition3.setTotalLengthsBehind(new TotalLengthsBehind("Head", 0.1));
                third.setRelativePosition(relativePosition3);
                add(third);

                PointOfCall fourth = new PointOfCall(5, "Str", "5f", 3300);
                RelativePosition relativePosition5 = new RelativePosition(2,
                        new LengthsAhead("1", 1.0));
                relativePosition5.setTotalLengthsBehind(new TotalLengthsBehind("1/2", 0.5));
                fourth.setRelativePosition(relativePosition5);
                add(fourth);

                PointOfCall fifth = new PointOfCall(6, "Fin", "6f", 3960);
                RelativePosition relativePosition6 = new RelativePosition(2,
                        new LengthsAhead("3", 3.0));
                relativePosition6.setTotalLengthsBehind(new TotalLengthsBehind("1 1/2", 1.5));
                fifth.setRelativePosition(relativePosition6);
                add(fifth);
            }});
            Starter secondStarter = second.build();
            Fractional frac2_1 = new Fractional(1, "1/4", "2f", 1320, "0:23.183", 23183L);
            Fractional frac2_2 = new Fractional(2, "1/2", "4f", 2640, "0:46.515", 46515L);
            Fractional frac2_3 = new Fractional(3, "5/8", "5f", 3300, "0:59.388", 59388L);
            Fractional frac2_6 = new Fractional(6, "Fin", "6f", 3960, "1:13.221", 73221L);
            secondStarter.setFractionals(new ArrayList<Fractional>() {{
                add(frac2_1);
                add(frac2_2);
                add(frac2_3);
                add(frac2_6);
            }});
            secondStarter.setSplits(new ArrayList<Split>() {{
                add(Split.calculate(null, frac2_1));
                add(Split.calculate(frac2_1, frac2_2));
                add(Split.calculate(frac2_2, frac2_3));
                add(Split.calculate(frac2_3, frac2_6));
            }});
            add(secondStarter);
        }};

        // starters for the test
        List<Starter> starters = new ArrayList<Starter>() {{
            // Back Stop
            Starter.Builder first = new Starter.Builder();
            first.horse(new Horse("Back Stop")).jockey(new Jockey("Dennis", "Collins"));
            first.pointsOfCall(new ArrayList<PointOfCall>() {{
                PointOfCall first = new PointOfCall(1, "Start",
                        "Start", null);
                first.setRelativePosition(new PointOfCall.RelativePosition(1, null));
                add(first);

                PointOfCall second = new PointOfCall(2, "1/4", "2f",
                        1320);
                second.setRelativePosition(
                        new PointOfCall.RelativePosition(1,
                                new PointOfCall.RelativePosition.LengthsAhead("2",
                                        2.0)));
                add(second);

                PointOfCall third = new PointOfCall(3, "1/2", "4f", 2640);
                third.setRelativePosition(
                        new PointOfCall.RelativePosition(1,
                                new PointOfCall.RelativePosition.LengthsAhead("Head",
                                        0.1)));
                add(third);

                PointOfCall fourth = new PointOfCall(5, "Str", "5f",
                        3300);
                fourth.setRelativePosition(
                        new PointOfCall.RelativePosition(1,
                                new PointOfCall.RelativePosition.LengthsAhead("1/2",
                                        0.5)));
                add(fourth);

                PointOfCall fifth = new PointOfCall(6, "Fin", "6f", 3960);
                fifth.setRelativePosition(
                        new PointOfCall.RelativePosition(1,
                                new PointOfCall.RelativePosition.LengthsAhead("1 1/2",
                                        1.5)));
                add(fifth);
            }});
            add(first.build());

            // Regal Sunset
            Starter.Builder second = new Starter.Builder();
            second.horse(new Horse("Regal Sunset")).jockey(new Jockey("Vince", "Guerra"));
            second.pointsOfCall(new ArrayList<PointOfCall>() {{
                PointOfCall first = new PointOfCall(1, "Start", "Start", null);
                first.setRelativePosition(new RelativePosition(2, null));
                add(first);

                PointOfCall second = new PointOfCall(2, "1/4", "2f", 1320);
                RelativePosition relativePosition2 = new RelativePosition(2,
                        new LengthsAhead("1 1/2", 1.5));
                relativePosition2.setTotalLengthsBehind(new TotalLengthsBehind
                        ("2", 2.0));
                second.setRelativePosition(relativePosition2);
                add(second);

                PointOfCall third = new PointOfCall(3, "1/2", "4f", 2640);
                RelativePosition relativePosition3 = new RelativePosition(3,
                        new LengthsAhead("4", 4.0));
                relativePosition3.setTotalLengthsBehind(new TotalLengthsBehind
                        ("Head", 0.1));
                third.setRelativePosition(relativePosition3);
                add(third);

                PointOfCall fourth = new PointOfCall(5, "Str", "5f", 3300);
                RelativePosition relativePosition5 = new RelativePosition(2,
                        new LengthsAhead("1", 1.0));
                relativePosition5.setTotalLengthsBehind(new TotalLengthsBehind
                        ("1/2", 0.5));
                fourth.setRelativePosition(relativePosition5);
                add(fourth);

                PointOfCall fifth = new PointOfCall(6, "Fin", "6f", 3960);
                RelativePosition relativePosition6 = new RelativePosition(2,
                        new LengthsAhead("3", 3.0));
                relativePosition6.setTotalLengthsBehind(new TotalLengthsBehind
                        ("1 1/2", 1.5));
                fifth.setRelativePosition(relativePosition6);
                add(fifth);
            }});
            add(second.build());
        }};

        RaceResult.Builder raceBuilder = new RaceResult.Builder();

        List<Fractional> fractionals = new ArrayList<>();
        fractionals.add(new Fractional(1, "1/4", "2f", 1320, "0:22.880", 22880L));
        fractionals.add(new Fractional(2, "1/2", "4f", 2640, "0:46.500", 46500L));
        fractionals.add(new Fractional(3, "5/8", "5f", 3300, "0:59.310", 59310L));
        fractionals.add(new Fractional(6, "Fin", "6f", 3960, "1:12.980", 72980L));

        // method under test
        starters = raceBuilder.calculateFractionalsAndSplits(starters, fractionals);

        // check that the each starter's individual fractionals and splits were calculated
        assertThat(starters, equalTo(expectedStarters));
    }

    @Test
    public void calculateRaceFractionalsAndSplits_WithQH_CreatesFractionalsAndSplits() {
        RaceResult.Builder raceBuilder = new RaceResult.Builder();

        // method under test
        raceBuilder.calculateRaceFractionalsFromWinner(SampleStarters.quarterHorseStarters(), null);

        RaceResult raceResult = raceBuilder.build();

        // check that the race has the winner's fractionals
        Fractional fin = new Fractional(6, "Fin", "350y", 1050, "0:18.015", 18015L);
        List<Fractional> expectedFractionals = new ArrayList<Fractional>() {{
            add(fin);
        }};
        assertThat(raceResult.getFractionals(), equalTo(expectedFractionals));

        // check that the race has the winner's splits
        List<Split> expectedSplits = new ArrayList<Split>() {{
            add(Split.calculate(null, fin));
        }};
        assertThat(raceResult.getSplits(), equalTo(expectedSplits));
    }

    @Test
    public void detectDeadHeat_WithWithUniqueOfficialPositions_ReturnsFalse() {
        List<Starter> starters = new ArrayList<Starter>() {{
            Starter first = new Starter.Builder().program("7")
                    .horse(new Horse("Prater Sixty Four"))
                    .jockey(new Jockey("Karlo", "Lopez")).build();
            first.setOfficialPosition(1);

            Starter second = new Starter.Builder().program("8")
                    .horse(new Horse("Candy Sweetheart"))
                    .jockey(new Jockey("Dennis", "Collins")).build();
            second.setOfficialPosition(2);

            Starter third = new Starter.Builder().program("3")
                    .horse(new Horse("Midnightwithdrawal"))
                    .jockey(new Jockey("Alfredi", "Triana Jr.")).build();
            third.setOfficialPosition(3);

            add(first);
            add(second);
            add(third);
        }};

        RaceResult.Builder raceBuilder = new RaceResult.Builder();
        assertFalse(raceBuilder.detectDeadHeat(starters));
    }

    @Test
    public void detectDeadHeat_WithTwoStartersWithOfficialPositionOne_ReturnsTrue() {
        List<Starter> starters = new ArrayList<Starter>() {{
            Starter first = new Starter.Builder().program("7")
                    .horse(new Horse("Prater Sixty Four"))
                    .jockey(new Jockey("Karlo", "Lopez")).build();
            first.setOfficialPosition(1);

            Starter second = new Starter.Builder().program("8")
                    .horse(new Horse("Candy Sweetheart"))
                    .jockey(new Jockey("Dennis", "Collins")).build();
            second.setOfficialPosition(1);

            Starter third = new Starter.Builder().program("3")
                    .horse(new Horse("Midnightwithdrawal"))
                    .jockey(new Jockey("Alfredi", "Triana Jr.")).build();
            third.setOfficialPosition(3);

            add(first);
            add(second);
            add(third);
        }};

        RaceResult.Builder raceBuilder = new RaceResult.Builder();
        assertTrue(raceBuilder.detectDeadHeat(starters));
    }

    @Test
    public void detectDeadHeat_WithTwoStartersWithOfficialPositionTwo_ReturnsFalse() {
        List<Starter> starters = new ArrayList<Starter>() {{
            Starter first = new Starter.Builder().program("7")
                    .horse(new Horse("Prater Sixty Four"))
                    .jockey(new Jockey("Karlo", "Lopez")).build();
            first.setOfficialPosition(1);

            Starter second = new Starter.Builder().program("8")
                    .horse(new Horse("Candy Sweetheart"))
                    .jockey(new Jockey("Dennis", "Collins")).build();
            second.setOfficialPosition(2);

            Starter third = new Starter.Builder().program("3")
                    .horse(new Horse("Midnightwithdrawal"))
                    .jockey(new Jockey("Alfredi", "Triana Jr.")).build();
            third.setOfficialPosition(2);

            Starter last = new Starter.Builder().program("4")
                    .horse(new Horse("Did Not Finish"))
                    .jockey(new Jockey("No", "Name")).build();
            third.setOfficialPosition(null);

            add(first);
            add(second);
            add(third);
            add(last);
        }};

        RaceResult.Builder raceBuilder = new RaceResult.Builder();
        assertFalse(raceBuilder.detectDeadHeat(starters));
    }


    @Test
    public void markCoupledAndFieldEntries_WithTenEntries_MarksNineCoupledOrFieldEntries() {
        List<Starter> expected = new ArrayList<Starter>() {{
            Starter first = new Starter.Builder().program("2")
                    .horse(new Horse("Sword Trick"))
                    .jockey(new Jockey("M.", "Berry")).build();
            first.setEntry(true);

            Starter second = new Starter.Builder().program("4D")
                    .horse(new Horse("Boca Bay"))
                    .jockey(new Jockey("Chris", "Landeros")).build();
            second.setEntry(true);

            Starter third = new Starter.Builder().program("3c")
                    .horse(new Horse("Swass Like Me"))
                    .jockey(new Jockey("Jose", "Medina")).build();
            third.setEntry(true);

            Starter fourth = new Starter.Builder().program("4F")
                    .horse(new Horse("Field Goal"))
                    .jockey(new Jockey("Luis", "Quinonez")).build();
            fourth.setEntry(true);

            Starter fifth = new Starter.Builder().program("2B")
                    .horse(new Horse("Coyote Canyon"))
                    .jockey(new Jockey("Roman", "Chapa")).build();
            fifth.setEntry(true);

            Starter sixth = new Starter.Builder().program("1a")
                    .horse(new Horse("Jones Way"))
                    .jockey(new Jockey("Junior", "Chacaltana")).build();
            sixth.setEntry(true);

            Starter seventh = new Starter.Builder().program("3F")
                    .horse(new Horse("Statler"))
                    .jockey(new Jockey("Erik", "McNeil")).build();
            seventh.setEntry(true);

            // uncoupled
            Starter eighth = new Starter.Builder().program("5")
                    .horse(new Horse("Primistalla"))
                    .jockey(new Jockey("Tony", "McNeil")).build();
            eighth.setEntry(false);

            Starter ninth = new Starter.Builder().program("1")
                    .horse(new Horse("Jones Focus"))
                    .jockey(new Jockey("Gerard", "Melancon")).build();
            ninth.setEntry(true);

            Starter tenth = new Starter.Builder().program("1X")
                    .horse(new Horse("Secretariat"))
                    .jockey(new Jockey("Ron", "Turcotte")).build();
            tenth.setEntry(true);

            add(first);
            add(second);
            add(third);
            add(fourth);
            add(fifth);
            add(sixth);
            add(seventh);
            add(eighth); // uncoupled
            add(ninth);
            add(tenth);
        }};

        RaceResult.Builder raceBuilder = new RaceResult.Builder();
        List<Starter> starters = new ArrayList<Starter>() {{
            add(new Starter.Builder().program("2")
                    .horse(new Horse("Sword Trick"))
                    .jockey(new Jockey("M.", "Berry")).build());
            add(new Starter.Builder().program("4D")
                    .horse(new Horse("Boca Bay"))
                    .jockey(new Jockey("Chris", "Landeros")).build());
            add(new Starter.Builder().program("3c")
                    .horse(new Horse("Swass Like Me"))
                    .jockey(new Jockey("Jose", "Medina")).build());
            add(new Starter.Builder().program("4F")
                    .horse(new Horse("Field Goal"))
                    .jockey(new Jockey("Luis", "Quinonez")).build());
            add(new Starter.Builder().program("2B")
                    .horse(new Horse("Coyote Canyon"))
                    .jockey(new Jockey("Roman", "Chapa")).build());
            add(new Starter.Builder().program("1a")
                    .horse(new Horse("Jones Way"))
                    .jockey(new Jockey("Junior", "Chacaltana")).build());
            add(new Starter.Builder().program("3F")
                    .horse(new Horse("Statler"))
                    .jockey(new Jockey("Erik", "McNeil")).build());
            add(new Starter.Builder().program("5")
                    .horse(new Horse("Primistalla"))
                    .jockey(new Jockey("Tony", "McNeil")).build());
            add(new Starter.Builder().program("1")
                    .horse(new Horse("Jones Focus"))
                    .jockey(new Jockey("Gerard", "Melancon")).build());
            add(new Starter.Builder().program("1X")
                    .horse(new Horse("Secretariat"))
                    .jockey(new Jockey("Ron", "Turcotte")).build());
        }};

        // method under test
        starters = raceBuilder.markCoupledAndFieldEntries(starters);

        assertThat(starters, equalTo(expected));
    }

    @Test
    public void markPositionDeadHeats_WithDeadHeatForSecond_MarksThosePositionDeadHeatsAsTrue() {
        // expected (has positionDeadHeat == true for second and third)
        List<Starter> expected = new ArrayList<Starter>() {{
            Starter first = new Starter.Builder().program("7")
                    .horse(new Horse("Prater Sixty Four"))
                    .jockey(new Jockey("Karlo", "Lopez")).build();
            first.setFinishPosition(1);
            first.setOfficialPosition(1);
            first.setPositionDeadHeat(false);

            Starter second = new Starter.Builder().program("8")
                    .horse(new Horse("Candy Sweetheart"))
                    .jockey(new Jockey("Dennis", "Collins")).build();
            second.setFinishPosition(2);
            second.setOfficialPosition(2);
            second.setPositionDeadHeat(true);

            Starter third = new Starter.Builder().program("3")
                    .horse(new Horse("Midnightwithdrawal"))
                    .jockey(new Jockey("Alfredi", "Triana Jr.")).build();
            third.setFinishPosition(2);
            third.setOfficialPosition(3);
            third.setPositionDeadHeat(true);
            third.setDisqualified(true);

            add(first);
            add(second);
            add(third);
        }};


        // actual
        List<Starter> starters = new ArrayList<Starter>() {{
            Starter first = new Starter.Builder().program("7")
                    .horse(new Horse("Prater Sixty Four"))
                    .jockey(new Jockey("Karlo", "Lopez")).build();
            first.setFinishPosition(1);

            Starter second = new Starter.Builder().program("8")
                    .horse(new Horse("Candy Sweetheart"))
                    .jockey(new Jockey("Dennis", "Collins")).build();
            second.setFinishPosition(2);

            Starter deadHeatSecondButDQToThird = new Starter.Builder().program("3")
                    .horse(new Horse("Midnightwithdrawal"))
                    .jockey(new Jockey("Alfredi", "Triana Jr.")).build();
            deadHeatSecondButDQToThird.setFinishPosition(2);
            deadHeatSecondButDQToThird.updateDisqualification(
                    new Disqualification("3", new Horse("Midnightwithdrawal"), 2, 3));

            add(first);
            add(second);
            add(deadHeatSecondButDQToThird);
        }};

        RaceResult.Builder raceBuilder = new RaceResult.Builder();
        starters = raceBuilder.markPositionDeadHeats(starters);

        // ensure the race isn't reporting a dead heat - that's only for when winners dead heat
        assertFalse(raceBuilder.detectDeadHeat(starters));

        assertThat(starters, equalTo(expected));
    }
}