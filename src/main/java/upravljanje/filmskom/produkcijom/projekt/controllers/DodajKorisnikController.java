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
import upravljanje.filmskom.produkcijom.projekt.entiteti.Korisnik;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Projekt;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Rola;
import upravljanje.filmskom.produkcijom.projekt.iznimke.AdminException;
import upravljanje.filmskom.produkcijom.projekt.iznimke.LozinkaException;

import java.time.LocalDate;

public class DodajKorisnikController {

    public static final Logger logger = LoggerFactory.getLogger(DodajKorisnikController.class);
    @FXML
    private TextField idKorID;
    @FXML
    private PasswordField lozinkaID;
    @FXML
    private PasswordField lozinka2ID;
    @FXML
    private TextField imeID;
    @FXML
    private TextField prezimeID;
    @FXML
    private ComboBox<Rola> comboRola;

    public void initialize() {

        ObservableList<Rola> obesrvableRole = FXCollections.observableArrayList((Rola) null);

        obesrvableRole.addAll(Database.dohvatiRole());

        comboRola.setItems(obesrvableRole);
    }

    public void dodajKorisnika() {
        try {


            String id=idKorID.getText();
            String lozinka=lozinkaID.getText();
            String lozinka2=lozinka2ID.getText();
            String ime=imeID.getText();
            String prezime=prezimeID.getText();

            Rola rola = comboRola.getValue();


            if(id.equals("admin")){
                throw new AdminException();
            }
            if (!lozinka.equals(lozinka2)) {
                throw new LozinkaException();
            }


            Korisnik korisnik = new Korisnik(id, lozinka, ime, prezime, rola);

            Database.dodajKorisnika(korisnik);

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
        catch(LozinkaException e){
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Greška");
            errorAlert.setHeaderText("Lozinke se ne podudaraju!");
            errorAlert.setContentText("Molimo Vas da ponovno uneste lozinke.");
            errorAlert.showAndWait();
        }
        catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }
}
