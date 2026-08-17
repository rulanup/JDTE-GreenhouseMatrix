package com.jdte.matrix.setup;

import com.jdte.matrix.JDTEMatrix;
import com.jdte.matrix.common.blocks.CreativeGreenhouseBlock;
import com.jdte.matrix.common.blocks.GreenhouseMatrixAutoCraftingBlock;
import com.jdte.matrix.common.blocks.GreenhouseMatrixCasingBlock;
import com.jdte.matrix.common.blocks.GreenhouseMatrixControllerBlock;
import com.jdte.matrix.common.blocks.GreenhouseMatrixEnhancementBlock;
import com.jdte.matrix.common.blocks.GreenhouseMatrixPortBlock;
import com.jdte.matrix.common.blocks.SolarPanelBlock;
import com.jdte.matrix.common.greenhouse.GreenhouseMatrixEnhancement;
import com.jdte.matrix.common.greenhouse.GreenhouseMatrixPortType;
import com.jdte.matrix.common.solar.SolarPanelTier;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MatrixBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(JDTEMatrix.MODID);

    public static final DeferredHolder<Block, GreenhouseMatrixControllerBlock> GREENHOUSE_MATRIX_CONTROLLER = BLOCKS.register("greenhouse_matrix_controller", GreenhouseMatrixControllerBlock::new);
    public static final DeferredHolder<Block, GreenhouseMatrixCasingBlock> GREENHOUSE_MATRIX_CASING = BLOCKS.register("greenhouse_matrix_casing", GreenhouseMatrixCasingBlock::new);
    public static final DeferredHolder<Block, GreenhouseMatrixPortBlock> GREENHOUSE_MATRIX_ITEM_INPUT = BLOCKS.register("greenhouse_matrix_item_input", () -> new GreenhouseMatrixPortBlock(GreenhouseMatrixPortType.ITEM_INPUT));
    public static final DeferredHolder<Block, GreenhouseMatrixPortBlock> GREENHOUSE_MATRIX_ITEM_OUTPUT = BLOCKS.register("greenhouse_matrix_item_output", () -> new GreenhouseMatrixPortBlock(GreenhouseMatrixPortType.ITEM_OUTPUT));
    public static final DeferredHolder<Block, GreenhouseMatrixPortBlock> GREENHOUSE_MATRIX_FLUID_INPUT = BLOCKS.register("greenhouse_matrix_fluid_input", () -> new GreenhouseMatrixPortBlock(GreenhouseMatrixPortType.FLUID_INPUT));
    public static final DeferredHolder<Block, GreenhouseMatrixPortBlock> GREENHOUSE_MATRIX_ENERGY_INPUT = BLOCKS.register("greenhouse_matrix_energy_input", () -> new GreenhouseMatrixPortBlock(GreenhouseMatrixPortType.ENERGY_INPUT));
    public static final DeferredHolder<Block, GreenhouseMatrixEnhancementBlock> GREENHOUSE_MATRIX_SPEED = BLOCKS.register("greenhouse_matrix_speed", () -> new GreenhouseMatrixEnhancementBlock(GreenhouseMatrixEnhancement.SPEED));
    public static final DeferredHolder<Block, GreenhouseMatrixEnhancementBlock> GREENHOUSE_MATRIX_EFFICIENCY = BLOCKS.register("greenhouse_matrix_efficiency", () -> new GreenhouseMatrixEnhancementBlock(GreenhouseMatrixEnhancement.EFFICIENCY));
    public static final DeferredHolder<Block, GreenhouseMatrixEnhancementBlock> GREENHOUSE_MATRIX_SEED = BLOCKS.register("greenhouse_matrix_seed", () -> new GreenhouseMatrixEnhancementBlock(GreenhouseMatrixEnhancement.SEED_CONVERSION));
    public static final DeferredHolder<Block, GreenhouseMatrixEnhancementBlock> GREENHOUSE_MATRIX_ESSENCE = BLOCKS.register("greenhouse_matrix_essence", () -> new GreenhouseMatrixEnhancementBlock(GreenhouseMatrixEnhancement.ESSENCE_CONVERSION));
    public static final DeferredHolder<Block, GreenhouseMatrixAutoCraftingBlock> GREENHOUSE_MATRIX_AUTO_CRAFTING = BLOCKS.register(
            "greenhouse_matrix_auto_crafting", GreenhouseMatrixAutoCraftingBlock::new);

    public static final DeferredHolder<Block, CreativeGreenhouseBlock> CREATIVE_GREENHOUSE = BLOCKS.register(
            "creative_greenhouse", CreativeGreenhouseBlock::new);

    // Tiered Solar Panels
    public static final DeferredHolder<Block, SolarPanelBlock> CONCENTRATED_SOLAR_PANEL = BLOCKS.register(
            "concentrated_solar_panel", () -> new SolarPanelBlock(SolarPanelTier.CONCENTRATED));
    public static final DeferredHolder<Block, SolarPanelBlock> SINGULARITY_SOLAR_PANEL = BLOCKS.register(
            "singularity_solar_panel", () -> new SolarPanelBlock(SolarPanelTier.SINGULARITY));
    public static final DeferredHolder<Block, SolarPanelBlock> STELLAR_FUSION_SOLAR_PANEL = BLOCKS.register(
            "stellar_fusion_solar_panel", () -> new SolarPanelBlock(SolarPanelTier.STELLAR_FUSION));
    public static final DeferredHolder<Block, SolarPanelBlock> DIMENSIONAL_COLLAPSE_SOLAR_PANEL = BLOCKS.register(
            "dimensional_collapse_solar_panel", () -> new SolarPanelBlock(SolarPanelTier.DIMENSIONAL_COLLAPSE));
    public static final DeferredHolder<Block, SolarPanelBlock> CREATIVE_SOLAR_PANEL = BLOCKS.register(
            "creative_solar_panel", () -> new SolarPanelBlock(SolarPanelTier.CREATIVE));
}
