package com.softwaremill.smlpaas

import org.jivesoftware.smack.Connection
import org.jivesoftware.smack.Roster
import org.jivesoftware.smack.RosterListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.IQ
import org.jivesoftware.smack.packet.Presence

class ArchipelClient extends Thread {

    private static shouldRun = true

    @Override
    void run() {
        while (shouldRun) {
            try {Thread.sleep(100)} catch (Exception e) {}
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: USERNAME PASSWORD NEWNAME")
            System.exit(-1)
        }

        // Create a connection to the jabber.org server.
        Connection conn = new XMPPConnection("xmpp.pacmanvps.com");
        conn.connect();

        def username = args[0]
        def password = args[1]
        def newVM = args[2]

        conn.login(username, password);

        String smlpaasID

        conn.getRoster().entries.each {if (it.name == "smlpaas") smlpaasID = it.getUser()}

        conn.getRoster().setSubscriptionMode(Roster.SubscriptionMode.accept_all)

        conn.getRoster().addRosterListener(new RosterListener() {
            @Override
            void entriesAdded(Collection<String> addresses) {
                // we'll just assume that this is our new server, not some random crap in which case we're just screwed

                shouldRun = false
            }

            @Override
            void entriesUpdated(Collection<String> addresses) {
            }

            @Override
            void entriesDeleted(Collection<String> addresses) {
            }

            @Override
            void presenceChanged(Presence presence) {
            }
        })

        if (smlpaasID == null) {
            System.err.println("Cannot locate smlpaas")
            System.exit(-2)
        }

        println "Cloning ${smlpaasID} to ${newVM}"

        new ArchipelClient().start()

        def packet = new ArchipelPacket(smlpaasID, newVM)

        conn.sendPacket(packet)
    }
}

class ArchipelPacket extends IQ {

    private final String source
    private final String newName

    ArchipelPacket(String source, String newName) {
        this.newName = newName
        this.source = source
        setType(IQ.Type.SET)
        setTo("sml.cumulushost.eu@xmpp.pacmanvps.com/sml.cumulushost.eu")
        setPacketID("17755")
    }

    @Override
    String getChildElementXML() {
        return "<query xmlns=\"archipel:hypervisor:control\">" +
                "<archipel action=\"clone\" jid=\"${source}\" name=\"${newName}\"/>" +
                "</query>"
    }
}
