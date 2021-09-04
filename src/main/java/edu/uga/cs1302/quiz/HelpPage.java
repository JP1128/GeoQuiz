package edu.uga.cs1302.quiz;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.effects.JFXDepthManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpPage extends VBox {
    // Constant Properties
    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;

    private final IntegerProperty page = new SimpleIntegerProperty(0);

    private final VBox mainContainer = new VBox();

    private final HBox placeholder = new HBox();
    private final List<HBox> helpBoxes = new ArrayList<>();

    private final HBox buttonContainer = new HBox();
    private final JFXButton back = new JFXButton("", new ImageView(ResourceManager.ICON_BACK));
    private final JFXButton next = new JFXButton("", new ImageView(ResourceManager.ICON_NEXT));

    public HelpPage() {
        getStylesheets().add(ResourceManager.HELP_PAGE_CSS);
        setId("help-page");
        setPrefSize(WIDTH, HEIGHT);

        initMainContainer();
        initButtons();

        mainContainer.getChildren().addAll(placeholder, buttonContainer);

        VBox.setVgrow(placeholder, Priority.ALWAYS);
        VBox.setMargin(placeholder, new Insets(30, 30, 10, 30));
        VBox.setMargin(buttonContainer, new Insets(10));

        VBox.setVgrow(mainContainer, Priority.ALWAYS);
        VBox.setMargin(mainContainer, new Insets(30));

        JFXDepthManager.setDepth(mainContainer, 1);

        getChildren().addAll(mainContainer);
    } // HelpPage

    private void prev() {
        page.setValue(page.subtract(1).getValue());
        placeholder.getChildren().clear();
        placeholder.getChildren().add(helpBoxes.get(page.get()));
    } // prev

    private void next() {
        page.setValue(page.add(1).getValue());
        placeholder.getChildren().clear();
        placeholder.getChildren().add(helpBoxes.get(page.get()));
    } // next

    private void initMainContainer() {
        mainContainer.setId("help-main-container");

        initHelpBoxes();

        placeholder.setId("placeholder");

        if (helpBoxes.isEmpty()) {
            placeholder.getChildren().add(new Text("no help available"));
        } // if
        placeholder.getChildren().add(helpBoxes.get(0));
    } // initMainContainer

    private void initHelpBoxes() {
        final HBox page1 = createHelpPage(
                "To start the GeoQuiz, you must input your " +
                        "username into the username field (shown below) on the main screen.\n",
                ResourceManager.USERNAME_FIELD,
                "The username must consist only of alphanumeric " +
                        "characters and must be shorter than 17 characters.\n\n" +
                        "Once you input your username, you can press the start button.",
                ResourceManager.START_BUTTON
        );

        final HBox page2 = createHelpPage(
                "When you have pressed the start button, a new window will " +
                        "open that will ask you to input the number of countries to be tested. Whatever " +
                        "number you input means the number of countries you would like to be tested.",
                ResourceManager.TRANSITION_SCREEN,
                "The number of countries cannot be 0 or greater than the " +
                        "number of countries stored by this program."
        );

        final HBox page3 = createHelpPage(
                "The quiz will display a prompt that asks in which continent " +
                        "a random country is located in. You will be shown 3 options to choose from. " +
                        "Your job is to select the one you think is right. Once you have selected an " +
                        "option you can press submit to see if your selection was correct or incorrect.",
                ResourceManager.QUIZ_SCREEN
        );

        final HBox page4 = createHelpPage(
                "Once you have completed all the questions, your overall score will be displayed. If you " +
                        "want to check the details of your quiz, then click the button shown below once " +
                        "you are back at the main screen.",
                ResourceManager.REPORT_BUTTON,
                "To see the outcome of each questions of one quiz session, then click the row " +
                        "of the quiz you want to see.",
                ResourceManager.REPORT_VIEW
        );

        helpBoxes.addAll(Arrays.asList(page1, page2, page3, page4));
    } // initHelpBoxes

    private void initButtons() {
        buttonContainer.setId("button-container");

        back.setOnAction(event -> prev());
        back.disableProperty().bind(page.lessThanOrEqualTo(0));

        final HBox prevContainer = new HBox(back);
        prevContainer.setAlignment(Pos.CENTER_LEFT);

        final JFXButton close = new JFXButton("CLOSE");
        close.setId("close-button");
        close.setOnAction(event -> ((Stage) getScene().getWindow()).close());

        JFXDepthManager.setDepth(close, 2);

        final HBox closeContainer = new HBox(close);
        closeContainer.setAlignment(Pos.CENTER);

        next.setOnAction(event -> next());
        next.disableProperty().bind(page.greaterThanOrEqualTo(helpBoxes.size() - 1));

        final HBox nextContainer = new HBox(next);
        nextContainer.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(prevContainer, Priority.ALWAYS);
        HBox.setHgrow(nextContainer, Priority.ALWAYS);

        buttonContainer.getChildren().addAll(prevContainer, closeContainer, nextContainer);
    } // initButtons

    private static HBox createHelpPage(Object... objects) {
        final VBox pageContent = new VBox();

        for (Object object : objects) {
            if (object instanceof String) {
                String content = (String) object;
                final Text text = new Text(content);
                text.setId("placeholder-text");
                pageContent.getChildren().add(new TextFlow(text));
            } else if (object instanceof Image) {
                Image image = (Image) object;
                final HBox imageContainer = new HBox(new ImageView(image));
                imageContainer.setId("placeholder-image-box");
                imageContainer.setAlignment(Pos.CENTER);
                VBox.setMargin(imageContainer, new Insets(10));
                pageContent.getChildren().add(imageContainer);
            } else {
                throw new IllegalArgumentException("Parameter value must be an instance of String or Image");
            } // if-else

        } // for

        return new HBox(pageContent);
    } // createHelpPage

} // HelpPage
