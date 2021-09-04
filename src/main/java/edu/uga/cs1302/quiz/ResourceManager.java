package edu.uga.cs1302.quiz;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ResourceManager {
    // Images
    private static final String WORLD_MAP_PNG = "images/world_map.png";
    private static final String LOGO_SCREEN_PNG = "images/logo_screen.png";
    private static final String USERNAME_FIELD_PNG = "images/username_field.PNG";
    private static final String START_BUTTON_PNG = "images/start_button.PNG";
    private static final String TRANSITION_SCREEN_PNG = "images/transition_screen.PNG";
    private static final String QUIZ_SCREEN_PNG = "images/quiz_screen.PNG";
    private static final String REPORT_BUTTON_PNG = "images/report_button.PNG";
    private static final String REPORT_VIEW_PNG = "images/report_view.png";
    public static final Image WORLD_MAP = getImage(WORLD_MAP_PNG);
    public static final Image LOGO_SCREEN = getImage(LOGO_SCREEN_PNG);
    public static final Image USERNAME_FIELD = getImage(USERNAME_FIELD_PNG);
    public static final Image START_BUTTON = getImage(START_BUTTON_PNG);
    public static final Image TRANSITION_SCREEN = getImage(TRANSITION_SCREEN_PNG);
    public static final Image QUIZ_SCREEN = getImage(QUIZ_SCREEN_PNG);
    public static final Image REPORT_BUTTON = getImage(REPORT_BUTTON_PNG);
    public static final Image REPORT_VIEW = getImage(REPORT_VIEW_PNG);

    // Icons
    private static final String ICON_CLOSE_PNG = "icons/close.png";
    private static final String ICON_HELP_PNG = "icons/help.png";
    private static final String ICON_MINIMIZE_PNG = "icons/minimize.png";
    private static final String ICON_REPORT_PNG = "icons/report.png";
    private static final String ICON_LOGO_PNG = "icons/logo_icon.png";
    private static final String ICON_START_PNG = "icons/start.png";
    private static final String ICON_BACK_PNG = "icons/back.png";
    private static final String ICON_NEXT_PNG = "icons/next.png";
    public static final Image ICON_CLOSE = getImage(ICON_CLOSE_PNG);
    public static final Image ICON_HELP = getImage(ICON_HELP_PNG);
    public static final Image ICON_MINIMIZE = getImage(ICON_MINIMIZE_PNG);
    public static final Image ICON_REPORT = getImage(ICON_REPORT_PNG);
    public static final Image ICON_LOGO = getImage(ICON_LOGO_PNG);
    public static final Image ICON_START = getImage(ICON_START_PNG);
    public static final Image ICON_BACK = getImage(ICON_BACK_PNG);
    public static final Image ICON_NEXT = getImage(ICON_NEXT_PNG);

    // Internal Data
    public static final String COUNTRY_CONTINENT_CSV = "data/country_continent.csv";

    // External Data
    private static final String APPLICATION_DIRECTORY = System.getProperty("user.home") + File.separator + ".geographyQuiz";
    private static final String QUIZZES_DAT = "quizzes.dat";
    public static final File QUIZZES = getQuizData();

    // CSS
    public static final String TITLE_BAR_CSS = "css/title_bar.css";
    public static final String MAIN_PAGE_CSS = "css/main_page.css";
    public static final String REPORT_PAGE_CSS = "css/report_page.css";
    public static final String TRANSITION_PAGE_CSS = "css/transition_page.css";
    public static final String QUIZ_PAGE_CSS = "css/quiz_page.css";
    public static final String HELP_PAGE_CSS = "css/help_page.css";

    private static Image getImage(String filePath) {
        return new Image(getInputStream(filePath));
    } // getImage

    public static InputStream getInputStream(String filePath) {
        ClassLoader classLoader = ResourceManager.class.getClassLoader();
        return classLoader.getResourceAsStream(filePath);
    } // getInputStream

    private static File getQuizData() {
        File dir = new File(APPLICATION_DIRECTORY);
        File quizzes = new File(APPLICATION_DIRECTORY + File.separator + QUIZZES_DAT);

        if (!dir.exists()) {
            if (dir.mkdir()) {
                System.out.println("Successfully created " + dir.getAbsolutePath());
            } else {
                System.out.println("Failed to create " + dir.getAbsolutePath());
                System.exit(1);
            } // if-else

        } // if

        if (!quizzes.exists()) {
            try {
                if (quizzes.createNewFile()) {
                    System.out.println("Successfully created " + quizzes.getAbsolutePath());
                } else {
                    System.out.println("Failed to create " + quizzes.getAbsolutePath());
                    System.exit(1);
                } // if-else

            } catch (IOException io) {
                io.printStackTrace();
                System.exit(1);
            } // try-catch

        } // if

        return quizzes;

    } // getQuizData

} // ResourceManager
