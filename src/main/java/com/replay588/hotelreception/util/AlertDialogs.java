package com.replay588.hotelreception.util;

import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

/**
 * Dialogs to show errors and notifications
 * @see <a href="http://controlsfx.bitbucket.org/org/controlsfx/dialog/Dialogs.html">Controlsfx Dialogs</a>
 */
public abstract class AlertDialogs {

    public static void showErrorDialog(String message, Stage stage) {
        org.controlsfx.dialog.Dialogs.create()
                .owner(stage)
                .title("Error Dialog")
                .masthead("Error!")
                .message(message)
                .showError();
    }

    public static void showNotifications(String title, String text, Stage stage) {
        Notifications.create()
                .owner(stage)
                .title(title)
                .text(text)
                .showInformation();
    }
}
