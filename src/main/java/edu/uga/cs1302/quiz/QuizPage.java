package edu.uga.cs1302.quiz;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.controls.JFXTooltip;
import com.jfoenix.effects.JFXDepthManager;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class QuizPage extends StackPane {
    private final List<QuestionFactory.Question> questions;
    private final int numberOfQuestions; // total number of questions
    private final String username;

    private final QuizResult quizResult;

    private QuestionFactory.Question currentQuestion;
    private int numberOfQuestionsRight = 0; // current number of questions answered correctly
    private int questionNumber = 1; // current number of questions quizzed

    private final VBox quizContainer = new VBox();

    private final VBox questionContainer = new VBox();

    private final HBox progressContainer = new HBox();
    private final Label progressLabel = new Label();
    private final JFXProgressBar progressBar = new JFXProgressBar();

    private final Text prompt = new Text();

    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final JFXToggleNode answer1 = new JFXToggleNode();
    private final JFXToggleNode answer2 = new JFXToggleNode();
    private final JFXToggleNode answer3 = new JFXToggleNode();

    private final JFXButton next = new JFXButton("NEXT");
    private final JFXTooltip error = new JFXTooltip("Select a continent");

    public QuizPage(String username, List<QuestionFactory.Question> questions) {
        getStylesheets().add(ResourceManager.QUIZ_PAGE_CSS);
        setId("quiz-page");
        setPrefSize(500, 500);

        this.questions = questions;
        this.numberOfQuestions = questions.size();
        this.quizResult = new QuizResult(username);
        this.username = username;

        initQuestionContainer();
        initAnswers();
        initNext();

        quizContainer.setSpacing(20);
        quizContainer.setAlignment(Pos.CENTER);
        quizContainer.getChildren().addAll(questionContainer, answer1, answer2, answer3, next);

        next();

        getChildren().add(quizContainer);
    } // GameProgress

    private void next() {
        currentQuestion = questions.get(questionNumber - 1);

        if (toggleGroup.getSelectedToggle() != null) {
            toggleGroup.getSelectedToggle().setSelected(false);
        } // if

        // update progress
        progressLabel.setText("Q " + questionNumber + "/" + numberOfQuestions);
        progressLabel.setId("progress-label");

        progressBar.setProgress((double) numberOfQuestionsRight / numberOfQuestions);
        progressBar.setSecondaryProgress((double) questionNumber / numberOfQuestions);

        // update question
        prompt.setText("On which continent is " + currentQuestion.getCountry().getName() + " located?");
        answer1.setText(currentQuestion.getOptions().get(0));
        answer2.setText(currentQuestion.getOptions().get(1));
        answer3.setText(currentQuestion.getOptions().get(2));

        if (questionNumber == numberOfQuestions) {
            next.setId("finish-button");
            next.setText("FINISH");
            next.setOnAction(event -> {
                if (getSelectedText() != null) {
                    check();
                    finish();
                } else {
                    if (!error.isShowing()) {
                        error.show(next, next.getLayoutX(), next.getLayoutY());
                    } // if

                } // if-else

            });

        } // if

    } // next

    private void finish() {
        final VBox finishBox = new VBox();
        finishBox.setPrefSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        finishBox.setId("finish-box");
        finishBox.setAlignment(Pos.CENTER_LEFT);
        finishBox.setPadding(new Insets(30));
        finishBox.setSpacing(20);

        final Text usernameText = new Text(username.toUpperCase() + ",");
        usernameText.setId("finish-username-text");

        String scoreString = String.format("YOU SCORED %2.2f%%", ((double) numberOfQuestionsRight / numberOfQuestions) * 100);
        final Text scoreText = new Text(scoreString);
        scoreText.setId("finish-score-text");

        final Text confirmation = new Text("Quiz report is saved");
        confirmation.setId("finish-confirmation-text");

        final JFXButton close = new JFXButton("CLOSE");
        close.setId("finish-close-button");
        close.setOnAction(event -> ((Stage) getScene().getWindow()).close());

        finishBox.getChildren().addAll(usernameText, scoreText, confirmation, close);

        Util.writeResult(quizResult);

        getChildren().clear();
        getChildren().add(finishBox);
    } // finish

    private void check() {
        final String guessed = getSelectedText();
        final String answer = currentQuestion.getCountry().getContinent();

        final boolean correct = answer.equalsIgnoreCase(guessed);

        if (correct) {
            progressBar.setId("correct-bar");
            numberOfQuestionsRight++;
        } else {
            progressBar.setId("incorrect-bar");
        } // if-else

        Util.runNow(() -> displayOutcome(correct, answer, numberOfQuestionsRight));

        quizResult.add(currentQuestion.getCountry(), guessed);

        questionNumber++;
    } // check

    private void displayOutcome(boolean correct, String answer, int numberOfQuestionsRight) {
        final VBox outcomeBox = new VBox();
        outcomeBox.setAlignment(Pos.CENTER);
        outcomeBox.setSpacing(20);

        final Text outcomeMessage = new Text();
        outcomeMessage.setId("outcome-text");
        outcomeBox.getChildren().add(outcomeMessage);

        if (correct) {
            outcomeBox.setId("correct-box");
            outcomeMessage.setText("CORRECT!");
        } else {
            outcomeBox.setId("incorrect-box");
            outcomeMessage.setText("INCORRECT!");

            final Text incorrectMessage = new Text("Correct answer: " + answer);
            incorrectMessage.setId("incorrect-message");

            outcomeBox.getChildren().add(incorrectMessage);
        } // if-else

        final Text progressCheck = new Text("You answered " + numberOfQuestionsRight + " questions correctly!");
        progressCheck.setId("progress-check-text");

        outcomeBox.getChildren().add(progressCheck);
        outcomeBox.setPadding(new Insets(10));

        Platform.runLater(() -> {
            getChildren().add(outcomeBox);
            quizContainer.setDisable(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(pauseEvent -> {
                FadeTransition fade = new FadeTransition(Duration.seconds(1), outcomeBox);
                fade.setFromValue(1);
                fade.setToValue(0);
                fade.setOnFinished(fadeEvent -> {
                    getChildren().remove(outcomeBox);
                    quizContainer.setDisable(false);
                });
                fade.play();
            });

            outcomeBox.setOnMouseClicked(event -> {
                if (pause.getStatus().equals(Animation.Status.RUNNING)) {
                    pause.stop();
                    getChildren().remove(outcomeBox);
                    quizContainer.setDisable(false);
                } // if
            });

            pause.play();
        });

    } // displayOutcome

    private void initQuestionContainer() {
        questionContainer.setId("question-container-vbox");
        questionContainer.setMaxSize(300, 150);
        questionContainer.setPrefSize(300, 150);

        JFXDepthManager.setDepth(questionContainer, 2);

        initProgressContainer();

        prompt.setId("prompt-text");
        prompt.setWrappingWidth(300 - 50);

        VBox.setMargin(progressContainer, new Insets(10));

        questionContainer.getChildren().addAll(progressContainer, prompt);
    } // initQuestionContainer

    private void initProgressContainer() {
        progressBar.setMaxHeight(10);
        progressBar.setProgress(0);
        progressContainer.setAlignment(Pos.CENTER);
        progressContainer.setSpacing(10);
        progressContainer.setTranslateY(-10);
        progressContainer.getChildren().addAll(progressLabel, progressBar);
    } // initProgressContainer

    private void initAnswers() {
        createButton(answer1);
        createButton(answer2);
        createButton(answer3);
        toggleGroup.getToggles().addAll(answer1, answer2, answer3);
    } // initAnswers

    private void createButton(JFXToggleNode button) {
        button.setId("answer-button");
        button.setMaxSize(200, 50);
        JFXDepthManager.setDepth(button, 2);
    } // createButton

    private void initNext() {
        next.setId("next-button");
        next.setMaxWidth(100);
        next.setOnAction(event -> {
            if (getSelectedText() != null) {
                check();
                next();
            } else {
                if (!error.isShowing()) {
                    error.show(next, next.getLayoutX(), next.getLayoutY());
                } // if
            } // if-else
        });

        error.setAutoHide(true);
        error.setId("error-tooltip");

        JFXDepthManager.setDepth(next, 3);
    } // initNext

    private String getSelectedText() {
        if (toggleGroup.getSelectedToggle() != null) {
            JFXToggleNode selected = (JFXToggleNode) toggleGroup.getSelectedToggle();
            return selected.getText();
        } else {
            return null;
        } // if-else

    } // getSelectedText

} // GameProgress