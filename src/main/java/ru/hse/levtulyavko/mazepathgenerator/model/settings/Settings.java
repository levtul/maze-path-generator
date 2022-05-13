package ru.hse.levtulyavko.mazepathgenerator.model.settings;

public class Settings {
    public GenerationSettings generationSettings;
    public double speed;
    public boolean paused;
    public boolean isReplay;

    public Settings() {
        generationSettings = new GenerationSettings();
        speed = 1;
        paused = false;
        isReplay = false;
    }
}
