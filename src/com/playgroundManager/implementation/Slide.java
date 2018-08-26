package com.playgroundManager.implementation;

public class Slide extends PlaySite {

    public Slide(){}

    public Slide(String playSiteName, int maxChildren, int snapshotIntervalInMinutes){
        super(playSiteName, maxChildren, snapshotIntervalInMinutes);
    }

    @Override
    public double calculatePlaySiteUtilization() {
        return (double) getChildrenInPlaySite().size() / getCapacity() * 100;
    }

}
