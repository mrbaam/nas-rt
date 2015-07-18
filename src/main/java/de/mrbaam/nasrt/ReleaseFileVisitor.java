package de.mrbaam.nasrt;

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
    private final ObservableList<Path> releases;


    public ReleaseFileVisitor(ObservableList<Path> releases) {
        this.releases = releases;
    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        final String dirName = dir.getFileName().toString();

        if (dir.getParent() != null && dirName.matches("[Ss](taffel)\\s\\d+")) {
            if (!releases.contains(dir.getParent()))
                releases.add(dir.getParent());
        }

        return FileVisitResult.CONTINUE;
    }
}
