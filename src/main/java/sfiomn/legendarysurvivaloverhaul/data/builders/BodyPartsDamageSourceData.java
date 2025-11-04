package sfiomn.legendarysurvivaloverhaul.data.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.builder.IBodyPartsDamageSourceData;

import java.util.ArrayList;
import java.util.List;

public class BodyPartsDamageSourceData implements IBodyPartsDamageSourceData
{
    private final List<BodyPartEnum> bodyParts;
    private DamageDistributionEnum damageDistribution;

    public BodyPartsDamageSourceData()
    {
        bodyParts = new ArrayList<>();
    }

    @Override
    public IBodyPartsDamageSourceData damageDistribution(DamageDistributionEnum damageDistribution)
    {
        this.damageDistribution = damageDistribution;
        return this;
    }

    @Override
    public IBodyPartsDamageSourceData addBodyPart(BodyPartEnum bodyPart)
    {
        this.bodyParts.add(bodyPart);
        return this;
    }

    @Override
    public IBodyPartsDamageSourceData addBodyParts(List<BodyPartEnum> bodyParts)
    {
        this.bodyParts.addAll(bodyParts);
        return this;
    }

    @Override
    public JsonObject build()
    {
        JsonObject json = new JsonObject();
        json.addProperty("damage_distribution", this.damageDistribution.name());

        JsonArray jsonBodyParts = new JsonArray();
        bodyParts.forEach(b -> jsonBodyParts.add(b.name()));
        json.add("body_parts", jsonBodyParts);
        return json;
    }
}
