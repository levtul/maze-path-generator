package ru.hse.levtulyavko.mazepathgenerator.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import ru.hse.levtulyavko.mazepathgenerator.App;

import java.io.IOException;

public enum SceneVariant {
    MAIN_MENU,
    UPLOADER,
    GENERATOR,
    EXIT;

    public String getViewName() {
        return switch (this) {
            case MAIN_MENU -> "main-menu";
            case UPLOADER -> "upload";
            case GENERATOR -> "generator";
            case EXIT -> "exit";
        } + "-view.fxml";
    }
}
