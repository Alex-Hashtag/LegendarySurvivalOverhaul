package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.List;

public enum DamageDistributionEnum
{
    NONE(),
    ONE_OF(),
    ALL();

    DamageDistributionEnum()
    {
    }

    public static DamageDistributionEnum get(String name)
    {
        for (DamageDistributionEnum b : values())
            if (b.name().equalsIgnoreCase(name)) return b;
        throw new IllegalArgumentException();
    }

    public List<BodyPartEnum> getBodyParts(Player player, List<BodyPartEnum> bodyParts)
    {
        if (this == DamageDistributionEnum.ONE_OF)
        {
            return Collections.singletonList(bodyParts.get(player.getRandom().nextInt(bodyParts.size())));
        }
        return bodyParts;
    }
}
