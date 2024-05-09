package helper_classes_and_methods;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FireAlarmStorageHandler implements StorageHandler {
    private File file;

    public FireAlarmStorageHandler(Context context) {
        File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "FireAlarmLogs");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file = new File(dir, "fire_alarm_log.txt");
    }

    @Override
    public void saveData(String key, String value) {
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.append(key).append(": ").append(value).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String loadData(String key) {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(key + ": ")) {
                    return line.substring(key.length() + 2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

