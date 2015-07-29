package de.mrbaam.nasrt.data;

import javafx.beans.property.*;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mrbaam on 21.07.2015.
 * @author mrbaam
 */
public class Release {
    /** Constant for movie type. */
    public static final int MOVIE       = 1;
    /** Constant for tv show type. */
    public static final int TVSHOW      = 2;
    /** Constant for Error status. */
    public static final String ERROR    = "Error";
    /** Constant for OK status. */
    public static final String OK       = "OK";
    /** Constant for in progress status. */
    public static final String PROGRESS = "In Arbeit";
    /** Constant for to-do status. */
    public static final String TODO     = "Ausstehend";

    private BooleanProperty      goodStructure;
    private IntegerProperty      type;
    private ObjectProperty<Path> pathToRelease;
    private StringProperty       status;
    private StringProperty       title;

    private Map<String, Path> deletingCandidates;
    private Map<String, Path> movingCandidates;
    private Map<String, Path> renamingCandidates;


    public Release(Path path, String releaseTitle, int releaseType) {
        if (releaseType != Release.TVSHOW && releaseType != Release.MOVIE)
            throw new IllegalArgumentException("Unknown release type.");

        deletingCandidates = new HashMap<>();
        movingCandidates   = new HashMap<>();
        renamingCandidates = new HashMap<>();

        goodStructure = new SimpleBooleanProperty(true);
        pathToRelease = new SimpleObjectProperty<>(path);
        status        = new SimpleStringProperty(OK);
        title         = new SimpleStringProperty(releaseTitle);
        type          = new SimpleIntegerProperty(releaseType);
    }


    public void addDeleteCandidate(String name, Path path) {
        deletingCandidates.put(name, path);
    }


    public Map<String, Path> getDeletingCandidates() {
        return deletingCandidates;
    }


    public void addMoveCandidate(String name, Path path) {
        movingCandidates.put(name, path);
    }


    public Map<String, Path> getMovingCandidates() {
        return movingCandidates;
    }


    public void addRenameCandidate(String name, Path path) {
        renamingCandidates.put(name, path);
    }


    public Map<String, Path> getRenamingCandidates() {
        return renamingCandidates;
    }


    public boolean hasGoodStructure() {
        return goodStructure.get();
    }


    public BooleanProperty goodStructureProperty() {
        return goodStructure;
    }


    public void setGoodStructure(boolean goodStructure) {
        this.goodStructure.set(goodStructure);
    }


    public Path getPathToRelease() {
        return pathToRelease.get();
    }


    public ObjectProperty<Path> pathToReleaseProperty() {
        return pathToRelease;
    }


    public void setPathToRelease(Path pathToRelease) {
        this.pathToRelease.set(pathToRelease);
    }


    public String getStatus() {
        return status.get();
    }


    public StringProperty statusProperty() {
        return status;
    }


    public void setStatus(String status) {
        this.status.set(status);
    }


    public String getTitle() {
        return title.get();
    }


    public StringProperty titleProperty() {
        return title;
    }


    public void setTitle(String title) {
        this.title.set(title);
    }


    public int getType() {
        return type.get();
    }


    public IntegerProperty typeProperty() {
        return type;
    }
}
