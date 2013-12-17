package com.softwaremill.smlpaas

import com.softwaremill.smlpaas.packets.ArchipelPacket
import org.jivesoftware.smack.Connection
import org.jivesoftware.smack.Roster
import org.jivesoftware.smack.XMPPConnection

class ArchipelClient extends Thread {

    private static shouldRun = true
    public static final String PAAS_GROUP = "smlpaas"

    @Override
    void run() {
        while (shouldRun) {
            try { Thread.sleep(100) } catch (Exception e) {}
        }
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: USERNAME PASSWORD COMMAND")
            System.exit(-1)
        }

        // Create a connection to the jabber.org server.
        Connection conn = new XMPPConnection("xmpp.pacmanvps.com");
        conn.connect();

        def username = args[0]
        def password = args[1]

        conn.login(username, password);

        switch(args[2]) {
            case "clone":
                cloneVM(conn, args.drop(3))
                break
            case "list":
                listVMs(conn)
                break
            default:
                println "Unknown command: ${args[2]}"
                break
        }
    }

    static void cloneVM(Connection conn, String[] args) {
        if (args.length != 1) {
            println "Usage: USERNAME PASSWORD clone NEW_VM_NAME"
        }

        def newVM = args[0]

        String smlpaasID

        conn.getRoster().entries.each { if (it.name == "smlpaas") smlpaasID = it.getUser() }

        conn.getRoster().setSubscriptionMode(Roster.SubscriptionMode.manual)

        conn.addPacketListener(new SubscriptionListener(conn, newVM), new SubscriptionPacketFilter())

        conn.getRoster().addRosterListener(new NewVMListener(conn, newVM, {shouldRun = false}))

        if (smlpaasID == null) {
            System.err.println("Cannot locate smlpaas")
            System.exit(-2)
        }

        println "Cloning ${smlpaasID} to ${newVM}"

        new ArchipelClient().start()

        def packet = new ArchipelPacket(smlpaasID, newVM)

        conn.sendPacket(packet)
    }

    static void listVMs(Connection conn) {
        println "Name: ID"
        println "========"

        conn.getRoster().getEntries().each {
            if (it.groups.find {it.name == PAAS_GROUP}) {
                println "${it.name}: ${it.user}"
            }
        }
    }
}

