package com.jdte.matrix.setup;

import com.jdte.matrix.JDTEMatrix;
import com.jdte.matrix.common.items.GreenhouseMatrixQuickInstallUpgradeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MatrixItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(JDTEMatrix.MODID);

    public static final DeferredHolder<Item, GreenhouseMatrixQuickInstallUpgradeItem> GREENHOUSE_MATRIX_QUICK_INSTALL_UPGRADE =
            ITEMS.register("greenhouse_matrix_quick_install_upgrade", GreenhouseMatrixQuickInstallUpgradeItem::new);

    public static final DeferredHolder<Item, BlockItem> GREENHOUSE_MATRIX_CONTROLLER = blockItem("greenhouse_matrix_controller", MatrixBlocks.GREENHOUSE_MATRIX_CONTROLLER);
    public static final DeferredHolder<Item, BlockItem> GREENHOUSE_MATRIX_CASING = blockItem("greenhouse_matrix_casing", MatrixBlocks.GREENHOUSE_MATRIX_CASING);
    public static final DeferredHolder<Item, BlockItem> GREENHOUSE_MATRIX_ITEM_INPUT = blockItem("greenhouse_matrix_item_input", MatrixBlocks.GREENHOUSE_MATRIX_ITEM_INPUT);
    public static final DeferredHolder<Item, BlockItem> GREENHOUSE_MATRIX_ITEM_OUTPUT = blockItem("greenhouse_matrix_item_output", MatrixBlocks.GREENHOUSE_MATRIX_ITEM_OUTPUT);
    public static final DeferredHolder<Item, BlockItem> GREENHOUSE_MATRIX_FLUID_INPUT = blockItem("greenhouse_matrix_fluid_input", MatrixBlocks.GREENHOUSE_MATRIX_FLUID_INPUT);
    public static final DeferredHolder<Item, BlockItem> GREENHOUSE_MATRIX_ENERGY_INPUT = blockItem("greenhouse_matrix_energy_input", MatrixBlocks.GREENHOUSE_MATRIX_ENERGY_INPUT);
    public static final DeferredHolder<Item, BlockItem> GREENHOUSE_MATRIX_SPEED = blockItem("greenhouse_matrix_speed", MatrixBlocks.GREENHOUSE_MATRIX_SPEED);
    public static final DeferredHolder<Item, BlockItem> GREENHOUSE_MATRIX_EFFICIENCY = blockItem("greenhouse_matrix_efficiency", MatrixBlocks.GREENHOUSE_MATRIX_EFFICIENCY);
    public static final DeferredHolder<Item, BlockItem> GREENHOUSE_MATRIX_SEED = blockItem("greenhouse_matrix_seed", MatrixBlocks.GREENHOUSE_MATRIX_SEED);
    public static final DeferredHolder<Item, BlockItem> GREENHOUSE_MATRIX_ESSENCE = blockItem("greenhouse_matrix_essence", MatrixBlocks.GREENHOUSE_MATRIX_ESSENCE);
    public static final DeferredHolder<Item, BlockItem> GREENHOUSE_MATRIX_AUTO_CRAFTING = blockItem(
            "greenhouse_matrix_auto_crafting", MatrixBlocks.GREENHOUSE_MATRIX_AUTO_CRAFTING);

    // Tiered Solar Panels
    public static final DeferredHolder<Item, BlockItem> CONCENTRATED_SOLAR_PANEL = blockItem(
            "concentrated_solar_panel", MatrixBlocks.CONCENTRATED_SOLAR_PANEL);
    public static final DeferredHolder<Item, BlockItem> SINGULARITY_SOLAR_PANEL = blockItem(
            "singularity_solar_panel", MatrixBlocks.SINGULARITY_SOLAR_PANEL);
    public static final DeferredHolder<Item, BlockItem> STELLAR_FUSION_SOLAR_PANEL = blockItem(
            "stellar_fusion_solar_panel", MatrixBlocks.STELLAR_FUSION_SOLAR_PANEL);
    public static final DeferredHolder<Item, BlockItem> DIMENSIONAL_COLLAPSE_SOLAR_PANEL = blockItem(
            "dimensional_collapse_solar_panel", MatrixBlocks.DIMENSIONAL_COLLAPSE_SOLAR_PANEL);
    public static final DeferredHolder<Item, BlockItem> CREATIVE_SOLAR_PANEL = blockItem(
            "creative_solar_panel", MatrixBlocks.CREATIVE_SOLAR_PANEL);

    private static DeferredHolder<Item, BlockItem> blockItem(String name, DeferredHolder<? extends net.minecraft.world.level.block.Block, ? extends net.minecraft.world.level.block.Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
