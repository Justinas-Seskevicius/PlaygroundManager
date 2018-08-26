package com.playgroundManager;

import com.playgroundManager.implementation.DoubleSwings;
import com.playgroundManager.implementation.Slide;
import com.playgroundManager.domainClasses.Child;
import com.playgroundManager.domainClasses.NormalTicket;
import com.playgroundManager.domainClasses.PlaySiteTemplate;
import com.playgroundManager.domainClasses.VIPTicket;

public class Tutorial {
    public static void main(String[] args) throws InterruptedException {
        // TODO please notice that there are some TODO notes inside project classes
        // this project can still be improved with more information
        boolean success;
        // First create an instance of PlaygroundManager
        PlaygroundManager pm = new PlaygroundManager();

        // Then create a PlaySite Template - PlaySite configuration
        // String for its Name (should be Unique), int for Children Capacity, long for SnapshotInterval in seconds
        // and any class that Extends PlaySite as a Class
        String psName = "Triple Slide";
        PlaySiteTemplate template = new PlaySiteTemplate<>(psName, 3, 1, Slide.class);

        // Add a new PlaySite to PlaygroundManager by passing PlaySiteTemplate as an argument
        // PM will add it to its list of PlaySites and start a Thread to run IntervalSnapshots
        success = pm.addPlaySite(template);
        System.out.println(template.getName() + " was added successfully - " + success);

        // Create Child objects by giving them String Name, int Age, Ticket (NormalTicket or VIPTicket)
        // with int TicketNumber and int TicketUses and boolean True/False if Child would wait in Queue
        Child tom = new Child("Tom", 5, new NormalTicket(123, 5), true);
        Child bob = new Child("Bob", 7, new NormalTicket(586, 3), true);
        Child mark = new Child("Mark", 6, new NormalTicket(456, 6), true);
        Child jerry = new Child("Jerry", 8, new NormalTicket(455, 4), false);
        Child linda = new Child("Linda", 7, new NormalTicket(223, 5), true);
        Child betsy = new Child("Betsy", 7, new NormalTicket(655, 8), true);
        Child eric = new Child("Eric", 7, new NormalTicket(655, 8), true);
        Child donna = new Child("Donna", 7, new NormalTicket(655, 8), true);
        Child timmy = new Child("Timmy", 7, new VIPTicket(655, 8), true);
        Child lara = new Child("Lara", 7, new VIPTicket(655, 8), true);

        // Add children via PlaygroundManager by giving the Name of the PlaySite to which to add the child
        // and the Child object which you want to add
        success = pm.addChildToPlaySite(psName, tom);
        System.out.println(tom.getName() + " was added successfully - " + success);
        pm.addChildToPlaySite(psName, bob);
        pm.addChildToPlaySite(psName, mark);
        // If we try to add more children than PlaySite capacity, child will be entered into its Queue
        // unless Child is not willing to wait in line, example below
        success = pm.addChildToPlaySite(psName, jerry);
        System.out.println("Was " + jerry.getName() + " added to Queue - " + success);
        pm.addChildToPlaySite(psName, linda);
        pm.addChildToPlaySite(psName, betsy);
        pm.addChildToPlaySite(psName, eric);
        pm.addChildToPlaySite(psName, donna);

        // PM can give you list of the current Queue or Children currently playing in specific PlaySite
        pm.getPlaySiteQueue(psName).forEach(child ->
                System.out.println(child.getName() + " || " + child.getTicket().getClass()));

        // VIP feature, a child with a Valid (has more than 0 uses) VIP ticket can skip line
        pm.addChildToPlaySite(psName, timmy);
        System.out.println("============== VIP FEATURE ================");
        pm.getPlaySiteQueue(psName).forEach(child ->
                System.out.println(child.getName() + " || " + child.getTicket().getClass()));
        // If we add a second VIP he will placed after minimum of 3 Normal Ticket children (can be modified inside PlaySite)
        pm.addChildToPlaySite(psName, lara);
        System.out.println("============== VIP FEATURE with 2 VIP ================");
        pm.getPlaySiteQueue(psName).forEach(child ->
                System.out.println(child.getName() + " || " + child.getTicket().getClass()));

        // Removing a child from PlaySite will either remove him from PlaySite and
        // move up another from Queue to PlaySite or move up in Queue
        System.out.println("Children playing in \"Triple Slide\": ");
        pm.getPlaySiteChildrenList(psName).forEach(child -> System.out.println(child.getName()));
        pm.removeChildFromPlaySite(psName, tom);
        pm.removeChildFromPlaySite(psName, bob);
        System.out.println("============ AFTER REMOVAL ===============");
        System.out.println("Children playing in \"" + psName + "\": ");
        pm.getPlaySiteChildrenList(psName).forEach(child -> System.out.println(child.getName()));
        System.out.println("============ IN QUEUE ===================");
        pm.getPlaySiteQueue(psName).forEach(child ->
                System.out.println(child.getName() + " || " + child.getTicket().getClass()));

        // Uncomment below to see Utilization snapshot
//        Thread.sleep(2000);

        // PM can provide PlaySite utilization Snapshots
        // Utilization snapshots will only be taken if they are within given setting in WorkingTime class
        // Default settings are -> Only on workdays (Mon - Fry)
        // Only during work hours -> 8:00 - 17:00
        // To change the settings go to WorkingTime class
        System.out.println(pm.getPlaySiteUtilizationSnapshots(psName));

        // Utilization is calculated differently for each PlaySite implementation
        // for Slide it is kidsOnSite / maxCapacity * 100
        pm.removeChildFromPlaySite(psName, mark);
        pm.removeChildFromPlaySite(psName, timmy);
        pm.removeChildFromPlaySite(psName, linda);
        pm.removeChildFromPlaySite(psName, betsy);
        pm.removeChildFromPlaySite(psName, eric);
        pm.removeChildFromPlaySite(psName, lara);
        pm.removeChildFromPlaySite(psName, lara);

        // Uncomment below line to see change in Utilization Snapshots
//        Thread.sleep(2000);

        System.out.println("================ UTILIZATION SNAPSHOT AFTER REMOVAL =================");
        System.out.println(pm.getPlaySiteUtilizationSnapshots(psName));

        // PM can provide total visitors in PlaySite
        System.out.println("================ TOTAL VISITORS IN " + psName + " -> " + pm.getPlaySiteTotalVisitors(psName));

        // PM can remove a PlaySite and by that stop Utilization Snapshots (Thread)
        pm.removePlaySite(psName);


        // PM registers child's history - PlaySite name and Duration in seconds in it (not in Queue)
        // after the child is removed from PlaySite
        String psNameTwo = "Double Swings";
        template = new PlaySiteTemplate<>(psNameTwo, 2, 1, DoubleSwings.class);
        pm.addPlaySite(template);

        pm.addChildToPlaySite(psNameTwo, mark);
        // Uncomment below line to see change in Utilization Snapshots
//        Thread.sleep(2000);
        System.out.println("================ UTILIZATION SNAPSHOT OF DOUBLE SWINGS WITH 1 CHILD =================");
        System.out.println(pm.getPlaySiteUtilizationSnapshots(psNameTwo));

        pm.removeChildFromPlaySite(psNameTwo, mark);

        System.out.println("================= PLAYSITE HISTORY =================");
        System.out.println(mark.getName() + " playtime history: " + mark.getPlaySiteHistory());

        pm.removePlaySite(psNameTwo);

        // PM can provide Total visitors for all it's PlaySites
        System.out.println("================ TOTAL VISITORS FOR THIS PLAYGROUND -> " + pm.getPlayGroundTotalVisitors());
    }
}
