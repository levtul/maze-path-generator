package ru.hse.levtulyavko.mazepathgenerator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.hse.levtulyavko.mazepathgenerator.controller.SceneSwitcher;
import ru.hse.levtulyavko.mazepathgenerator.controller.SceneVariant;
import ru.hse.levtulyavko.mazepathgenerator.model.graph.Graph;
import ru.hse.levtulyavko.mazepathgenerator.model.json.Maze;
import ru.hse.levtulyavko.mazepathgenerator.model.settings.Settings;

import java.io.IOException;

public class App extends Application {
    public static SceneSwitcher switcher;
    public static Scene scene;
    public static Maze maze;
    public static Graph graph;
    public static final Settings settings = new Settings();
    public static String[] generatedPaths;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(
          SceneVariant.MAIN_MENU.getViewName()
        ));
        scene = new Scene(fxmlLoader.load());
        switcher = new SceneSwitcher(scene);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}