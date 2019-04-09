package com.robinhowlett.data.samples.running_line;

import com.robinhowlett.data.running_line.MedicationEquipment;

import static com.robinhowlett.data.running_line.MedicationEquipment.Equipment.BLINKERS;
import static com.robinhowlett.data.running_line.MedicationEquipment.Equipment.FRONT_BANDAGES;
import static com.robinhowlett.data.running_line.MedicationEquipment.Medication.BUTE;
import static com.robinhowlett.data.running_line.MedicationEquipment.Medication.LASIX;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class SampleMedicationEquipment {

    public static MedicationEquipment noMedicationOrEquipment() {
        return new MedicationEquipment("- -", emptyList(), emptyList());
    }

    public static MedicationEquipment buteLasixBlinkersAndFrontBandages() {
        return new MedicationEquipment("BL bf", asList(BUTE, LASIX),
                asList(BLINKERS, FRONT_BANDAGES));
    }

}
