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
import upravljanje.filmskom.produkcijom.projekt.entiteti.Adresa;
import upravljanje.filmskom.produkcijom.projekt.entiteti.LokacijaSnimanja;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Projekt;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LokacijeController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private ComboBox<Adresa> comboAdresa;

    @FXML
    private ComboBox<Projekt> comboProjekt;

    @FXML
    private TableView<LokacijaSnimanja> TablicaLokacija;
    @FXML
    private TableColumn<LokacijaSnimanja, String> idID;
    @FXML
    private TableColumn<LokacijaSnimanja, String> adresaID;
    @FXML
    private TableColumn<LokacijaSnimanja, String> projektID;
    @FXML
    private TableColumn<LokacijaSnimanja, String> odID;
    @FXML
    private TableColumn<LokacijaSnimanja, String> doID;
    @FXML
    private Button filter;
    @FXML
    private Button obrisi;
    @FXML
    private Button promjeni;

    ObservableList<LokacijaSnimanja> observableLokacije= FXCollections.observableArrayList(Database.dohvatiLokacijeSnimanja());

    public void initialize() {

        if (PocetniLoginController.getRolica().equals("admin")){
        }
        else{
            obrisi.setVisible(false);
            promjeni.setVisible(false);
        }

        ObservableList<Adresa> observableAdrese = FXCollections.observableArrayList((Adresa) null);
        ObservableList<Projekt> observableProjekti = FXCollections.observableArrayList((Projekt) null);

        observableAdrese.addAll(Database.dohvatiAdrese());
        observableProjekti.addAll(Database.dohvatiProjekte());

        comboAdresa.setItems(observableAdrese);
        comboProjekt.setItems(observableProjekti);


        idID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getIdLokacije().toString()));
        adresaID.setCellValueFactory(param -> {
            Adresa adresa = param.getValue().getLokacija();
            return new ReadOnlyStringWrapper((adresa != null) ? adresa.getId().toString() + " - " + adresa.getUlica() : "");
        });
        projektID.setCellValueFactory(param -> {
            Projekt projekt = param.getValue().getProjekt();
            return new ReadOnlyStringWrapper((projekt != null) ? projekt.getIdProjekta().toString() : "");
        });
        odID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getRezerviranoOd().toString()));
        doID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getRezerviranoDo().toString()));



        TablicaLokacija.getSelectionModel().selectedItemProperty().addListener((observable, starOdabir, noviOdabir) -> {
            obrisi.setDisable(noviOdabir == null);
            promjeni.setDisable(noviOdabir == null);
        });


        TablicaLokacija.setItems(observableLokacije);
    }


    public void lokacijeSearch() {
        List<LokacijaSnimanja> listaLokacija = Database.dohvatiLokacijeSnimanja();
        List<LokacijaSnimanja> filtrirana = listaLokacija;

        Adresa adresa = comboAdresa.getValue();
        Projekt projekt = comboProjekt.getValue();

        filtrirana = filtrirana.stream()
                .filter(i -> adresa == null || adresa.getId() == null || i.getLokacija().getId().equals(adresa.getId()))
                .filter(i -> projekt == null || projekt.getIdProjekta() == null || i.getProjekt().getIdProjekta().equals(projekt.getIdProjekta()))
                .collect(Collectors.toList());


        observableLokacije = FXCollections.observableArrayList(filtrirana);

        TablicaLokacija.setItems(observableLokacije);
    }


    @FXML
    private void deleteLokacija() {
        LokacijaSnimanja selectedLokacija = TablicaLokacija.getSelectionModel().getSelectedItem();
        if (selectedLokacija != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Izbriši lokaciju snimanja");
            alert.setContentText("Da li ste sigurni da želite obrisati ovu lokaciju?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                observableLokacije.remove(selectedLokacija);
                Database.obrisiLokaciju(selectedLokacija);

            }
        }
    }


    public static LokacijaSnimanja lokacijaEditDialog(LokacijaSnimanja lokacija) {
        Dialog<LokacijaSnimanja> dialog = new Dialog<>();
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

        ObservableList<Adresa> observableAdrese = FXCollections.observableArrayList((Adresa) null);
        ObservableList<Projekt> observableProjekti = FXCollections.observableArrayList((Projekt) null);

        observableAdrese.addAll(Database.dohvatiAdrese());
        observableProjekti.addAll(Database.dohvatiProjekte());
        // Create ComboBoxes and DatePickers
        ComboBox<Adresa> adresaField = new ComboBox<>(observableAdrese);
        ComboBox<Projekt> projektField = new ComboBox<>(observableProjekti);
        DatePicker odField = new DatePicker();
        DatePicker doField = new DatePicker();

        // Set existing values
        adresaField.setValue(lokacija.getLokacija());
        projektField.setValue(lokacija.getProjekt());
        odField.setValue(lokacija.getRezerviranoOd().toLocalDate());
        doField.setValue(lokacija.getRezerviranoDo().toLocalDate());

        // Add labels and input fields to the GridPane
        grid.add(new Label("Adresa:"), 0, 0);
        grid.add(adresaField, 1, 0);
        grid.add(new Label("Projekt:"), 0, 1);
        grid.add(projektField, 1, 1);
        grid.add(new Label("Od:"), 0, 2);
        grid.add(odField, 1, 2);
        grid.add(new Label("Do:"), 0, 3);
        grid.add(doField, 1, 3);

        // Enable/Disable save button depending on whether a field is filled
        Node saveButton = dialog.getDialogPane().lookupButton(spremiButton);
        saveButton.setDisable(true);

        // Add listeners to enable/disable the save button based on input fields
        adresaField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || projektField.getValue() == null ||
                        odField.getValue() == null || doField.getValue() == null));
        projektField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || adresaField.getValue() == null ||
                        odField.getValue() == null || doField.getValue() == null));
        odField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || adresaField.getValue() == null ||
                        projektField.getValue() == null || doField.getValue() == null));
        doField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || adresaField.getValue() == null ||
                        projektField.getValue() == null || odField.getValue() == null));

        // Set the GridPane as the content of the dialog
        dialog.getDialogPane().setContent(grid);

        // Convert the result to a LokacijaSnimanja object when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == spremiButton) {
                try {
                    Adresa selectedAdresa = adresaField.getValue();
                    Projekt selectedProjekt = projektField.getValue();
                    LocalDate odDate = odField.getValue();
                    LocalDate doDate = doField.getValue();

                    // Create a new LokacijaSnimanja with the selected values
                    return new LokacijaSnimanja(
                            lokacija.getIdLokacije(),
                            selectedAdresa,
                            selectedProjekt,
                            odDate.atStartOfDay(),
                            doDate.atStartOfDay()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle exception if needed
                }
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<LokacijaSnimanja> result = dialog.showAndWait();

        return result.orElse(lokacija);
    }



    @FXML
    private void editLokacija() {
        LokacijaSnimanja selectedLokacija = TablicaLokacija.getSelectionModel().getSelectedItem();
        if (selectedLokacija != null) {
            // Open edit dialog
            LokacijaSnimanja editedLokacija = lokacijaEditDialog(selectedLokacija);

            Iterator<LokacijaSnimanja> iterator = observableLokacije.iterator();
            while (iterator.hasNext()) {
                LokacijaSnimanja lokacija = iterator.next();
                if (lokacija.getIdLokacije().equals(editedLokacija.getIdLokacije())) {

                    Main.zapisPromjene(lokacija, editedLokacija);

                    iterator.remove(); // Remove the current element from the list
                    observableLokacije.add(editedLokacija); // Add the edited element back to the list
                    Database.updateLokaciju(editedLokacija);
                    break; // Exit the loop since we found and edited the element
                }
            }


            // Perform additional actions if needed
            // Example: Database.editAdresa(editedAdresa);

            TablicaLokacija.setItems(observableLokacije);
        }
    }




}
