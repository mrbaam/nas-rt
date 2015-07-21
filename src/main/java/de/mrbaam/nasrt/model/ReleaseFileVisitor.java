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
    /** Regular expression for a season. */
    private static final String REGEX = "[Ss](taffel)\\s\\d+";

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

        if (dir.getParent() != null && dirName.matches(REGEX)) {
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
        final Path parent = file.getParent();

        if (Release.TVSHOW == tmpRelease.getType()) {
            if (!_hasCorrectSeasonName(file))
                tmpRelease.addRenameCandidate(file.getFileName().toString(), file);

            if (!parent.getFileName().toString().matches(REGEX))
                tmpRelease.addMoveCandidate(file.getFileName().toString(), file);
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
        tmpTitle = tmpTitle.replaceAll("\\s+", ".");

        regExBuilder.append("(").append(tmpTitle).append(".)");
        regExBuilder.append("([Ss][0-9]+)?[Ee][0-9]+");
        regExBuilder.append(".").append("((mkv)|(mp4)|(avi)|(wmv))");

        return file.getFileName().toString().matches(regExBuilder.toString());
    }


    public void setTmpRelease(Release release) {
        tmpRelease = release;
    }
}
