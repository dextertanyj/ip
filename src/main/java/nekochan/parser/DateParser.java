package nekochan.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import nekochan.exceptions.NekoException;

/**
 * The {@code DateParser} class provides methods for parsing date-time and duration related strings.
 * Also provides methods for converting {@code LocalDateTime} objects to standardised strings.
 */
public class DateParser {

    private static final List<String> ACCEPTED_FORMATS_WITH_TIME = Arrays.asList("d MMM uuuu HH:mm", "d-M-uuuu HH:mm",
            "d/M/uuuu HH:mm");

    private static final List<String> ACCEPTED_FORMATS_DATE_ONLY = Arrays.asList("d MMM uuuu", "d-M-uuuu", "d/M/uuuu");

    private static final int HAS_TIME_INDICATOR = 0;
    private static final int NULL_TIME_INDICATOR = 30;

    /**
     * Parses the specified {@code input} string into a {@code LocalDateTime} object.
     * Does not support storing seconds.
     *
     * @param input the string to be parsed.
     * @return the {@code LocalDateTime} object representing the specified date and time.
     * @throws NekoException if the format of the input is not recognised.
     */
    public static LocalDateTime parseString(String input) throws NekoException {
        if (input.contains(":")) {
            for (String format : ACCEPTED_FORMATS_WITH_TIME) {
                try {
                    DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
                    DateTimeFormatter formatter = builder.parseCaseInsensitive().appendPattern(format).toFormatter();
                    return LocalDateTime.parse(input, formatter)
                            .withSecond(HAS_TIME_INDICATOR);
                } catch (DateTimeParseException e) {
                    // We continue to try other formats until all fails before we throw an exception.
                }
            }
        } else {
            for (String format : ACCEPTED_FORMATS_DATE_ONLY) {
                try {
                    // Since we do not support seconds for date and time based information, we use the second field to
                    // differentiate between a LocalDateTime with no defined time and one with time defined at midnight.
                    DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
                    DateTimeFormatter formatter = builder.parseCaseInsensitive().appendPattern(format).toFormatter();
                    return LocalDate.parse(input, formatter).atStartOfDay()
                            .withSecond(NULL_TIME_INDICATOR);
                } catch (DateTimeParseException e) {
                    // We continue to try other formats until all fails before we throw an exception.
                }
            }
        }
        throw new NekoException("I can't quite understand what you're saying...");
    }

    /**
     * Parses the specified {@code input} string into its corresponding duration in minutes.
     *
     * @param input the string to be parsed.
     * @return the parsed duration in minutes.
     * @throws NekoException if the format of the input is not recognised.
     */
    public static int parseDuration(String input) throws NekoException {
        try {
            int minutes = 0;
            if (input.contains("d")) {
                minutes += 1440 * Double.parseDouble(input.split("d")[0].trim());
                if (input.split("d").length > 1) {
                    input = input.split("d")[1].replaceAll("[^\\d]", "");
                } else {
                    input = null;
                }
            }
            if (input != null && input.contains("h")) {
                minutes += 60 * Double.parseDouble(input.split("h")[0].trim());
                if (input.split("h").length > 1) {
                    input = input.split("h")[1].replaceAll("[^\\d]", "");
                } else {
                    input = null;
                }
            }
            if (input != null && input.contains("m")) {
                minutes += Double.parseDouble(input.split("m")[0]);
            }
            return minutes;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new NekoException("I can't understand what you're saying...");
        }
    }

    /**
     * Converts the specified {@code LocalDateTime} object to a string.
     *
     * @param dateTime the {@code LocalDateTime} object to be converted.
     * @return the string representation of the specified {@code LocalDateTime} object.
     */
    public static String parseLocalDateTime(LocalDateTime dateTime) {
        if (dateTime.getSecond() == NULL_TIME_INDICATOR) {
            return dateTime.format(DateTimeFormatter.ofPattern(ACCEPTED_FORMATS_DATE_ONLY.get(0)));
        } else {
            return dateTime.format(DateTimeFormatter.ofPattern(ACCEPTED_FORMATS_WITH_TIME.get(0)));
        }
    }

    /**
     * Returns true if the specified {@code LocalDateTime} object represents only a date with no time.
     *
     * @param dateTime the {@code LocalDateTime} object to test.
     * @return true if the {@code LocalDateTime} object represents only a date with no time.
     */
    public static boolean isDateOnly(LocalDateTime dateTime) {
        return dateTime.getSecond() == NULL_TIME_INDICATOR;
    }
}
