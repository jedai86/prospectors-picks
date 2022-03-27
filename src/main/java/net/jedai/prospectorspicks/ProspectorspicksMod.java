package net.jedai.prospectorspicks;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.jedai.prospectorspicks.config.ModConfig;
import net.jedai.prospectorspicks.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProspectorspicksMod implements ModInitializer {
	public static final String MOD_ID = "prospectorspicks";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final ItemGroup PROSPECTORS_TAB = FabricItemGroupBuilder.build(new Identifier(ProspectorspicksMod.MOD_ID, "prospectorspickstab"),
			() -> new ItemStack(ModItems.IRON_PROSPECTORS_PICK));

	@Override
	public void onInitialize() {
		AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
		ModItems.registerModItems();

	}
}
