package de.mrbaam.nasrt.model;

import de.mrbaam.nasrt.TestCaseBase;
import de.mrbaam.nasrt.TestUtils;
import de.mrbaam.nasrt.data.Release;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by mrbaam on 17.07.2015.
 * @author mrbaam
 */
public class ModelTestCase extends TestCaseBase {
    private Model model;


    @Before
    protected void setUp() throws Exception {
        super.setUp();

        model = new Model();
    }


    @After
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    @Test
    public void testCheckRelease() throws Exception {
        model.readReleases(tmpFolderPath);

        for (Release release : model.getReleases()) {
            assertFalse(release.hasGoodStructure());

            model.checkRelease(release);

            if ("Arrow".equals(release.getTitle()))
                assertTrue(release.hasGoodStructure());
            else
                assertFalse(release.hasGoodStructure());
        }
    }


    @Test
    public void testReadReleases() throws Exception {
        boolean containsArrow      = false;
        boolean containsBurnNotice = false;
        boolean containsChuck      = false;

        assertTrue(model.getReleases().isEmpty());

        model.readReleases(tmpFolderPath);

        assertFalse(model.getReleases().isEmpty());
        assertEquals(3, model.getReleases().size());

        for (Release release : model.getReleases()) {
            containsArrow      = TestUtils.checkRelease("Arrow", containsArrow, release.getTitle());
            containsBurnNotice = TestUtils.checkRelease("Burn Notice", containsBurnNotice, release.getTitle());
            containsChuck      = TestUtils.checkRelease("Chuck", containsChuck, release.getTitle());
        }

        assertTrue(containsArrow);
        assertTrue(containsBurnNotice);
        assertTrue(containsChuck);
    }
}
