package ru.acelost.coupler.core;

public class Anchor {

    final String sourceId;

    boolean hasBefore;

    boolean hasAfter;

    public Anchor(String sourceId, boolean hasBefore, boolean hasAfter) {
        this.sourceId = sourceId;
        this.hasBefore = hasBefore;
        this.hasAfter = hasAfter;
    }

    public String getSourceId() {
        return sourceId;
    }

    public boolean getHasBefore() {
        return hasBefore;
    }

    public boolean getHasAfter() {
        return hasAfter;
    }

}
