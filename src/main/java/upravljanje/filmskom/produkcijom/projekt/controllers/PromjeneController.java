package upravljanje.filmskom.produkcijom.projekt.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.Aplikacija;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Promjena;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PromjeneController {

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);
    @FXML
    private TextArea textAreaPromjene;

    private static final String PROMJENE_DAT="C:\\Java-faks\\#Projekt\\Projekt\\dat\\promjenePodataka.dat";

    public void ispis(){
        List<Promjena<Object>> lista=Main.Deserijalizacija();
        for(Promjena promjena:lista){

            String datum=Main.getDateTimeFormatted(promjena.getVrijemePromjene().toString());

            this.textAreaPromjene.appendText( "Promjena: " + "\n");
            this.textAreaPromjene.appendText("Stara vrijednost: " + promjena.getStaraVrijednost().toString() + "\n");
            this.textAreaPromjene.appendText("Nova vrijednost: " + promjena.getNovaVrijednost().toString() + "\n");
            this.textAreaPromjene.appendText("Rola promjene: " + promjena.getRola() + "\n");
            this.textAreaPromjene.appendText("Datum promjene: " + datum + "\n");
            this.textAreaPromjene.appendText("\n");

        }
    }


}
