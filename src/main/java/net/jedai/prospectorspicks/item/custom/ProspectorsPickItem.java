package net.jedai.prospectorspicks.item.custom;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.jedai.prospectorspicks.config.ModConfig;
import net.jedai.prospectorspicks.item.ModToolMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProspectorsPickItem extends PickaxeItem {
    ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    public final int iron_radius = config.iron_radius;
    public final int copper_radius = config.copper_radius;
    public final int diamond_radius = config.diamond_radius;
    public final int netherite_radius = config.netherite_radius;
    public List<String> additionalBlocks = config.additionalBlocks;
    public final int MIN_DISTANCE = 3;
    public final int MAX_DISTANCE = 7;

    public ProspectorsPickItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && state.getHardness(world, pos) != 0.0f) {
            stack.damage(1, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        int radius = getRadius();
        if (this.isSuitableFor(state)) {
            PlayerEntity player = (PlayerEntity) miner;
            boolean foundBlock = false;
            ArrayList<String> blocksFound = new ArrayList<>();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            if (stack.hasNbt() && stack.getNbt().contains("try")) {
                if (stack.getNbt().getInt("try") == 1) {
                    int lastX = stack.getNbt().getIntArray("pos1")[0];
                    int lastY = stack.getNbt().getIntArray("pos1")[1];
                    int lastZ = stack.getNbt().getIntArray("pos1")[2];
                    int distance = maxDistance(lastX, lastY, lastZ, x, y, z);
                    if (distance > MAX_DISTANCE+1) {
                        stack.getOrCreateNbt().putIntArray("pos1",new int[]{x,y,z});
                        player.sendMessage(new TranslatableText("item.prospectorspicks.pick.error_far"), false);
                    } else if (distance <= MIN_DISTANCE) {
                        stack.getOrCreateNbt().putIntArray("pos1",new int[]{x,y,z});
                        player.sendMessage(new TranslatableText("item.prospectorspicks.pick.error_close"), false);
                    } else {
                        stack.getOrCreateNbt().putIntArray("pos2",new int[]{x,y,z});
                        stack.getOrCreateNbt().putInt("try", 2);
                        player.sendMessage(new TranslatableText("item.prospectorspicks.pick.second_shot"), false);
                    }
                } else if (stack.getNbt().getInt("try") == 2) {
                    int lastX1 = stack.getNbt().getIntArray("pos1")[0];
                    int lastY1 = stack.getNbt().getIntArray("pos1")[1];
                    int lastZ1 = stack.getNbt().getIntArray("pos1")[2];
                    int lastX2 = stack.getNbt().getIntArray("pos2")[0];
                    int lastY2 = stack.getNbt().getIntArray("pos2")[1];
                    int lastZ2 = stack.getNbt().getIntArray("pos2")[2];
                    int distance1 = maxDistance(lastX1, lastY1, lastZ1, x, y, z);
                    int distance2 = maxDistance(lastX2, lastY2, lastZ2, x, y, z);
                    if (distance2 > MAX_DISTANCE+1) {
                        stack.getOrCreateNbt().putIntArray("pos1",new int[]{x,y,z});
                        stack.removeSubNbt("pos2");
                        stack.getOrCreateNbt().putInt("try", 1);
                        player.sendMessage(new TranslatableText("item.prospectorspicks.pick.error_far"), false);
                    } else if (distance1 <= MIN_DISTANCE || distance2 <= MIN_DISTANCE) {
                        stack.getOrCreateNbt().putIntArray("pos1",new int[]{x,y,z});
                        stack.removeSubNbt("pos2");
                        stack.getOrCreateNbt().putInt("try", 1);
                        player.sendMessage(new TranslatableText("item.prospectorspicks.pick.error_close"), false);
                    } else {
                        for (int i = x - radius; i <= x + radius; i++) {
                            for (int j = y - radius; j <= y + radius; j++) {
                                for (int k = z - radius; k <= z + radius; k++) {
                                    BlockPos position = new BlockPos(i, j, k);
                                    if (isOre(world, position)) {
                                        Block blockBelow = world.getBlockState(position).getBlock();
                                        blocksFound.add(blockBelow.asItem().getName().getString());
                                    }
                                }
                            }
                        }
                        if (!blocksFound.isEmpty()) {
                            foundBlock = true;
                            HashSet<String> set = new HashSet<>(blocksFound);
                            blocksFound.clear();
                            blocksFound.addAll(set);
                            String s = String.join(",\n", blocksFound);
                            outputValuableBlocks(player, s);
                        }

                        if (!foundBlock) {
                            player.sendMessage(new TranslatableText("item.prospectorspicks.pick.no_valuables"), false);
                        }
                        stack.removeSubNbt("pos1");
                        stack.removeSubNbt("pos2");
                        stack.removeSubNbt("try");
                    }
                }
            } else {
                stack.getOrCreateNbt().putIntArray("pos1",new int[]{x,y,z});
                stack.getOrCreateNbt().putInt("try", 1);
                player.sendMessage(new TranslatableText("item.prospectorspicks.pick.first_shot"), false);
            }
        }
        return true;
    }

    private int maxDistance(int x1, int y1, int z1, int x2, int y2, int z2) {
        int r1 = Math.abs(x1 - x2);
        int r2 = Math.abs(y1 - y2);
        int r3 = Math.abs(z1 - z2);
        return Math.max(r1, Math.max(r2, r3));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        LiteralText line1 = new LiteralText(new TranslatableText("item.prospectorspicks.pick.tooltip_r").getString() + getRadius());
        LiteralText line2 = new LiteralText(new TranslatableText("item.prospectorspicks.pick.tooltip_shift").getString());
        LiteralText line_shift_1 = new LiteralText(new TranslatableText("item.prospectorspicks.pick.tooltip_1").getString());
        LiteralText line_shift_2 = new LiteralText(new TranslatableText("item.prospectorspicks.pick.tooltip_2").getString());

        tooltip.add(line1.formatted(Formatting.YELLOW));

        if(Screen.hasShiftDown()) {
            tooltip.add(line_shift_1.formatted(Formatting.GRAY));
            tooltip.add(line_shift_2.formatted(Formatting.GRAY));
        } else {
            tooltip.add(line2);
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public int getRadius() {
        if (this.getMaterial() == ModToolMaterial.IRON) {
            return iron_radius;
        } else if (this.getMaterial() == ModToolMaterial.COPPER) {
            return copper_radius;
        } else if (this.getMaterial() == ModToolMaterial.DIAMOND) {
            return diamond_radius;
        } else if (this.getMaterial() == ModToolMaterial.NETHERITE) {
            return netherite_radius;
        }
        return 0;
    }

    private void outputValuableBlocks(PlayerEntity player, String blocks) {
        player.sendMessage(new LiteralText(new TranslatableText("item.prospectorspicks.pick.found").getString() + "\n§a" + blocks), false);
    }

    private boolean isOre(World world, BlockPos blockPos) {
        BlockState state = world.getBlockState(blockPos);
        Block block = state.getBlock();

        return !state.isAir()
                && !(block instanceof FluidBlock)
                && state.getHardness(world, blockPos) >= 0f
                && isOreCheckId(Registry.BLOCK.getId(block).toString());
    }

    boolean isOreCheckId(String id) {
        return id.endsWith("_ore") || additionalBlocks.contains(id);
    }
}
