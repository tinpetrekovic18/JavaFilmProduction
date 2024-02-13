package upravljanje.filmskom.produkcijom.projekt.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.Aplikacija;
import upravljanje.filmskom.produkcijom.projekt.database.Database;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Adresa;
import upravljanje.filmskom.produkcijom.projekt.entiteti.LokacijaSnimanja;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Projekt;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DodajLokacijuController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private ComboBox<Adresa> comboAdresa;

    @FXML
    private ComboBox<Projekt> comboProjekt;
    @FXML
    private DatePicker dateOD;

    @FXML
    private DatePicker dateDO;
    @FXML
    private Button dodaj;

    public void initialize() {
        ObservableList<Adresa> observableAdrese = FXCollections.observableArrayList((Adresa) null);
        ObservableList<Projekt> observableProjekti = FXCollections.observableArrayList((Projekt) null);

        observableAdrese.addAll(Database.dohvatiAdrese());
        observableProjekti.addAll(Database.dohvatiProjekte());

        comboAdresa.setItems(observableAdrese);
        comboProjekt.setItems(observableProjekti);
    }

    public void dodajLokaciju() {
        try {
            Adresa lokacija = comboAdresa.getValue();
            Projekt projekt = comboProjekt.getValue();
            LocalDateTime rezOD = dateOD.getValue().atStartOfDay();
            LocalDateTime rezDO = dateDO.getValue().atStartOfDay();

            LokacijaSnimanja lokacijaSnimanja = new LokacijaSnimanja(Long.valueOf(0), lokacija, projekt, rezOD, rezDO);

            Database.dodajLokaciju(lokacijaSnimanja);

            // Show information alert after successful addition
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremljeno!");
            alert.setHeaderText("Lokacija uspješno pohranjena u bazu podataka.");
            alert.setContentText("Novu lokaciju možete sada vidjeti u tablici sa ostalima.");
            alert.showAndWait();
        } catch (NumberFormatException e) {
            // Handle the case where inputBroj is not a valid integer
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Greška");
            errorAlert.setHeaderText("Neispravan unos!");
            errorAlert.setContentText("Molimo Vas da unesete ispravan broj i pokušate ponovo.");
            errorAlert.showAndWait();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }
}
