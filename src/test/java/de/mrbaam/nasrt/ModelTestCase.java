package de.mrbaam.nasrt;

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
    public void testReadReleases() throws Exception {
        assertTrue(model.getReleases().isEmpty());

        model.readReleases(tmpFolderPath);

        assertFalse(model.getReleases().isEmpty());
    }
}
