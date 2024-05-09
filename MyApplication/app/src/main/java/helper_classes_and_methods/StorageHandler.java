package helper_classes_and_methods;

public interface StorageHandler {
    void saveData(String key, String value);
    String loadData(String key);

}

