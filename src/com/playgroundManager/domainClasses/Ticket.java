package com.playgroundManager.domainClasses;

public abstract class Ticket {
    private long number;
    private int uses;

    public Ticket(long ticketNumber, int ticketUses) {
        number = ticketNumber;
        uses = ticketUses;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getUses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }
}
