/*
 * Copyright (c) TheDragonTeam 2016-2017.
 */

package net.thedragonteam.armorplus.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.thedragonteam.armorplus.compat.crafttweaker.actions.AddShaped;
import net.thedragonteam.armorplus.compat.crafttweaker.actions.AddShapeless;
import net.thedragonteam.armorplus.compat.crafttweaker.actions.Remove;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static net.thedragonteam.armorplus.api.crafting.base.BaseCraftingManager.getWBInstance;
import static net.thedragonteam.armorplus.compat.crafttweaker.CTArmorPlusPlugin.toWorkbenchShapedObjects;
import static net.thedragonteam.armorplus.compat.crafttweaker.InputHelper.toStack;

@ZenClass("mods.armorplus.Workbench")
public class Workbench {

    @ZenMethod
    public static void addShapeless(IItemStack output, IIngredient[] ingredients) {
        CraftTweakerAPI.apply(new AddShapeless(getWBInstance(), output, ingredients));
    }

    @ZenMethod
    public static void addShaped(IItemStack output, IIngredient[][] ingredients) {
        CraftTweakerAPI.apply(new AddShaped(getWBInstance(), 3, output, toWorkbenchShapedObjects(ingredients)));
    }

    @ZenMethod
    public static void remove(IItemStack target) {
        CraftTweakerAPI.apply(new Remove(getWBInstance(), toStack(target)));
    }
}