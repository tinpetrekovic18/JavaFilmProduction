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
import upravljanje.filmskom.produkcijom.projekt.entiteti.Promjena;
import upravljanje.filmskom.produkcijom.projekt.main.Main;
import upravljanje.filmskom.produkcijom.projekt.threads.DeserijalizacijaThread;
import upravljanje.filmskom.produkcijom.projekt.threads.SerijalizacijaThread;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdreseController {

    @FXML
    private TextField InputUlica;

    @FXML
    private TextField InputGrad;

    @FXML
    private TextField InputPbr;

    @FXML
    private TableView<Adresa> TablicaAdresa;
    @FXML
    private TableColumn<Adresa, String> idID;
    @FXML
    private TableColumn<Adresa, String> brojID;
    @FXML
    private TableColumn<Adresa, String> ulicaID;
    @FXML
    private TableColumn<Adresa, String> gradID;
    @FXML
    private TableColumn<Adresa, String> pbrID;
    @FXML
    private TableColumn<Adresa, String> drzavaID;

    @FXML
    private Button filter;
    @FXML
    private Button obrisi;
    @FXML
    private Button promjeni;

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    ObservableList<Adresa> observableAdrese= FXCollections.observableArrayList(Database.dohvatiAdrese());

    public void initialize() {

        if (PocetniLoginController.getRolica().equals("admin")){
        }
        else{
            obrisi.setVisible(false);
            promjeni.setVisible(false);
        }

        idID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getId().toString()));
        brojID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getBroj().toString()));
        ulicaID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getUlica()));
        gradID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getGrad()));
        pbrID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getPostanskiBroj()));
        drzavaID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getDrzava()));


        TablicaAdresa.getSelectionModel().selectedItemProperty().addListener((observable, starOdabir, noviOdabir) -> {
            obrisi.setDisable(noviOdabir == null);
            promjeni.setDisable(noviOdabir == null);
        });


        TablicaAdresa.setItems(observableAdrese);
    }

    public void adreseSearch(){
        List<Adresa> listaAdresa= Database.dohvatiAdrese();
        List<Adresa> filtrirana=listaAdresa;

        String ulica=InputUlica.getText();
        String grad=InputGrad.getText();
        String pbr=InputPbr.getText();

        filtrirana = filtrirana.stream()
                .filter(i -> i.getUlica().contains(ulica))
                .filter(i -> i.getGrad().contains(grad))
                .filter(i -> i.getPostanskiBroj().contains(pbr))
                .collect(Collectors.toList());


        observableAdrese= FXCollections.observableArrayList(filtrirana);

        TablicaAdresa.setItems(observableAdrese);
    };
    @FXML
    private void deleteAdresa() {
        Adresa selectedAdresa = TablicaAdresa.getSelectionModel().getSelectedItem();
        if (selectedAdresa != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Izbriši adresu");
            alert.setContentText("Da li ste sigurni da želite obrisati ovu adresu?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                observableAdrese.remove(selectedAdresa);
                Database.obrisiAdresu(selectedAdresa);
                // Call your delete method in the Database class if needed
                // Database.deleteAdresa(selectedAdresa);
            }
        }
    }
    public static Adresa adresaEditDialog(Adresa adresa) {
        Dialog<Adresa> dialog = new Dialog<>();
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

        // Create text input fields for each property
        TextField brojField = new TextField(adresa.getBroj().toString());
        TextField ulicaField = new TextField(adresa.getUlica());
        TextField gradField = new TextField(adresa.getGrad());
        TextField postanskiBrojField = new TextField(adresa.getPostanskiBroj());
        TextField drzavaField = new TextField(adresa.getDrzava());

        // Add labels and text input fields to the GridPane
        grid.add(new Label("Broj:"), 0, 0);
        grid.add(brojField, 1, 0);
        grid.add(new Label("Ulica:"), 0, 1);
        grid.add(ulicaField, 1, 1);
        grid.add(new Label("Grad:"), 0, 2);
        grid.add(gradField, 1, 2);
        grid.add(new Label("Postanski Broj:"), 0, 3);
        grid.add(postanskiBrojField, 1, 3);
        grid.add(new Label("Drzava:"), 0, 4);
        grid.add(drzavaField, 1, 4);

        // Enable/Disable save button depending on whether a field is filled
        Node saveButton = dialog.getDialogPane().lookupButton(spremiButton);
        saveButton.setDisable(true);

        // Add listeners to enable/disable the save button based on input fields
        brojField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        ulicaField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        gradField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        postanskiBrojField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        drzavaField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));

        // Set the GridPane as the content of the dialog
        dialog.getDialogPane().setContent(grid);

        // Convert the result to an Adresa object when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == spremiButton) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Da li ste sigurni da želite promjeniti adresu?");
                alert.setContentText("Potvrdite svoj odabir");

                ButtonType promjena = new ButtonType("Promjeni");
                ButtonType odustani = new ButtonType("Odustani");

                alert.getButtonTypes().setAll(promjena, odustani);

                Optional<ButtonType> resultButtonType = alert.showAndWait();

                if (resultButtonType.isPresent() && resultButtonType.get() == promjena) {
                    try {
                        Integer broj = Integer.parseInt(brojField.getText().trim());
                        String ulica = ulicaField.getText().trim();
                        String grad = gradField.getText().trim();
                        String postanskiBroj = postanskiBrojField.getText().trim();
                        String drzava = drzavaField.getText().trim();

                        return new Adresa(adresa.getId(), broj, ulica, grad, postanskiBroj, drzava);
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
        Optional<Adresa> result = dialog.showAndWait();

        return result.orElse(adresa);
    }
    @FXML
    private void editAdresa() {
        Adresa selectedAdresa = TablicaAdresa.getSelectionModel().getSelectedItem();
        if (selectedAdresa != null) {
            // Open edit dialog
            Adresa editedAdresa = adresaEditDialog(selectedAdresa);

            Iterator<Adresa> iterator = observableAdrese.iterator();
            while (iterator.hasNext()) {
                Adresa adresa = iterator.next();
                if (adresa.getId().equals(editedAdresa.getId())) {

                    Main.zapisPromjene(adresa, editedAdresa);

                    iterator.remove(); // Remove the current element from the list
                    observableAdrese.add(editedAdresa); // Add the edited element back to the list
                    Database.updateAdresu(editedAdresa);
                    break; // Exit the loop since we found and edited the element
                }
            }


            // Perform additional actions if needed
            // Example: Database.editAdresa(editedAdresa);

            TablicaAdresa.setItems(observableAdrese);
        }
    }


}
