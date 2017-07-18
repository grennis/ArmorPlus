/*
 * Copyright (c) TheDragonTeam 2016-2017.
 */

package net.thedragonteam.armorplus.api.crafting.ultitechbench;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.thedragonteam.armorplus.api.crafting.ultitechbench.recipes.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * net.thedragonteam.armorplus.api.crafting.hightechbench
 * ArmorPlus created by sokratis12GR on 6/19/2016 12:29PM.
 * - TheDragonTeam
 */
public class UltiTechBenchCraftingManager {
    /**
     * The
     * static instance of
     * this class
     */
    private static final UltiTechBenchCraftingManager INSTANCE = new UltiTechBenchCraftingManager();
    private final List<IRecipe> recipes = Lists.newArrayList();

    private UltiTechBenchCraftingManager() {
        new ModUltimateRecipes().addRecipes(this);
        new ModEnderDragonRecipes().addRecipes(this);
        new ModSuperStarRecipes().addRecipes(this);
        new ModGuardianRecipes().addRecipes(this);
        new ModWeaponTierThreeRecipes().addRecipes(this);
        new ModItemRecipes().addRecipes(this);
        this.recipes.sort((pCompare1, pCompare2) -> Integer.compare(pCompare2.getRecipeSize(), pCompare1.getRecipeSize()));
    }

    /**
     * Returns the
     * static instance of
     * this class
     */
    public static UltiTechBenchCraftingManager getInstance() {
        // The static instance of this class
        return INSTANCE;
    }

    /**
     * Adds a shaped recipe to the games recipe list.
     */
    public ShapedRecipes addRecipe(ItemStack stack, Object... recipeComponents) {
        StringBuilder s = new StringBuilder();
        int i = 0;
        int j = 0;
        int k = 0;

        if (recipeComponents[i] instanceof String[]) {
            String[] astring = (String[]) recipeComponents[i++];

            for (String s2 : astring) {
                ++k;
                j = s2.length();
                s.append(s2);
            }
        } else {
            while (recipeComponents[i] instanceof String) {
                String s1 = (String) recipeComponents[i++];
                ++k;
                j = s1.length();
                s.append(s1);
            }
        }

        Map<Character, ItemStack> map;

        for (map = Maps.newHashMap(); i < recipeComponents.length; i += 2) {
            Character character = (Character) recipeComponents[i];
            ItemStack itemstack = ItemStack.EMPTY;

            if (recipeComponents[i + 1] instanceof Item) {
                itemstack = new ItemStack((Item) recipeComponents[i + 1]);
            } else if (recipeComponents[i + 1] instanceof Block) {
                itemstack = new ItemStack((Block) recipeComponents[i + 1], 1, 32767);
            } else if (recipeComponents[i + 1] instanceof ItemStack) {
                itemstack = (ItemStack) recipeComponents[i + 1];
            }

            map.put(character, itemstack);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        IntStream.range(0, j * k).forEachOrdered(l -> {
            char c0 = s.charAt(l);
            aitemstack[l] = map.containsKey(c0) ? map.get(c0).copy() : ItemStack.EMPTY;
        });

        ShapedRecipes shapedrecipes = new ShapedRecipes(j, k, aitemstack, stack);
        this.recipes.add(shapedrecipes);
        return shapedrecipes;
    }

    /**
     * Adds a shapeless crafting recipe to the the game.
     */
    public void addShapelessRecipe(ItemStack stack, Object... recipeComponents) {
        List<ItemStack> list = Lists.newArrayList();

        Arrays.stream(recipeComponents).forEachOrdered(object -> {
            if (object instanceof ItemStack) {
                list.add(((ItemStack) object).copy());
            } else if (object instanceof Item) {
                list.add(new ItemStack((Item) object));
            } else {
                assert object instanceof Block : "Invalid shapeless recipe: unknown type " + object.getClass().getName() + "!";

                list.add(new ItemStack((Block) object));
            }
        });

        this.recipes.add(new ShapelessRecipes(stack, list));
    }

    /**
     * Adds an IRecipe to the list of crafting recipes.
     */
    public void addRecipe(IRecipe recipe) {
        this.recipes.add(recipe);
    }

    /**
     * Removes an IRecipe to the list of crafting recipes.
     */
    public void removeRecipe(IRecipe recipe) {
        this.recipes.remove(recipe);
    }

    /**
     * Retrieves an ItemStack that has multiple recipes for it.
     */
    public ItemStack findMatchingRecipe(InventoryCrafting craftMatrix, World worldIn) {
        for (IRecipe recipe : this.recipes) {
            if (recipe.matches(craftMatrix, worldIn)) {
                return Optional.of(recipe).map(irecipe -> irecipe.getCraftingResult(craftMatrix)).orElse(ItemStack.EMPTY);
            }
        }
        return Optional.<IRecipe>empty().map(irecipe -> irecipe.getCraftingResult(craftMatrix)).orElse(ItemStack.EMPTY);
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting craftMatrix, World worldIn) {
        for (IRecipe irecipe : this.recipes) {
            if (irecipe.matches(craftMatrix, worldIn)) return irecipe.getRemainingItems(craftMatrix);
        }

        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(craftMatrix.getSizeInventory(), ItemStack.EMPTY);

        IntStream.range(0, nonnulllist.size()).forEachOrdered(i -> nonnulllist.set(i, craftMatrix.getStackInSlot(i)));

        return nonnulllist;
    }

    public List<IRecipe> getRecipeList() {
        return this.recipes;
    }
}