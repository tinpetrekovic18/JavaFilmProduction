package upravljanje.filmskom.produkcijom.projekt;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import upravljanje.filmskom.produkcijom.projekt.controllers.PocetniLoginController;
import upravljanje.filmskom.produkcijom.projekt.main.Main;
import upravljanje.filmskom.produkcijom.projekt.threads.InformacijeThread;

import java.io.IOException;

public class Aplikacija extends Application {

    public static Stage mainstage;

    public static final Logger logger = LoggerFactory.getLogger(Aplikacija.class);

    @Override
    public void start(Stage stage) throws IOException {
        mainstage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Aplikacija.class.getResource("pocetni-login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setScene(scene);
        stage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {Platform.runLater(new InformacijeThread());}));

        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();


    }

    public static Stage getMainstage(){
        return mainstage;
    }

    public static void main(String[] args) {
        launch();
    }
}