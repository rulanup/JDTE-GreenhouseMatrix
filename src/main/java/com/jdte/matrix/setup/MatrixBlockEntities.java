package com.jdte.matrix.setup;

import com.jdte.matrix.JDTEMatrix;
import com.jdte.matrix.common.blockentities.CreativeGreenhouseBE;
import com.jdte.matrix.common.blockentities.GreenhouseMatrixAutoCraftingBE;
import com.jdte.matrix.common.blockentities.GreenhouseMatrixControllerBE;
import com.jdte.matrix.common.blockentities.GreenhouseMatrixPortBE;
import com.jdte.matrix.common.blockentities.SolarPanelBE;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MatrixBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, JDTEMatrix.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GreenhouseMatrixControllerBE>> GREENHOUSE_MATRIX_CONTROLLER = BLOCK_ENTITIES.register(
            "greenhouse_matrix_controller", () -> BlockEntityType.Builder.of(GreenhouseMatrixControllerBE::new, MatrixBlocks.GREENHOUSE_MATRIX_CONTROLLER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GreenhouseMatrixPortBE>> GREENHOUSE_MATRIX_PORT = BLOCK_ENTITIES.register(
            "greenhouse_matrix_port", () -> BlockEntityType.Builder.of(GreenhouseMatrixPortBE::new,
                    MatrixBlocks.GREENHOUSE_MATRIX_ITEM_INPUT.get(), MatrixBlocks.GREENHOUSE_MATRIX_ITEM_OUTPUT.get(),
                    MatrixBlocks.GREENHOUSE_MATRIX_FLUID_INPUT.get(), MatrixBlocks.GREENHOUSE_MATRIX_ENERGY_INPUT.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GreenhouseMatrixAutoCraftingBE>> GREENHOUSE_MATRIX_AUTO_CRAFTING = BLOCK_ENTITIES.register(
            "greenhouse_matrix_auto_crafting", () -> BlockEntityType.Builder.of(
                    GreenhouseMatrixAutoCraftingBE::new, MatrixBlocks.GREENHOUSE_MATRIX_AUTO_CRAFTING.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CreativeGreenhouseBE>> CREATIVE_GREENHOUSE = BLOCK_ENTITIES.register(
            "creative_greenhouse", () -> BlockEntityType.Builder.of(
                    CreativeGreenhouseBE::new, MatrixBlocks.CREATIVE_GREENHOUSE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SolarPanelBE>> SOLAR_PANEL = BLOCK_ENTITIES.register(
            "solar_panel", () -> BlockEntityType.Builder.of(SolarPanelBE::new,
                    MatrixBlocks.CONCENTRATED_SOLAR_PANEL.get(), MatrixBlocks.SINGULARITY_SOLAR_PANEL.get(),
                    MatrixBlocks.STELLAR_FUSION_SOLAR_PANEL.get(), MatrixBlocks.DIMENSIONAL_COLLAPSE_SOLAR_PANEL.get(),
                    MatrixBlocks.CREATIVE_SOLAR_PANEL.get()).build(null));
}
