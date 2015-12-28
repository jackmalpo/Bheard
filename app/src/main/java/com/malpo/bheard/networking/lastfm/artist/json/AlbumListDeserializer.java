package com.malpo.bheard.networking.lastfm.artist.json;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.malpo.bheard.models.Album;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jack on 10/9/15.
 * Deserializes LastFm LIST Artist responses.
 */
public class AlbumListDeserializer implements JsonDeserializer<List<Album>>
{

    @Override
    public List<Album> deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        Gson gson = new Gson();

        //LastfmSearch
        JsonElement content = je.getAsJsonObject().get("topalbums");
        if (content != null) {
            content = content.getAsJsonObject().get("album").getAsJsonArray();
            return gson.fromJson(content, type);
        }

        return null;
    }
}
