package upravljanje.filmskom.produkcijom.projekt.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.Aplikacija;
import upravljanje.filmskom.produkcijom.projekt.database.Database;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Korisnik;
import upravljanje.filmskom.produkcijom.projekt.iznimke.KorisnikNePostojiException;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PocetniLoginController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInButton;

    public static String rolica="user";
    public static void showPocetna() {
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("pocetna.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Aplikacija.getMainstage().setScene(scene);
            Aplikacija.getMainstage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loginBaza(String id, Integer hashlozinka){

        List<Korisnik> listaKorisnika= Database.dohvatiKorisnike();

        Boolean postoji=false;
    try{
        for(Korisnik korisnik:listaKorisnika){
            String idKorisnik=korisnik.getIdKorisnika();
            Integer lozinkaKorisnik=korisnik.getLozinka().hashCode();
            Long rola=korisnik.getRola().getIdRole();
            if((idKorisnik.equals(id)) && (lozinkaKorisnik.equals(hashlozinka))){
                if(rola==1){
                    System.out.println("Uspješno ste se prijavili kao Administrator!");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Uspješna prijava");
                    alert.setHeaderText("Uspješno ste se prijavili kao Administrator!");
                    alert.setContentText("Dodijeljene su Vam sve ovlasti u aplikaciji."  );
                    alert.showAndWait();
                    rolica="admin";
                    showPocetna();
                }
                else {
                    System.out.println("Dobro došli " + korisnik.getIme() + " " + korisnik.getPrezime());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Uspješna prijava");
                    alert.setHeaderText("Uspješno ste se prijavili kao " + korisnik.getIme() + " " + korisnik.getPrezime());
                    alert.setContentText("Dobro došli."  );
                    alert.showAndWait();
                    rolica="user";
                    showPocetna();
                }
                postoji=true;
                break;
            }
        }
        if(!postoji) {
            throw new KorisnikNePostojiException();
        }

    } catch (KorisnikNePostojiException e) {
        System.out.println("Neispravan unos podataka!");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Neuspješna prijava");
        alert.setHeaderText("Neispravan unos podataka!");
        alert.setContentText("Molimo Vas da provjerite točnost svojih podataka i pokušate ponovo.");
        alert.showAndWait();
    }
    }



    public static String getRolica() {
        return rolica;
    }

    public void login(){
        String username=usernameField.getText();
        String password=passwordField.getText();
        int hash=password.hashCode();
        loginBaza(username, hash);

    }


}