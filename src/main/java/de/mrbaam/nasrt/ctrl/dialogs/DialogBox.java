package de.mrbaam.nasrt.ctrl.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

import java.util.Optional;

/**
 * Created by mrbaam on 24.07.2015.
 * @author mrbaam
 */
public class DialogBox extends Alert {

    private DialogBox(AlertType type) {
        super(type);
    }


    public static Optional<ButtonType> showQuestion(String title, String header, String content) {
        final DialogBox box = new DialogBox(AlertType.CONFIRMATION);

        _initDialog(title, header, content, box);
        box.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        return box.showAndWait();
    }


    public static Optional<ButtonType> showWarning(String title, String header, String content) {
        final DialogBox box = new DialogBox(AlertType.WARNING);

        _initDialog(title, header, content, box);
        box.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        return box.showAndWait();
    }


    private static void _initDialog(String title, String header, String content, DialogBox box) {
        box.initStyle(StageStyle.UTILITY);
        box.setTitle(title);
        box.setHeaderText(header);
        box.setContentText(content);
    }
}
