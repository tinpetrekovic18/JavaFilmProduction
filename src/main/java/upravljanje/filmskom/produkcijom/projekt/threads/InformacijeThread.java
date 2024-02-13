package upravljanje.filmskom.produkcijom.projekt.threads;

import upravljanje.filmskom.produkcijom.projekt.Aplikacija;
import upravljanje.filmskom.produkcijom.projekt.database.Database;
import upravljanje.filmskom.produkcijom.projekt.entiteti.Projekt;

import java.sql.DatabaseMetaData;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InformacijeThread implements Runnable{
    public InformacijeThread() {}

    @Override
    public void run() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String now = formatter.format(date);

        Integer brojProjekata = Database.dohvatiProjekte().size();

        Aplikacija.getMainstage().setTitle("U vrijeme: " + now + " broj filmova u bazi je:  " + brojProjekata );
    }
}
