package com.robinhowlett.data.wagering;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.robinhowlett.data.Horse;
import com.robinhowlett.data.Starter;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static com.robinhowlett.data.Race.round;

/**
 * Parses the wagering grid and stores {@link WinPlaceShowPayoffPool} and {@link ExoticPayoffPool}
 * instances
 */
@Data
public class WagerPayoffPools {

    @JsonProperty("winPlaceShow")
    private final WinPlaceShowPayoffPool winPlaceShowPayoffPools;
    @JsonProperty("exotics")
    private final List<ExoticPayoffPool> exoticPayoffPools;

    @JsonCreator
    public WagerPayoffPools(WinPlaceShowPayoffPool winPlaceShowPayoffPools,
            List<ExoticPayoffPool> exoticPayoffPools) {
        this.winPlaceShowPayoffPools = winPlaceShowPayoffPools;
        this.exoticPayoffPools = exoticPayoffPools;
    }

    @EqualsAndHashCode
    abstract static class Wager {
        @Getter
        protected final Double unit;
        @Getter
        protected final Double payoff;
        protected Double odds; // for JSON

        public Wager(Double unit, Double payoff) {
            this.unit = unit;
            this.payoff = payoff;
        }

        public Double getOdds() {
            if (unit != null && payoff != null && unit > 0 && payoff > 0) {
                double calc = ((payoff - unit) / unit);
                if (!Double.isInfinite(calc)) {
                    return round(calc).doubleValue();
                }
            }
            return null;
        }
    }

    /**
     * Stores the total Win-Place-Show (WPS) pool amount, and a list of the various {@link
     * WinPlaceShowPayoff} wager details
     */
    @EqualsAndHashCode
    public static class WinPlaceShowPayoffPool {

        @JsonProperty("totalWPSPool")
        @Getter
        private final Integer totalWinPlaceShowPool;
        @JsonProperty("payoffs")
        @Getter
        private final List<WinPlaceShowPayoff> winPlaceShowPayoffs;

        @JsonCreator
        public WinPlaceShowPayoffPool(Integer totalWinPlaceShowPool,
                List<WinPlaceShowPayoff> winPlaceShowPayoffs) {
            this.totalWinPlaceShowPool = totalWinPlaceShowPool;
            this.winPlaceShowPayoffs = winPlaceShowPayoffs;
        }

        /**
         * For Win-Place-Show (WPS) wager payoffs, this stores the program number of the {@link
         * com.robinhowlett.data.Starter}, the horse name, and, where applicable, the win, place,
         * and tshow payoff amounts. All payoffs amounts are based on a $2 unit.
         */
        @EqualsAndHashCode
        @ToString
        public static class WinPlaceShowPayoff {
            @Getter
            private final String program;
            @JsonIgnoreProperties({"color", "sex", "sire", "dam", "damSire", "foalingDate",
                    "foalingLocation", "breeder"})
            @Getter
            private final Horse horse;
            @Getter
            private final Win win;
            @Getter
            private final Place place;
            @Getter
            private final Show show;
            @JsonIgnore
            @Getter
            private final List<WinPlaceShow> winPlaceShows;

            public WinPlaceShowPayoff(Double winPayoff, Double placePayoff, Double showPayoff) {
                this(null, null,
                        (winPayoff != null ? new Win(winPayoff) : null),
                        (placePayoff != null ? new Place(placePayoff) : null),
                        (showPayoff != null ? new Show(showPayoff) : null));
            }

            @JsonCreator
            public WinPlaceShowPayoff(String program, Horse horse, Win win, Place place,
                    Show show) {
                this.program = program;
                this.horse = horse;
                this.win = win;
                this.place = place;
                this.show = show;

                winPlaceShows = new ArrayList<>();
                if (win != null) {
                    winPlaceShows.add(win);
                }
                if (place != null) {
                    winPlaceShows.add(place);
                }
                if (show != null) {
                    winPlaceShows.add(show);
                }
            }

