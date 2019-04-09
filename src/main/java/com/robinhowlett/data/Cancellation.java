package com.robinhowlett.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Stores if a race was cancelled and, if provided, for what reason
 */
@JsonInclude(NON_NULL)
@JsonPropertyOrder({"cancelled", "reason"})
@EqualsAndHashCode
@ToString
public class Cancellation {

    public static final String NO_REASON_AVAILABLE = null;

    @Getter
    private final boolean cancelled;
    @Getter
    private final String reason;

    public Cancellation(String reason) {
        this(true, reason);
    }

    public Cancellation(boolean cancelled, String reason) {
        this.cancelled = cancelled;
        this.reason = reason;
    }

    public static Cancellation notCancelled() {
        return new Cancellation(false, NO_REASON_AVAILABLE);
    }
}
