package de.mrbaam.nasrt.model;

import de.mrbaam.nasrt.TestCaseBase;
import de.mrbaam.nasrt.TestUtils;
import de.mrbaam.nasrt.data.Release;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by mrbaam on 17.07.2015.
 * @author mrbaam
 */
public class ModelTestCase extends TestCaseBase {
    private Model model;


    @Before
    protected void setUp() throws Exception {
        super.setUp();

        model = Model.getInstance();
    }


    @After
    protected void tearDown() throws Exception {
        Model.clearInstance();

        super.tearDown();
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


    @Test
    public void testRefactorFiles() throws Exception {
        model.readReleases(tmpFolderPath);

        for (final Release release : model.getReleases()) {
            model.refactorFiles(release);

            Files.walkFileTree(release.getPathToRelease(), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    final String season = file.getParent().getFileName().toString();
                    final String tvShow = file.getParent().getParent().getFileName().toString();

                    assertTrue(file.getFileName().toString().matches(_createRegEx(release.getTitle())));
                    assertTrue(season.matches("[Ss](taffel)\\s\\d+"));
                    assertEquals(release.getTitle(), tvShow);

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }


    private String _createRegEx(String title) {
        final StringBuilder builder;
        final String        regExTitle;

        builder    = new StringBuilder();
        regExTitle = title.replaceAll("[^a-zA-Z0-9ÄäÖöÜüß]", " ").replaceAll("\\s+", "\\.");

        builder.append(regExTitle).append("\\.").append("([Ss]\\d+)?[Ee]\\d+").append("\\.((mkv)|(avi)|(mp4)|(wmv))");

        return builder.toString();
    }
}
