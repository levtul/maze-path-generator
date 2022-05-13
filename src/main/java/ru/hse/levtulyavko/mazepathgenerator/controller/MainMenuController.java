package ru.hse.levtulyavko.mazepathgenerator.controller;

import javafx.event.ActionEvent;
import ru.hse.levtulyavko.mazepathgenerator.App;

import java.io.IOException;

public class MainMenuController {

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void toUpload(ActionEvent actionEvent) throws IOException {
        App.switcher.switchScene(SceneVariant.UPLOADER);
    }
}
