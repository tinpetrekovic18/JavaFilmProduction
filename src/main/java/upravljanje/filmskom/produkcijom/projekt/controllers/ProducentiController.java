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
import upravljanje.filmskom.produkcijom.projekt.entiteti.Producent;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Projekt;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProducentiController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private TextField inputUmjet;

    @FXML
    private ComboBox<Projekt> comboProjekt;

    @FXML
    private TableView<Producent> TablicaProducenata;
    @FXML
    private TableColumn<Producent, String> idID;
    @FXML
    private TableColumn<Producent, String> imeID;
    @FXML
    private TableColumn<Producent, String> prezimeID;
    @FXML
    private TableColumn<Producent, String> oibID;
    @FXML
    private TableColumn<Producent, String> dobID;
    @FXML
    private TableColumn<Producent, String> umjetID;
    @FXML
    private TableColumn<Producent, String> nomID;
    @FXML
    private TableColumn<Producent, String> nagID;
    @FXML
    private TableColumn<Producent, String> projektID;
    @FXML
    private TableColumn<Producent, String> pocetakID;

    @FXML
    private Button filter;
    @FXML
    private Button obrisi;
    @FXML
    private Button promjeni;

    ObservableList<Producent> observableProducenti= FXCollections.observableArrayList(Database.dohvatiProducente());

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
        umjetID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getUmjetnickoIme()));
        nomID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getBrojNominacija().toString()));
        nagID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getBrojNagrada().toString()));
        projektID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getTrenutniProjekt().getIdProjekta().toString()));
        pocetakID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getPocetakRada().toString()));


        TablicaProducenata.getSelectionModel().selectedItemProperty().addListener((observable, starOdabir, noviOdabir) -> {
            obrisi.setDisable(noviOdabir == null);
            promjeni.setDisable(noviOdabir == null);
        });


        TablicaProducenata.setItems(observableProducenti);
    }


    public void producentiSearch(){
        List<Producent> listProducenata= Database.dohvatiProducente();
        List<Producent> filtrirana=listProducenata;

        String umjetnicko=inputUmjet.getText();
        Projekt projekt = comboProjekt.getValue();


        filtrirana = filtrirana.stream()
                .filter(i -> i.getUmjetnickoIme().contains(umjetnicko))
                .filter(i -> projekt == null || projekt.getIdProjekta() == null || i.getTrenutniProjekt().getIdProjekta().equals(projekt.getIdProjekta()))
                .collect(Collectors.toList());


        observableProducenti= FXCollections.observableArrayList(filtrirana);

        TablicaProducenata.setItems(observableProducenti);
    };
    @FXML
    private void deleteProducent() {
        Producent selectedProducent = TablicaProducenata.getSelectionModel().getSelectedItem();
        if (selectedProducent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Izbriši glumca");
            alert.setContentText("Da li ste sigurni da želite obrisati ovog glumca?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                observableProducenti.remove(selectedProducent);
                Database.obrisiProducent(selectedProducent);
                // Call your delete method in the Database class if needed
                // Database.deleteAdresa(selectedAdresa);
            }
        }
    }
    public static Producent producentEditDialog(Producent producent) {
        Dialog<Producent> dialog = new Dialog<>();
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
        TextField imeField = new TextField(producent.getIme());
        TextField prezimeField = new TextField(producent.getPrezime());
        TextField oibField = new TextField(producent.getOIB());
        TextField dobField = new TextField(producent.getDob().toString());
        TextField umjetnickoField = new TextField(producent.getUmjetnickoIme());
        TextField nominaciejField = new TextField(producent.getBrojNominacija().toString());
        TextField nagradeField = new TextField(producent.getBrojNagrada().toString());
        ComboBox<Projekt> projektField = new ComboBox<>(observableProjekti);
        DatePicker pocetakField = new DatePicker();

        projektField.setValue(producent.getTrenutniProjekt());
        pocetakField.setValue(producent.getPocetakRada());

        // Add labels and text input fields to the GridPane
        grid.add(new Label("Ime:"), 0, 0);
        grid.add(imeField, 1, 0);
        grid.add(new Label("Prezime:"), 0, 1);
        grid.add(prezimeField, 1, 1);
        grid.add(new Label("Oib:"), 0, 2);
        grid.add(oibField, 1, 2);
        grid.add(new Label("Dob:"), 0, 3);
        grid.add(dobField, 1, 3);
        grid.add(new Label("Umjetnicko ime:"), 0, 4);
        grid.add(umjetnickoField, 1, 4);
        grid.add(new Label("Broj nominacija:"), 0, 5);
        grid.add(nominaciejField, 1, 5);
        grid.add(new Label("Broj nagrada:"), 0, 6);
        grid.add(nagradeField, 1, 6);
        grid.add(new Label("Trenutni projekt:"), 0, 7);
        grid.add(projektField, 1, 7);
        grid.add(new Label("Pocetak rada:"), 0, 8);
        grid.add(pocetakField, 1, 8);

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
        umjetnickoField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        nominaciejField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        nagradeField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        projektField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || projektField.getValue() == null ||
                        projektField.getValue() == null || pocetakField.getValue() == null));
        pocetakField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || projektField.getValue() == null ||
                        projektField.getValue() == null || pocetakField.getValue() == null));





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
                        String umjetnicko = umjetnickoField.getText();
                        Integer nominacije = Integer.parseInt(nominaciejField.getText().trim());
                        Integer nagrade = Integer.parseInt(nagradeField.getText().trim());
                        Projekt projekt = projektField.getValue();
                        LocalDate pocetak = pocetakField.getValue();

                        return new Producent(producent.getIdOsobe(), ime, prezime, oib, dob, umjetnicko, nominacije, nagrade, projekt, pocetak);
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
        Optional<Producent> result = dialog.showAndWait();

        return result.orElse(producent);
    }
    @FXML
    private void editProducent() {
        Producent selectedProducent = TablicaProducenata.getSelectionModel().getSelectedItem();
        if (selectedProducent != null) {
            // Open edit dialog
            Producent editedProducent = producentEditDialog(selectedProducent);

            Iterator<Producent> iterator = observableProducenti.iterator();
            while (iterator.hasNext()) {
                Producent producent = iterator.next();
                if (producent.getIdOsobe().equals(editedProducent.getIdOsobe())) {

                    Main.zapisPromjene(producent, editedProducent);

                    iterator.remove(); // Remove the current element from the list
                    observableProducenti.add(editedProducent); // Add the edited element back to the list
                    Database.updateProducent(editedProducent);
                    break; // Exit the loop since we found and edited the element
                }
            }


            // Perform additional actions if needed
            // Example: Database.editAdresa(editedAdresa);

            TablicaProducenata.setItems(observableProducenti);
        }
    }
}
