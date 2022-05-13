package ru.hse.levtulyavko.mazepathgenerator.controller;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import ru.hse.levtulyavko.mazepathgenerator.App;
import ru.hse.levtulyavko.mazepathgenerator.model.json.GenerationResults;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExitController {
    public void exportResults(ActionEvent actionEvent) {
        Gson gson = new Gson();
        String json =
                gson.toJson(new GenerationResults(App.maze,
                        App.generatedPaths));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Экспорт результатов");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json"));
        fileChooser.setInitialFileName("results.json");
        File file = fileChooser.showSaveDialog(App.scene.getWindow());
        if (file != null) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void replay(ActionEvent actionEvent) throws IOException {
        App.settings.isReplay = true;
        App.switcher.switchScene(SceneVariant.GENERATOR);
    }

    public void changeSettings(ActionEvent actionEvent) throws IOException {
        App.generatedPaths = null;
        App.switcher.switchScene(SceneVariant.GENERATOR);
    }

    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        App.switcher.switchScene(SceneVariant.MAIN_MENU);
    }
}
