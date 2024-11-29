package project.an.CoffeeOngBau;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.an.CoffeeOngBau.Repositories.NhanVienRepository;
import project.an.CoffeeOngBau.Utils.DBUtils;

import java.io.IOException;
import java.sql.Connection;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("fxml/home"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlPath + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
//        launch();
//        Connection conn = DBUtils.openConnection("banhang", "root", "");
        NhanVienRepository nv = new NhanVienRepository();
        Connection conn = nv.getConn();

        if(conn!=null) System.out.println("Thành công");
        else System.out.println("Thất bại");
    }

}