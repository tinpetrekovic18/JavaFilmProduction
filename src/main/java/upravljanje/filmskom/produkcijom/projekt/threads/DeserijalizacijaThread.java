package upravljanje.filmskom.produkcijom.projekt.threads;

import upravljanje.filmskom.produkcijom.projekt.entiteti.Promjena;
import upravljanje.filmskom.produkcijom.projekt.main.Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeserijalizacijaThread<T> implements Runnable{

    public final static String filePath="C:\\Java-faks\\#Projekt\\Projekt\\dat\\promjeneClanEkipe.dat";

    private List<Promjena<T>> listaPromjena;

    public DeserijalizacijaThread(List<Promjena<T>> listaPromjena) {
        this.listaPromjena = listaPromjena;
    }

    @Override
    public void run() {
        synchronized (listaPromjena) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                // Read the list from the file
                listaPromjena = (List<Promjena<T>>) ois.readObject();
            } catch (FileNotFoundException e) {
                // The file doesn't exist yet, initialize an empty list
                System.out.println("File not found. Initializing an empty list.");
                listaPromjena = new ArrayList<>();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Promjena<T>> getListaPromjena() {
        return listaPromjena;
    }
}
