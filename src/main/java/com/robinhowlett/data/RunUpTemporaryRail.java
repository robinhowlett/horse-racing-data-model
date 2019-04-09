package com.robinhowlett.data;

import lombok.Data;

/**
 * The registered run-up, measured in feet (the distance between the starting stalls and when the
 * race timer is actually started - a longer run up means the horses are potentially traveling at a
 * much higher speed already versus a short run-up)
 */
@Data
public class RunUpTemporaryRail {
    private final Integer runUp;
    private final Integer tempRail;
}
