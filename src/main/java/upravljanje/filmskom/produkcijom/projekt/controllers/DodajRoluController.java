package upravljanje.filmskom.produkcijom.projekt.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.Aplikacija;
import upravljanje.filmskom.produkcijom.projekt.database.Database;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Adresa;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Rola;
import upravljanje.filmskom.produkcijom.projekt.iznimke.AdminException;

public class DodajRoluController {
    public static final Logger logger = LoggerFactory.getLogger(DodajRoluController.class);
    @FXML
    private TextField nazivID;

    @FXML
    private TextArea opisID;

    @FXML
    private Button dodaj;

    public void initialize() {

    }
    public void dodajRolu() {
        try {
            String naziv = nazivID.getText();
            String opis = opisID.getText();

            if(naziv.equals("admin")){
                throw new AdminException();
            }

            Rola rola = new Rola(0L, naziv, opis);

            Database.dodajRolu(rola);

            // Show information alert after successful addition
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremljeno!");
            alert.setHeaderText("Rola uspješno pohranjena u bazu podataka.");
            alert.setContentText("Novu rolu možete sada vidjeti u tablici sa ostalima.");
            alert.showAndWait();
        }
        catch (AdminException e){
            e.printStackTrace();
            String message="Originalni Admin se ne može mijenjati";
            logger.error(message);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Greška");
            errorAlert.setHeaderText("Originalni Admin se ne može mijenjati!");
            errorAlert.setContentText("Odaberite drugog korisnika.");
            errorAlert.showAndWait();
        }
        catch (NumberFormatException e) {
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
