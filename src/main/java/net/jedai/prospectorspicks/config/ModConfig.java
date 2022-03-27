package net.jedai.prospectorspicks.config;

import com.google.common.collect.Lists;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

import java.util.*;

import static net.jedai.prospectorspicks.ProspectorspicksMod.MOD_ID;

@Config(name = MOD_ID)
public class ModConfig implements ConfigData {

    @Override
    public void validatePostLoad() {
        Set<String> validatedGroups = new HashSet<>();

        if (additionalBlocks.isEmpty()) {
            List<String> defaults = Lists.newArrayList(
                    "minecraft:ancient_debris"
            );
            validatedGroups.addAll(defaults);
        }
        validatedGroups.addAll(additionalBlocks);
        additionalBlocks = Lists.newArrayList(validatedGroups);
    }

    @Comment("Radius of copper pick")
    public int copper_radius = 4;
    @Comment("Radius of iron pick")
    public int iron_radius = 5;
    @Comment("Radius of diamond pick")
    public int diamond_radius = 6;
    @Comment("Radius of Netherite pick")
    public int netherite_radius = 7;
    @Comment("Additional block to detect as ore")
    public List<String> additionalBlocks = new ArrayList<>();
}
