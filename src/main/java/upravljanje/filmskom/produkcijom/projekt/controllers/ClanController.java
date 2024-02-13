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
import upravljanje.filmskom.produkcijom.projekt.entiteti.ClanEkipe;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Producent;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Projekt;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class ClanController {
    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private TextField inputImePrezime;

    @FXML
    private ComboBox<Projekt> comboProjekt;

    @FXML
    private TableView<ClanEkipe> TablicaClanova;
    @FXML
    private TableColumn<ClanEkipe, String> idID;
    @FXML
    private TableColumn<ClanEkipe, String> imeID;
    @FXML
    private TableColumn<ClanEkipe, String> prezimeID;
    @FXML
    private TableColumn<ClanEkipe, String> oibID;
    @FXML
    private TableColumn<ClanEkipe, String> dobID;
    @FXML
    private TableColumn<ClanEkipe, String> opisID;
    @FXML
    private TableColumn<ClanEkipe, String> projektID;

    @FXML
    private Button filter;
    @FXML
    private Button obrisi;
    @FXML
    private Button promjeni;

    ObservableList<ClanEkipe> observableClanovi= FXCollections.observableArrayList(Database.dohvatiClanove());

    public void initialize() {

        if (PocetniLoginController.getRolica().equals("admin")){
        }
        else{
            obrisi.setVisible(false);
            promjeni.setVisible(false);
        }
        ObservableList<Projekt> observableProjekti = FXCollections.observableArrayList((Projekt) null);

        observableProjekti.addAll(Database.dohvatiProjekte());

        comboProjekt.setItems(observableProjekti);

        idID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getIdOsobe().toString()));
        imeID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getIme()));
        prezimeID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getPrezime()));
        oibID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getOIB()));
        dobID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDob().toString()));
        opisID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getOpisPosla()));
        projektID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getTrenutniProjekt().getIdProjekta().toString()));


        TablicaClanova.getSelectionModel().selectedItemProperty().addListener((observable, starOdabir, noviOdabir) -> {
            obrisi.setDisable(noviOdabir == null);
            promjeni.setDisable(noviOdabir == null);
        });


        TablicaClanova.setItems(observableClanovi);
    }


    public void clanoviSearch(){
        List<ClanEkipe> listaClanova= Database.dohvatiClanove();
        List<ClanEkipe> filtrirana=listaClanova;

        String imeprez=inputImePrezime.getText();
        Projekt projekt = comboProjekt.getValue();


        filtrirana = filtrirana.stream()
                .filter(i -> i.getPrezime().contains(imeprez))
                .filter(i -> projekt == null || projekt.getIdProjekta() == null ||
                        (i.getTrenutniProjekt() != null && i.getTrenutniProjekt().getIdProjekta().equals(projekt.getIdProjekta())))
                .collect(Collectors.toList());



        observableClanovi= FXCollections.observableArrayList(filtrirana);

        TablicaClanova.setItems(observableClanovi);
    };
    @FXML
    private void deleteClan() {
        ClanEkipe selectedClan = TablicaClanova.getSelectionModel().getSelectedItem();
        if (selectedClan != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Izbriši člana");
            alert.setContentText("Da li ste sigurni da želite obrisati ovog člana?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                observableClanovi.remove(selectedClan);
                Database.obrisiClana(selectedClan);
                // Call your delete method in the Database class if needed
                // Database.deleteAdresa(selectedAdresa);
            }
        }
    }
    public static ClanEkipe clanEditDialog(ClanEkipe clanEkipe) {
        Dialog<ClanEkipe> dialog = new Dialog<>();
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


        ObservableList<Projekt> observableProjekti = FXCollections.observableArrayList();

        observableProjekti.addAll(Database.dohvatiProjekte());

        // Create text input fields for each property
        TextField imeField = new TextField(clanEkipe.getIme());
        TextField prezimeField = new TextField(clanEkipe.getPrezime());
        TextField oibField = new TextField(clanEkipe.getOIB());
        TextField dobField = new TextField(clanEkipe.getDob().toString());
        TextArea opisField = new TextArea(clanEkipe.getOpisPosla());
        ComboBox<Projekt> projektField = new ComboBox<>(observableProjekti);

        projektField.setValue(clanEkipe.getTrenutniProjekt());


        // Add labels and text input fields to the GridPane
        grid.add(new Label("Ime:"), 0, 0);
        grid.add(imeField, 1, 0);
        grid.add(new Label("Prezime:"), 0, 1);
        grid.add(prezimeField, 1, 1);
        grid.add(new Label("Oib:"), 0, 2);
        grid.add(oibField, 1, 2);
        grid.add(new Label("Dob:"), 0, 3);
        grid.add(dobField, 1, 3);
        grid.add(new Label("Opis posla:"), 0, 4);
        grid.add(opisField, 1, 4);
        grid.add(new Label("Trenutni projekt:"), 0, 5);
        grid.add(projektField, 1, 5);


        // Enable/Disable save button depending on whether a field is filled
        Node saveButton = dialog.getDialogPane().lookupButton(spremiButton);
        saveButton.setDisable(true);

        // Add listeners to enable/disable the save button based on input fields
        imeField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        prezimeField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        oibField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        dobField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        opisField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        projektField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || projektField.getValue() == null ||
                        projektField.getValue() == null));






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
                        String ime =imeField.getText().trim();
                        String prezime = prezimeField.getText().trim();
                        String oib = oibField.getText();
                        Integer dob = Integer.parseInt(dobField.getText().trim());
                        String opis = opisField.getText();
                        Projekt projekt = projektField.getValue();

                        return new ClanEkipe(clanEkipe.getIdOsobe(), ime, prezime, oib, dob, opis, projekt);
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
        Optional<ClanEkipe> result = dialog.showAndWait();

        return result.orElse(clanEkipe);
    }
    @FXML
    private void editClan() {
        ClanEkipe selectedClan = TablicaClanova.getSelectionModel().getSelectedItem();
        if (selectedClan != null) {
            // Open edit dialog
            ClanEkipe editedClan = clanEditDialog(selectedClan);

            Iterator<ClanEkipe> iterator = observableClanovi.iterator();
            while (iterator.hasNext()) {
                ClanEkipe clanEkipe = iterator.next();
                if (clanEkipe.getIdOsobe().equals(editedClan.getIdOsobe())) {

                    Main.zapisPromjene(clanEkipe, editedClan);

                    iterator.remove(); // Remove the current element from the list
                    observableClanovi.add(editedClan); // Add the edited element back to the list
                    Database.updateClan(editedClan);
                    break; // Exit the loop since we found and edited the element
                }
            }


            // Perform additional actions if needed
            // Example: Database.editAdresa(editedAdresa);

            TablicaClanova.setItems(observableClanovi);
        }
    }
}
