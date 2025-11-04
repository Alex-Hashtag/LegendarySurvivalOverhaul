package sfiomn.legendarysurvivaloverhaul.api.data.builder;

import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;

import java.util.List;

public interface IBodyPartsDamageSourceData
{

    IBodyPartsDamageSourceData damageDistribution(DamageDistributionEnum damageDistribution);

    IBodyPartsDamageSourceData addBodyPart(BodyPartEnum bodyPart);

    IBodyPartsDamageSourceData addBodyParts(List<BodyPartEnum> bodyParts);

    JsonObject build();
}
