package com.gamevault;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * JavaFX application class that integrates with the Spring Boot context.
 * Responsible for initializing Spring, loading the UI, and managing lifecycle events.
 */
public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext springContext;

    /**
     * Initializes the Spring application context before the UI starts.
     */
    @Override
    public void init() {
        springContext = SpringApplication.run(GameVaultSpringApp.class);
    }

    /**
     * Starts the JavaFX UI by loading the main view and setting up the stage.
     *
     * @param stage the primary application window
     * @throws Exception if the FXML cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/gamevault/view/MainView.fxml")
        );
        loader.setControllerFactory(springContext::getBean);
        Parent root = loader.load();

        stage.setTitle("GameVault");
        stage.setScene(new Scene(root));
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    /**
     * Cleans up resources and shuts down the Spring context when the application stops.
     */
    @Override
    public void stop() {
        springContext.close();
    }
}