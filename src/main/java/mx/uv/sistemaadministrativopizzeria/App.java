package mx.uv.sistemaadministrativopizzeria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import javafx.stage.Modality;
import mx.uv.sistemaadministrativopizzeria.controladores.componentesReutilizables.Ventana;

/**
 * JavaFX App
 */
public class App extends Application {

    private static HashMap<String, Object> metadatos;
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        configurarVentana(
                stage,
                "Sistema Administrativo Pizzeria Italia Pizza",
                700, 300,
                815, 650,
                900, 700,
                false
        );
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
    
    public static <T> Ventana<T> abrirVentanaEmergente(
            String fxml,
            String titulo,
            int ancho,
            int alto,
            boolean modal
    ) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                App.class.getResource(fxml + ".fxml")
        );

        Parent root = loader.load();

        Scene nuevaEscena = new Scene(root, ancho, alto);

        nuevaEscena.getStylesheets().add(
                App.class.getResource("/css/primer-light.css").toExternalForm()
        );

        Stage stage = new Stage();

        stage.setTitle(titulo);
        stage.setScene(nuevaEscena);
        stage.setResizable(false);

        if (modal) {
            stage.initModality(Modality.APPLICATION_MODAL);
        }

        T controller = loader.getController();

        return new Ventana<>(stage, controller);
    }
    
    public static <T> Ventana<T> setRootVentana(String fxml) throws IOException{
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource(fxml + ".fxml")
        );
        
        Parent root = loader.load();
        scene.setRoot(root);
        Stage stage = (Stage) scene.getWindow();
        
        T controller = loader.getController();
        
        return new Ventana<>(stage, controller);
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
    
    public static void configurarVentana(Stage stage,
                                        String titulo,
                                        int minW, int minH,
                                        int prefW, int prefH,
                                        int maxW, int maxH,
                                        boolean resizable) {

       stage.setTitle(titulo);
       stage.setMinWidth(minW);
       stage.setMinHeight(minH);
       stage.setWidth(prefW);
       stage.setHeight(prefH);
       stage.setMaxWidth(maxW);
       stage.setMaxHeight(maxH);
       stage.setResizable(resizable);
   }

    public static void main(String[] args) {
        launch();
    }

}