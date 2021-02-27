package dyskal;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import javax.swing.Box.Filler;
import java.awt.*;
import java.util.Objects;

public class AppIdChooser extends JFrame {
    private static String applicationId;
    private final TomlManager tomlManager = new TomlManager();

    public AppIdChooser() {
        JFrame frame = new JFrame();
        frame.setTitle("DiscordRP");
        frame.setPreferredSize(new Dimension(250, 137));
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/icon.png"))).getImage());
        frame.setResizable(false);

        Box bAppID = Box.createVerticalBox();
        JLabel applicationIdLabel = new JLabel("Enter or select your application id:");
        applicationIdLabel.setAlignmentX(CENTER_ALIGNMENT);
        JComboBox<String> applicationIdList = new JComboBox<>(tomlManager.getListAppIds().toArray(new String[0]));
        applicationIdList.setEditable(true);
        applicationIdList.setMaximumSize(new Dimension(215, 40));


        Box bMgmt = Box.createHorizontalBox();
        JButton validation = new JButton("Validate");
        JLabel errorLabel = new JLabel();
        errorLabel.setAlignmentX(CENTER_ALIGNMENT);
        validation.addActionListener(e -> {
            if (applicationIdList.getSelectedItem() != null && ((String) applicationIdList.getSelectedItem()).matches("^[0-9]+$")) {
                applicationId = (String) applicationIdList.getSelectedItem();
                if (!tomlManager.getListAppIds().contains(applicationId)) {
                    tomlManager.addAppId(applicationId);
                }
                frame.dispose();
                SwingUtilities.invokeLater(DiscordRP::new);
            } else {
                errorLabel.setText("Application Id is invalid");
            }
        });

        JButton remove = new JButton("Remove");
        remove.addActionListener(e -> {
            tomlManager.removeAppId((String) applicationIdList.getSelectedItem());
            applicationIdList.removeItem(applicationIdList.getSelectedItem());
        });

        Dimension dim5 = new Dimension(0, 5);
        Dimension dim7 = new Dimension(0, 7);

        bAppID.add(new Filler(dim5, dim5, dim5));
        bAppID.add(applicationIdLabel);
        bAppID.add(new Filler(dim5, dim5, dim5));
        bAppID.add(applicationIdList);
        bAppID.add(new Filler(dim5, dim5, dim5));
        bMgmt.add(validation);
        bMgmt.add(remove);

        Box body = Box.createVerticalBox();
        body.add(bAppID);
        body.add(bMgmt);
        body.add(errorLabel);
        body.add(new Filler(dim7, dim7, dim7));

        frame.add(body);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static String getAppId() {
        return applicationId;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(AppIdChooser::new);
    }
}
