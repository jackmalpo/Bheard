package com.malpo.bheard.networking.lastfm.artist.json;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.malpo.bheard.models.Artist;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jack on 10/9/15.
 */
public class ArtistDeserializer implements JsonDeserializer<Artist>
{

    JsonElement content;
    Gson gson;

    @Override
    public Artist deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException
    {
        gson = new Gson();

        content = je.getAsJsonObject().get("artist");
        if(content != null)
            return gson.fromJson(content, Artist.class);


        //ArtistCorrection
        content = je.getAsJsonObject().get("corrections");
        if(content != null) {
            content = content.getAsJsonObject().get("correction");
            if (content != null) {
                content = content.getAsJsonObject().get("artist");
                return gson.fromJson(content, Artist.class);
            }
        }

        //ArtistSearch
        content = je.getAsJsonObject().get("results");
        if(content != null) {
            content = content.getAsJsonObject().get("artistmatches");
            if (content != null)
                content = content.getAsJsonObject().get("artist").getAsJsonArray();

            Type listOfArtists = new TypeToken<List<Artist>>() {}.getType();
            String s = gson.toJson(content, listOfArtists);

            return gson.fromJson(s, listOfArtists);
        }

        return null;
    }
}
