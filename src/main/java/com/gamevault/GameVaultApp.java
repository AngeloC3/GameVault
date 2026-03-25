package com.gamevault;

import javafx.application.Application;

/**
 * Main entry point for launching the JavaFX application.
 * Delegates startup to the JavaFxApplication class.
 */
public class GameVaultApp {

    /**
     * Launches the JavaFX application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }
}