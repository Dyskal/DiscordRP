package dyskal;

import com.electronwill.nightconfig.core.file.FileConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static net.harawata.appdirs.AppDirsFactory.getInstance;

class TomlManager {
    private final File dir = new File(getInstance().getUserConfigDir("DiscordRP", null, "Dyskal", true));
    private final File file = new File(dir + "\\settings.toml");
    private final FileConfig config = FileConfig.of(file);
    private ArrayList<String> listAppIds = new ArrayList<>();
    private boolean recreate = false;

    TomlManager() {
        try {
            makeFile();
        } catch (IOException e) {
            e.printStackTrace();
            recreate = true;
            try {
                makeFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void makeFile() throws IOException {
        if (!recreate && dir.exists() && file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            if (br.readLine() != null && br.readLine().isEmpty()) {
                config.load();
                listAppIds = config.get("appId");
                return;
            }
        }
        ArrayList<String> placeholder = new ArrayList<>();
        placeholder.add("1234567890");
        dir.mkdirs();
        file.createNewFile();
        config.set("appId", placeholder);
        config.save();

        config.load();
        listAppIds = config.get("appId");
        recreate = false;
    }

    ArrayList<String> getListAppIds() {
        return listAppIds;
    }

    private void writer() {
        config.remove("appId");
        config.set("appId", listAppIds);
        config.save();
    }

    void addAppId(String appId) {
        listAppIds.add(appId);
        writer();
    }

    void removeAppId(String appId) {
        listAppIds.remove(appId);
        writer();
    }
}