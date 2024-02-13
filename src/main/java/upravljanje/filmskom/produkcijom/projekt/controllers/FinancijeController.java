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
import upravljanje.filmskom.produkcijom.projekt.entiteti.Cijena;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Financije;
import upravljanje.filmskom.produkcijom.projekt.iznimke.CijenaException;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FinancijeController {
    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private TextField inputOD;

    @FXML
    private TextField inputDO;

    @FXML
    private TableView<Financije> TablicaFinancija;
    @FXML
    private TableColumn<Financije, String> idID;
    @FXML
    private TableColumn<Financije, String> proracunID;
    @FXML
    private TableColumn<Financije, String> troskoviID;
    @FXML
    private TableColumn<Financije, String> prihodiID;

    @FXML
    private Button filter;
    @FXML
    private Button obrisi;
    @FXML
    private Button promjeni;

    ObservableList<Financije> observableFinacnije= FXCollections.observableArrayList(Database.dohvatiFinancije());

    public void initialize() {

        if (PocetniLoginController.getRolica().equals("admin")){
        }
        else{
            obrisi.setVisible(false);
            promjeni.setVisible(false);
        }

        idID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getIdFinancije().toString()));
        proracunID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getProracun().toString()));
        troskoviID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getTroskovi().toString()));
        prihodiID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getPrihodi().toString()));



        TablicaFinancija.getSelectionModel().selectedItemProperty().addListener((observable, starOdabir, noviOdabir) -> {
            obrisi.setDisable(noviOdabir == null);
            promjeni.setDisable(noviOdabir == null);
        });


        TablicaFinancija.setItems(observableFinacnije);
    }

    private boolean unutarCijene(Financije financije, BigDecimal cijenaOd, BigDecimal cijenaDo) {
        BigDecimal proracun = financije.getProracun().cijena();
        BigDecimal troskovi = financije.getTroskovi().cijena();
        BigDecimal prihodi = financije.getPrihodi().cijena();

        return (proracun.compareTo(cijenaOd) >= 0 && proracun.compareTo(cijenaDo) <= 0) ||
                (troskovi.compareTo(cijenaOd) >= 0 && troskovi.compareTo(cijenaDo) <= 0) ||
                (prihodi.compareTo(cijenaOd) >= 0 && prihodi.compareTo(cijenaDo) <= 0);
    }
    public void financijeSearch() {
        List<Financije> listaFinancija = Database.dohvatiFinancije();
        List<Financije> filtrirana = listaFinancija;

        String inputODOdText = inputOD.getText();
        String inputDODoText = inputDO.getText();

        if (inputODOdText.isEmpty() || inputDODoText.isEmpty()) {
            inputODOdText="0";
            inputDODoText="100000000000000000000000000";
            return;
        }

        BigDecimal cijenaOd = BigDecimal.valueOf(Long.parseLong(inputODOdText));
        BigDecimal cijenaDo = BigDecimal.valueOf(Long.parseLong(inputDODoText));

        filtrirana = filtrirana.stream()
                .filter(i -> unutarCijene(i, cijenaOd, cijenaDo))
                .collect(Collectors.toList());

        observableFinacnije = FXCollections.observableArrayList(filtrirana);

        TablicaFinancija.setItems(observableFinacnije);
    }

    @FXML
    private void deleteFinancije() {
        Financije selected = TablicaFinancija.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Izbriši financiju");
            alert.setContentText("Da li ste sigurni da želite obrisati ovu financiju?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                observableFinacnije.remove(selected);
                Database.obrisiFinanciju(selected);
                // Call your delete method in the Database class if needed
                // Database.deleteAdresa(selectedAdresa);
            }
        }
    }
    public static Financije adresaEditDialog(Financije financije) {
        Dialog<Financije> dialog = new Dialog<>();
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
        TextField proracunField = new TextField(financije.getProracun().toString());
        TextField troskoviField = new TextField(financije.getTroskovi().toString());
        TextField prihodiField = new TextField(financije.getPrihodi().toString());


        // Add labels and text input fields to the GridPane
        grid.add(new Label("Proracun:"), 0, 0);
        grid.add(proracunField, 1, 0);
        grid.add(new Label("Troskovi:"), 0, 1);
        grid.add(troskoviField, 1, 1);
        grid.add(new Label("Prihodi:"), 0, 2);
        grid.add(prihodiField, 1, 2);


        // Enable/Disable save button depending on whether a field is filled
        Node saveButton = dialog.getDialogPane().lookupButton(spremiButton);
        saveButton.setDisable(true);

        // Add listeners to enable/disable the save button based on input fields
        proracunField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        troskoviField.textProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue.trim().isEmpty()));
        prihodiField.textProperty().addListener((observable, oldValue, newValue) ->
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
                        BigDecimal proracun = BigDecimal.valueOf(Long.parseLong(proracunField.getText()));
                        BigDecimal troskovi = BigDecimal.valueOf(Long.parseLong(troskoviField.getText()));
                        BigDecimal prihodi = BigDecimal.valueOf(Long.parseLong(prihodiField.getText()));

                        Cijena proracunCijena = new Cijena(proracun);
                        Cijena troskoviCijena = new Cijena(troskovi);
                        Cijena prihodiCijena = new Cijena(prihodi);

                        return new Financije(financije.getIdFinancije(), proracunCijena, troskoviCijena, prihodiCijena);

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
        Optional<Financije> result = dialog.showAndWait();

        return result.orElse(financije);
    }


    @FXML
    private void editFinancije() {
        Financije selected = TablicaFinancija.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Open edit dialog
            Financije edited = adresaEditDialog(selected);

            Iterator<Financije> iterator = observableFinacnije.iterator();
            while (iterator.hasNext()) {
                Financije financije = iterator.next();
                if (financije.getIdFinancije().equals(edited.getIdFinancije())) {

                    Main.zapisPromjene(financije, edited);

                    iterator.remove(); // Remove the current element from the list
                    observableFinacnije.add(edited); // Add the edited element back to the list
                    try{
                        Database.updateFinancija(edited);
                    }catch (CijenaException e){
                        e.printStackTrace();
                    }


                    break; // Exit the loop since we found and edited the element
                }
            }


            // Perform additional actions if needed
            // Example: Database.editAdresa(editedAdresa);

            TablicaFinancija.setItems(observableFinacnije);
        }
    }

}
