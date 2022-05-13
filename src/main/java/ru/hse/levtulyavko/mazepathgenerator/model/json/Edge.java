package ru.hse.levtulyavko.mazepathgenerator.model.json;

public final class Edge {
    public final char from;
    public final char to;
    public final boolean bidirectional;

    public Edge(char from, char to, boolean bidirectional) {
        this.from = from;
        this.to = to;
        this.bidirectional = bidirectional;
    }
}
