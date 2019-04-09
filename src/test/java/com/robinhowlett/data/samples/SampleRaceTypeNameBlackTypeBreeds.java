package com.robinhowlett.data.samples;

import com.robinhowlett.data.RaceTypeNameBlackTypeBreed;

import static com.robinhowlett.data.Breed.QUARTER_HORSE;
import static com.robinhowlett.data.Breed.THOROUGHBRED;

public class SampleRaceTypeNameBlackTypeBreeds {

    public static RaceTypeNameBlackTypeBreed maidenQH() {
        return new RaceTypeNameBlackTypeBreed("MAIDEN", QUARTER_HORSE);
    }

    public static RaceTypeNameBlackTypeBreed mswTB() {
        return new RaceTypeNameBlackTypeBreed("MAIDEN SPECIAL WEIGHT", THOROUGHBRED);
    }

    public static RaceTypeNameBlackTypeBreed juvenileTurfSprintListedStakes() {
        return new RaceTypeNameBlackTypeBreed("STAKES", "Juvenile Turf Sprint S.",
                "Listed", THOROUGHBRED);
    }

    public static RaceTypeNameBlackTypeBreed goldenStateJuvBlackTypeStakes() {
        return new RaceTypeNameBlackTypeBreed("STAKES", "Golden State Juvenile S.",
                "Black Type", THOROUGHBRED);
    }

    public static RaceTypeNameBlackTypeBreed breedersCupClassicGrade1Stakes() {
        return new RaceTypeNameBlackTypeBreed("STAKES", "Breeders' Cup Classic", 1,
                "Grade 1", THOROUGHBRED);
    }

}
