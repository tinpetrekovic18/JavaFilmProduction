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
import upravljanje.filmskom.produkcijom.projekt.entiteti.*;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScenaController {
    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private ComboBox<LokacijaSnimanja> comboLokacija;
    @FXML
    private ComboBox<Projekt> comboProjekt;
    @FXML
    private ComboBox<Producent> comboProducent;
    @FXML
    private TableView<Scena> TablicaScena;
    @FXML
    private TableColumn<Scena, String> idID;
    @FXML
    private TableColumn<Scena, String> pocetakID;
    @FXML
    private TableColumn<Scena, String> krajID;
    @FXML
    private TableColumn<Scena, String> lokacijaID;
    @FXML
    private TableColumn<Scena, String> projektID;
    @FXML
    private TableColumn<Scena, String> producentID;
    @FXML
    private Button filter;
    @FXML
    private Button obrisi;
    @FXML
    private Button promjeni;

    ObservableList<Scena> observableScene= FXCollections.observableArrayList(Database.dohvatiScene());

    public void initialize() {

        if (PocetniLoginController.getRolica().equals("admin")){
        }
        else{
            obrisi.setVisible(false);
            promjeni.setVisible(false);
        }
        ObservableList<LokacijaSnimanja> observableLokacije = FXCollections.observableArrayList((LokacijaSnimanja) null);
        ObservableList<Projekt> observableProjekti = FXCollections.observableArrayList((Projekt) null);
        ObservableList<Producent> observableProducenti = FXCollections.observableArrayList((Producent) null);

        observableLokacije.addAll(Database.dohvatiLokacijeSnimanja());
        observableProjekti.addAll(Database.dohvatiProjekte());
        observableProducenti.addAll(Database.dohvatiProducente());

        comboLokacija.setItems(observableLokacije);
        comboProjekt.setItems(observableProjekti);
        comboProducent.setItems(observableProducenti);

        idID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getIdScene().toString()));
        pocetakID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getPocetakSnimanja().toString()));
        krajID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getKrajSnimanja().toString()));
        lokacijaID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getLokacija().toString()));
        projektID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getProjekt().toString()));
        producentID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getProducent().getIdOsobe().toString()));


        TablicaScena.getSelectionModel().selectedItemProperty().addListener((observable, starOdabir, noviOdabir) -> {
            obrisi.setDisable(noviOdabir == null);
            promjeni.setDisable(noviOdabir == null);
        });


        TablicaScena.setItems(observableScene);
    }

    public void sceneSearch(){
        List<Scena> listaScena= Database.dohvatiScene();
        List<Scena> filtrirana=listaScena;

        LokacijaSnimanja lokacija = comboLokacija.getValue();
        Projekt projekt = comboProjekt.getValue();
        Producent producent = comboProducent.getValue();


        filtrirana = filtrirana.stream()
                .filter(i -> lokacija == null || lokacija.getIdLokacije() == null || i.getLokacija().getIdLokacije().equals(lokacija.getIdLokacije()))
                .filter(i -> projekt == null || projekt.getIdProjekta() == null || i.getProjekt().getIdProjekta().equals(projekt.getIdProjekta()))
                .filter(i -> producent == null || producent.getIdOsobe() == null || i.getProducent().getIdOsobe().equals(producent.getIdOsobe()))
                .collect(Collectors.toList());


        observableScene= FXCollections.observableArrayList(filtrirana);

        TablicaScena.setItems(observableScene);
    };
    @FXML
    private void deleteScena() {
        Scena selected = TablicaScena.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Izbriši scenu");
            alert.setContentText("Da li ste sigurni da želite obrisati ovu scenu?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                observableScene.remove(selected);
                Database.obrisiScenu(selected);
                // Call your delete method in the Database class if needed
                // Database.deleteAdresa(selectedAdresa);
            }
        }
    }

    public static Scena scenaEditDialog(Scena scena) {
        Dialog<Scena> dialog = new Dialog<>();
        dialog.setTitle("Promjena scene");
        dialog.setHeaderText(null);

        // Set the button types
        ButtonType spremiButton = new ButtonType("Spremi promjenu", ButtonBar.ButtonData.OK_DONE);
        ButtonType odustaniButton = new ButtonType("Odustani", ButtonType.CANCEL.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(spremiButton, odustaniButton);

        // Create a GridPane to layout the input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);


        ObservableList<LokacijaSnimanja> observableLokacije = FXCollections.observableArrayList();
        ObservableList<Projekt> observableProjekti = FXCollections.observableArrayList();
        ObservableList<Producent> observableProducenti = FXCollections.observableArrayList();

        observableLokacije.addAll(Database.dohvatiLokacijeSnimanja());
        observableProjekti.addAll(Database.dohvatiProjekte());
        observableProducenti.addAll(Database.dohvatiProducente());

        // Create text input fields for each property
        DatePicker pocetakField = new DatePicker();
        DatePicker krajField = new DatePicker();

        ComboBox<LokacijaSnimanja> lokacijaField = new ComboBox<>(observableLokacije);
        ComboBox<Projekt> projektField = new ComboBox<>(observableProjekti);
        ComboBox<Producent> producentField = new ComboBox<>(observableProducenti);


        lokacijaField.setValue(scena.getLokacija());
        projektField.setValue(scena.getProjekt());
        producentField.setValue(scena.getProducent());

        pocetakField.setValue(LocalDate.from(scena.getPocetakSnimanja()));
        krajField.setValue(LocalDate.from(scena.getKrajSnimanja()));

        // Add labels and text input fields to the GridPane
        grid.add(new Label("Pocetak snimanja:"), 0, 0);
        grid.add(pocetakField, 1, 0);
        grid.add(new Label("Kraj snimanja:"), 0, 1);
        grid.add(krajField, 1, 1);
        grid.add(new Label("Lokacija:"), 0, 2);
        grid.add(lokacijaField, 1, 2);
        grid.add(new Label("Projekt:"), 0, 3);
        grid.add(projektField, 1, 3);
        grid.add(new Label("Producent:"), 0, 4);
        grid.add(producentField, 1, 4);


        // Enable/Disable save button depending on whether a field is filled
        Node saveButton = dialog.getDialogPane().lookupButton(spremiButton);
        saveButton.setDisable(true);

        // Add listeners to enable/disable the save button based on input fields

        pocetakField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || krajField.getValue() == null ||
                        projektField.getValue() == null || lokacijaField.getValue() == null));
        krajField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || producentField.getValue() == null ||
                        projektField.getValue() == null || lokacijaField.getValue() == null));

        lokacijaField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || krajField.getValue() == null ||
                        projektField.getValue() == null || pocetakField.getValue() == null));
        projektField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || krajField.getValue() == null ||
                        producentField.getValue() == null || pocetakField.getValue() == null));
        producentField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || krajField.getValue() == null ||
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
                        LocalDate pocetak = pocetakField.getValue();
                        LocalDate kraj = krajField.getValue();

                        LokacijaSnimanja lokacijaSnimanja = lokacijaField.getValue();
                        Projekt projekt = projektField.getValue();
                        Producent producent = producentField.getValue();


                        return new Scena(scena.getIdScene(), pocetak.atStartOfDay(), kraj.atStartOfDay(), lokacijaSnimanja, projekt, producent);
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
        Optional<Scena> result = dialog.showAndWait();

        return result.orElse(scena);
    }
    @FXML
    private void editScena() {
        Scena selected = TablicaScena.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Open edit dialog
            Scena edited = scenaEditDialog(selected);

            Iterator<Scena> iterator = observableScene.iterator();
            while (iterator.hasNext()) {
                Scena scena = iterator.next();
                if (scena.getIdScene().equals(edited.getIdScene())) {

                    Main.zapisPromjene(scena, edited);

                    iterator.remove(); // Remove the current element from the list
                    observableScene.add(edited); // Add the edited element back to the list
                    Database.updateScena(edited);
                    break; // Exit the loop since we found and edited the element
                }
            }


            // Perform additional actions if needed
            // Example: Database.editAdresa(editedAdresa);

            TablicaScena.setItems(observableScene);
        }
    }
}
