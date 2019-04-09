package com.robinhowlett.data.samples;

import com.robinhowlett.data.Track;

public class SampleTracks {

    public static Track getSampleTrackAraphaoe() {
        Track track = new Track();
        track.setCode("ARP");
        track.setCanonical("ARP");
        track.setName("ARAPAHOE PARK");
        track.setCity("AURORA");
        track.setState("CO");
        track.setCountry("USA");
        return track;
    }
}
