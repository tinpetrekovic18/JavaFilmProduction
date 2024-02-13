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
import upravljanje.filmskom.produkcijom.projekt.entiteti.Distribucija;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DistribucijaController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private DatePicker dateOD;

    @FXML
    private DatePicker dateDO;

    @FXML
    private TableView<Distribucija> TablicaDistribucija;
    @FXML
    private TableColumn<Distribucija, String> idID;
    @FXML
    private TableColumn<Distribucija, String> pocetakID;
    @FXML
    private TableColumn<Distribucija, String> krajID;
    @FXML
    private TableColumn<Distribucija, String> izlazID;

    @FXML
    private Button filter;
    @FXML
    private Button obrisi;
    @FXML
    private Button promjeni;

    ObservableList<Distribucija> observableDistribucije= FXCollections.observableArrayList(Database.dohvatiDistribucije());

    private boolean datumUnutar(Temporal date, LocalDate startDate, LocalDate endDate) {
        if (date instanceof LocalDate && endDate != null) {
            LocalDate localDate = (LocalDate) date;
            return !localDate.isBefore(startDate) && !localDate.isAfter(endDate);
        }
        return false; // Handle other Temporal types as needed
    }


    public void initialize() {

        if (PocetniLoginController.getRolica().equals("admin")){
        }
        else{
            obrisi.setVisible(false);
            promjeni.setVisible(false);
        }

        idID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getIdDistribucije().toString()));
        pocetakID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getPocetakProjekta().toString()));
        krajID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getKrajProjekta().toString()));
        izlazID.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getIzlazakFilma().toString()));



        TablicaDistribucija.getSelectionModel().selectedItemProperty().addListener((observable, starOdabir, noviOdabir) -> {
            obrisi.setDisable(noviOdabir == null);
            promjeni.setDisable(noviOdabir == null);
        });


        TablicaDistribucija.setItems(observableDistribucije);
    }


    public void distSearch(){
        List<Distribucija> listaDistribucija= Database.dohvatiDistribucije();
        List<Distribucija> filtrirana=listaDistribucija;

        LocalDate dateod=dateOD.getValue();
        LocalDate datedo=dateDO.getValue();


        filtrirana = filtrirana.stream()
                .filter(i -> datumUnutar(i.getPocetakProjekta(), dateod, datedo))
                .filter(i -> datumUnutar(i.getKrajProjekta(), dateod, datedo))
                .filter(i -> datumUnutar(i.getIzlazakFilma(), dateod, datedo))
                .collect(Collectors.toList());


        observableDistribucije= FXCollections.observableArrayList(filtrirana);

        TablicaDistribucija.setItems(observableDistribucije);
        if(dateod==null || datedo==null){
            TablicaDistribucija.setItems(observableDistribucije= FXCollections.observableArrayList(listaDistribucija));
        }
    };

    @FXML
    private void deleteDist() {
        Distribucija selected = TablicaDistribucija.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Izbriši distribuciju");
            alert.setContentText("Da li ste sigurni da želite obrisati ovu distribuciju?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                observableDistribucije.remove(selected);
                Database.obrisiDistribuciju(selected);
                // Call your delete method in the Database class if needed
                // Database.deleteAdresa(selectedAdresa);
            }
        }
    }
    public static Distribucija distEditDialog(Distribucija distribucija) {
        Dialog<Distribucija> dialog = new Dialog<>();
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
        DatePicker pocetakField = new DatePicker((LocalDate) distribucija.getPocetakProjekta());
        DatePicker krajField = new DatePicker((LocalDate) distribucija.getKrajProjekta());
        DatePicker izlazField = new DatePicker((LocalDate) distribucija.getIzlazakFilma());

        // Add labels and text input fields to the GridPane
        grid.add(new Label("Pocetak projekta:"), 0, 0);
        grid.add(pocetakField, 1, 0);
        grid.add(new Label("Kraj projekta:"), 0, 1);
        grid.add(krajField, 1, 1);
        grid.add(new Label("Izlazak filma:"), 0, 2);
        grid.add(izlazField, 1, 2);

        // Enable/Disable save button depending on whether a field is filled
        Node saveButton = dialog.getDialogPane().lookupButton(spremiButton);
        saveButton.setDisable(true);

        // Add listeners to enable/disable the save button based on input fields
        pocetakField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || krajField.getValue() == null ||
                        izlazField.getValue() == null));
        krajField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || izlazField.getValue() == null ||
                        pocetakField.getValue() == null ));
        izlazField.valueProperty().addListener((observable, oldValue, newValue) ->
                saveButton.setDisable(newValue == null || krajField.getValue() == null ||
                        pocetakField.getValue() == null ));

        // Set the GridPane as the content of the dialog
        dialog.getDialogPane().setContent(grid);

        // Convert the result to an Adresa object when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == spremiButton) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Da li ste sigurni da želite promjeniti distribuciju?");
                alert.setContentText("Potvrdite svoj odabir");

                ButtonType promjena = new ButtonType("Promjeni");
                ButtonType odustani = new ButtonType("Odustani");

                alert.getButtonTypes().setAll(promjena, odustani);

                Optional<ButtonType> resultButtonType = alert.showAndWait();

                if (resultButtonType.isPresent() && resultButtonType.get() == promjena) {
                    try {
                        LocalDate pocetak = pocetakField.getValue();
                        LocalDate kraj = krajField.getValue();
                        LocalDate izlaz = izlazField.getValue();

                        return new Distribucija(distribucija.getIdDistribucije(), pocetak, kraj, izlaz);
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
        Optional<Distribucija> result = dialog.showAndWait();

        return result.orElse(distribucija);
    }


    @FXML
    private void editDist() {
        Distribucija selected = TablicaDistribucija.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Open edit dialog
            Distribucija edited = distEditDialog(selected);

            Iterator<Distribucija> iterator = observableDistribucije.iterator();
            while (iterator.hasNext()) {
                Distribucija dist = iterator.next();
                if (dist.getIdDistribucije()==(edited.getIdDistribucije())) {

                    Main.zapisPromjene(dist, edited);

                    iterator.remove(); // Remove the current element from the list
                    observableDistribucije.add(edited); // Add the edited element back to the list
                    Database.updateDistribucija(edited);
                    break; // Exit the loop since we found and edited the element
                }
            }


            // Perform additional actions if needed
            // Example: Database.editAdresa(editedAdresa);

            TablicaDistribucija.setItems(observableDistribucije);
        }
    }

}
