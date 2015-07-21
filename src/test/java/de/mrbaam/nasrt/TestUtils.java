package de.mrbaam.nasrt;

import static org.junit.Assert.assertFalse;

/**
 * Created by mrbaam on 21.07.2015.
 * @author mrbaam
 */
public class TestUtils {
    public static boolean checkRelease(String name, boolean checkValue, String fileName) {
        if (fileName.equals(name))
        {
            if (!checkValue)
                checkValue = true;
            else
                assertFalse(name + " already exists!", true);
        }

        return checkValue;
    }
}
