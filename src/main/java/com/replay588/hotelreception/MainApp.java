package com.replay588.hotelreception;

import com.replay588.hotelreception.controller.MainController;
import com.replay588.hotelreception.util.hib.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import java.io.IOException;

/**
 * Main class for HotelReception
 * Run this program from here
 * @author replay588
 */
public class MainApp extends Application {

    final private String fxmlMainSceneFile = "/fxml/main_scene.fxml";

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream(fxmlMainSceneFile));

        MainController controller = loader.getController();
        controller.setMainWindow(primaryStage);

        primaryStage.setTitle("Hotel Reception 1.0");
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            closeApp(primaryStage);
        });

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * A confirmation dialog closing the program
     * @see <a href="http://controlsfx.bitbucket.org/org/controlsfx/dialog/Dialogs.html">Controlsfx Dialogs</a>
     * @param stage
     */
    private void closeApp(Stage stage) {
        Action response = Dialogs.create()
                .owner(stage)
                .title("Close App")
                .message("Are you sure you want to exit?")
                .actions(Dialog.ACTION_OK, Dialog.ACTION_CANCEL)
                .showConfirm();
        if (response == Dialog.ACTION_OK) {
            HibernateUtil.shutdown();
            System.exit(0);
        }
    }
}
