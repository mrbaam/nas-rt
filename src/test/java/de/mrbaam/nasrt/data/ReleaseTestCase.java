package de.mrbaam.nasrt.data;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by mrbaam on 21.07.2015.
 * @author mrbaam
 */
@RunWith(value = BlockJUnit4ClassRunner.class)
public class ReleaseTestCase extends TestCase {
    private static final Path   PATH  = Paths.get("Test");
    private static final String TITLE = "Title";

    private Release release;


    @Before
    public void setUp() throws Exception {
        super.setUp();

        release = new Release(PATH, TITLE, Release.TVSHOW);
    }


    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }


    @Test
    public void testGoodStructure() {
        assertFalse(release.hasGoodStructure());
    }


    @Test
    public void testMovingCandidates() {
        assertTrue(release.getMovingCandidates().isEmpty());

        release.addMoveCandidate("Test", Paths.get("Test"));

        assertFalse(release.getMovingCandidates().isEmpty());
    }


    @Test
    public void testPathToRelease() {
        final ObjectProperty<Path> testProp = new SimpleObjectProperty<>();
        final Path                 testPath = Paths.get("new Path");

        testProp.bind(release.pathToReleaseProperty());

        assertEquals(PATH, release.getPathToRelease());
        assertEquals(PATH, testProp.get());

        release.setPathToRelease(testPath);

        assertEquals(testPath, release.getPathToRelease());
        assertEquals(testPath, testProp.get());
    }


    @Test
    public void testRenamingCandidates() {
        assertTrue(release.getRenamingCandidates().isEmpty());

        release.addRenameCandidate("Test", Paths.get("Test"));

        assertFalse(release.getRenamingCandidates().isEmpty());
    }


    @Test
    public void testTitle() {
        final StringProperty testProp = new SimpleStringProperty();

        testProp.bind(release.titleProperty());

        assertEquals(TITLE, release.getTitle());
        assertEquals(TITLE, testProp.get());

        release.setTitle("new Title");

        assertEquals("new Title", release.getTitle());
        assertEquals("new Title", testProp.get());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testType() {
        assertEquals(Release.TVSHOW, release.getType());

        new Release(Paths.get("Test"), "Test", 0);
    }
}
