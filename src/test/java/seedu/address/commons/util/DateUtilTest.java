package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;

import java.time.format.FormatStyle;

import org.junit.Test;

public class DateUtilTest {

    @Test
    public void getHumanReadableDateFrom() {
        // valid dates
        assertEquals(
            "February 1, 2019",
            DateUtil.getHumanReadableDateFrom("2019-02-01"));
        assertEquals(
            "February 1, 2019",
            DateUtil.getHumanReadableDateFrom("2019-02-01T01:30"));
        assertEquals(
            "February 1, 2019",
            DateUtil.getHumanReadableDateFrom("2019-02-01+01:30"));
        assertEquals("February 1, 2019",
            DateUtil.getHumanReadableDateFrom("2019-02-01T10:15:30Z"));

        // valid dates with custom format
        assertEquals(
            "Feb 1, 2019",
            DateUtil.getHumanReadableDateFrom("2019-02-01", FormatStyle.MEDIUM));

        // invalid dates are untouched
        assertEquals(
            "invalid date",
            DateUtil.getHumanReadableDateFrom("invalid date"));
        assertEquals(
            "invalid date",
            DateUtil.getHumanReadableDateFrom("invalid date", FormatStyle.MEDIUM));
    }

    @Test
    public void getHumanReadableDateTimeFrom() {
        // valid dates
        assertEquals(
            "February 1, 2019",
            DateUtil.getHumanReadableDateTimeFrom("2019-02-01"));
        assertEquals(
            "February 1, 2019, 1:30 AM",
            DateUtil.getHumanReadableDateTimeFrom("2019-02-01T01:30"));
        assertEquals(
            "February 1, 2019",
            DateUtil.getHumanReadableDateTimeFrom("2019-02-01+01:30"));
        assertEquals(
            "February 1, 2019, 10:15 AM",
            DateUtil.getHumanReadableDateTimeFrom("2019-02-01T10:15:30Z"));

        // valid dates with custom format
        assertEquals(
            "Feb 1, 2019, 10:15:30 AM",
            DateUtil.getHumanReadableDateTimeFrom("2019-02-01T10:15:30Z", FormatStyle.MEDIUM, FormatStyle.MEDIUM));

        // invalid dates are untouched
        assertEquals(
            "invalid date",
            DateUtil.getHumanReadableDateTimeFrom("invalid date"));
        assertEquals(
            "invalid date",
            DateUtil.getHumanReadableDateTimeFrom("invalid date", FormatStyle.MEDIUM, FormatStyle.MEDIUM));
    }

}
