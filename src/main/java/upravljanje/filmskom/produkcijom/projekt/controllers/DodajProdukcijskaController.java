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
import upravljanje.filmskom.produkcijom.projekt.entiteti.ProdukcijskaKuca;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Projekt;

import java.time.LocalDateTime;

public class DodajProdukcijskaController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private ComboBox<Adresa> comboAdresa;

    @FXML
    private TextField nazivID;
    @FXML
    private TextField godinaID;

    @FXML
    private Button dodaj;

    public void initialize() {
        ObservableList<Adresa> observableAdrese = FXCollections.observableArrayList((Adresa) null);

        observableAdrese.addAll(Database.dohvatiAdrese());

        comboAdresa.setItems(observableAdrese);

    }

    public void dodajKucu() {
        try {

            String naziv=nazivID.getText();
            Integer godina=Integer.valueOf(godinaID.getText());
            Adresa sjediste = comboAdresa.getValue();


            ProdukcijskaKuca produkcijskaKuca = new ProdukcijskaKuca(Long.valueOf(0), naziv, godina, sjediste);

            Database.dodajKucu(produkcijskaKuca);

            // Show information alert after successful addition
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremljeno!");
            alert.setHeaderText("Produkcijska kuća uspješno pohranjena u bazu podataka.");
            alert.setContentText("Novu produkcijsku kuću možete sada vidjeti u tablici sa ostalima.");
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
