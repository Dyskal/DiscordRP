package dyskal;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class DiscordRP {

    public DiscordRP() {
        JFrame frame = new JFrame();
        frame.setTitle("Discord RP");
        frame.setPreferredSize(new Dimension(600, 300));
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/icon.png"))).getImage());

        DiscordRPC lib = DiscordRPC.INSTANCE;
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        lib.Discord_Initialize(AppIdChooser.getAppId(), handlers, true, null);

        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.details = "";
        presence.state = "";
        presence.largeImageKey = "";
        presence.largeImageText = "";
        presence.smallImageKey = "";
        presence.smallImageText = "";

        lib.Discord_UpdatePresence(presence);

        Thread t = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
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
        disconnect.addActionListener(e -> {
            t.interrupt();
            frame.dispose();
            SwingUtilities.invokeLater(AppIdChooser::new);
        });
        menuBar.add(disconnect);

        Box bDetails = Box.createHorizontalBox();
        JLabel detailsLabel = new JLabel("Details");
        JTextField detailsText = new JTextField(presence.details);
        detailsText.setMaximumSize(dimension);
        bDetails.add(detailsLabel);
        bDetails.add(detailsText);

        Box bState = Box.createHorizontalBox();
        JLabel stateLabel = new JLabel("State");
        JTextField stateText = new JTextField(presence.state);
        stateText.setMaximumSize(dimension);
        bState.add(stateLabel);
        bState.add(stateText);

        Box bLargeImage = Box.createHorizontalBox();
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

        Box bSmallImage = Box.createHorizontalBox();
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

        Box bButtons = Box.createHorizontalBox();
        JButton updatePresence = new JButton("Update Presence");
        updatePresence.addActionListener(e -> {
            presence.details = detailsText.getText();
            presence.state = stateText.getText();
            presence.largeImageKey = ((String) Objects.requireNonNull(largeImageKey.getSelectedItem())).toLowerCase();
            presence.largeImageText = largeImageText.getText();
            presence.smallImageKey = ((String) Objects.requireNonNull(smallImageKey.getSelectedItem())).toLowerCase();
            presence.smallImageText = smallImageText.getText();
            lib.Discord_UpdatePresence(presence);
        });
        bButtons.add(updatePresence);

        Box body = Box.createVerticalBox();
        Dimension minSize = new Dimension(10, 20);
        Dimension prefSize = new Dimension(20, 20);
        Dimension maxSize = new Dimension(50, 70);
        body.add(new Box.Filler(minSize, new Dimension(0, 10), maxSize));
        body.add(bDetails);
        body.add(new Box.Filler(minSize, prefSize, maxSize));
        body.add(bState);
        body.add(new Box.Filler(minSize, prefSize, maxSize));
        body.add(bLargeImage);
        body.add(new Box.Filler(minSize, prefSize, maxSize));
        body.add(bSmallImage);
        body.add(new Box.Filler(minSize, prefSize, maxSize));
        body.add(bButtons);
        body.add(new Box.Filler(minSize, new Dimension(0, 10), maxSize));

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                t.interrupt();
            }
        });

        frame.setJMenuBar(menuBar);
        frame.add(body);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
