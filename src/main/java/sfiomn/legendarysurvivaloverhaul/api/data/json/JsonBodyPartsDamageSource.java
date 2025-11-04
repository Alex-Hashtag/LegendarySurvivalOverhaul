package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;

import java.util.List;

public class JsonBodyPartsDamageSource
{
    public static final Codec<JsonBodyPartsDamageSource> CODEC = RecordCodecBuilder.create((inst) -> inst.group(
            Codec.STRING.fieldOf("damage_distribution").forGetter(d -> d.damageDistribution.name()),
            Codec.STRING.listOf().fieldOf("body_parts").forGetter(d -> d.bodyParts.stream().map(Enum::name).toList())
    ).apply(inst, JsonBodyPartsDamageSource::new));

    public List<BodyPartEnum> bodyParts;
    public DamageDistributionEnum damageDistribution;

    public JsonBodyPartsDamageSource(String damageDistribution, List<String> bodyParts)
    {
        this.damageDistribution = DamageDistributionEnum.get(damageDistribution);
        this.bodyParts = bodyParts.stream().map(BodyPartEnum::get).toList();
    }

    public List<BodyPartEnum> getBodyParts(Player player)
    {
        return this.damageDistribution.getBodyParts(player, this.bodyParts);
    }
}
