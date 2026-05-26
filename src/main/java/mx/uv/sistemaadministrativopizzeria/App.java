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
                App.class.getResource("/css/primer-light.css").toExternalForm()
        );
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    /**
    * Método que permite agregar un valor al repositorio de metadatos
    * @param nombre
    * @param valor
    */
    public static void setMetadato(String nombre, Object valor){
        if(metadatos == null){
            metadatos = new HashMap<>();
        }
        metadatos.put(nombre, valor);
    }
    /**
     * Método que permite obtener un valor de los metadatos con base en su nombre
    * @param nombre
    * @return
    */
    public static Object getMetadato(String nombre) {
        if (metadatos == null) {
            return null;
        }
        return metadatos.get(nombre);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}