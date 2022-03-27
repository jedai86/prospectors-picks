package net.jedai.prospectorspicks.item;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

public enum ModToolMaterial implements ToolMaterial {
    IRON(2, 250 , 5.0F , 0.0f, 0, () -> {
        return Ingredient.ofItems(Items.IRON_INGOT);
    }),
    COPPER(2, 150, 4.0F, 0.0F, 0, () -> {
        return Ingredient.ofItems(Items.COPPER_INGOT);
    }),
    DIAMOND(3, 1000, 6.0F, 0.0f, 0, () -> {
        return Ingredient.ofItems(Items.DIAMOND);
    }),
    NETHERITE(4, 1500, 7.0F, 0.0F, 0, () -> {
        return Ingredient.ofItems(Items.NETHERITE_INGOT);
    });

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
