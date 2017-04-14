package name.kingbright.messagetransfer.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonUtil {

    private static final String TAG = "JsonUtil";
    private static final Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();
        //Add strategy if needed.
        gson = builder.create();
    }

    public static String toJson(Object object) {
        if (object == null) {
            return "";
        }
        try {
            return gson.toJson(object);
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
    }

    public static <T> T fromJson(String json, Class<T> klass) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        if (klass == null) {
            return null;
        }
        try {
            return gson.fromJson(json, klass);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> ArrayList<T> listFromJson(String json, Class<T> classOfT) {
        ArrayList<T> listOfT = new ArrayList<>();

        Type type;
        if (classOfT.isPrimitive()) {
            type = new TypeToken<ArrayList<JsonPrimitive>>() {
            }.getType();

            ArrayList<JsonPrimitive> jsonObjs = gson.fromJson(json, type);

            for (JsonPrimitive jsonObj : jsonObjs) {
                listOfT.add(gson.fromJson(jsonObj, classOfT));
            }
        } else {
            type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjs = gson.fromJson(json, type);

            for (JsonObject jsonObj : jsonObjs) {
                listOfT.add(gson.fromJson(jsonObj, classOfT));
            }
        }

        return listOfT;
    }
}
