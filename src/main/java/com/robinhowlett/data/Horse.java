package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Stores the horse name and breeding information
 */
@JsonInclude(NON_NULL)
@EqualsAndHashCode
@ToString
public class Horse {
    @Getter
    private final String name;
    @Getter
    @Setter
    private String color;
    @Getter
    @Setter
    private String sex;
    @Getter
    @Setter
    private Horse sire;
    @Getter
    @Setter
    private Horse dam;
    @Getter
    @Setter
    private Horse damSire;
    @Getter
    @Setter
    private LocalDate foalingDate;
    @Getter
    @Setter
    private String foalingLocation;
    @Getter
    @Setter
    private Breeder breeder;

    public Horse(String name) {
        this.name = name;
    }

    @JsonCreator
    public Horse(String name, String color, String sex, Horse sire, Horse dam, Horse damSire,
            LocalDate foalingDate, String foalingLocation, Breeder breeder) {
        this.name = name;
        this.color = color;
        this.sex = sex;
        this.sire = sire;
        this.dam = dam;
        this.damSire = damSire;
        this.foalingDate = foalingDate;
        this.foalingLocation = foalingLocation;
        this.breeder = breeder;
    }
}
