package upravljanje.filmskom.produkcijom.projekt.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.Aplikacija;

import java.io.IOException;
import java.util.Optional;

public class MenuController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private Menu Korisnici;

    @FXML
    private Menu Promjene;

    @FXML
    private MenuItem DodavanjeAdresa;

    @FXML
    private MenuItem DodavanjeLokacija;

    @FXML
    private MenuItem DodajGlumce;

    @FXML
    private MenuItem DodajProducente;

    @FXML
    private MenuItem DodajRadnike;

    @FXML
    private MenuItem DodajProjekt;

    @FXML
    private MenuItem DodajScenu;

    @FXML
    private MenuItem DodajDist;

    @FXML
    private MenuItem DodajFin;
    @FXML
    private MenuItem DodajProdKuc;


    public void initialize(){
        if (PocetniLoginController.getRolica().equals("admin")){
        }
        else{
            Korisnici.setVisible(false);
            Promjene.setVisible(false);
            DodavanjeAdresa.setVisible(false);
            DodavanjeLokacija.setVisible(false);
            DodajGlumce.setVisible(false);
            DodajProducente.setVisible(false);
            DodajRadnike.setVisible(false);
            DodajProjekt.setVisible(false);
            DodajScenu.setVisible(false);
            DodajDist.setVisible(false);
            DodajFin.setVisible(false);
            DodajProdKuc.setVisible(false);
        }
    }


    public void showLogin(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("pocetni-login.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPromjene(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("promjene.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDodajAdresu(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("dodajAdresu.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAdrese(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("Adrese.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showLokacije(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("Lokacije.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDodajLokaciju(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("dodajLokaciju.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showGlumci(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("Glumci.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showDodajGlumca(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("dodajGlumca.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showProducenti(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("Producenti.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showDodajProducenta(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("dodajProducenta.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showClan(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("Clan.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showDodajClan(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("dodajClana.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showKuca(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("Produkcijska.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showDodajKuca(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("dodajProdukcijska.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showKorisnik(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("Korisnik.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showDodajKorisnik(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("dodajKorisnik.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRole(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("Rola.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showDodajRolu(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("dodajRolu.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showProjekt(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("Projekt.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showDodajProjekt(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("dodajProjekt.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showScena(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("Scena.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDodajScenu(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("dodajScenu.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDistribucija(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("Distribucija.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDodajDistribuciju(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("dodajDistribuciju.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFinancije(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("Financije.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDodajFinancije(){
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("dodajFinancije.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void odjava(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Odjava");
        alert.setHeaderText("Jeste li sigurni da se želite odjaviti?");
        alert.setContentText("Potvrdite svoj odabir");

        ButtonType povratak = new ButtonType("Povratak");
        ButtonType odjava = new ButtonType("Odjavi se");

        alert.getButtonTypes().setAll(povratak, odjava);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == povratak) {
                System.out.println("Povratak");
            } else if (buttonType == odjava) {
                System.out.println("Uspješno ste se odjavili");
                showLogin();
            }
        });
    }


}
