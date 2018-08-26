package com.playgroundManager.constants;

import java.time.DayOfWeek;

public final class WorkingTime {
    public static final int OPENING_HOUR = 8;
    public static final int OPENING_MINUTE = 0;
    public static final int CLOSING_HOUR = 23;
    public static final int CLOSING_MINUTE = 0;
    // This value represent the last day this Play site works on
    public static final int LAST_WORKING_WEEKDAY = DayOfWeek.SUNDAY.getValue();
}
