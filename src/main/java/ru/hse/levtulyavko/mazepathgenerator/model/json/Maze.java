package ru.hse.levtulyavko.mazepathgenerator.model.json;

import com.google.gson.annotations.SerializedName;

public final class Maze {
    @SerializedName("nodes_count")
    public final int nodesCount;
    @SerializedName("edges_count")
    public final int edgesCount;
    @SerializedName("start_node")
    public final char startNode;
    @SerializedName("node_labels")
    public final char[] nodeLabels;
    public final char[] entrances;
    public final char[] exits;
    public final char[] feeders;
    public final Edge[] edges;
    @SerializedName("matrix_positions")
    public final int[][] matrixPositions;

    public Maze(int nodesCount, int edgesCount,
                Edge[] edges,
                char[] nodeLabels,
                char startNode, char[] entrances, char[] exits, char[] feeders,
                int[][] matrixPositions) {
        this.nodesCount = nodesCount;
        this.edgesCount = edgesCount;
        this.startNode = startNode;
        this.nodeLabels = nodeLabels;
        this.entrances = entrances;
        this.exits = exits;
        this.feeders = feeders;
        this.edges = edges;
        this.matrixPositions = matrixPositions;
    }
}
