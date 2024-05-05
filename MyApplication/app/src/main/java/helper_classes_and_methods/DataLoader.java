package helper_classes_and_methods;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    private BTree bTree;
    private Context context;


    public DataLoader(Context context) {
        this.bTree = new BTree(5, String::compareTo);
        this.context = context;
    }
    public JSONArray loadData(String fileName) throws Exception {
        InputStream is;
        try {
            is = context.openFileInput(fileName);
        } catch (Exception e) {
            is = context.getAssets().open(fileName);
        }
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String json = new String(buffer, "UTF-8");
        return new JSONArray(json);
    }
    public void loadDataFromFile(String filePath) {
        try {

            JSONArray jsonArray = loadData(filePath);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Dwelling dwelling = createDwellingFromJson(jsonObject);
                bTree.put(dwelling.getAddress(), dwelling);
            }
            System.out.println("Data loaded successfully into BTree.");
        } catch (Exception e) {
            System.out.println("Failed to read or parse JSON file: " + e.getMessage());
        }
    }

    private Dwelling createDwellingFromJson(JSONObject jsonObject) throws Exception {
        String address = jsonObject.optString("address","");
        LocalDate constructionDate = LocalDate.parse(jsonObject.optString("constructionDate",""));
        BuildingMaterial material = BuildingMaterial.valueOf(jsonObject.optString("buildingMaterial","").toUpperCase());

        ArrayList<Observer> observers = new ArrayList<>(); // This should be dynamically filled if needed
        User maintainer = null;
        try {
            JSONObject maintainerJsonObject = jsonObject.getJSONObject("maintainer");
            maintainer = new User(maintainerJsonObject.getString("name"),
                    maintainerJsonObject.getString("password"),
                    maintainerJsonObject.getBoolean("isMaintainer"),
                    maintainerJsonObject.getString("userID"));
        } catch (Exception e) {
            //maintainer = new User(jsonObject.optString("maintainer",""),null, true, null);
        }
        Dwelling.Location location = new Dwelling.Location(jsonObject.getJSONObject("location").optDouble("lat",0.0),
                jsonObject.getJSONObject("location").optDouble("lng",0.0));

        Dwelling dwelling = new Dwelling(address, constructionDate, material, observers, maintainer, location);
        return dwelling;
    }

    private JSONObject dwellingToJson(Dwelling dwelling) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address", dwelling.getAddress());
        jsonObject.put("constructionDate", dwelling.getConstructionDate().toString());
        jsonObject.put("lastRepairDate", dwelling.getLastRepairDate().toString());
        jsonObject.put("fireAlarm", dwelling.isFireAlarm());
        jsonObject.put("buildingMaterial", dwelling.getBuildingMaterial().name().toLowerCase());
        JSONObject maintainer = new JSONObject();
        if (dwelling.getMaintainer() != null) {
            maintainer.put("name", dwelling.getMaintainer().getUsername());
            maintainer.put("password", dwelling.getMaintainer().getPassword());
            maintainer.put("userID", dwelling.getMaintainer().getUserID());
            maintainer.put("isMaintainer", dwelling.getMaintainer().isMaintainer());
        }
        jsonObject.put("maintainer", maintainer);
        JSONObject location = new JSONObject();
        if (dwelling.getLocation() != null) {
            location.put("lat", dwelling.getLocation().getLat());
            location.put("lng", dwelling.getLocation().getLng());
        }
        jsonObject.put("location", location);
        return jsonObject;
    }


    public void saveDwellingsToFile(String filePath) {
        try {
            List<Dwelling> dwellings = bTree.getDwellings();
            JSONArray jsonArray = new JSONArray();

            for (Dwelling dwelling : dwellings) {
                JSONObject jsonObject = dwellingToJson(dwelling);
                jsonArray.put(jsonObject);
            }

            saveDataInternalStorage(jsonArray.toString(),context, filePath);
            System.out.println("Data successfully saved to " + filePath);
        } catch (Exception e) {
            System.err.println("Failed to write JSON file: " + e.getMessage());
        }
    }

    public void saveDataInternalStorage(String data, Context context, String filename) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BTree getBTree() {
        return bTree;
    }

    @Override
    protected void finalize() throws Throwable {
        this.saveDwellingsToFile("dataset.json");
    }
}
