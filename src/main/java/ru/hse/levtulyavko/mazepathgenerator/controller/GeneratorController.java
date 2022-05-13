package ru.hse.levtulyavko.mazepathgenerator.controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Pair;
import ru.hse.levtulyavko.mazepathgenerator.App;
import ru.hse.levtulyavko.mazepathgenerator.model.json.Edge;
import ru.hse.levtulyavko.mazepathgenerator.model.generator.SimpleGenerator;
import ru.hse.levtulyavko.mazepathgenerator.model.graph.Graph;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GeneratorController implements Initializable {
    public Slider symmetrySlider, compressionSlider, ringSlider, inversionSlider;
    public TextField symmetryValue, compressionValue, ringValue, inversionValue;
    public TextField iterationsValue;
    public Slider speedSlider;
    public Label speedValue;
    public ImageView playButton;
    public GridPane graphTable;
    public Pane pane;
    private Map<Pair<Character, Character>, Line> edges;
    private volatile boolean isReady;
    private static final Color[] colors = new Color[]{
            Color.valueOf("#cd001a"),
            Color.valueOf("#ef6a00"),
            Color.valueOf("#f2cd00"),
            Color.valueOf("#79c300"),
            Color.valueOf("#1961ae"),
            Color.valueOf("#61007d"),
    };
    private static int currentColor = -1;

    private void drawGraph() {
        Graph graph = App.graph;
        for (int i = 0; i < graph.matrixX; i++) {
            graphTable.getColumnConstraints().add(new ColumnConstraints(55));
        }
        for (int i = 0; i < graph.matrixY; i++) {
            graphTable.getRowConstraints().add(new RowConstraints(55));
        }
        for (int i = 0; i < graph.matrixPositions.length; i++) {
            int[] position = graph.matrixPositions[i];
            Color color = Color.BLACK;
            if (graph.isEntrance[i]) {
                color = Color.DARKBLUE;
            } else if (graph.isExit[i]) {
                color = Color.DARKGREEN;
            } else if (graph.nodeLabels[i] == graph.startNode) {
                color = Color.DARKRED;
            } else if (graph.isFeeder[i]) {
                color = Color.ORANGE;
            }
            Circle circle = new Circle(20, color);
            Label text = new Label(String.valueOf(graph.nodeLabels[i]));
            text.setFont(Font.font(20));
            text.setStyle("-fx-text-fill: white;");
            StackPane stack = new StackPane();
            stack.setAlignment(Pos.CENTER);
            stack.getChildren().addAll(circle, text);
            graphTable.add(stack, position[0],
                    position[1]);
        }
        drawEdges();
    }

    private void drawEdges() {
        Graph graph = App.graph;
        edges = new HashMap<>();
        for (Edge edge : graph.edges) {
            int[] start = graph.matrixPositions[graph.labels.get(edge.from)];
            int[] end = graph.matrixPositions[graph.labels.get(edge.to)];
            double startX = start[0] * 55 + 27.5;
            double startY = start[1] * 55 + 27.5;
            double endX = end[0] * 55 + 27.5;
            double endY = end[1] * 55 + 27.5;
            // draw line from center of first node to center of second node
            Line line = new Line(startX, startY, endX, endY);
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(2);
            pane.getChildren().add(line);
            edges.put(new Pair<>(edge.from, edge.to), line);
            edges.put(new Pair<>(edge.to, edge.from), line);
        }
    }

    private Color changeColor() {
        return colors[++currentColor % colors.length];
    }

    private void drawPath(char[] path) {
        Thread t = new Thread(() -> {
            final Color[] color = {Color.FIREBRICK};
            for (int i = 0; i < path.length - 1; i++) {
                int ii = i;
                if (App.settings.paused) {
                    try {
                        synchronized (App.settings){
                            App.settings.wait();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                Platform.runLater(() -> {
                    Pair<Character, Character> edge = new Pair<>(path[ii],
                            path[ii + 1]);
                    Line line = edges.get(edge);
                    Color currentColor = (Color) line.getStroke();
                    if (currentColor.equals(color[0])) {
                        color[0] = changeColor();
                    }
                    line.setStroke(color[0]);
                    line.setStrokeWidth(4);
                });
                try {

                    Thread.sleep((long) (200 / App.settings.speed));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            isReady = true;
        });
        t.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        symmetrySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            symmetryValue.setText(decimalFormat.format(newValue.doubleValue())
                    .replace(",", "."));
            App.settings.generationSettings.symmetryProbability = newValue.doubleValue();
        });
        compressionSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            compressionValue.setText(decimalFormat.format(newValue.doubleValue())
                    .replace(",", "."));
            App.settings.generationSettings.compressionProbability = newValue.doubleValue();
        });
        ringSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ringValue.setText(decimalFormat.format(newValue.doubleValue())
                    .replace(",", "."));
            App.settings.generationSettings.ringProbability = newValue.doubleValue();
        });
        inversionSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            inversionValue.setText(decimalFormat.format(newValue.doubleValue())
                    .replace(",", "."));
            App.settings.generationSettings.inversionProbability = newValue.doubleValue();
        });
        decimalFormat.applyPattern("#.#");

        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double d = (int) (newValue.doubleValue() * 2) / 2.0;
            speedValue.setText(decimalFormat.format(d)
                    .replace(",", ".") + "x");
            App.settings.speed = d;
        });
        setUpOnAction(symmetryValue, symmetrySlider);
        setUpOnAction(compressionValue, compressionSlider);
        setUpOnAction(ringValue, ringSlider);
        setUpOnAction(inversionValue, inversionSlider);
        iterationsValue.setOnAction(event -> {
            try {
                int value = Integer.parseInt(iterationsValue.getText());
                if (value < 1) {
                    value = 1;
                } else if (value > 15) {
                    value = 15;
                }
                iterationsValue.setText(String.valueOf(value));
                App.settings.generationSettings.iterationsCount = value;
                iterationsValue.setStyle("-fx-focus-color: green ;");
            } catch (NumberFormatException e) {
                iterationsValue.setText("1");
                iterationsValue.setStyle("-fx-focus-color: red ;");
            }
        });
        playButton.setOnMouseClicked(event -> {
            if (playButton.getImage().getUrl().endsWith("play.png")) {
                playButton.setImage(new Image(App.class.getResource(
                        "icons/pause.png").toExternalForm()));
                disableControls();
                if (!App.settings.paused) {
                    if (App.generatedPaths == null) {
                        App.generatedPaths = new SimpleGenerator().generatePaths(App.graph,
                                App.settings.generationSettings);
                    }
                    new Thread(() -> {
                        int count = 0;
                        for (var path : App.generatedPaths) {
                            if (count >= App.settings.generationSettings.iterationsCount) {
                                break;
                            }
                            for (var edge : edges.values()) {
                                edge.setStroke(Color.GRAY);
                                edge.setStrokeWidth(2);
                            }
                            isReady = false;
                            drawPath(path.toCharArray());
                            while (!isReady) {
                                Thread.onSpinWait();
                            }
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            count++;
                            currentColor = -1;
                        }
                        try {
                            App.switcher.switchScene(SceneVariant.EXIT);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                } else {
                    App.settings.paused = false;
                    synchronized (App.settings) {
                        App.settings.notify();
                    }
                }
            } else {
                playButton.setImage(new Image(App.class.getResource(
                        "icons/play.png").toExternalForm()));
                App.settings.paused = true;
            }
        });
        symmetrySlider.setValue(App.settings.generationSettings.symmetryProbability);
        compressionSlider.setValue(App.settings.generationSettings.compressionProbability);
        ringSlider.setValue(App.settings.generationSettings.ringProbability);
        inversionSlider.setValue(App.settings.generationSettings.inversionProbability);
        iterationsValue.setText(String.valueOf(App.settings.generationSettings.iterationsCount));
        speedSlider.setValue(App.settings.speed);
        enableControls();
        drawGraph();
        if (App.settings.isReplay) {
            App.settings.isReplay = false;
            playButton.getOnMouseClicked().handle(null);
        }
    }

    private void setUpOnAction(TextField filedValue, Slider slider) {
        filedValue.setOnAction(event -> {
            try {
                double value = Double.parseDouble(filedValue.getText());
                slider.setValue(value);
                if (slider.getMin() > value) {
                    slider.setValue(slider.getMax());
                    slider.setValue(slider.getMin());
                } else if (slider.getMax() < value) {
                    slider.setValue(slider.getMin());
                    slider.setValue(slider.getMax());
                }
                filedValue.setStyle("-fx-focus-color: green ;");
            } catch (NumberFormatException e) {
                slider.setValue(1);
                filedValue.setStyle("-fx-focus-color: red ;");
            }
        });
    }

    private void disableControls() {
        symmetrySlider.setDisable(true);
        compressionSlider.setDisable(true);
        ringSlider.setDisable(true);
        inversionSlider.setDisable(true);
        iterationsValue.setDisable(true);
        symmetryValue.setDisable(true);
        compressionValue.setDisable(true);
        ringValue.setDisable(true);
        inversionValue.setDisable(true);
    }

    private void enableControls() {
        symmetrySlider.setDisable(false);
        compressionSlider.setDisable(false);
        ringSlider.setDisable(false);
        inversionSlider.setDisable(false);
        iterationsValue.setDisable(false);
        symmetryValue.setDisable(false);
        compressionValue.setDisable(false);
        ringValue.setDisable(false);
        inversionValue.setDisable(false);
    }
}