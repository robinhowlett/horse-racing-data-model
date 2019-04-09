package com.robinhowlett.data.samples;

import com.robinhowlett.data.DistanceSurfaceTrackRecord;
import com.robinhowlett.data.DistanceSurfaceTrackRecord.RaceDistance;
import com.robinhowlett.data.DistanceSurfaceTrackRecord.TrackRecord;
import com.robinhowlett.data.Horse;

import java.time.LocalDate;

public class SampleDistanceSurfaceTrackRecords {

    public static final RaceDistance sixFurlongs() {
        return new RaceDistance("Six Furlongs", "6 Furlongs", "6f", true, 3960);
    }

    public static TrackRecord noItAint10819Aug122011TrackRecord() {
        return new TrackRecord(new Horse("No It Ain't"), "1:08.190", 68190L,
                LocalDate.of(2011, 8, 12));
    }

    public static DistanceSurfaceTrackRecord arapahoeSixFurlongs() {
        return new DistanceSurfaceTrackRecord(sixFurlongs(), "Dirt", null, null,
                noItAint10819Aug122011TrackRecord());
    }

    public static DistanceSurfaceTrackRecord arapahoeSixFurlongsOffTheTurf() {
        return new DistanceSurfaceTrackRecord(sixFurlongs(), "Dirt", "Turf", null,
                noItAint10819Aug122011TrackRecord());
    }

    public static DistanceSurfaceTrackRecord arapahoeSixFurlongsOffTheTurfWithDistanceChange() {
        RaceDistance scheduledDistance =
                new RaceDistance("One And One Sixteenth Miles", "1 1/16 Miles", "1 1/16m", true,
                        5610);
        return new DistanceSurfaceTrackRecord(sixFurlongs(), "Dirt", "Turf", scheduledDistance,
                noItAint10819Aug122011TrackRecord());
    }

}
