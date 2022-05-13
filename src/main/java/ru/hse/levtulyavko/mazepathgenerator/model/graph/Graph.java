package ru.hse.levtulyavko.mazepathgenerator.model.graph;

import javafx.util.Pair;
import ru.hse.levtulyavko.mazepathgenerator.model.json.Edge;
import ru.hse.levtulyavko.mazepathgenerator.model.json.Maze;

import java.util.*;

public class Graph {
    public final int nodesCount;
    public final int edgesCount;
    public final char startNode;
    public final char[] nodeLabels;
    public final char[] entrances;
    public final char[] exits;
    public final char[] feeders;
    public final Edge[] edges;
    public final int[][] matrixPositions;

    public HashMap<Pair<Integer, Integer>, Integer> edgesMap;
    public ArrayList<ArrayList<Integer>> adjacencyList;
    public Map<Character, Integer> labels;
    public boolean[] isFeeder, isEntrance, isExit;
    public int matrixX, matrixY;

    public Graph(Maze maze) {
        this.nodesCount = maze.nodesCount;
        this.edgesCount = maze.edgesCount;
        this.startNode = maze.startNode;
        this.nodeLabels = maze.nodeLabels;
        this.entrances = maze.entrances;
        this.exits = maze.exits;
        this.feeders = maze.feeders;
        this.edges = maze.edges;
        this.matrixPositions = maze.matrixPositions;
        initialCheck();
        initMaps();
        checkNodes();
        checkMatrix();
    }

    private void checkMatrix() {
        int maxX = matrixPositions[0][0];
        int maxY = matrixPositions[0][1];
        for (int i = 0; i < nodesCount; i++) {
            int x = matrixPositions[i][0];
            int y = matrixPositions[i][1];
            if (x < 0 || y < 0) {
                throw new IllegalArgumentException("matrix_position[i][0] < 0" +
                        " или matrix_position[i][1] < 0");
            }
            if (x > maxX) {
                maxX = x;
            }
            if (y > maxY) {
                maxY = y;
            }
        }
        matrixX = maxX;
        matrixY = maxY;
    }

    private void checkNodes() {
        isFeeder = new boolean[nodesCount];
        isEntrance = new boolean[nodesCount];
        isExit = new boolean[nodesCount];
        if (!labels.containsKey(startNode)) {
            throw new IllegalArgumentException("Некорректный символ начальной" +
                    " вершины");
        }
        for (char entrance : entrances) {
            int node = labels.getOrDefault(entrance, -1);
            if (node == -1) {
                throw new IllegalArgumentException("Входная вершина " + entrance + " не найдена");
            }
            if (isEntrance[node]) {
                throw new IllegalArgumentException("Вершина " + entrance + " " +
                        "повторяется в entrances");
            }
            isEntrance[node] = true;
        }
        for (char exit : exits) {
            int node = labels.getOrDefault(exit, -1);
            if (node == -1) {
                throw new IllegalArgumentException("Выходная вершина " + exit + " не найдена");
            }
            if (isExit[node]) {
                throw new IllegalArgumentException("Вершина " + exit + " " +
                        "повторяется в exits");
            }
            isExit[node] = true;
        }
        for (char feeder : feeders) {
            int node = labels.getOrDefault(feeder, -1);
            if (node == -1) {
                throw new IllegalArgumentException("Вершина-кормушка " + feeder +
                        " не найдена");
            }
            if (isFeeder[node]) {
                throw new IllegalArgumentException("Вершина " + feeder + " " +
                        "повторяется в feeders");
            }
            isFeeder[node] = true;
        }
    }

    private void initMaps() {
        labels = new HashMap<>();
        for (int i = 0; i < nodeLabels.length; i++) {
            if (labels.containsKey(nodeLabels[i])) {
                throw new IllegalArgumentException("node_labels содержит повторяющиеся символы");
            }
            labels.put(nodeLabels[i], i);
        }
        edgesMap = new HashMap<>();
        adjacencyList = new ArrayList<>(nodesCount);
        for (int i = 0; i < nodesCount; i++) {
            adjacencyList.add(new ArrayList<>());
        }
        for (int i = 0; i < edges.length; i++) {
            Edge edge = edges[i];
            if (edge.from == edge.to) {
                throw new IllegalArgumentException("Вершина " + edge.from +
                        " имеет ребро сама в себя");
            }
            int from = labels.getOrDefault(edge.from, -1);
            int to = labels.getOrDefault(edge.to, -1);
            if (from == -1 || to == -1) {
                throw new IllegalArgumentException("Ребро " + edge.from +
                        " - " + edge.to + " имеет некоректный символ вершины");
            }
            if (edgesMap.containsKey(new Pair<>(from, to))) {
                throw new IllegalArgumentException("Ребро " + edge.from +
                        " - " + edge.to + " повторяется");
            }
            edgesMap.put(new Pair<>(from, to), i);
            adjacencyList.get(from).add(to);
            if (edge.bidirectional) {
                adjacencyList.get(to).add(from);
            }
        }
    }

    private void initialCheck() {
        if (nodesCount == 0) {
            throw new IllegalArgumentException("nodes_count == 0");
        }
        if (edgesCount == 0) {
            throw new IllegalArgumentException("edges_count == 0");
        }
        if (nodeLabels == null || nodeLabels.length != nodesCount) {
            throw new IllegalArgumentException("node_labels.length != nodes_count");
        }
        if (edges == null || edges.length != edgesCount) {
            throw new IllegalArgumentException("edges.length != edges_count");
        }
        if (entrances == null || entrances.length < 1) {
            throw new IllegalArgumentException("entrances.length < 1");
        }
        if (exits == null || exits.length < 1) {
            throw new IllegalArgumentException("exits.length < 1");
        }
        if (feeders == null || feeders.length < 1) {
            throw new IllegalArgumentException("feeders.length < 1");
        }
        if (matrixPositions == null || matrixPositions.length != nodesCount) {
            throw new IllegalArgumentException("matrix_position.length != nodes_count");
        }
        for (int[] pos : matrixPositions) {
            if (pos == null || pos.length != 2) {
                throw new IllegalArgumentException("matrix_position[i].length != 2");
            }
        }
    }
}
