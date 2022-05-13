package ru.hse.levtulyavko.mazepathgenerator.controller;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import ru.hse.levtulyavko.mazepathgenerator.App;
import ru.hse.levtulyavko.mazepathgenerator.model.graph.Graph;
import ru.hse.levtulyavko.mazepathgenerator.model.json.Maze;

import java.io.*;
import java.util.stream.Collectors;

public class UploadController {
    @FXML
    public Label errorLabel;

    private String loadFile(File file) {
        String lines = "";
        if (file == null) {
            throw new IllegalArgumentException("файл не выбран");
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                lines = reader.lines().collect(Collectors.joining("\n"));
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("файл не найден");
            }
        }
        return lines;
    }

    private Maze parseMaze(String lines) {
        Maze maze = null;
        Gson gson = new com.google.gson.Gson();
        try {
            maze = gson.fromJson(lines, Maze.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("некорректный формат файла");
        }
        if (maze == null) {
            throw new IllegalArgumentException("файл не содержит данных");
        }
        return maze;
    }

    private Graph loadMaze(File file) {
        String lines = loadFile(file);
        Maze maze = parseMaze(lines);
        Graph graph = new Graph(maze);
        App.maze = maze;
        return graph;
    }

    public void uploadFile(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters()
                .addAll(new ExtensionFilter("JSON file", "*.json"));
        File file = fileChooser.showOpenDialog(App.scene.getWindow());

        try {
            App.graph = loadMaze(file);
        } catch (IllegalArgumentException e) {
            errorLabel.setText("Ошибка: " + e.getMessage());
            return;
        }
        App.switcher.switchScene(SceneVariant.GENERATOR);
    }
}
