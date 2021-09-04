package edu.uga.cs1302.quiz;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/*
 * mainClass
 */
public class GeographyQuiz extends Application {
    // Constant Properties
    private static final String WINDOW_TITLE = "GeoQuiz";

    @Override
    public void start(Stage stage) {
        MainPage mainPage = new MainPage();
        Util.setUpStage(stage, mainPage, WINDOW_TITLE);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.show();
    } // start

    /* Application Entry Point */
    public static void main(String[] args) {
        launch(args);
    } // main

} // GeographyQuiz