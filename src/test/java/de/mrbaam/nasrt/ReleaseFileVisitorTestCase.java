package de.mrbaam.nasrt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Fabian on 18.07.2015.
 */
public class ReleaseFileVisitorTestCase extends TestCaseBase {
    private ReleaseFileVisitor   visitor;
    private ObservableList<Path> releases;


    @Before
    protected void setUp() throws Exception {
        super.setUp();

        releases = FXCollections.observableArrayList();
        visitor  = new ReleaseFileVisitor(releases);
    }


    @After
    protected void tearDown() throws Exception {
        super.tearDown();

        releases.clear();
    }


    @Test
    public void test() throws Exception {
        boolean containsArrow      = false;
        boolean containsBurnNotice = false;
        boolean containsChuck      = false;

        assertTrue(releases.isEmpty());

        Files.walkFileTree(tmpFolderPath, visitor);

        assertEquals(3, releases.size());

        for (Path path : releases)
        {
            final String fileName = path.getFileName().toString();

            containsArrow      = checkRelease("Arrow", containsArrow, fileName);
            containsBurnNotice = checkRelease("Burn Notice", containsBurnNotice, fileName);
            containsChuck      = checkRelease("Chuck", containsChuck, fileName);
        }

        assertTrue(containsArrow);
        assertTrue(containsBurnNotice);
        assertTrue(containsChuck);
    }

    private boolean checkRelease(String name, boolean checkValue, String fileName) {
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
