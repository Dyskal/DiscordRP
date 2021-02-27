package dyskal;

import com.electronwill.nightconfig.core.file.FileConfig;
import net.harawata.appdirs.AppDirsFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TomlManager {
    private final File dir = new File(AppDirsFactory.getInstance().getUserConfigDir("DiscordRP", null, "Dyskal", true));
    private final File file = new File(dir + "\\settings.toml");
    private final FileConfig config = FileConfig.of(file);
    private ArrayList<String> listAppIds = new ArrayList<>();
    private boolean recreate = false;

    public TomlManager() {
        try {
            makeFile();
            config.load();
            listAppIds = config.get("appId");
        } catch (Exception e) {
            e.printStackTrace();
            recreate = true;
            try {
                makeFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void makeFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        if (!dir.exists() || !file.exists() || br.readLine() == null || recreate) {
            ArrayList<String> placeholder = new ArrayList<>();
            placeholder.add("1234567890");
            dir.mkdirs();
            file.createNewFile();
            config.set("appId", placeholder);
            config.save();
            recreate = false;
        }
    }

    public ArrayList<String> getListAppIds() {
        return listAppIds;
    }

    public void writer() {
        config.remove("appId");
        config.set("appId", listAppIds);
        config.save();
    }

    public void addAppId(String appId) {
        listAppIds.add(appId);
        writer();
    }

    public void removeAppId(String appId) {
        listAppIds.remove(appId);
        writer();
    }
}