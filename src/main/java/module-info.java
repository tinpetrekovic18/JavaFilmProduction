module upravljanje.filmskom.produkcijom.projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires com.h2database;


    opens upravljanje.filmskom.produkcijom.projekt to javafx.fxml;
    exports upravljanje.filmskom.produkcijom.projekt;
    exports upravljanje.filmskom.produkcijom.projekt.controllers;
    opens upravljanje.filmskom.produkcijom.projekt.controllers to javafx.fxml;
}