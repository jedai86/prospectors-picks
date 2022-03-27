package net.jedai.prospectorspicks.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.jedai.prospectorspicks.ProspectorspicksMod;
import net.jedai.prospectorspicks.item.custom.ProspectorsPickItem;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final Item IRON_PROSPECTORS_PICK = registerItem("iron_prospectors_pick",
            new ProspectorsPickItem(ModToolMaterial.IRON, 3, -3.0F, new FabricItemSettings().group(ProspectorspicksMod.PROSPECTORS_TAB)));

    public static final Item COPPER_PROSPECTORS_PICK = registerItem("copper_prospectors_pick",
            new ProspectorsPickItem(ModToolMaterial.COPPER, 3, -3.0F, new FabricItemSettings().group(ProspectorspicksMod.PROSPECTORS_TAB)));

    public static final Item DIAMOND_PROSPECTORS_PICK = registerItem("diamond_prospectors_pick",
            new ProspectorsPickItem(ModToolMaterial.DIAMOND, 4, -2.8F, new FabricItemSettings().group(ProspectorspicksMod.PROSPECTORS_TAB)));

    public static final Item NETHERITE_PROSPECTORS_PICK = registerItem("netherite_prospectors_pick",
            new ProspectorsPickItem(ModToolMaterial.NETHERITE, 5, -2.8F, new FabricItemSettings().group(ProspectorspicksMod.PROSPECTORS_TAB)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(ProspectorspicksMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ProspectorspicksMod.LOGGER.info("Registering Mod Items for " + ProspectorspicksMod.MOD_ID);
    }
}
