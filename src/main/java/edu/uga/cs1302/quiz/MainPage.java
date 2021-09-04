package edu.uga.cs1302.quiz;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.StringLengthValidator;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainPage extends HBox {
    // Constant Properties
    private static final String MENU_TITLE = "GeoQuiz";
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    // MainPage
    private final VBox display = new VBox();
    private final VBox menu = new VBox();

    private final JFXTextField username = new JFXTextField();
    private final JFXButton start = new JFXButton("START");
    private final JFXButton quit = new JFXButton("QUIT");
    private final JFXButton report = new JFXButton("", new ImageView(ResourceManager.ICON_REPORT));
    private final JFXButton help = new JFXButton("", new ImageView(ResourceManager.ICON_HELP));

    public MainPage() {
        getStylesheets().add(ResourceManager.MAIN_PAGE_CSS);
        setId("main-page");
        setPrefSize(WIDTH, HEIGHT);

        initDisplay();
        initMenu();

        HBox.setMargin(display, new Insets(30, 10, 30, 30));
        HBox.setMargin(menu, new Insets(30));
        HBox.setHgrow(menu, Priority.ALWAYS);

        getChildren().addAll(display, menu);
    } // MainPage

    private void initDisplay() {
        display.setId("display-vbox");

        final ImageView worldMapImageView = new ImageView(ResourceManager.WORLD_MAP);
        worldMapImageView.setPreserveRatio(true);
        worldMapImageView.setFitWidth(WIDTH - 400);

        JFXDepthManager.setDepth(worldMapImageView, 1);

        display.setAlignment(Pos.CENTER);

        display.getChildren().addAll(worldMapImageView);
    } // initDisplay

    private void initMenu() {
        menu.setId("menu-vbox");

        final Text titleText = new Text(MENU_TITLE);
        titleText.setId("menu-title-text");

        createUsername();
        createStart();
        createQuit();
        createReport();
        createHelp();

        final HBox buttons = new HBox(quit, report, help);
        HBox.setHgrow(quit, Priority.ALWAYS);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(5);

        VBox.setMargin(username, new Insets(0, 0, 15, 0));

        menu.setPadding(new Insets(20));
        menu.setSpacing(20);

        JFXDepthManager.setDepth(menu, 1);

        final VBox descriptionContainer = new VBox();
        descriptionContainer.setId("description-box");

        final TextFlow descriptionFlow = new TextFlow();
        descriptionFlow.setId("description-textflow");

        final Text description = new Text("CREATED BY JP\n\n" +
                "This program is designed to test your geographical knowledge.\n\n" +
                "It will quiz you on how well you know where the countries are located.");
        description.setId("description-text");

        descriptionFlow.getChildren().addAll(description);

        VBox.setMargin(descriptionFlow, new Insets(20));
        VBox.setMargin(descriptionContainer, new Insets(60, 0, 0, 0));

        descriptionContainer.getChildren().add(descriptionFlow);

        JFXDepthManager.setDepth(descriptionContainer, 2);

        menu.getChildren().addAll(titleText, username, start, buttons, descriptionContainer);
    } // initMenu

    private void createUsername() {
        username.setId("username-textfield");
        username.setPromptText("username");

        RegexValidator rv = new RegexValidator("Only alphanumeric characters");
        rv.setRegexPattern("^[A-Za-z0-9]*");

        username.setValidators(
                new RequiredFieldValidator("username is required"),
                rv,
                new StringLengthValidator(16)
        );

        username.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                username.validate();
            } // if
        });

        username.setOnAction(event -> start.fire());
    } // createUsername

    private void createStart() {
        start.setId("start-button");
        start.setButtonType(JFXButton.ButtonType.RAISED);
        start.setMaxWidth(Double.MAX_VALUE);

        start.setOnAction(event -> {
            if (!username.validate()) {
                username.requestFocus();
            } else {
                String usernameText = username.getText();
                TransitionPage transitionPage = new TransitionPage(usernameText);
                openApplicationWindow(Util.setUpStage(new Stage(), transitionPage, "Quiz: " + usernameText));
            } // if-else
        });
    } // createStart

    private void createQuit() {
        quit.setId("quit-button");
        quit.setButtonType(JFXButton.ButtonType.RAISED);
        quit.setMaxWidth(Double.MAX_VALUE);
        quit.setMaxHeight(Double.MAX_VALUE);
        quit.setOnAction(event -> Platform.exit());
    } // createQuit

    private void createReport() {
        report.setId("report-button");
        report.setButtonType(JFXButton.ButtonType.RAISED);
        report.setMaxHeight(Double.MAX_VALUE);
        report.setOnAction(event -> openApplicationWindow(Util.setUpStage(new Stage(), new ReportPage(), "Report")));
        report.setTooltip(new Tooltip("Past result"));
    } // createReport

    private void createHelp() {
        help.setId("help-button");
        help.setButtonType(JFXButton.ButtonType.RAISED);
        help.setMaxHeight(Double.MAX_VALUE);
        help.setOnAction(event -> openApplicationWindow(Util.setUpStage(new Stage(), new HelpPage(), "Help")));
        help.setTooltip(new Tooltip("Help"));
    } // createHelp

    private void openApplicationWindow(Stage stage) {
        getScene().getWindow().setOpacity(0);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnHiding(event -> {
            System.out.println();
            getScene().getWindow().setOpacity(1);
        });
        stage.showAndWait();
    } // openApplicationWindow

} // MainPage
