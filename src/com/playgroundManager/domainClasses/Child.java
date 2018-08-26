package com.playgroundManager.domainClasses;

import javafx.util.Pair;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Child {
    private String name;
    private int age;
    private Ticket ticket;
    // TODO ask how the history should be represented, maybe as a Collection of Classes
    // history class with PlaySite name, Start date, End date, Duration
    private List<Pair<String, Long>> playSiteHistory;
    private Pair<String, LocalTime> currentPlaySiteStart;
    private boolean willWaitInLine;

    public Child(String name, int age, Ticket ticket, boolean willWait) {
        this.name = name;
        this.age = age;
        this.ticket = ticket;
        willWaitInLine = willWait;
        playSiteHistory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public List<Pair<String, Long>> getPlaySiteHistory() {
        return playSiteHistory;
    }

    public boolean willWaitInLine() {
        return willWaitInLine;
    }

    public void setWillWaitInLine(boolean willWaitInLine) {
        this.willWaitInLine = willWaitInLine;
    }

    public Pair<String, LocalTime> getCurrentPlaySiteStart() {
        return currentPlaySiteStart;
    }

    public void setCurrentPlaySiteStart(Pair<String, LocalTime> currentPlaySiteStart) {
        this.currentPlaySiteStart = currentPlaySiteStart;
    }
}
