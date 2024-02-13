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

public class ProdukcijskaController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private TextField inputNaziv;
    @FXML
    private TableView<ProdukcijskaKuca> TablicaProduk;
    @FXML
    private TableColumn<ProdukcijskaKuca, String> idID;
    @FXML
    private TableColumn<ProdukcijskaKuca, String> nazivID;
    @FXML
    private TableColumn<ProdukcijskaKuca, String> godinaID;
    @FXML
    private TableColumn<ProdukcijskaKuca, String> sjedisteID;
    @FXML
    private Button filter;
    @FXML
    private Button obrisi;
    @FXML
    private Button promjeni;

    ObservableList<ProdukcijskaKuca> observableKuce= FXCollections.observableArrayList(Database.dohvatiProdukcijskeKuce());

    public void initialize() {

        if (PocetniLoginController.getRolica().equals("admin")){
        }
        else{
            obrisi.setVisible(false);
            promjeni.setVisible(false);
        }



        idID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getIdProdukcijskeKuce().toString()));
        nazivID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getNaziv()));
        godinaID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getGodinaOsnutka().toString()));
        sjedisteID.setCellValueFactory(param -> {
            Adresa adresa = param.getValue().getSjediste();
            return new ReadOnlyStringWrapper((adresa != null) ? adresa.getId().toString() + " - " + adresa.getUlica() : "");
        });



        TablicaProduk.getSelectionModel().selectedItemProperty().addListener((observable, starOdabir, noviOdabir) -> {
            obrisi.setDisable(noviOdabir == null);
            promjeni.setDisable(noviOdabir == null);
        });


        TablicaProduk.setItems(observableKuce);
    }


    public void kuceSearch() {
        List<ProdukcijskaKuca> listKuca = Database.dohvatiProdukcijskeKuce();
        List<ProdukcijskaKuca> filtrirana = listKuca;

        String naziv = inputNaziv.getText();

        filtrirana = filtrirana.stream()
                .filter(i -> i.getNaziv().contains(naziv))
                .collect(Collectors.toList());


        observableKuce = FXCollections.observableArrayList(filtrirana);

        TablicaProduk.setItems(observableKuce);
    }


    @FXML
    private void deleteKuca() {
        ProdukcijskaKuca selectedKuca = TablicaProduk.getSelectionModel().getSelectedItem();
        if (selectedKuca != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Izbriši lokaciju snimanja");
            alert.setContentText("Da li ste sigurni da želite obrisati ovu lokaciju?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                observableKuce.remove(selectedKuca);
                Database.obrisiKucu(selectedKuca);

            }
        }
    }

    public static ProdukcijskaKuca kucaEditDialog(ProdukcijskaKuca produkcijskaKuca) {
        Dialog<ProdukcijskaKuca> dialog = new Dialog<>();
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


        ObservableList<Adresa> observableAdrese = FXCollections.observableArrayList();

        observableAdrese.addAll(Database.dohvatiAdrese());

        // Create text input fields for each property
        TextField nazivField = new TextField(produkcijskaKuca.getNaziv());

        TextField godinaField = new TextField(produkcijskaKuca.getGodinaOsnutka().toString());

        ComboBox<Adresa> adresaField = new ComboBox<>(observableAdrese);

        adresaField.setValue(produkcijskaKuca.getSjediste());


        // Add labels and text input fields to the GridPane
        grid.add(new Label("Naziv:"), 0, 0);
        grid.add(nazivField, 1, 0);
        grid.add(new Label("Godina osnutka:"), 0, 1);
        grid.add(godinaField, 1, 1);
        grid.add(new Label("Sjedište:"), 0, 2);
        grid.add(adresaField, 1, 2);


        // Enable/Disable save button depending on whether a field is filled
        Node saveButton = dialog.getDialogPane().lookupButton(spremiButton);
        saveButton.setDisable(true);

        // Add listeners to enable/disable the save button based on input fields
        nazivField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        godinaField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        adresaField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null));



        // Set the GridPane as the content of the dialog
        dialog.getDialogPane().setContent(grid);

        // Convert the result to an Adresa object when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == spremiButton) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Da li ste sigurni da želite promjeniti produkcijsku kuću?");
                alert.setContentText("Potvrdite svoj odabir");

                ButtonType promjena = new ButtonType("Promjeni");
                ButtonType odustani = new ButtonType("Odustani");

                alert.getButtonTypes().setAll(promjena, odustani);

                Optional<ButtonType> resultButtonType = alert.showAndWait();

                if (resultButtonType.isPresent() && resultButtonType.get() == promjena) {
                    try {
                        String naziv =nazivField.getText().trim();
                        Integer godina = Integer.parseInt(godinaField.getText().trim());
                        Adresa adresa = adresaField.getValue();

                        return new ProdukcijskaKuca(produkcijskaKuca.getIdProdukcijskeKuce(), naziv, godina, adresa);
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
        Optional<ProdukcijskaKuca> result = dialog.showAndWait();

        return result.orElse(produkcijskaKuca);
    }

    @FXML
    private void editKuca() {
        ProdukcijskaKuca selected = TablicaProduk.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Open edit dialog
            ProdukcijskaKuca edited = kucaEditDialog(selected);

            Iterator<ProdukcijskaKuca> iterator = observableKuce.iterator();
            while (iterator.hasNext()) {
                ProdukcijskaKuca kuca = iterator.next();
                if (kuca.getIdProdukcijskeKuce().equals(edited.getIdProdukcijskeKuce())) {

                    Main.zapisPromjene(kuca, edited);

                    iterator.remove(); // Remove the current element from the list
                    observableKuce.add(edited); // Add the edited element back to the list
                    Database.updateKuca(edited);
                    break; // Exit the loop since we found and edited the element
                }
            }


            // Perform additional actions if needed
            // Example: Database.editAdresa(editedAdresa);

            TablicaProduk.setItems(observableKuce);
        }
    }
}
