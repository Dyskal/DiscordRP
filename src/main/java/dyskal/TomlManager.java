package dyskal;

import com.electronwill.nightconfig.core.file.FileConfig;
import javax.swing.*;
import java.io.File;

public class TomlManager {
    static TomlManager tomlManager = new TomlManager();
    private final File dir = new File(System.getenv("APPDATA") + "\\Dyskal\\DiscordRP");
    private final File file = new File(dir + "\\settings.toml");

    public void isAvailable() {
        try {
            FileConfig config = FileConfig.of(file);
            config.load();
            String appId = config.get("appId");
            if (appId == null || appId.isEmpty() || !appId.matches("^[0-9]+$")) {
                SwingUtilities.invokeLater(AppIdChooser::new);
            } else {
                AppIdChooser.setAppId(appId);
                SwingUtilities.invokeLater(DiscordRP::new);
            }
        } catch (Exception ex){
            SwingUtilities.invokeLater(AppIdChooser::new);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void addAppId() throws Exception {
        dir.mkdirs();
        file.createNewFile();
        FileConfig config = FileConfig.of(file);
        config.set("appId", AppIdChooser.getAppId());
        config.save();
    }
}