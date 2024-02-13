package upravljanje.filmskom.produkcijom.projekt.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.Aplikacija;
import upravljanje.filmskom.produkcijom.projekt.database.Database;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Glumac;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Korisnik;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Projekt;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Rola;
import upravljanje.filmskom.produkcijom.projekt.iznimke.AdminException;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class KorisnikController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private TextField inputID;

    @FXML
    private ComboBox<Rola> comboRola;

    @FXML
    private TableView<Korisnik> TablicaKorisnika;
    @FXML
    private TableColumn<Korisnik, String> idKorID;
    @FXML
    private TableColumn<Korisnik, String> lozinkaID;
    @FXML
    private TableColumn<Korisnik, String> imeID;
    @FXML
    private TableColumn<Korisnik, String> prezimeID;
    @FXML
    private TableColumn<Korisnik, String> rolaID;


    @FXML
    private Button filter;
    @FXML
    private Button obrisi;
    @FXML
    private Button promjeni;

    ObservableList<Korisnik> observableKorisnici= FXCollections.observableArrayList(Database.dohvatiKorisnike());

    public void initialize() {

        if (PocetniLoginController.getRolica().equals("admin")){
        }
        else{
            obrisi.setVisible(false);
            promjeni.setVisible(false);
        }
        ObservableList<Rola> obesrvableRole = FXCollections.observableArrayList((Rola) null);

        obesrvableRole.addAll(Database.dohvatiRole());

        comboRola.setItems(obesrvableRole);

        idKorID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getIdKorisnika()));
        lozinkaID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getLozinka()));
        imeID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getIme()));
        prezimeID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getPrezime()));
        rolaID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getRola().getNazivRole()));


        TablicaKorisnika.getSelectionModel().selectedItemProperty().addListener((observable, starOdabir, noviOdabir) -> {
            obrisi.setDisable(noviOdabir == null);
            promjeni.setDisable(noviOdabir == null);
        });


        TablicaKorisnika.setItems(observableKorisnici);
    }

    public void korisniciSearch(){
        List<Korisnik> listKorisnika= Database.dohvatiKorisnike();
        List<Korisnik> filtrirana=listKorisnika;

        String id=inputID.getText();
        Rola rola = comboRola.getValue();


        filtrirana = filtrirana.stream()
                .filter(i -> i.getIdKorisnika().contains(id))
                .filter(i -> rola == null || rola.getIdRole() == null || i.getRola().getIdRole().equals(rola.getIdRole()))
                .collect(Collectors.toList());


        observableKorisnici= FXCollections.observableArrayList(filtrirana);

        TablicaKorisnika.setItems(observableKorisnici);
    };
    @FXML
    private void deleteKorisnik() {
        Korisnik selected = TablicaKorisnika.getSelectionModel().getSelectedItem();
        try{
            if(selected.getIdKorisnika().equals("admin")){
                throw new AdminException();
            }
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Izbriši glumca");
                alert.setContentText("Da li ste sigurni da želite obrisati ovog glumca?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    observableKorisnici.remove(selected);
                    Database.obrisiKorisnika(selected);
                    // Call your delete method in the Database class if needed
                    // Database.deleteAdresa(selectedAdresa);
                }
            }
        }catch (AdminException e) {
            String message="Originalni Admin se ne može mijenjati";
            logger.error(message);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Greška");
            errorAlert.setHeaderText("Originalni Admin se ne može mijenjati!");
            errorAlert.setContentText("Odaberite drugog korisnika.");
            errorAlert.showAndWait();
        }

    }
    public static Korisnik korisnikEditDialog(Korisnik korisnik) {
        Dialog<Korisnik> dialog = new Dialog<>();
        dialog.setTitle("Promjena adrese");
        dialog.setHeaderText(null);

        // Set the button types
        ButtonType spremiButton = new ButtonType("Spremi promjenu", ButtonBar.ButtonData.OK_DONE);
        ButtonType odustaniButton = new ButtonType("Odustani", ButtonType.CANCEL.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(spremiButton, odustaniButton);

        // Create a GridPane to layout the input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);


        ObservableList<Rola> observableRole = FXCollections.observableArrayList();

        observableRole.addAll(Database.dohvatiRole());

        // Create text input fields for each property
        TextField lozinkaField = new TextField(korisnik.getLozinka());
        TextField imeField = new TextField(korisnik.getIme());
        TextField prezimeField = new TextField(korisnik.getPrezime());

        ComboBox<Rola> rolaField = new ComboBox<>(observableRole);

        rolaField.setValue(korisnik.getRola());


        // Add labels and text input fields to the GridPane
        grid.add(new Label("Lozinka:"), 0, 0);
        grid.add(lozinkaField, 1, 0);
        grid.add(new Label("Ime:"), 0, 1);
        grid.add(imeField, 1, 1);
        grid.add(new Label("Prezime:"), 0, 2);
        grid.add(prezimeField, 1, 2);
        grid.add(new Label("Rola:"), 0, 3);
        grid.add(rolaField, 1, 3);


        // Enable/Disable save button depending on whether a field is filled
        Node saveButton = dialog.getDialogPane().lookupButton(spremiButton);
        saveButton.setDisable(true);

        // Add listeners to enable/disable the save button based on input fields
        lozinkaField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        imeField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        prezimeField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        rolaField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || lozinkaField.getText() == null ||
                        imeField.getText() == null || prezimeField.getText() == null));



        // Set the GridPane as the content of the dialog
        dialog.getDialogPane().setContent(grid);

        // Convert the result to an Adresa object when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == spremiButton) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Da li ste sigurni da želite promjeniti glumca?");
                alert.setContentText("Potvrdite svoj odabir");

                ButtonType promjena = new ButtonType("Promjeni");
                ButtonType odustani = new ButtonType("Odustani");

                alert.getButtonTypes().setAll(promjena, odustani);

                Optional<ButtonType> resultButtonType = alert.showAndWait();

                if (resultButtonType.isPresent() && resultButtonType.get() == promjena) {
                    try {
                        String lozinka =lozinkaField.getText().trim();
                        String ime =imeField.getText().trim();
                        String prezime = prezimeField.getText().trim();
                        Rola rola = rolaField.getValue();

                        return new Korisnik(korisnik.getIdKorisnika(),lozinka, ime, prezime, rola);
                    } catch (NumberFormatException e) {
                        System.out.println("Neispravan unos podataka!");
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Greška");
                        errorAlert.setHeaderText("Neispravan unos podataka!");
                        errorAlert.setContentText("Molimo Vas da provjerite točnost svojih podataka i pokušate ponovo.");
                        errorAlert.showAndWait();
                    }
                } else {
                    // User clicked "Odustani" or closed the dialog
                    alert.close();
                }
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Korisnik> result = dialog.showAndWait();

        return result.orElse(korisnik);
    }
    @FXML
    private void editKorisnik() {
        Korisnik selected = TablicaKorisnika.getSelectionModel().getSelectedItem();
        try{
            if(selected.getIdKorisnika().equals("admin")){
                throw new AdminException();
            }
            if (selected != null) {
                // Open edit dialog
                Korisnik edited = korisnikEditDialog(selected);

                Iterator<Korisnik> iterator = observableKorisnici.iterator();
                while (iterator.hasNext()) {
                    Korisnik korisnik = iterator.next();
                    if (korisnik.getIdKorisnika().equals(edited.getIdKorisnika())) {

                        Main.zapisPromjene(korisnik, edited);

                        iterator.remove(); // Remove the current element from the list
                        observableKorisnici.add(edited); // Add the edited element back to the list
                        Database.updateKorisnik(edited);
                        break; // Exit the loop since we found and edited the element
                    }
                }


                // Perform additional actions if needed
                // Example: Database.editAdresa(editedAdresa);

                TablicaKorisnika.setItems(observableKorisnici);
            }
        } catch (AdminException e) {
            String message="Originalni Admin se ne može mijenjati";
            logger.error(message);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Greška");
            errorAlert.setHeaderText("Originalni Admin se ne može mijenjati!");
            errorAlert.setContentText("Odaberite drugog korisnika.");
            errorAlert.showAndWait();
        }

    }
}
