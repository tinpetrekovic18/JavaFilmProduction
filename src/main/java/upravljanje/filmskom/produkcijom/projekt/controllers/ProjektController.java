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

public class ProjektController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private ComboBox<ProdukcijskaKuca> comboKuca;
    @FXML
    private TableView<Projekt> TablicaProjekata;
    @FXML
    private TableColumn<Projekt, String> idID;
    @FXML
    private TableColumn<Projekt, String> kucaID;
    @FXML
    private TableColumn<Projekt, String> nomID;
    @FXML
    private TableColumn<Projekt, String> nagID;
    @FXML
    private TableColumn<Projekt, String> finID;
    @FXML
    private TableColumn<Projekt, String> disID;
    @FXML
    private TableColumn<Projekt, String> pocetakID;

    @FXML
    private Button filter;
    @FXML
    private Button obrisi;
    @FXML
    private Button promjeni;

    ObservableList<Projekt> observableProjekti= FXCollections.observableArrayList(Database.dohvatiProjekte());

    public void initialize() {

        if (PocetniLoginController.getRolica().equals("admin")){
        }
        else{
            obrisi.setVisible(false);
            promjeni.setVisible(false);
        }
        ObservableList<ProdukcijskaKuca> observableKuce = FXCollections.observableArrayList((ProdukcijskaKuca) null);

        observableKuce.addAll(Database.dohvatiProdukcijskeKuce());

        comboKuca.setItems(observableKuce);

        idID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getIdProjekta().toString()));
        kucaID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getProdukcijskaKuca().getIdProdukcijskeKuce().toString()));
        nomID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getBrojNominacija().toString()));
        nagID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getBrojNagrada().toString()));
        finID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getFinancije().getIdFinancije().toString()));
        disID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDistribucija().toString()));
        pocetakID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getPocetakRada().toString()));


        TablicaProjekata.getSelectionModel().selectedItemProperty().addListener((observable, starOdabir, noviOdabir) -> {
            obrisi.setDisable(noviOdabir == null);
            promjeni.setDisable(noviOdabir == null);
        });


        TablicaProjekata.setItems(observableProjekti);
    }

    public void projektiSearch(){
        List<Projekt> listaProjekata= Database.dohvatiProjekte();
        List<Projekt> filtrirana=listaProjekata;

        ProdukcijskaKuca kuca = comboKuca.getValue();


        filtrirana = filtrirana.stream()
                .filter(i -> kuca == null || kuca.getIdProdukcijskeKuce() == null || i.getProdukcijskaKuca().getIdProdukcijskeKuce().equals(kuca.getIdProdukcijskeKuce()))
                .collect(Collectors.toList());


        observableProjekti= FXCollections.observableArrayList(filtrirana);

        TablicaProjekata.setItems(observableProjekti);
    };
    @FXML
    private void deleteProjekt() {
        Projekt selected = TablicaProjekata.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Izbriši glumca");
            alert.setContentText("Da li ste sigurni da želite obrisati ovog glumca?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                observableProjekti.remove(selected);
                Database.obrisiProjekt(selected);
                // Call your delete method in the Database class if needed
                // Database.deleteAdresa(selectedAdresa);
            }
        }
    }

    public static Projekt projetkEditDialog(Projekt projekt) {
        Dialog<Projekt> dialog = new Dialog<>();
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


        ObservableList<ProdukcijskaKuca> observableKuce = FXCollections.observableArrayList();
        ObservableList<Financije> observableFinancije = FXCollections.observableArrayList();
        ObservableList<Distribucija> observableDistribucije = FXCollections.observableArrayList();

        observableKuce.addAll(Database.dohvatiProdukcijskeKuce());
        observableFinancije.addAll(Database.dohvatiFinancije());
        observableDistribucije.addAll(Database.dohvatiDistribucije());

        // Create text input fields for each property
        ComboBox<ProdukcijskaKuca> kucaField = new ComboBox<>(observableKuce);

        TextField nominacijeField = new TextField(projekt.getBrojNominacija().toString());
        TextField nagradeField = new TextField(projekt.getBrojNagrada().toString());

        ComboBox<Financije> financijeField = new ComboBox<>(observableFinancije);
        ComboBox<Distribucija> distribucijaField = new ComboBox<>(observableDistribucije);
        DatePicker pocetakField = new DatePicker();

        kucaField.setValue(projekt.getProdukcijskaKuca());
        financijeField.setValue(projekt.getFinancije());
        distribucijaField.setValue(projekt.getDistribucija());

        pocetakField.setValue(projekt.getPocetakRada());

        // Add labels and text input fields to the GridPane
        grid.add(new Label("Produkcijska kuća:"), 0, 0);
        grid.add(kucaField, 1, 0);
        grid.add(new Label("Broj nominacija:"), 0, 1);
        grid.add(nominacijeField, 1, 1);
        grid.add(new Label("Broj nagrada:"), 0, 2);
        grid.add(nagradeField, 1, 2);
        grid.add(new Label("Financije:"), 0, 3);
        grid.add(financijeField, 1, 3);
        grid.add(new Label("Distribucija:"), 0, 4);
        grid.add(distribucijaField, 1, 4);
        grid.add(new Label("Pocetak rada:"), 0, 5);
        grid.add(pocetakField, 1, 5);

        // Enable/Disable save button depending on whether a field is filled
        Node saveButton = dialog.getDialogPane().lookupButton(spremiButton);
        saveButton.setDisable(true);

        // Add listeners to enable/disable the save button based on input fields

        kucaField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || financijeField.getValue() == null ||
                        distribucijaField.getValue() == null || pocetakField.getValue() == null));

        nominacijeField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        nagradeField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));


        financijeField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || kucaField.getValue() == null ||
                        distribucijaField.getValue() == null || pocetakField.getValue() == null));
        distribucijaField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || financijeField.getValue() == null ||
                        kucaField.getValue() == null || pocetakField.getValue() == null));
        pocetakField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || financijeField.getValue() == null ||
                        distribucijaField.getValue() == null || kucaField.getValue() == null));





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
                        ProdukcijskaKuca kuca = kucaField.getValue();

                        Integer nominacije = Integer.parseInt(nominacijeField.getText().trim());
                        Integer nagrade = Integer.parseInt(nagradeField.getText().trim());

                        Financije financije = financijeField.getValue();
                        Distribucija distribucija = distribucijaField.getValue();
                        LocalDate pocetak = pocetakField.getValue();

                        return new Projekt(projekt.getIdProjekta(), kuca,nominacije, nagrade, financije, distribucija, pocetak);
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
        Optional<Projekt> result = dialog.showAndWait();

        return result.orElse(projekt);
    }
    @FXML
    private void editProjekt() {
        Projekt selected = TablicaProjekata.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Open edit dialog
            Projekt edited = projetkEditDialog(selected);

            Iterator<Projekt> iterator = observableProjekti.iterator();
            while (iterator.hasNext()) {
                Projekt projekt = iterator.next();
                if (projekt.getIdProjekta().equals(edited.getIdProjekta())) {

                    Main.zapisPromjene(projekt, edited);

                    iterator.remove(); // Remove the current element from the list
                    observableProjekti.add(edited); // Add the edited element back to the list
                    Database.updateProjekt(edited);
                    break; // Exit the loop since we found and edited the element
                }
            }


            // Perform additional actions if needed
            // Example: Database.editAdresa(editedAdresa);

            TablicaProjekata.setItems(observableProjekti);
        }
    }
}
