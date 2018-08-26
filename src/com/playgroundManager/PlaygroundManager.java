package com.playgroundManager;

import com.playgroundManager.implementation.DoubleSwings;
import com.playgroundManager.implementation.PlaySite;
import com.playgroundManager.domainClasses.Child;
import com.playgroundManager.domainClasses.PlaySiteTemplate;

import java.time.LocalTime;
import java.util.*;

public class PlaygroundManager {

    private Map<String, PlaySite> playSites;
    private int totalVisitorsFromRemovedPlaySites;

    public PlaygroundManager(){
        playSites = new HashMap<>();
    }

    public boolean addPlaySite(PlaySiteTemplate playSiteTemplate){
        if (checkIfPlaySiteExists(playSiteTemplate.getName())) {
            return false;
        }

        try {
            PlaySite ps = configurePlaySite(playSiteTemplate);
            playSites.put(ps.getName(), ps);
            new Thread(ps).start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private <T extends PlaySite> PlaySite configurePlaySite(PlaySiteTemplate<T> playSiteTemplate)
            throws IllegalAccessException, InstantiationException {

        PlaySite ps = playSiteTemplate.getPlaySiteType().newInstance();
        ps.setName(playSiteTemplate.getName());
        ps.setUtilizationIntervalInSeconds(playSiteTemplate.getSnapshotIntervalInSeconds());
        if (ps instanceof DoubleSwings) {
            ps.setCapacity(2);
        } else {
            ps.setCapacity(playSiteTemplate.getMaxChildren());
        }
        return ps;
    }

    public boolean removePlaySite(String playSiteName){
        if (checkIfPlaySiteExists(playSiteName)) {
            playSites.get(playSiteName).setActive(false);
            totalVisitorsFromRemovedPlaySites += playSites.get(playSiteName).getTotalVisitors();
            playSites.remove(playSiteName);
            return true;
        }
        return false;
    }

    public Map<String, PlaySite> getPlaySitesAsUnmodifiableMap(){
        return Collections.unmodifiableMap(playSites);
    }

    public boolean addChildToPlaySite(String playSiteName, Child child) {
        // Same Child can be added to multiple queues at the same time, should this be fixed ?
        if (checkIfPlaySiteExists(playSiteName)) {
            PlaySite ps = playSites.get(playSiteName);
            return ps.addChildToPlaySite(child);
        }
        return false;
    }

    public boolean removeChildFromPlaySite(String playSiteName, Child child) {
        if (checkIfPlaySiteExists(playSiteName)) {
            PlaySite ps = playSites.get(playSiteName);
            return ps.removeChildFromPlaySite(child);
        }
        return false;
    }

    public List<Child> getPlaySiteChildrenList(String playSiteName) {
        if (checkIfPlaySiteExists(playSiteName)) {
            return Collections.unmodifiableList(playSites.get(playSiteName).getChildrenInPlaySite());
        }
        return Collections.emptyList();
    }

    public List<Child> getPlaySiteQueue(String playSiteName) {
        if (checkIfPlaySiteExists(playSiteName)) {
            return Collections.unmodifiableList(playSites.get(playSiteName).getQueue());
        }
        return Collections.emptyList();
    }

    public int getPlayGroundTotalVisitors(){
        int total = 0;
        for (Map.Entry<String, PlaySite> ps : playSites.entrySet()) {
            total += ps.getValue().getTotalVisitors();
        }
        return total + totalVisitorsFromRemovedPlaySites;
    }

    public int getPlaySiteTotalVisitors(String playSiteName) {
        if (checkIfPlaySiteExists(playSiteName)) {
            return playSites.get(playSiteName).getTotalVisitors();
        }
        return -1;
    }

    public Map<LocalTime, Double> getPlaySiteUtilizationSnapshots(String playSiteName){
        if (checkIfPlaySiteExists(playSiteName)){
            return Collections.unmodifiableMap(playSites.get(playSiteName).getPlaySiteUtilizationSnapshots());
        }
        return Collections.emptyMap();
    }

    private boolean checkIfPlaySiteExists(String playSiteName) {
        return playSites.containsKey(playSiteName);
    }
}
