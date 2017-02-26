/*
 * Copyright (c) TheDragonTeam 2016-2017.
 */

package net.thedragonteam.armorplus.items.base

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.thedragonteam.armorplus.iface.IItemHelper
import net.thedragonteam.armorplus.iface.IModelHelper
import net.thedragonteam.armorplus.items.enums.DevItems
import net.thedragonteam.thedragonlib.util.ItemStackUtils

class BaseDevItem(private val devItems: DevItems) : BaseItem(devItems.getName()), IItemHelper, IModelHelper {

    init {
        if (devItems.hasSubTypes()) {
            this.setHasSubtypes(true)
            maxDamage = 0
        }
    }

    override fun setCreativeTab(tab: CreativeTabs): Item {
        return this
    }

    override fun initModel() {
        if (devItems.hasSubTypes()) this.initModel(this, registryName!!.toString() + "_second", 1)
        this.initModel(this, registryName, 0)
    }

    override fun getItem(): Item {
        return this
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: NonNullList<ItemStack>) {
        if (devItems.hasSubTypes()) {
            subItems.add(ItemStackUtils.getItemStack(itemIn, 0))
            subItems.add(ItemStackUtils.getItemStack(itemIn, 1))
        }
    }
}