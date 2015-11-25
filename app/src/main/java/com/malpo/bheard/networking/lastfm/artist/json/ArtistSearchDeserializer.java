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
public class ArtistSearchDeserializer implements JsonDeserializer<Artist>
{
    @Override
    public Artist deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException
    {
        JsonElement content = je.getAsJsonObject().get("results");
        if(content != null)
            content = content.getAsJsonObject().get("artistmatches");
        if(content != null)
            content = content.getAsJsonObject().get("artist").getAsJsonArray();

        Gson gson = new Gson();
        Type listOfArtists = new TypeToken<List<Artist>>(){}.getType();
        String s = gson.toJson(content, listOfArtists);

        return gson.fromJson(s, listOfArtists);
    }
}
