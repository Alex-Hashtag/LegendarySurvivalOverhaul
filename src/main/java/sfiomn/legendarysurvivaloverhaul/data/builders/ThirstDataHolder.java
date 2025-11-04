package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonArray;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstData;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IThirstDataHolder;

import java.util.ArrayList;
import java.util.List;

public class ThirstDataHolder implements IThirstDataHolder
{
    private final List<IThirstData> thirstDataList;

    public ThirstDataHolder()
    {
        thirstDataList = new ArrayList<>();
    }

    @Override
    public IThirstDataHolder addThirst(IThirstData thirstData)
    {
        thirstDataList.add(thirstData.copy());
        return this;
    }

    @Override
    public JsonArray build()
    {
        JsonArray json = new JsonArray();
        thirstDataList.forEach(t -> json.add(t.build()));
        return json;
    }
}
