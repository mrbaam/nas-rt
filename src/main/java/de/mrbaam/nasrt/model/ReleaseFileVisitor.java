package de.mrbaam.nasrt.model;

import de.mrbaam.nasrt.data.Release;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by mrbaam on 18.07.2015.
 * @author mrbaam
 */
public class ReleaseFileVisitor implements FileVisitor<Path> {
    /** Regular expression for a sample. */
    private static final String REGEX_SAMPLE = "[Ss](ample)\\.*";
    /** Regular expression for a season. */
    private static final String REGEX_SEASON = "[Ss](taffel)\\s\\d+";
    /** Regular expression for a video file. */
    private static final String REGEX_VIDEO  = ".*\\.((mkv)|(avi)|(mp4)|(wmv))";

    private final ObservableList<Release> releases;

    private Release tmpRelease;


    public ReleaseFileVisitor(ObservableList<Release> releases) {
        this.releases = releases;
    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        final Release release;
        final String  dirName;

        dirName = dir.getFileName().toString();

        if (dir.getParent() != null && dirName.matches(REGEX_SEASON)) {
            for (int index = 0; index < releases.size(); index++) {
                if (releases.get(index).getTitle().equals(dir.getParent().getFileName().toString()))
                    return FileVisitResult.CONTINUE;
            }

            release = new Release(dir.getParent(), dir.getParent().getFileName().toString(), Release.TVSHOW);
            releases.add(release);

            tmpRelease = release;
        }

        return FileVisitResult.CONTINUE;
    }


    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        final Path   parent   = file.getParent();
        final String fileName = file.getFileName().toString();

        if (Release.TVSHOW == tmpRelease.getType()) {
            if (_isSample(file) || _isNoVideoFile(file)) {
                tmpRelease.addDeleteCandidate(fileName, file);
                tmpRelease.setGoodStructure(false);

                return FileVisitResult.CONTINUE;
            }

            if (!_hasCorrectSeasonName(file)) {
                tmpRelease.addRenameCandidate(fileName, file);
                tmpRelease.setGoodStructure(false);
            }

            if (!parent.getFileName().toString().matches(REGEX_SEASON)) {
                tmpRelease.addMoveCandidate(fileName, file);
                tmpRelease.setGoodStructure(false);
            }
        }

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


    private boolean _hasCorrectSeasonName(Path file) {
        final StringBuilder regExBuilder = new StringBuilder();

        String tmpTitle;

        tmpTitle = tmpRelease.getTitle().replaceAll("[^a-zA-Z0-9ÄäÖöÜüß]", " ");
        tmpTitle = tmpTitle.replaceAll("\\s+", "\\.");

        regExBuilder.append("(").append(tmpTitle).append("\\.)");
        regExBuilder.append("([Ss][0-9]+)?[Ee][0-9]+");
        regExBuilder.append("\\.").append("((mkv)|(mp4)|(avi)|(wmv))");

        return file.getFileName().toString().matches(regExBuilder.toString());
    }


    private boolean _isNoVideoFile(Path file) {
        return !file.getFileName().toString().toLowerCase().matches(REGEX_VIDEO);
    }


    private boolean _isSample(Path file) {
        final String parentDirName = file.getParent().getFileName().toString().toLowerCase();

        if (parentDirName.matches(REGEX_SAMPLE) || file.getFileName().toString().toLowerCase().matches(REGEX_SAMPLE))
            return true;

        return false;
    }
}
