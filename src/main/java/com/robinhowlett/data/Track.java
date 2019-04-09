package com.robinhowlett.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Stores a track's code, canonical code (for continuity), country, and name
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Track {

    @Setter
    private String code;
    // the original code if a track has changed code or uses multiple codes for sponsorship reasons
    // e.g. PHA for PRX, HOL for BHP and OTH, SA for OSA etc.
    @Setter
    private String canonical;
    @Setter
    private String country;
    @Setter
    private String state;
    @Setter
    private String city;
    @Getter
    @Setter
    private String name;

    public String getCode() {
        return code.trim();
    }

    public String getCanonical() {
        return (canonical != null && !canonical.trim().isEmpty() ? canonical.trim() : getCode());
    }

    public String getState() {
        return (state != null && !state.trim().isEmpty()) ? state.trim() : null;
    }

    public String getCountry() {
        return (country != null && !country.trim().isEmpty()) ? country.trim() : null;
    }

    public String getCity() {
        return (city != null && !city.trim().isEmpty()) ? city.trim() : null;
    }

}
