package upravljanje.filmskom.produkcijom.projekt.threads;

import upravljanje.filmskom.produkcijom.projekt.entiteti.Promjena;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SerijalizacijaThread<T> implements Runnable{

    private final String filePath = "C:\\Java-faks\\#Projekt\\Projekt\\dat\\promjeneClanEkipe.dat";
    private final List<Promjena<T>> listaPromjena;

    public SerijalizacijaThread(List<Promjena<T>> listaPromjena) {
        this.listaPromjena = listaPromjena;
    }

    @Override
    public void run() {
        synchronized (listaPromjena) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
                oos.writeObject(listaPromjena);
                System.out.println("Objects serialized and saved to " + filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
