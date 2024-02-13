package upravljanje.filmskom.produkcijom.projekt.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.Aplikacija;
import upravljanje.filmskom.produkcijom.projekt.database.Database;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Adresa;

import java.math.BigDecimal;

public class DodajAdresuController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private TextField inputBroj;

    @FXML
    private TextField inputUlica;

    @FXML
    private TextField inputGrad;
    @FXML
    private TextField inputPbr;

    @FXML
    private TextField inputDrzava;
    @FXML
    private Button dodaj;

    public void initialize() {

    }

    public void dodajAdresu() {
        try {
            Integer broj = Integer.valueOf(inputBroj.getText());
            String ulica = inputUlica.getText();
            String grad = inputGrad.getText();
            String pbr = inputPbr.getText();
            String drzava = inputDrzava.getText();

            Adresa adresa = new Adresa(Long.valueOf(0), broj, ulica, grad, pbr, drzava);

            Database.dodajAdresu(adresa);

            // Show information alert after successful addition
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremljeno!");
            alert.setHeaderText("Adresa uspješno pohranjena u bazu podataka.");
            alert.setContentText("Novu adresu možete sada vidjeti u tablici sa ostalima.");
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
