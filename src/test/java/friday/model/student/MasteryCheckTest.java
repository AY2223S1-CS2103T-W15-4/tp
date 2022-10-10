package friday.model.student;

import static friday.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class MasteryCheckTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new MasteryCheck(null));
    }

    @Test
    public void isValidMasteryCheck() {
        assertFalse(MasteryCheck.isValidMasteryCheck("09-01-2022")); // wrong order
        assertFalse(MasteryCheck.isValidMasteryCheck("20220901")); // missing '-' symbols
        assertFalse(MasteryCheck.isValidMasteryCheck("01-01")); // missing year
        assertFalse(MasteryCheck.isValidMasteryCheck("0000-12-05")); // invalid year
        assertFalse(MasteryCheck.isValidMasteryCheck("2022-13-05")); // invalid month
        assertFalse(MasteryCheck.isValidMasteryCheck("0000-12-32")); // invalid day
        assertFalse(MasteryCheck.isValidMasteryCheck("2022/09/01")); // '/' instead of '-'
        assertFalse(MasteryCheck.isValidMasteryCheck("20 22-09-01")); // space in year
        assertFalse(MasteryCheck.isValidMasteryCheck("2022- 09 -01")); // spaces in month
        assertFalse(MasteryCheck.isValidMasteryCheck(" 2022-09-01")); // leading space
        assertFalse(MasteryCheck.isValidMasteryCheck("2022-09-01 ")); // trailing space
        assertFalse(MasteryCheck.isValidMasteryCheck("2022--09--01")); // double '-' symbol
        assertFalse(MasteryCheck.isValidMasteryCheck("2022-09-0-1")); // extra '-' symbol


        // valid dates
        assertTrue(MasteryCheck.isValidMasteryCheck("2019-06-07"));
        assertTrue(MasteryCheck.isValidMasteryCheck("2020-02-28"));
    }

    @Test
    public void isEmpty() {
        // not the empty instance
        assertFalse(new MasteryCheck(LocalDate.of(2001, 01, 01)).isEmpty());

        // the empty instance
        assertTrue(MasteryCheck.EMPTY_MASTERYCHECK.isEmpty());
    }
}
