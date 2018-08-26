package com.playgroundManager.implementation;

import com.playgroundManager.components.IPlaySite;
import com.playgroundManager.domainClasses.Child;
import com.playgroundManager.domainClasses.NormalTicket;
import com.playgroundManager.domainClasses.Ticket;
import com.playgroundManager.domainClasses.VIPTicket;
import javafx.util.Pair;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.playgroundManager.constants.WorkingTime.*;

public abstract class PlaySite implements IPlaySite, Runnable {
    private String name;
    private int capacity;
    private List<Child> childrenInPlaySite;
    private int totalVisitors;
    private long utilizationIntervalInSeconds;
    private Map<LocalTime, Double> utilizationSnapshots;
    private List<Child> queue;
    private boolean active;
    // How many Normal tickets should be after a VIP for the next one to skip line to
    private final int NORMAL_TICKETS_AFTER_VIP = 3;

    public PlaySite(){
        utilizationSnapshots = new LinkedHashMap<>();
        childrenInPlaySite = new ArrayList<>();
        queue = new ArrayList<>();
        active = true;
    }

    public PlaySite(String playSiteName, int maxChildren, long snapshotIntervalInSeconds) {
        this();
        name = playSiteName;
        capacity = maxChildren;
        utilizationIntervalInSeconds = snapshotIntervalInSeconds;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int maxChildren) {capacity = maxChildren;}

    public List<Child> getChildrenInPlaySite() {
        return childrenInPlaySite;
    }

    public List<Child> getQueue() {
        return queue;
    }

    public String getName() {
        return name;
    }

    public void setName(String playSiteName) {name = playSiteName;}

    public void setUtilizationIntervalInSeconds(int intervalInSeconds) { utilizationIntervalInSeconds = intervalInSeconds;}

    @Override
    public boolean addChildToPlaySite(Child child) {

        Ticket ticket = child.getTicket();

        if (!checkIfTicketIsValid(ticket)) {
            return false;
        }

        if (childrenInPlaySite.size() < capacity) {
            // TODO ask client if all (even Normal) tickets should get their uses reduced after entering PlaySite
            useTicket(ticket);
            child.setCurrentPlaySiteStart(new Pair<>(name, LocalTime.now()));
            childrenInPlaySite.add(child);
            totalVisitors++;
            return true;
        } else if (child.willWaitInLine()){
            addChildToQueue(child);
            return true;
        }

        return false;
    }

    private void addChildToQueue(Child child) {
        Ticket ticket = child.getTicket();

        if (ticket instanceof VIPTicket) {
            // Check the queue for currently waiting VIP
            int lastVIPposition = checkLastVIPposition();
            // If there are no VIPs in Queue, add this VIP to front of queue
            if (lastVIPposition == -1) {
                queue.add(0, child);
            } else {
                // Get the new position to insert this VIP to
                queue.add(whereToInsertNextVIP(lastVIPposition), child);
            }


        } else {
            queue.add(child);
        }
    }

    private int whereToInsertNextVIP(int lastVipPosition) {
        int normalTickets = 0;
        // If there aren't enough Normal tickets after the last VIP (need 3 tickets),
        // VIP should be inserted at the end of queue
        int positionToInsert = queue.size();

        // Start the loop at the next child in queue after the last VIP
        for (int i = lastVipPosition + 1; i < queue.size(); i++) {
            // Double check if the currently checked Ticket is a Normal one
            if (queue.get(i).getTicket() instanceof NormalTicket) {
                normalTickets++;
            }
            // If we have 3 normal tickets after the last VIP this is the position where the next VIP should go to
            if (normalTickets == NORMAL_TICKETS_AFTER_VIP) {
                positionToInsert = i + 1;
                break;
            }
        }

        return positionToInsert;
    }

    private int checkLastVIPposition(){
        // In case no VIP tickets are found method will return -1
        int vip = -1;
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getTicket() instanceof VIPTicket) {
                vip = i;
            }
        }
        return vip;
    }

    private boolean checkIfTicketIsValid(Ticket ticket) {
        return ticket != null && ticket.getUses() >= 1;
    }

    private void useTicket(Ticket ticket) {
        ticket.setUses(ticket.getUses() - 1);
    }

    @Override
    public boolean removeChildFromPlaySite(Child child) {
        if (childrenInPlaySite.contains(child)) {
            childrenInPlaySite.remove(child);
            addHistoryEntry(child);

            if (!queue.isEmpty()) {
                addChildToPlaySite(queue.get(0));
                queue.remove(0);
            }

            return true;

        } else if (queue.contains(child)){
            queue.remove(child);
            return true;
        }
        return false;
    }

    private void addHistoryEntry(Child child){
        Pair<String, Long> timeSpent = new Pair<>(name,
                Duration.between(child.getCurrentPlaySiteStart().getValue(),
                                            LocalTime.now()).toMillis()/1000);
        child.getPlaySiteHistory().add(timeSpent);
        child.setCurrentPlaySiteStart(null);
    }

    @Override
    public int getTotalVisitors() {
        return totalVisitors;
    }

    @Override
    public void run() {
        /* TODO ask the client if he intends to leave the program running for undermined time
            if yes then change control flow to:
            while(active) -> if(WorkDay) -> if(WorkingHours) -> snapshot
            if it will be running only through out workdays:
            while(WorkDay && active) -> if(WorkingHours) -> snapshot
         */
        LocalTime time = LocalTime.now();
        if (LocalDateTime.now().getDayOfWeek().getValue() <= LAST_WORKING_WEEKDAY) {
            while (time.isAfter(LocalTime.of(OPENING_HOUR, OPENING_MINUTE))
                    && time.isBefore(LocalTime.of(CLOSING_HOUR, CLOSING_MINUTE))
                    && active) {
                // Has enough time passed until the next Snapshot
                if (time.plusSeconds(utilizationIntervalInSeconds).compareTo(LocalTime.now()) <= 0) {
                    time = LocalTime.now();
                    utilizationSnapshots.put(time, calculatePlaySiteUtilization());
                }

            }
        }
    }

    @Override
    public Map<LocalTime, Double> getPlaySiteUtilizationSnapshots() {
        return utilizationSnapshots;
    }
}
