package de.mrbaam.nasrt.model;

import de.mrbaam.nasrt.data.Release;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by mrbaam on 17.07.2015.
 * @author mrbaam
 */
public class Model {
    private final ObservableList<Release> l_releases;


    public Model() {
        l_releases = FXCollections.observableArrayList();
    }


    public void checkRelease(Release release) {
        release.setGoodStructure(false);
    }


    public void readReleases(Path rootPath) throws IOException {
        final ReleaseFileVisitor l_visitor;

        l_visitor = new ReleaseFileVisitor(l_releases);

        Files.walkFileTree(rootPath, l_visitor);
    }


    public ObservableList<Release> getReleases() {
        return l_releases;
    }
}
