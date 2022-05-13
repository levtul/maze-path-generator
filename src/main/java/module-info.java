module ru.hse.levtulyavko.mazepathgenerator {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    exports ru.hse.levtulyavko.mazepathgenerator;
    exports ru.hse.levtulyavko.mazepathgenerator.controller;
    exports ru.hse.levtulyavko.mazepathgenerator.model.settings;
    exports ru.hse.levtulyavko.mazepathgenerator.model.json;
    exports ru.hse.levtulyavko.mazepathgenerator.model.graph;
    opens ru.hse.levtulyavko.mazepathgenerator.icons;
    opens ru.hse.levtulyavko.mazepathgenerator.controller to javafx.fxml;
    opens ru.hse.levtulyavko.mazepathgenerator;
}