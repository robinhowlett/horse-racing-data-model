package com.robinhowlett.data.running_line;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Stores the text summary of the medication and equipment used in this race by the {@link
 * com.robinhowlett.data.Starter}, and also contains list for each individual {@link Medication} and
 * {@link Equipment} instance
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MedicationEquipment {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicationEquipment.class);

    @Getter
    private final String text;
    @Getter
    @Setter
    private List<Medication> medications;
    @Getter
    @Setter
    private List<Equipment> equipment;

    public MedicationEquipment(String text) {
        this.text = text;
        this.medications = new ArrayList<>();
        this.equipment = new ArrayList<>();

        if (text != null) {
            char[] chars = text.toCharArray();
            for (char aChar : chars) {
                if (Character.isUpperCase(aChar)) {
                    Medication medication = Medication.lookup(aChar);
                    medications.add(medication);
                } else if (Character.isLowerCase(aChar) || Character.isDigit(aChar)) {
                    Equipment equip = Equipment.lookup(aChar);
                    equipment.add(equip);
                }
            }
        }
    }

    /**
     * Medications used on race day by a {@link com.robinhowlett.data.Starter}
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @ToString
    public enum Medication {
        BUTE('B', "Bute"),
        LASIX('L', "Lasix"),
        ADJUNCT('A', "Adjunct Medication"),
        FIRST_TIME_BUTE('C', "First Time Bute"),
        FIRST_TIME_LASIX('M', "First Time Lasix");

        public static final Medication[] MEDICATIONS = Medication.values();
        @Getter
        private char code;
        @Getter
        private String text;

        Medication(char code, String text) {
            this.code = code;
            this.text = text;
        }

        @JsonCreator
        public static Medication lookup(char code) {
            for (Medication medication : MEDICATIONS) {
                if (medication.code == code) {
                    return medication;
                }
            }
            LOGGER.error(String.format("Unable to find Medication for code: %s", code));
            return null;
        }

        @JsonIgnore
        public String getCodeAsString() {
            return String.valueOf(code);
        }
    }

    /**
     * Equipment used on race day by a {@link com.robinhowlett.data.Starter}
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @ToString
    public enum Equipment {
        ALUMINUM_PADS('a', "Aluminium Pads"),
        BLINKERS('b', "Blinkers"),
        MUD_CAULKS('c', "Mud Caulks"),
        GLUED_SHOES('d', "Glued Shoes"),
        INNER_RIMS('e', "Inner Rims"),
        FRONT_BANDAGES('f', "Front Bandages"),
        GOGGLES('g', "Goggles"),
        OUTER_RIMS('h', "Outer Rims"),
        INSERTS('i', "Inserts"),
        ALUMINUM_PAD('j', "Aluminium Pad"),
        FLIPPING_HALTER('k', "Flipping Halter"),
        BAR_SHOES('l', "Bar Shoes"),
        NO_WHIP('n', "No Whip"),
        BLINKER_OFF('o', "Blinker Off"),
        PADS('p', "Pads"),
        NASAL_STRIP_OFF('q', "Nasal Strip Off"),
        BAR_SHOE('r', "Bar Shoe"),
        NASAL_STRIP('s', "Nasal Strip"),
        TURNDOWNS('t', "Turndowns"),
        SPURS('u', "Spurs"),
        VISOR('v', "Visor"),
        QUEENS_PLATES('w', "Queens Plates"),
        CHEEK_PIECE_OFF('x', "Cheek Piece Off"),
        NO_SHOES('y', "No Shoes"),
        TONGUE_TIE('z', "Tongue Tie"),
        RUNNING_WS('1', "Running W's"),
        SCREENS('2', "Screens"),
        SHIELDS('3', "Shields");

        public static final Equipment[] EQUIPMENT = Equipment.values();
        @Getter
        private char code;
        @Getter
        private String text;

        Equipment(char code, String text) {
            this.code = code;
            this.text = text;
        }

        @JsonCreator
        public static Equipment lookup(char code) {
            for (Equipment equipment : EQUIPMENT) {
                if (equipment.code == code) {
                    return equipment;
                }
            }
            LOGGER.error(String.format("Unable to find Equipment for code: %s", code));
            return null;
        }

        @JsonIgnore
        public String getCodeAsString() {
            return String.valueOf(code);
        }
    }
}
