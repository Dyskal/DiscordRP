package dyskal;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import javax.swing.Box.Filler;
import java.awt.*;
import java.util.ArrayList;

import static java.util.Objects.requireNonNull;
import static javax.swing.Box.createHorizontalBox;
import static javax.swing.Box.createVerticalBox;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.UIManager.setLookAndFeel;

class AppIdChooser extends JFrame {
    private String applicationId;

    AppIdChooser() {
        super("DiscordRP");
        setPreferredSize(new Dimension(250, 137));
        //TODO fix image
//        ArrayList<Image> icons = new ArrayList<>();
//        for (String size : new String[]{"","16x16", "20x20", "24x24", "30x30", "31x31", "32x32", "40x40", "48x48", "60x60", "64x64", "96x96", "120x120", "256x256"}) {
//            icons.add(new ImageIcon(requireNonNull(getClass().getClassLoader().getResource("assets/icon"+size+".png"))).getImage());
//        }
//        setIconImages(icons);
        setIconImage(new ImageIcon(requireNonNull(getClass().getClassLoader().getResource("assets/icon.png"))).getImage());
        setResizable(false);

        TomlManager tomlManager = new TomlManager();

        Box bAppID = createVerticalBox();
        JLabel applicationIdLabel = new JLabel("Enter or select your application id:");
        applicationIdLabel.setAlignmentX(CENTER_ALIGNMENT);

        JComboBox<String> applicationIdList = new JComboBox<>(tomlManager.getListAppIds().toArray(new String[0]));
        applicationIdList.setEditable(true);
        applicationIdList.setMaximumSize(new Dimension(215, 40));

        Box bMgmt = createHorizontalBox();
        JButton validation = new JButton("Validate");
        JLabel errorLabel = new JLabel();
        errorLabel.setAlignmentX(CENTER_ALIGNMENT);
        validation.addActionListener(event -> {
            if (applicationIdList.getSelectedItem() != null && ((String) applicationIdList.getSelectedItem()).matches("^[0-9]+$")) {
                applicationId = (String) applicationIdList.getSelectedItem();
                if (!tomlManager.getListAppIds().contains(applicationId)) {
                    tomlManager.addAppId(applicationId);
                }
                dispose();
                invokeLater(DiscordRP::new);
            } else {
                errorLabel.setText("Application Id is invalid");
            }
        });

        JButton remove = new JButton("Remove");
        remove.addActionListener(event -> {
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

        Box body = createVerticalBox();
        body.add(bAppID);
        body.add(bMgmt);
        body.add(errorLabel);
        body.add(new Filler(dim7, dim7, dim7));

        add(body);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    String getAppId() {
        return applicationId;
    }

    public static void main(String... args) {
        try {
            setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        invokeLater(AppIdChooser::new);
    }
}
