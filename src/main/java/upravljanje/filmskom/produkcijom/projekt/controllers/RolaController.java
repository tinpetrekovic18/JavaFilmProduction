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
import upravljanje.filmskom.produkcijom.projekt.entiteti.Rola;
import upravljanje.filmskom.produkcijom.projekt.iznimke.AdminException;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RolaController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private TableView<Rola> TablicaRola;
    @FXML
    private TableColumn<Rola, String> idID;
    @FXML
    private TableColumn<Rola, String> nazivID;
    @FXML
    private TableColumn<Rola, String> opisID;

    @FXML
    private Button obrisi;
    @FXML
    private Button promjeni;



    ObservableList<Rola> observableRole= FXCollections.observableArrayList(Database.dohvatiRole());

    public void initialize() {

        if (PocetniLoginController.getRolica().equals("admin")){
        }
        else{
            obrisi.setVisible(false);
            promjeni.setVisible(false);
        }

        idID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getIdRole().toString()));
        nazivID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getNazivRole()));
        opisID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getOpisRole()));


        TablicaRola.getSelectionModel().selectedItemProperty().addListener((observable, starOdabir, noviOdabir) -> {
            obrisi.setDisable(noviOdabir == null);
            promjeni.setDisable(noviOdabir == null);
        });


        TablicaRola.setItems(observableRole);
    }

    public void osvjezi(){
        List<Rola> listaRola= Database.dohvatiRole();

        observableRole= FXCollections.observableArrayList(listaRola);

        TablicaRola.setItems(observableRole);
    };

    @FXML
    private void deleteRola() {
        Rola selected = TablicaRola.getSelectionModel().getSelectedItem();
        try{
            if(selected.getNazivRole().equals("admin"))
            {
                throw new AdminException();
            }
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Izbriši rolu");
                alert.setContentText("Da li ste sigurni da želite obrisati ovu adresu?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    observableRole.remove(selected);
                    Database.obrisiRolu(selected);
                    // Call your delete method in the Database class if needed
                    // Database.deleteAdresa(selectedAdresa);
                }
            }
        } catch (AdminException e) {
            e.printStackTrace();
            String message="Originalni Admin se ne može mijenjati";
            logger.error(message);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Greška");
            errorAlert.setHeaderText("Originalni Admin se ne može mijenjati!");
            errorAlert.setContentText("Odaberite drugog korisnika.");
            errorAlert.showAndWait();
        }

    }
    public static Rola rolaEditDialog(Rola rola) {
        Dialog<Rola> dialog = new Dialog<>();
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
        TextField nazivField = new TextField(rola.getNazivRole());
        TextField opisField = new TextField(rola.getOpisRole());

        // Add labels and text input fields to the GridPane
        grid.add(new Label("Naziv:"), 0, 0);
        grid.add(nazivField, 1, 0);
        grid.add(new Label("Opis:"), 0, 1);
        grid.add(opisField, 1, 1);

        // Enable/Disable save button depending on whether a field is filled
        Node saveButton = dialog.getDialogPane().lookupButton(spremiButton);
        saveButton.setDisable(true);

        // Add listeners to enable/disable the save button based on input fields
        nazivField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        opisField.textProperty().addListener((observable, oldValue, newValue) ->
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
                        String naziv = nazivField.getText().trim();
                        String opis = opisField.getText().trim();

                        return new Rola(rola.getIdRole(), naziv, opis);
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
        Optional<Rola> result = dialog.showAndWait();

        return result.orElse(rola);
    }

    @FXML
    private void editRola() {
        Rola selected = TablicaRola.getSelectionModel().getSelectedItem();
        try{
            if(selected.getNazivRole().equals("admin")){
                throw new AdminException();
            }
            if (selected != null) {
                // Open edit dialog
                Rola edited = rolaEditDialog(selected);

                Iterator<Rola> iterator = observableRole.iterator();
                while (iterator.hasNext()) {
                    Rola rola = iterator.next();
                    if (rola.getIdRole().equals(edited.getIdRole())) {

                        Main.zapisPromjene(rola, edited);

                        iterator.remove(); // Remove the current element from the list
                        observableRole.add(edited); // Add the edited element back to the list
                        Database.updateRolu(edited);
                        break; // Exit the loop since we found and edited the element
                    }
                }


                // Perform additional actions if needed
                // Example: Database.editAdresa(editedAdresa);

                TablicaRola.setItems(observableRole);
            }
        }catch (AdminException e) {
            e.printStackTrace();
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
