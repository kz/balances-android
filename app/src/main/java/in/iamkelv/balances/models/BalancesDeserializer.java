package in.iamkelv.balances.models;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Source: http://stackoverflow.com/a/23071080/2102540
 */
public class BalancesDeserializer implements JsonDeserializer<Balances>
{
    @Override
    public Balances deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException
    {
        // Get the "content" element from the parsed JSON
        JsonElement balances = je.getAsJsonObject().get("balances");

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(balances, Balances.class);

    }
}