package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonArray;

public interface IThirstDataHolder
{

    IThirstDataHolder addThirst(IThirstData thirstData);

    JsonArray build();
}
