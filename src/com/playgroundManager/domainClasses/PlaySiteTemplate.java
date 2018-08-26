package com.playgroundManager.domainClasses;

import com.playgroundManager.implementation.PlaySite;

public class PlaySiteTemplate <T extends PlaySite> {
    private String name;
    private int maxChildren;
    private int snapshotIntervalInSeconds;
    private Class<T> playSiteType;

    public PlaySiteTemplate(String name, int maxChildren, int snapshotIntervalInSeconds, Class<T> playSiteType) {
        this.name = name;
        this.maxChildren = maxChildren;
        this.snapshotIntervalInSeconds = snapshotIntervalInSeconds;
        this.playSiteType = playSiteType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxChildren() {
        return maxChildren;
    }

    public void setMaxChildren(int maxChildren) {
        this.maxChildren = maxChildren;
    }

    public int getSnapshotIntervalInSeconds() {
        return snapshotIntervalInSeconds;
    }

    public void setSnapshotIntervalInSeconds(int snapshotIntervalInSeconds) {
        this.snapshotIntervalInSeconds = snapshotIntervalInSeconds;
    }

    public Class<T> getPlaySiteType() {
        return playSiteType;
    }

    public void setPlaySiteType(Class<T> playSiteType) {
        this.playSiteType = playSiteType;
    }
}
