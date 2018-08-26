package com.playgroundManager.implementation;

public class BallPit extends PlaySite {

    public BallPit(){}

    public BallPit(String playSiteName, int maxChildren, int snapshotIntervalInMinutes){
        super(playSiteName, maxChildren, snapshotIntervalInMinutes);
    }

    @Override
    public double calculatePlaySiteUtilization() {
        return (double) getChildrenInPlaySite().size() / getCapacity() * 100;
    }
}
