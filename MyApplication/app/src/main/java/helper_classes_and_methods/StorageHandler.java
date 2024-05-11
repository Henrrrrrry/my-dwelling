package helper_classes_and_methods;

import java.util.List;

/**
 * Author: Xinfei Li
 * ID: u7785177
 * Create: 09/05/2024   7:00 pm
 * Last Edit: 10/05/2024   02:20 am
 */
public interface StorageHandler {
    void saveData(String str1, String str2);
     List<String> loadAllLogs();
}

