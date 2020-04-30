package dyskal;

import com.electronwill.nightconfig.core.file.FileConfig;
import javax.swing.*;
import java.io.File;

public class TomlManager {
    static TomlManager tomlManager = new TomlManager();
    File dir = new File(System.getenv("APPDATA") + "\\Dyskal\\DiscordRP");
    File file = new File(dir + "\\settings.toml");
    FileConfig config = FileConfig.of(file);

    public void isAvailable() {
        config.load();
        String appId = config.get("appId");
        try {
            if (appId == null || appId.isEmpty() || !appId.matches("^[0-9]+$")) {
                SwingUtilities.invokeLater(AppIdChooser::new);
            } else {
                AppIdChooser.applicationId = appId;
                SwingUtilities.invokeLater(DiscordRP::new);
            }
        } catch (Exception ex){
            SwingUtilities.invokeLater(AppIdChooser::new);
        }
    }

    public void addAppId() throws Exception {
        IGNORE_RESULT(dir.mkdirs());
        IGNORE_RESULT(file.createNewFile());
        config.set("appId", AppIdChooser.applicationId);
        config.save();
    }

    @SuppressWarnings("unused")
    private static void IGNORE_RESULT(boolean b){}

}