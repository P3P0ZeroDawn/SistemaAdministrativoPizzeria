package mx.uv.sistemaadministrativopizzeria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

/**
 * JavaFX App
 */
public class App extends Application {

    private static HashMap<String, Object> metadatos;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Sistema Administrativo Pizzeria Italia Pizza");
        stage.setMinWidth(700);
        stage.setMinHeight(300);
        stage.setMaxWidth(900);
        stage.setMaxHeight(700);
        stage.setResizable(false);
        scene = new Scene(loadFXML("inicioSesion"), 800, 600);
        scene.getStylesheets().add(
                App.class.getResource("/css/cupertino-dark.css").toExternalForm()
        );
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}