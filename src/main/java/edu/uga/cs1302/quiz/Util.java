package edu.uga.cs1302.quiz;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.commons.csv.CSVFormat;

import java.io.*;

public class Util {
    private static final Image STAGE_ICON = ResourceManager.ICON_LOGO;

    public static Stage setUpStage(Stage stage, Parent parent, String title) {
        TitleBar titleBar = new TitleBar();

        Scene scene = new Scene(new VBox(titleBar, parent));
        scene.setFill(Color.TRANSPARENT);

        stage.setOnShown(event -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1), parent);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        });

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle(title);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(STAGE_ICON);
        stage.setResizable(false);
        return stage;
    } // setUpStage

    public static World getWorld() {
        World world = new World();

        try (Reader reader = new InputStreamReader(ResourceManager.getInputStream(ResourceManager.COUNTRY_CONTINENT_CSV))) {
            CSVFormat.DEFAULT.parse(reader).forEach(record -> world.add(new World.Country(record.get(0), record.get(1))));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } // try-catch

        return world;
    } // getWorld

    public static QuizResultCollection readResults() {
        File quizzesFile = ResourceManager.QUIZZES;

        try (FileInputStream fis = new FileInputStream(quizzesFile)) {
            if (fis.available() > 0) {
                try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                    return (QuizResultCollection) ois.readObject();
                } catch (ClassNotFoundException cnfe) {
                    cnfe.printStackTrace();
                } // try-catch

            } // if

        } catch (IOException io) {
            io.printStackTrace();
        } // try-catch

        return new QuizResultCollection();
    } // readResults

    public static void writeResult(QuizResult quizResult) {
        QuizResultCollection quizResultCollection = readResults();
        quizResultCollection.add(quizResult);

        File quizzesFile = ResourceManager.QUIZZES;

        try (FileOutputStream fos = new FileOutputStream(quizzesFile);
             ObjectOutputStream ois = new ObjectOutputStream(fos)) {
            ois.writeObject(quizResultCollection);
        } catch (IOException e) {
            e.printStackTrace();
        } // try-catch

    } // writeResult

    public static void runNow(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    } // runNow

} // ApplicationUtil
