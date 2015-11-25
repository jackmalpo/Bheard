package com.malpo.bheard.networking.lastfm.artist.json;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.malpo.bheard.models.Artist;

import java.lang.reflect.Type;

/**
 * Created by Jack on 10/9/15.
 */
public class ArtistDeserializer implements JsonDeserializer<Artist>
{
    @Override
    public Artist deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException
    {
        JsonElement content = je.getAsJsonObject().get("artist");
        return new Gson().fromJson(content, Artist.class);
    }
}
