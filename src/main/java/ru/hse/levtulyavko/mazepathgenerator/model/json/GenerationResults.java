package ru.hse.levtulyavko.mazepathgenerator.model.json;

public class GenerationResults {
    public Maze maze;
    public String[] paths;

    public GenerationResults(Maze maze, String[] paths) {
        this.maze = maze;
        this.paths = paths;
    }
}
