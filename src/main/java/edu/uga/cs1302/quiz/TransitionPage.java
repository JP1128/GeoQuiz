package edu.uga.cs1302.quiz;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.IntegerValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.List;

public class TransitionPage extends StackPane {
    // Constant Properties
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    // TransitionPage
    private final World world;
    private final String username;

    public TransitionPage(String username) {
        getStylesheets().add(ResourceManager.TRANSITION_PAGE_CSS);
        setId("transition-page");
        setPrefSize(WIDTH, HEIGHT);

        this.username = username;
        this.world = Util.getWorld();

        final ImageView loading = new ImageView(ResourceManager.LOGO_SCREEN);
        loading.setPreserveRatio(true);
        loading.setFitWidth(WIDTH);

        final Text choose = new Text("INPUT THE NUMBER OF COUNTRIES TO BE TESTED");
        choose.setId("choose-text");
        choose.setWrappingWidth(WIDTH - 200);
        choose.setTranslateY(100);

        final JFXTextField numberField = new JFXTextField("6");
        numberField.setId("number-textfield");
        numberField.setMaxWidth(175);
        numberField.setPrefHeight(25);
        numberField.setValidators(
                new RequiredFieldValidator("required"),
                new IntegerValidator("must be an integer"),
                new ValidatorBase("1 <= number <= " + world.size()) {
                    @Override
                    protected void eval() {
                        if (srcControl.get() instanceof TextInputControl) {
                            TextInputControl textField = (TextInputControl) srcControl.get();
                            String text = textField.getText();

                            try {
                                int integer = Integer.parseInt(text);
                                hasErrors.set(1 > integer || integer > world.size());
                            } catch (Exception e) {
                                hasErrors.set(true);
                            } // try-catch

                        } // if

                    } // eval
                });

        numberField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                numberField.validate();
            } // if
        });

        final JFXButton begin = new JFXButton("", new ImageView(ResourceManager.ICON_START));
        begin.setButtonType(JFXButton.ButtonType.RAISED);
        begin.setOnAction(event -> {
            if (!numberField.validate()) {
                numberField.requestFocus();
            } else {
                beginQuiz(Integer.parseInt(numberField.getText()));
            } // if-else
        });

        numberField.setOnAction(event -> begin.fire());

        final HBox inputBox = new HBox(numberField, begin);
        inputBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        inputBox.setSpacing(5);
        inputBox.setTranslateY(150);
        inputBox.autosize();

        setAlignment(Pos.CENTER);

        getChildren().addAll(loading, choose, inputBox);
    } // TransitionPage

    private void beginQuiz(int number) {
        Util.runNow(() -> {
            ImageView worldMap = new ImageView(ResourceManager.WORLD_MAP);
            worldMap.setPreserveRatio(true);
            worldMap.setFitWidth(WIDTH - 50);

            List<QuestionFactory.Question> questions = QuestionFactory.getQuestions(world, number);
            QuizPage quizPage = new QuizPage(username, questions);

            Platform.runLater(() -> {
                getChildren().clear();
                getChildren().addAll(worldMap, quizPage);
                setMargin(worldMap, new Insets(10));
            });
        });
    } // beginQuiz

} // TransitionPage
