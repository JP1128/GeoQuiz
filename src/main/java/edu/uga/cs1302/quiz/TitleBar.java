package edu.uga.cs1302.quiz;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class TitleBar extends HBox {
    // Constant Properties
    private static final int HEIGHT = 25;
    private static final int BUTTON_WIDTH = 25;

    // Resources
    private static final Image LOGO = ResourceManager.ICON_LOGO;
    private static final Image MINIMIZE = ResourceManager.ICON_MINIMIZE;
    private static final Image CLOSE = ResourceManager.ICON_CLOSE;

    // TitleBar
    private final HBox logoContainer = new HBox();
    private final HBox controlsContainer = new HBox();

    // For moving stage
    private double xOffset = 0;
    private double yOffset = 0;

    public TitleBar() {
        getStylesheets().add(ResourceManager.TITLE_BAR_CSS);

        initControlsContainer();
        initLogoContainer();

        HBox.setHgrow(logoContainer, Priority.ALWAYS);
        HBox.setHgrow(this, Priority.ALWAYS);

        setMaxHeight(HEIGHT);

        getChildren().addAll(logoContainer, controlsContainer);

        // make window movable
        setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        setOnMouseDragged(event -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

    } // TitleBar

    private void initControlsContainer() {
        controlsContainer.setId("controls-container");

        final ImageView closeImageView = new ImageView(CLOSE);
        closeImageView.setFitWidth(BUTTON_WIDTH);
        closeImageView.setFitHeight(HEIGHT);

        final ImageView minimizeImageView = new ImageView(MINIMIZE);
        minimizeImageView.setFitWidth(BUTTON_WIDTH);
        minimizeImageView.setFitHeight(HEIGHT);

        final JFXButton minimize = new JFXButton("", minimizeImageView);
        minimize.setId("minimize-button");
        minimize.setOnAction(event -> ((Stage) getScene().getWindow()).setIconified(true));
        minimize.setDisableVisualFocus(true);

        final JFXButton close = new JFXButton("", closeImageView);
        close.setId("close-button");
        close.setOnAction(event -> ((Stage) getScene().getWindow()).close());
        close.setDisableVisualFocus(true);

        controlsContainer.getChildren().addAll(minimize, close);
    } // initWindowControls

    private void initLogoContainer() {
        logoContainer.setId("logo-container");

        final ImageView logo = new ImageView(LOGO);
        logo.setPreserveRatio(true);
        logo.setFitHeight(HEIGHT - 4);

        logoContainer.getChildren().add(logo);
        logoContainer.setAlignment(Pos.CENTER_LEFT);
        logoContainer.setMaxWidth(Double.MAX_VALUE);

        HBox.setMargin(logo, new Insets(5));
    } // initTitleBar

} // TitleBar