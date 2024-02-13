package upravljanje.filmskom.produkcijom.projekt.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.Aplikacija;
import upravljanje.filmskom.produkcijom.projekt.database.Database;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Adresa;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Cijena;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Financije;

import java.math.BigDecimal;

public class DodajFinancijeController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private TextField proID;

    @FXML
    private TextField troID;

    @FXML
    private TextField priID;


    @FXML
    private Button dodaj;

    public void initialize() {

    }

    public void dodajFin() {
        try {
            BigDecimal proracun = BigDecimal.valueOf(Long.parseLong(proID.getText()));
            BigDecimal troskovi = BigDecimal.valueOf(Long.parseLong(troID.getText()));
            BigDecimal prihodi = BigDecimal.valueOf(Long.parseLong(priID.getText()));

            Cijena proracunCijena = new Cijena(proracun);
            Cijena troskoviCijena = new Cijena(troskovi);
            Cijena prihodiCijena = new Cijena(prihodi);


            Financije financije = new Financije(Long.valueOf(0), proracunCijena, troskoviCijena, prihodiCijena);

            Database.dodajFinanciju(financije);

            // Show information alert after successful addition
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremljeno!");
            alert.setHeaderText("Financija uspješno pohranjena u bazu podataka.");
            alert.setContentText("Novu financiju možete sada vidjeti u tablici sa ostalima.");
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
