package ru.hse.levtulyavko.mazepathgenerator.model.settings;

public class GenerationSettings {
    public double symmetryProbability;
    public double compressionProbability;
    public double ringProbability;
    public double inversionProbability;
    public int iterationsCount;

    public GenerationSettings() {
        symmetryProbability = 1;
        compressionProbability = 1;
        ringProbability = 1;
        inversionProbability = 1;
        iterationsCount = 5;
    }
}
