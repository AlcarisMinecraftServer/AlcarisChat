package net.alcaris.plugin.chat.japanize;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public final class GoogleIME {

    private GoogleIME() {}

    public static String parseJson(String json) {
        StringBuilder result = new StringBuilder();
        for (JsonElement response : new Gson().fromJson(json, JsonArray.class)) {
            result.append(response.getAsJsonArray().get(1).getAsJsonArray().get(0).getAsString());
        }
        return result.toString();
    }
}
