package ru.javawebinar.topjava.web.json;

import org.springframework.format.Formatter;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Locale;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

public class TimeFormatter implements Formatter<LocalTime> {

    @Override
    public String print(LocalTime time, Locale locale) {
        if (time == null) {
            return "";
        }
        return time.toString();
    }

    @Override
    public LocalTime parse(String formatted, Locale locale) throws ParseException {
        if (formatted.length() == 0) {
            return null;
        }
        return parseLocalTime(formatted);
    }
}