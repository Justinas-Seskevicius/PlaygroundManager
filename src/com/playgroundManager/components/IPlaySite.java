package com.playgroundManager.components;

import com.playgroundManager.domainClasses.Child;

import java.time.LocalTime;
import java.util.Map;

public interface IPlaySite {
    boolean addChildToPlaySite(Child child);
    boolean removeChildFromPlaySite(Child child);
    double calculatePlaySiteUtilization();
    int getTotalVisitors();
    // TODO ask client if he wants only Time or also Date written in snapshots
    Map<LocalTime, Double> getPlaySiteUtilizationSnapshots();

}
