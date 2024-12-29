package project.an.CoffeeOngBau.Utils;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import project.an.CoffeeOngBau.Models.Entities.current_data;

import java.io.File;

public class ImportImageUtils {
    public static Image getImageFromUser(AnchorPane form){
        Image image = null;
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));
        File file = openFile.showOpenDialog(form.getScene().getWindow());
        if(file != null){
            current_data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 113, 125, false, true);
        }
        return image;
    }
}
