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

public class DodajProjektController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private TextField nominacijeID;
    @FXML
    private TextField nagradeID;

    @FXML
    private ComboBox<ProdukcijskaKuca> comboKuca;
    @FXML
    private ComboBox<Financije> comboFinancije;
    @FXML
    private ComboBox<Distribucija> comboDistribucija;
    @FXML
    private DatePicker datePocetak;
    @FXML
    private Button dodaj;

    public void initialize() {

        ObservableList<ProdukcijskaKuca> observableKuce = FXCollections.observableArrayList();
        ObservableList<Financije> observableFinancije = FXCollections.observableArrayList();
        ObservableList<Distribucija> observableDistribucije = FXCollections.observableArrayList();

        observableKuce.addAll(Database.dohvatiProdukcijskeKuce());
        observableFinancije.addAll(Database.dohvatiFinancije());
        observableDistribucije.addAll(Database.dohvatiDistribucije());

        comboKuca.setItems(observableKuce);
        comboFinancije.setItems(observableFinancije);
        comboDistribucija.setItems(observableDistribucije);
    }

    public void dodajProjekt() {
        try {
            ProdukcijskaKuca kuca = comboKuca.getValue();

            Integer nominacije = Integer.parseInt(nominacijeID.getText().trim());
            Integer nagrade = Integer.parseInt(nagradeID.getText().trim());

            Financije financije = comboFinancije.getValue();
            Distribucija distribucija = comboDistribucija.getValue();
            LocalDate pocetak = datePocetak.getValue();

            Projekt projekt = new Projekt(0L, kuca,nominacije, nagrade, financije, distribucija, pocetak);

            Database.dodajProjekt(projekt);

            // Show information alert after successful addition
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremljeno!");
            alert.setHeaderText("Glumac uspješno pohranjen u bazu podataka.");
            alert.setContentText("Novog glumca možete sada vidjeti u tablici sa ostalima.");
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
