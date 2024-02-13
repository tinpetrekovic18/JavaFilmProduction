package upravljanje.filmskom.produkcijom.projekt.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.Aplikacija;
import upravljanje.filmskom.produkcijom.projekt.database.Database;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Glumac;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Producent;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Projekt;

import java.time.LocalDate;

public class DodajProducentaController {
    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private TextField ime;
    @FXML
    private TextField prezime;
    @FXML
    private TextField OIB;
    @FXML
    private TextField dob;
    @FXML
    private TextField umjetnicko;
    @FXML
    private TextField nominacije;
    @FXML
    private TextField nagrade;

    @FXML
    private ComboBox<Projekt> comboProjekt;

    @FXML
    private DatePicker datePocetak;
    @FXML
    private Button dodaj;

    public void initialize() {

        ObservableList<Projekt> observableProjekti = FXCollections.observableArrayList((Projekt) null);

        observableProjekti.addAll(Database.dohvatiProjekte());

        comboProjekt.setItems(observableProjekti);
    }

    public void dodajProducenta() {
        try {


            String Ime=ime.getText();
            String Prezime=prezime.getText();
            String oib=OIB.getText();
            Integer Dob= Integer.valueOf(dob.getText());
            String Umjetnicko=umjetnicko.getText();
            Integer Nominacije= Integer.valueOf(nominacije.getText());
            Integer Nagrade= Integer.valueOf(nagrade.getText());
            Projekt projekt = comboProjekt.getValue();
            LocalDate pocetak = LocalDate.from(datePocetak.getValue());

            Producent producent = new Producent(Long.valueOf(0), Ime, Prezime, oib, Dob, Umjetnicko, Nominacije, Nagrade, projekt, pocetak);

            Database.dodajProducenta(producent);

            // Show information alert after successful addition
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremljeno!");
            alert.setHeaderText("Producent uspješno pohranjen u bazu podataka.");
            alert.setContentText("Novog producenta možete sada vidjeti u tablici sa ostalima.");
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
