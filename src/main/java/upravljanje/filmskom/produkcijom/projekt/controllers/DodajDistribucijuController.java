package upravljanje.filmskom.produkcijom.projekt.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.Aplikacija;
import upravljanje.filmskom.produkcijom.projekt.database.Database;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Adresa;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Distribucija;

import java.time.LocalDate;

public class DodajDistribucijuController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);

    @FXML
    private DatePicker pocetakID;

    @FXML
    private DatePicker krajID;

    @FXML
    private DatePicker izlazID;
    @FXML
    private Button dodaj;

    public void initialize() {

    }

    public void dodajDist() {
        try {
            LocalDate pocetak = pocetakID.getValue();
            LocalDate kraj = krajID.getValue();
            LocalDate izlaz = izlazID.getValue();


            Distribucija distribucija = new Distribucija(Long.valueOf(0), pocetak, kraj, izlaz);

            Database.dodajDistribuciju(distribucija);

            // Show information alert after successful addition
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremljeno!");
            alert.setHeaderText("Distribucija uspješno pohranjena u bazu podataka.");
            alert.setContentText("Novu distribuciju možete sada vidjeti u tablici sa ostalima.");
            alert.showAndWait();
        } catch (NumberFormatException e) {
            // Handle the case where inputBroj is not a valid integer
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Greška");
            errorAlert.setHeaderText("Neispravan unos broja!");
            errorAlert.setContentText("Molimo Vas da unesete ispravan broj i pokušate ponovo.");
            errorAlert.showAndWait();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }
}
