package ru.hse.levtulyavko.mazepathgenerator.model.generator;

import ru.hse.levtulyavko.mazepathgenerator.model.graph.Graph;
import ru.hse.levtulyavko.mazepathgenerator.model.settings.GenerationSettings;

public interface PathGenerator {
    public String[] generatePaths(Graph graph, GenerationSettings settings);
}
