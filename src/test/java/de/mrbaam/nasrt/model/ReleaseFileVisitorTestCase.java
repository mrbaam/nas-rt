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
                assertFalse(release.hasGoodStructure());
                assertEquals(2, release.getDeletingCandidates().size());
                assertEquals(1, release.getMovingCandidates().size());
                assertEquals(1, release.getRenamingCandidates().size());
            }
            else if ("Burn Notice".equals(release.getTitle())) {
                assertTrue(release.hasGoodStructure());
                assertTrue(release.getDeletingCandidates().isEmpty());
                assertTrue(release.getMovingCandidates().isEmpty());
                assertTrue(release.getRenamingCandidates().isEmpty());
            }
            else if ("Chuck".equals(release.getTitle())) {
                assertFalse(release.hasGoodStructure());
                assertEquals(1, release.getDeletingCandidates().size());
                assertEquals(2, release.getMovingCandidates().size());
                assertTrue(release.getRenamingCandidates().isEmpty());
            }
        }

        assertTrue(containsArrow);
        assertTrue(containsBurnNotice);
        assertTrue(containsChuck);
    }
}
