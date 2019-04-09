package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Stores the textual representation of the postTime time, the start comments (which note if any
 * {@link Starter} had a poor start), and, where applicable, if the timer used was electronic or
 * not
 */
@EqualsAndHashCode
@ToString
public class PostTimeStartCommentsTimer {

    @Getter
    private final String postTime;
    @Getter
    private final String startComments;
    @JsonInclude(NON_NULL)
    @Getter
    private final String timer;

    public PostTimeStartCommentsTimer(String postTime, String startComments) {
        this(postTime, startComments, null);
    }

    @JsonCreator
    public PostTimeStartCommentsTimer(String postTime, String startComments, String timer) {
        this.postTime = postTime;
        this.startComments = startComments;
        this.timer = timer;
    }
}
