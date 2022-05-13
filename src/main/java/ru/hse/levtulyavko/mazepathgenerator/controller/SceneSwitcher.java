package ru.hse.levtulyavko.mazepathgenerator.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import ru.hse.levtulyavko.mazepathgenerator.App;

import java.io.IOException;

public record SceneSwitcher(Scene scene) {
    public SceneSwitcher {
        if (scene == null) {
            throw new IllegalArgumentException("Scene cannot be null");
        }
    }

    public void switchScene(SceneVariant variant) throws IOException {
        System.out.println(variant.getViewName());
        scene.setRoot(new FXMLLoader(App.class.getResource(variant.getViewName())).load());
    }
}
