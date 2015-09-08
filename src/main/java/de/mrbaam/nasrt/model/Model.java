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
    private static Model model;

    private final ObservableList<Release> releases;
    private final ObservableList<Release> finishedReleases;


    private Model() {
        finishedReleases = FXCollections.observableArrayList();
        releases         = FXCollections.observableArrayList();
    }


    public static Model getInstance() {
        if (model == null)
            model = new Model();

        return model;
    }


    public static void clearInstance() {
        model = null;
    }


    public ObservableList<Release> readReleases(Path rootPath) throws IOException {
        final ReleaseFileVisitor l_visitor;

        l_visitor = new ReleaseFileVisitor(releases);

        Files.walkFileTree(rootPath, l_visitor);

        for (Release release : releases) {
            if (Release.OK.equals(release.getStatus()))
                finishedReleases.add(release);
        }

        return releases;
    }


    public void refactorFiles(Release release) throws IOException {
        final Map<String, Path> moveMap   = release.getMovingCandidates();
        final Map<String, Path> renameMap = release.getRenamingCandidates();
        final Map<String, Path> deleteMap = release.getDeletingCandidates();

        release.setStatus(Release.PROGRESS);

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

        Files.walkFileTree(release.getPathToRelease(), new CleanUpFileVisitor());

        release.setStatus(Release.OK);
    }


    private Path _getNewEpisodePath(Path path, String newName) {
        if (path.getParent().getFileName().toString().matches(REGEX.SEASON_TITLE))
            return path.getParent().resolve(newName);
        else
            return _getNewEpisodePath(path.getParent(), newName);
    }


    private String _renameEpisode(Path oldPath, String title) {
        final StringBuilder builder;
        final Pattern       pattern;
        final Matcher       matcher;

        builder = new StringBuilder();
        pattern = Pattern.compile(REGEX.DEFAULT_EPISODE);
        matcher = pattern.matcher(oldPath.toString());

        if (matcher.find()) {
            final String episodeID = matcher.group();
            final String fileName  = oldPath.getFileName().toString();

            builder.append(title.replaceAll(REGEX.ALL_UNSUPPORTED_SIGNS, " ").replaceAll(REGEX.WHITESPACES, "."));
            builder.append(episodeID);
            builder.append(fileName.substring(fileName.lastIndexOf(".")));

            return builder.toString();
        }
        else {
            final String episodeID = _findEpisodeID(oldPath);

            if (episodeID != null) {

            }
        }

        // TODO log error
        return null;
    }


    private String _findEpisodeID(Path path2file) {
        final StringBuilder builder;
        final Pattern       pattern;
        final Matcher       matcher;
        final String        season;

        season = _extractSeason(path2file);

        if (season == null)
            return null;

        builder = new StringBuilder();
        pattern = Pattern.compile("[0]*" + season + "[0]*\\d+");
        matcher = pattern.matcher(path2file.toAbsolutePath().toString());

        if (matcher.find()) {
            final String seasonNotation;
            final String episodeNotation;

            String episode;

            if (Integer.parseInt(season) < 10)
                seasonNotation = "S0" + season;
            else
                seasonNotation = "S" + season;

            episode = matcher.group();
            episode = episode.substring(episode.indexOf(season) + 1);

            if (!episode.startsWith("0") && Integer.parseInt(episode) < 10)
                episodeNotation = "E0" + episode;
            else
                episodeNotation = "E" + episode;
        }

        return builder.toString();
    }


    private String _extractSeason(Path path2file) {
        final Pattern pattern;
        final Matcher matcher;

        pattern = Pattern.compile(REGEX.SEASON_TITLE);
        matcher = pattern.matcher(path2file.toAbsolutePath().toString());

        if (matcher.find()) {
            final String season       = matcher.group();
            final String seasonNumber = season.substring(season.indexOf(" ") + 1);

            return seasonNumber;
        }

        return null;
    }


    public ObservableList<Release> getFinishedReleases() {
        return finishedReleases;
    }


    public ObservableList<Release> getReleases() {
        return releases;
    }
}
