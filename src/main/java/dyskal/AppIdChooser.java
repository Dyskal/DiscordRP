package dyskal;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AppIdChooser extends JFrame {
    private static String applicationId;
    private static final TomlManager tomlManager = new TomlManager();

    public AppIdChooser() {
        JFrame frame = new JFrame();
        frame.setTitle("DiscordRP");
        frame.setPreferredSize(new Dimension(300, 150));
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/icon.png"))).getImage());
        frame.setResizable(false);

        Box bAppID = Box.createVerticalBox();
        JLabel applicationIdLabel = new JLabel("Enter your application id:");
        JTextField applicationIdText = new JTextField(applicationId);

        Box bMgmt = Box.createHorizontalBox();
        JButton validationButton = new JButton("Validate");
        JLabel errorLabel = new JLabel();
        validationButton.addActionListener(e ->{
            if (applicationIdText.getText().matches("^[0-9]+$")) {
                applicationId = applicationIdText.getText();
                try {
                    tomlManager.addAppId();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                finally {
                    frame.dispose();
                    SwingUtilities.invokeLater(DiscordRP::new);
                }
            }
            else {
                errorLabel.setText("Application Id is invalid");
            }
        });

        bAppID.add(applicationIdLabel);
        bAppID.add(applicationIdText);
        bMgmt.add(validationButton);
        bMgmt.add(errorLabel);

        Box body = Box.createVerticalBox();
        Dimension minSize = new Dimension(10, 20);
        Dimension prefSize = new Dimension(0, 10);
        Dimension maxSize = new Dimension(50, 70);
        body.add(new Box.Filler(minSize, prefSize, maxSize));
        body.add(bAppID);
        body.add(bMgmt);
        body.add(new Box.Filler(minSize, prefSize, maxSize));

        frame.add(body);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void setAppId(String appId) {
        applicationId = appId;
    }

    public static String getAppId() {
        return applicationId;
    }
}
