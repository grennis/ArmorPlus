package net.thedragonteam.armorplus.blocks.benches;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.Objects;

import static net.thedragonteam.armorplus.util.EnumHelperUtil.addRarity;

public class ItemBlockBench extends ItemBlock {

    private Benches benches;

    public ItemBlockBench(BlockBench block) {
        super(block);
        this.benches = block.benches;
        this.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        switch (benches) {
            case WORKBENCH:
                return addRarity("WORKBENCH", TextFormatting.BLUE, "Workbench");
            case HIGH_TECH:
                return addRarity("HIGH_TECH", TextFormatting.DARK_RED, "High-Tech Bench");
            case ULTI_TECH:
                return addRarity("ULTI_TECH", TextFormatting.GREEN, "Ulti-Tech Bench");
            case CHAMPION:
                return addRarity("CHAMPION", TextFormatting.GOLD, "Champion Bench");
        }
        return EnumRarity.COMMON;
    }
}
