package de.mrbaam.nasrt.model;

import de.mrbaam.nasrt.TestCaseBase;
import de.mrbaam.nasrt.TestUtils;
import de.mrbaam.nasrt.data.Release;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;

/**
 * Created by Fabian on 18.07.2015.
 */
public class ReleaseFileVisitorTestCase extends TestCaseBase {
    private ReleaseFileVisitor      visitor;
    private ObservableList<Release> releases;


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

        for (Release release : releases) {
            containsArrow      = TestUtils.checkRelease("Arrow", containsArrow, release.getTitle());
            containsBurnNotice = TestUtils.checkRelease("Burn Notice", containsBurnNotice, release.getTitle());
            containsChuck      = TestUtils.checkRelease("Chuck", containsChuck, release.getTitle());

            if ("Arrow".equals(release.getTitle())) {
                assertEquals(1, release.getMovingCandidates().size());
                assertEquals(1, release.getRenamingCandidates().size());
            }
        }

        assertTrue(containsArrow);
        assertTrue(containsBurnNotice);
        assertTrue(containsChuck);
    }


//    @Test
//    public void testHasCorrectName() {
//        final Release rel = new Release(Paths.get("Test"), "Der Herr der Ringe - Die zwei Türme", Release.MOVIE);
//
//        visitor.setTmpRelease(rel);
//
//        visitor._hasCorrectName(null);
//    }
}
