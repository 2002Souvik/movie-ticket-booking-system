package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public static String format(LocalDateTime dt) {
        if (dt == null) return "";
        return dt.format(DATE_TIME_FMT);
    }

    public static String formatTime(LocalDateTime dt) {
        if (dt == null) return "";
        return dt.format(TIME_FMT);
    }

    public static LocalDateTime parse(String s) {
        if (s == null || s.isEmpty()) return null;
        return LocalDateTime.parse(s, DATE_TIME_FMT);
    }
}
