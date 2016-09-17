package me.dong.exwindowmanager;

import org.junit.Test;

import me.dong.exwindowmanager.util.PhoneNumberUtils;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class PhoneNumberUtilsUnitTest {

    @Test
    public void formatNumber_isCorrect() throws Exception {

        assertEquals("010-1234-1234", PhoneNumberUtils.formatNumber("01012341234"));
        assertEquals("010-123-1234", PhoneNumberUtils.formatNumber("0101231234"));
        assertEquals("02-123-1234", PhoneNumberUtils.formatNumber("021231234"));
    }


}