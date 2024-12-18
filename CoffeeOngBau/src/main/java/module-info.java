module project.an.CoffeeOngBau {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
    opens project.an.CoffeeOngBau.Models.Entities to javafx.base;
    opens project.an.CoffeeOngBau.Controller to javafx.fxml;
    exports project.an.CoffeeOngBau;
}
