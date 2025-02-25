package org.touchsoft.dto;

public class TimestampedNumber {
    private final int number;
    private final long timestamp;

    public TimestampedNumber(int number, long timestamp) {
        this.number = number;
        this.timestamp = timestamp;
    }

    public int getNumber() {
        return number;
    }

    public long getTimestamp() {
        return timestamp;
    }
}