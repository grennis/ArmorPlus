package net.thedragonteam.armorplus.iface;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IModdedItem extends IModelHelper, IRarityHelper, IForgeRegistryEntry<Item> {

    @SideOnly(Side.CLIENT)
    default void initModel(String suffix, String location, int meta, String variantIn) {
        this.initModel(this.getRegistryName(), suffix, location, meta, variantIn);
    }

    @SideOnly(Side.CLIENT)
    default void initModel(String suffix, String location, int meta) {
        this.initModel(suffix, location, meta, "");
    }

    @SideOnly(Side.CLIENT)
    default void initModel(String suffix, String location) {
        this.initModel(suffix, location, 0);
    }

    @SideOnly(Side.CLIENT)
    default void initModel(String location, int meta, String variantIn) {
        this.initModel("", location, meta, variantIn);
    }

    @SideOnly(Side.CLIENT)
    default void initModel(String location, int meta) {
        this.initModel(location, meta, "");
    }

    @SideOnly(Side.CLIENT)
    default void initModel(int meta, String variantIn) {
        this.initModel("", meta, variantIn);
    }

    @SideOnly(Side.CLIENT)
    default void initModel(String location) {
        this.initModel(location, 0);
    }

    @SideOnly(Side.CLIENT)
    default void initModel(int meta) {
        this.initModel(meta, "");
    }
}