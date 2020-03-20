package dyskal;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AppIdChooser extends JFrame {
    static String applicationId;
    public AppIdChooser() {
        super("DiscordRP");
        this.setPreferredSize(new Dimension(300, 150));
        this.setIconImage(new ImageIcon((Objects.requireNonNull(getClass().getClassLoader().getResource("assets/icon.png")))).getImage());
        this.setResizable(false);

        Box appId = Box.createVerticalBox();
        JLabel applicationIdLabel = new JLabel("Enter your application id:");
        JTextField applicationIdText = new JTextField(applicationId);
        JButton validationButton = new JButton("Validate");
        validationButton.addActionListener(e ->{
            if (!applicationIdText.getText().isEmpty()) {
                applicationId = applicationIdText.getText();
                System.out.println(applicationId);
                try {
                    TomlManager.tomlManager.addAppId();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                finally {
                    this.dispose();
                    SwingUtilities.invokeLater(DiscordRP::new);
                }
            }
        });
        appId.add(applicationIdLabel);
        appId.add(applicationIdText);
        appId.add(validationButton);

        Box body = Box.createVerticalBox();
        Dimension minSize = new Dimension(10, 20);
        Dimension prefSize = new Dimension(0, 10);
        Dimension maxSize = new Dimension(50, 70);
        body.add(new Box.Filler(minSize, prefSize, maxSize));
        body.add(appId);
        body.add(new Box.Filler(minSize, prefSize, maxSize));

        this.add(body);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
