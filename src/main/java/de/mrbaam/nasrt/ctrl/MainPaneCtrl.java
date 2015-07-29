package de.mrbaam.nasrt.ctrl;

import de.mrbaam.nasrt.ctrl.dialogs.DialogBox;
import de.mrbaam.nasrt.data.Release;
import de.mrbaam.nasrt.model.Model;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by mrbaam on 24.07.2015.
 * @author mrbaam
 */
public class MainPaneCtrl implements Initializable {
    private @FXML Button                       btnClear;
    private @FXML Button                       btnOpen;
    private @FXML Button                       btnRefresh;
    private @FXML Button                       btnStart;
    private @FXML CheckBox                     chkHideFinished;
    private @FXML ImageView                    ivClear;
    private @FXML TableColumn<Release, String> tcName;
    private @FXML TableColumn<Release, String> tcStatus;
    private @FXML TableColumn<Release, String> tcType;
    private @FXML TableView<Release>           tvReleases;
    private @FXML TextField                    tfDirectory;

    private Image imgClear;
    private Image imgClearHover;
    private Model model;


    @FXML
    private void onHideFinished() {
        if (chkHideFinished.isSelected())
            model.getReleases().removeAll(model.getFinishedReleases());
        else
            model.getReleases().addAll(model.getFinishedReleases());

        model.getReleases().sort((r1, r2) -> r1.getTitle().compareTo(r2.getTitle()));
    }


    @FXML
    private void onMouseIn() {
        if (imgClearHover == null)
            imgClearHover = new Image(getClass().getResourceAsStream("/clear_hover.png"));

        ivClear.setImage(imgClearHover);
    }


    @FXML
    private void onMouseOut() {
        if (imgClear == null)
            imgClear = new Image(getClass().getResourceAsStream("/clear.png"));

        ivClear.setImage(imgClear);
    }


    @FXML
    private void onOpen() {
        final DirectoryChooser        dirChooser;
        final File                    directory;
        final ObservableList<Release> releases;

        dirChooser = new DirectoryChooser();
        directory  = dirChooser.showDialog(btnOpen.getScene().getWindow());

        if (directory != null) {
            tfDirectory.setText(directory.getAbsolutePath());

            try {
                releases = model.readReleases(directory.toPath());

                if (releases.isEmpty()) {
                    Optional<ButtonType> result;

                    result = DialogBox.showWarning("Kein Release gefunden!",
                                                   "Es konnte kein Release gefunden werden!",
                                                   "Das ausgewählte Verzeichnis enthält keine Releases. " +
                                                   "Bitte wählen Sie ein anderes Verzeichnis.");

                    if (result.get() == ButtonType.OK)
                        onOpen();
                    else
                        tfDirectory.setText(null);
                }
                else {
                    tvReleases.setItems(releases);
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }
        else {
            tfDirectory.setText(null);
        }
    }


    @FXML
    private void onRefresh() {
        final Optional<ButtonType> result;

        result = DialogBox.showQuestion("Daten zurücksetzen", "Daten zurücksetzen",
                "Möchten Sie die Tabelle wirklich leeren?");

        if (result.get() == ButtonType.YES) {
            tfDirectory.setText(null);
            model.getReleases().clear();
        }
    }


    @FXML
    private void onStart() {
        final ObservableList<Release> copy = FXCollections.observableArrayList(model.getReleases());

        for (Release release : copy) {
            try {
                if (!Release.OK.equals(release.getStatus()))
                    model.refactorFiles(release);
            } catch (IOException ioex) {
                release.setStatus(Release.ERROR);
                ioex.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();

        _initTable();
        _initControls();
    }


    private void _initControls() {
        final ListProperty<Release> listProperty;

        listProperty = new SimpleListProperty<>();
        listProperty.bind(tvReleases.itemsProperty());

        btnClear.disableProperty().bind(tvReleases.getSelectionModel().selectedItemProperty().isNull());
        btnRefresh.disableProperty().bind(listProperty.emptyProperty());
        btnStart.disableProperty().bind(listProperty.emptyProperty());
        chkHideFinished.disableProperty().bind(listProperty.emptyProperty());

        tfDirectory.setOnMouseClicked(event -> {
            if (event.getClickCount() > 1)
                onOpen();
        });
    }


    private void _initTable() {
        tcName.setCellValueFactory(cell -> cell.getValue().titleProperty());
        tcStatus.setCellValueFactory(cell -> cell.getValue().statusProperty());
        tcType.setCellValueFactory(cell -> {
            if (cell.getValue().getType() == Release.MOVIE)
                return new SimpleStringProperty("Film");
            else if (cell.getValue().getType() == Release.TVSHOW)
                return new SimpleStringProperty("TV Serie");
            else
                return new SimpleStringProperty("");
        });
    }
}
