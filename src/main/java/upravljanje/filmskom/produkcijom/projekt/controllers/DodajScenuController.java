package upravljanje.filmskom.produkcijom.projekt.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.Aplikacija;
import upravljanje.filmskom.produkcijom.projekt.database.Database;
import upravljanje.filmskom.produkcijom.projekt.entiteti.*;

import java.time.LocalDate;

public class DodajScenuController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private ComboBox<LokacijaSnimanja> comboLokacija;
    @FXML
    private ComboBox<Projekt> comboProjekt;
    @FXML
    private ComboBox<Producent> comboProducent;
    @FXML
    private DatePicker datePocetak;

    @FXML
    private DatePicker dateKraj;
    @FXML
    private Button dodaj;

    public void initialize() {

        ObservableList<LokacijaSnimanja> observableLokacije = FXCollections.observableArrayList();
        ObservableList<Projekt> observableProjekti = FXCollections.observableArrayList();
        ObservableList<Producent> observableProducenti = FXCollections.observableArrayList();

        observableLokacije.addAll(Database.dohvatiLokacijeSnimanja());
        observableProjekti.addAll(Database.dohvatiProjekte());
        observableProducenti.addAll(Database.dohvatiProducente());

        comboLokacija.setItems(observableLokacije);
        comboProjekt.setItems(observableProjekti);
        comboProducent.setItems(observableProducenti);
    }

    public void dodajScenu() {
        try {
            LocalDate pocetak = datePocetak.getValue();
            LocalDate kraj = dateKraj.getValue();

            LokacijaSnimanja lokacijaSnimanja = comboLokacija.getValue();
            Projekt projekt = comboProjekt.getValue();
            Producent producent = comboProducent.getValue();



            Scena scena = new Scena(0L, pocetak.atStartOfDay(), kraj.atStartOfDay(), lokacijaSnimanja, projekt, producent);

            Database.dodajScenu(scena);

            // Show information alert after successful addition
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremljeno!");
            alert.setHeaderText("Scena uspješno pohranjena u bazu podataka.");
            alert.setContentText("Novu scenu možete sada vidjeti u tablici sa ostalima.");
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
