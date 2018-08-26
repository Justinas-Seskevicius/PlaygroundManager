package com.playgroundManager.implementation;

public class Carousel extends PlaySite{

    public Carousel(){}

    public Carousel(String playSiteName,int maxChildren, int snapshotIntervalInMinutes){
        super(playSiteName, maxChildren, snapshotIntervalInMinutes);
    }

    @Override
    public double calculatePlaySiteUtilization() {
        return (double) getChildrenInPlaySite().size() / getCapacity() * 100;
    }
}
