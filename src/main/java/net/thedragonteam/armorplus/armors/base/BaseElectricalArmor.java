/*
 * Copyright (c) TheDragonTeam 2016.
 */

package net.thedragonteam.armorplus.armors.base;

import cofh.api.energy.IEnergyContainerItem;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.thedragonteam.armorplus.APConfig;
import net.thedragonteam.armorplus.ArmorPlus;
import net.thedragonteam.armorplus.base.BaseARPTeslaContainerProvider;

import java.util.List;

import static net.thedragonteam.armorplus.APConfig.*;
import static net.thedragonteam.armorplus.ArmorPlus.getArmorPlusLocation;
import static net.thedragonteam.armorplus.registry.ModBlocks.electricalBlock;
import static net.thedragonteam.armorplus.registry.ModItems.electricalIngot;
import static net.thedragonteam.armorplus.util.ArmorPlusItemUtils.isItemRepairable;
import static net.thedragonteam.armorplus.util.Utils.setName;
import static net.thedragonteam.thedragonlib.util.TextHelper.localize;

@InterfaceList({
        @Interface(iface = "net.darkhax.tesla.api.ITeslaConsumer", modid = "tesla"),
        @Interface(iface = "net.darkhax.tesla.api.ITeslaProducer", modid = "tesla"),
        @Interface(iface = "net.darkhax.tesla.api.ITeslaHolder", modid = "tesla")
})
public class BaseElectricalArmor extends ItemArmor implements ITeslaConsumer, ITeslaProducer, ITeslaHolder, IEnergyContainerItem {

    private static float electricalArmorToughnessPoints = (float) APConfig.electricalArmorToughnessPoints;
    public static ItemArmor.ArmorMaterial electricalArmor = EnumHelper.addArmorMaterial("electricalArmor", getArmorPlusLocation("electrical_armor"), 19,
            APConfig.electricalArmorProtectionPoints, 13, SoundEvents.ITEM_ARMOR_EQUIP_IRON, electricalArmorToughnessPoints);
    private int power;
    private int maxCapacity;
    private int output;
    private int input;

    public BaseElectricalArmor(EntityEquipmentSlot slot, int maxCapacity, int input, int output) {
        super(electricalArmor, 0, slot);
        setMaxStackSize(1);
        switch (slot) {
            case FEET:
                String boots = "electrical_boots";
                setRegistryName(boots);
                setUnlocalizedName(setName(boots));
                break;
            case LEGS:
                String leggings = "electrical_leggings";
                setRegistryName(leggings);
                setUnlocalizedName(setName(leggings));
                break;
            case CHEST:
                String chestplate = "electrical_chestplate";
                setRegistryName(chestplate);
                setUnlocalizedName(setName(chestplate));
                break;
            case HEAD:
                String helmet = "electrical_helmet";
                setRegistryName(helmet);
                setUnlocalizedName(setName(helmet));
                break;
        }
        GameRegistry.register(this);
        setCreativeTab(ArmorPlus.tabArmorplusRF);
        this.power = 0;
        this.maxCapacity = maxCapacity;
        this.output = output;
        this.input = input;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return (TextFormatting.getValueByName(electricalArmorItemNameColor) + localize(this.getUnlocalizedNameInefficiently(stack) + ".name")).trim();
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return isItemRepairable(toRepair, electricalIngot, electricalBlock);
    }

    /**
     * Tesla
     */
    @Method(modid = "tesla")
    @Override
    public long givePower(long power, boolean simulated) {
        return input;
    }

    @Method(modid = "tesla")
    @Override
    public long takePower(long power, boolean simulated) {
        return output;
    }

    @Method(modid = "tesla")
    @Override
    public long getStoredPower() {
        return power;
    }

    @Method(modid = "tesla")
    @Override
    public long getCapacity() {
        return maxCapacity;
    }

    public BaseElectricalArmor setCapacity(int capacity) {
        this.maxCapacity = capacity;
        return this;
    }

    public int getPower() {
        return power;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (player.hurtTime > 0)
            this.extractEnergy(itemStack, outputElectricalArmor, false);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    private int createPoweredStack(ItemStack container, boolean simulate) {
        if ((container.getTagCompound() == null) || (!container.getTagCompound().hasKey("Energy"))) return 0;
        if (!simulate) container.getTagCompound().setInteger("Energy", maxCapacityElectricalArmor);

        return maxCapacityElectricalArmor;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        ItemStack powered = new ItemStack(itemIn, 1, 1);
        ItemStack unpowered = new ItemStack(itemIn);
        poweredItem(powered);
        subItems.add(powered);
        createPoweredStack(powered, false);
        subItems.add(unpowered);
    }

    private int poweredItem(ItemStack itemStack) {
        return receiveEnergy(itemStack, getMaxEnergyStored(itemStack), false);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getItemDamage() == 1)
            createPoweredStack(stack, false);
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return (1 - (double) this.getEnergyStored(stack) / (double) this.getMaxEnergyStored(stack));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Method(modid = "tesla")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        createTooltip(stack, tooltip);
    }

    private void createTooltip(ItemStack stack, List<String> tooltip) {
        final KeyBinding keyBindSneak = Minecraft.getMinecraft().gameSettings.keyBindSneak;
        if (GameSettings.isKeyDown(keyBindSneak)) {
            tooltip.add(TextFormatting.DARK_RED + I18n.format("") + this.getEnergyStored(stack) + "/" + this.getMaxEnergyStored(stack) + I18n.format(" RF"));
            tooltip.add(TextFormatting.DARK_RED + I18n.format("tooltip.rf.cost.tool", Integer.toString(this.getMaxReceive(stack))));
        } else
            tooltip.add(I18n.format("tooltip.rf.showinfo", TextFormatting.DARK_RED, keyBindSneak.getDisplayName(), TextFormatting.GRAY));
    }

    @Method(modid = "tesla")
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new BaseARPTeslaContainerProvider(new BaseTeslaContainer(), power, maxCapacity, output, input);
    }

    /**
     * RF
     */
    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return maxCapacity;
    }

    public BaseElectricalArmor setMaxTransfer(int maxTransfer) {
        setMaxReceive(maxTransfer);
        setMaxExtract(maxTransfer);
        return this;
    }

    public BaseElectricalArmor setMaxReceive(int maxReceive) {
        this.input = maxReceive;
        return this;
    }

    public BaseElectricalArmor setMaxExtract(int maxExtract) {
        this.output = maxExtract;
        return this;
    }

    public int getMaxExtract(ItemStack container) {
        return this.output;
    }

    public int getMaxReceive(ItemStack container) {
        return this.input;
    }

    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (!container.hasTagCompound()) container.setTagCompound(new NBTTagCompound());
        int energy = container.getTagCompound().getInteger("Energy");
        int energyReceived = Math.min(this.maxCapacity - energy, Math.min(this.input, maxReceive));
        if (!simulate) {
            energy += energyReceived;
            container.getTagCompound().setInteger("Energy", energy);
        }
        return energyReceived;
    }

    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if ((container.getTagCompound() == null) || (!container.getTagCompound().hasKey("Energy"))) return 0;
        int energy = container.getTagCompound().getInteger("Energy");
        int energyExtracted = Math.min(energy, Math.min(this.output, maxExtract));
        if (!simulate) {
            energy -= energyExtracted;
            container.getTagCompound().setInteger("Energy", energy);
        }
        return energyExtracted;
    }

    public int getEnergyStored(ItemStack container) {
        return (container.getTagCompound() == null) || (!container.getTagCompound().hasKey("Energy")) ? 0 : (power = container.getTagCompound().getInteger("Energy"));
    }
}
