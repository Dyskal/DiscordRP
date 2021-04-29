package dyskal;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import javax.swing.*;
import javax.swing.Box.Filler;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;
import static javax.swing.Box.createHorizontalBox;
import static javax.swing.Box.createVerticalBox;
import static javax.swing.SwingUtilities.invokeLater;

class DiscordRP extends JFrame {

    DiscordRP() {
        super("Discord RP");
        setPreferredSize(new Dimension(600, 300));
        //TODO fix image
//        ArrayList<Image> icons = new ArrayList<>();
//        for (String size : new String[]{"","16x16", "20x20", "24x24", "30x30", "31x31", "32x32", "40x40", "48x48", "60x60", "64x64", "96x96", "120x120", "256x256"}) {
//            icons.add(new ImageIcon(requireNonNull(getClass().getClassLoader().getResource("assets/icon"+size+".png"))).getImage());
//        }
//        setIconImages(icons);
        setIconImage(new ImageIcon(requireNonNull(getClass().getClassLoader().getResource("assets/icon.png"))).getImage());

        DiscordRPC lib = DiscordRPC.INSTANCE;
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        lib.Discord_Initialize(new AppIdChooser().getAppId(), handlers, true, null);

        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = currentTimeMillis() / 1000;
        presence.details = "";
        presence.state = "";
        presence.largeImageKey = "";
        presence.largeImageText = "";
        presence.smallImageKey = "";
        presence.smallImageText = "";

        lib.Discord_UpdatePresence(presence);

        Thread t = new Thread(() -> {
            while (!currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    lib.Discord_Shutdown();
                    break;
                }
            }
        }, "RPC-Callback-Handler");
        t.start();

        Dimension dimension = new Dimension(200, 100);

        JMenuBar menuBar = new JMenuBar();
        JButton disconnect = new JButton("Disconnect");
        disconnect.addActionListener(event -> {
            t.interrupt();
            dispose();
            invokeLater(AppIdChooser::new);
        });

        menuBar.add(disconnect);

        Box bDetails = createHorizontalBox();
        JLabel detailsLabel = new JLabel("Details");
        JTextField detailsText = new JTextField(presence.details);
        detailsText.setMaximumSize(dimension);

        bDetails.add(detailsLabel);
        bDetails.add(detailsText);

        Box bState = createHorizontalBox();
        JLabel stateLabel = new JLabel("State");
        JTextField stateText = new JTextField(presence.state);
        stateText.setMaximumSize(dimension);

        bState.add(stateLabel);
        bState.add(stateText);

        Box bLargeImage = createHorizontalBox();
        JLabel largeImageKeyLabel = new JLabel("Large Image Key");
        JComboBox<String> largeImageKey = new JComboBox<>(new String[]{presence.largeImageKey});
        largeImageKey.setEditable(true);
        largeImageKey.setMaximumSize(dimension);

        JLabel largeImageTextLabel = new JLabel("Large Image Text");
        JTextField largeImageText = new JTextField(presence.largeImageText);
        largeImageText.setMaximumSize(dimension);

        bLargeImage.add(largeImageKeyLabel);
        bLargeImage.add(largeImageKey);
        bLargeImage.add(largeImageTextLabel);
        bLargeImage.add(largeImageText);

        Box bSmallImage = createHorizontalBox();
        JLabel smallImageKeyLabel = new JLabel("Small Image Key");
        JComboBox<String> smallImageKey = new JComboBox<>(new String[]{presence.smallImageKey});
        smallImageKey.setEditable(true);
        smallImageKey.setMaximumSize(dimension);

        JLabel smallImageTextLabel = new JLabel("Small Image Text");
        JTextField smallImageText = new JTextField(presence.smallImageText);
        smallImageText.setMaximumSize(dimension);

        bSmallImage.add(smallImageKeyLabel);
        bSmallImage.add(smallImageKey);
        bSmallImage.add(smallImageTextLabel);
        bSmallImage.add(smallImageText);

        Box bButtons = createHorizontalBox();
        JButton updatePresence = new JButton("Update Presence");
        updatePresence.addActionListener(event -> {
            presence.details = detailsText.getText();
            presence.state = stateText.getText();
            presence.largeImageKey = ((String) requireNonNull(largeImageKey.getSelectedItem())).toLowerCase();
            presence.largeImageText = largeImageText.getText();
            presence.smallImageKey = ((String) requireNonNull(smallImageKey.getSelectedItem())).toLowerCase();
            presence.smallImageText = smallImageText.getText();
            lib.Discord_UpdatePresence(presence);
        });

        bButtons.add(updatePresence);

        Box body = createVerticalBox();
        Dimension minSize = new Dimension(10, 20);
        Dimension prefSize = new Dimension(20, 20);
        Dimension maxSize = new Dimension(50, 70);
        Dimension dim10 = new Dimension(0, 10);

        body.add(new Filler(minSize, dim10, maxSize));
        body.add(bDetails);
        body.add(new Filler(minSize, prefSize, maxSize));
        body.add(bState);
        body.add(new Filler(minSize, prefSize, maxSize));
        body.add(bLargeImage);
        body.add(new Filler(minSize, prefSize, maxSize));
        body.add(bSmallImage);
        body.add(new Filler(minSize, prefSize, maxSize));
        body.add(bButtons);
        body.add(new Filler(minSize, dim10, maxSize));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                t.interrupt();
            }
        });

        setJMenuBar(menuBar);
        add(body);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