            public WinPlaceShowPayoff(String program, Horse horse, Double winPayoff,
                    Double placePayoff, Double showPayoff) {
                this(
                        (program != null ? program.toUpperCase() : null),
                        horse,
                        (winPayoff != null ? new Win(winPayoff) : null),
                        (placePayoff != null ? new Place(placePayoff) : null),
                        (showPayoff != null ? new Show(showPayoff) : null)
                );
            }

            @JsonIgnore
            public String getEntryProgram() {
                return Starter.getEntryProgram(program);
            }

            @ToString
            enum WPSType {
                WIN("Win"),
                PLACE("Place"),
                SHOW("Show");

                @Getter
                private final String name;

                WPSType(String name) {
                    this.name = name;
                }
            }

            @ToString
            public abstract static class WinPlaceShow extends Wager {
                @Getter
                private final String type;

                // $2 default for WPS
                public WinPlaceShow(Double payoff, WPSType type) {
                    super(2.0, payoff);
                    this.type = type.getName();
                }
            }

            @ToString
            public static class Win extends WinPlaceShow {
                public Win(Double payoff) {
                    super(payoff, WPSType.WIN);
                }
            }

            @ToString
            public static class Place extends WinPlaceShow {
                public Place(Double payoff) {
                    super(payoff, WPSType.PLACE);
                }
            }

            @ToString
            public static class Show extends WinPlaceShow {
                public Show(Double payoff) {
                    super(payoff, WPSType.SHOW);
                }
            }
        }

    }

    /**
     * For a particular exotic wager, parses and stories, where applicable, the name of the exotic
     * bet, the wager unit the bet is based on, a textual description of the winning numbers, the
     * number of correct selections required, the payoff amount, and the pool and carryover size
     */
    @JsonPropertyOrder({"unit", "name", "winningNumbers", "numberCorrect", "payoff", "odds",
            "pool", "carryover"})
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class ExoticPayoffPool extends Wager {
        @Getter
        private final String name;
        @Getter
        private final String winningNumbers;
        @Getter
        private final Integer numberCorrect;
        @Getter
        private final Double pool;
        @Getter
        private final Double carryover;

        public ExoticPayoffPool(WagerNameUnit wagerNameUnit,
                WinningNumbersPayoff winningNumbersPayoff, Double pool, Double carryover) {
            super((wagerNameUnit != null ? wagerNameUnit.getWagerUnit() : null),
                    (winningNumbersPayoff != null ? winningNumbersPayoff.getPayoff() : null));
            this.name = (wagerNameUnit != null ? wagerNameUnit.getName() : null);
            this.winningNumbers = (winningNumbersPayoff != null ?
                    winningNumbersPayoff.getWinningNumbers() : null);
            this.numberCorrect = (winningNumbersPayoff != null ?
                    winningNumbersPayoff.getNumberCorrect() : null);
            this.pool = pool;
            this.carryover = carryover;
        }

        @JsonCreator
        public ExoticPayoffPool(Double unit, Double payoff, String name, String winningNumbers,
                Integer numberCorrect, Double pool, Double carryover) {
            super(unit, payoff);
            this.name = name;
            this.winningNumbers = winningNumbers;
            this.numberCorrect = numberCorrect;
            this.pool = pool;
            this.carryover = carryover;
        }
    }

    /**
     * For exotic bets, stores the name of the wager and the unit amount the payoff is based on
     */
    @Data
    public static class WagerNameUnit {
        private final Double wagerUnit;
        private final String name;
    }

    /**
     * For exotics, stories the winning number sequence, how many correct selections were needed for
     * the bet to win (e.g. 6 in a Pick 6), and the payoff
     */
    @Data
    public static class WinningNumbersPayoff {
        private final String winningNumbers;
        private final Integer numberCorrect;
        private final Double payoff;
    }
}
