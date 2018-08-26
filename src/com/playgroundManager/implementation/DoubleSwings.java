package com.playgroundManager.implementation;

public class DoubleSwings extends PlaySite {

    public DoubleSwings(){}

    public DoubleSwings(String playSiteName, int snapshotIntervalInMinutes){
        super(playSiteName,2, snapshotIntervalInMinutes);
    }

    @Override
    public double calculatePlaySiteUtilization() {
        if (getChildrenInPlaySite().size() == 2) {
            return 100;
        } else {
            return 0;
        }
    }
}
