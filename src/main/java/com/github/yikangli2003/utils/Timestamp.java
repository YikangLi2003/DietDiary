package com.github.yikangli2003.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Timestamp(int year, int month, int day, int hour, int minute, int second) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Timestamp(String timestamp) {
        this(LocalDateTime.parse(timestamp, FORMATTER));
    }

    public Timestamp(LocalDateTime dateTime) {
        this(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(),
                dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
    }

    @Override
    public String toString() {
        return LocalDateTime.of(year, month, day, hour, minute, second).format(FORMATTER);
    }
}