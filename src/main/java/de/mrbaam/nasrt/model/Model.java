package de.mrbaam.nasrt.model;

import de.mrbaam.nasrt.data.Release;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mrbaam on 17.07.2015.
 * @author mrbaam
 */
public class Model {
    private final ObservableList<Release> l_releases;


    public Model() {
        l_releases = FXCollections.observableArrayList();
    }


    public void readReleases(Path rootPath) throws IOException {
        final ReleaseFileVisitor l_visitor;

        l_visitor = new ReleaseFileVisitor(l_releases);

        Files.walkFileTree(rootPath, l_visitor);
    }


    public void refactorFiles(Release release) throws IOException {
        final Map<String, Path> moveMap   = release.getMovingCandidates();
        final Map<String, Path> renameMap = release.getRenamingCandidates();
        final Map<String, Path> deleteMap = release.getDeletingCandidates();

        for (String name : renameMap.keySet()) {
            final Path   oldPath;
            final Path   newPath;
            final String newName;

            oldPath = renameMap.get(name);
            newName = _renameEpisode(oldPath, release.getTitle());

            if (moveMap.containsKey(name)) {
                newPath = _getNewEpisodePath(oldPath, newName);
                moveMap.remove(name);
            }
            else {
                newPath = oldPath.getParent().resolve(newName);
            }

            Files.move(oldPath, newPath);
        }

        for (String name : moveMap.keySet()) {
            final Path oldPath;
            final Path newPath;

            oldPath = moveMap.get(name);
            newPath = _getNewEpisodePath(oldPath, name);

            Files.move(oldPath, newPath);
        }

        for (String name : deleteMap.keySet()) {
            Files.deleteIfExists(deleteMap.get(name));
        }
    }


    private Path _getNewEpisodePath(Path path, String newName) {
        if (path.getParent().getFileName().toString().matches("[Ss](taffel)\\s\\d+"))
            return path.getParent().resolve(newName);
        else
            return _getNewEpisodePath(path.getParent(), newName);
    }


    private String _renameEpisode(Path oldPath, String title) {
        final StringBuilder builder;
        final Pattern       pattern;
        final Matcher       matcher;

        builder = new StringBuilder();
        pattern = Pattern.compile("\\W([Ss]\\d+)?[Ee]\\d+");
        matcher = pattern.matcher(oldPath.toString());

        if (matcher.find()) {
            final String episodeID = matcher.group();
            final String fileName  = oldPath.getFileName().toString();

            builder.append(title.replaceAll("[^a-zA-Z0-9ÄäÖöÜüß]", " ").replaceAll("\\s+", "."));
            builder.append(episodeID);
            builder.append(fileName.substring(fileName.lastIndexOf(".")));

            return builder.toString();
        }

        // TODO log error
        return null;
    }


    public ObservableList<Release> getReleases() {
        return l_releases;
    }
}
