package com.jdte.matrix.setup;

import com.jdte.matrix.JDTEMatrix;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MatrixCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JDTEMatrix.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(JDTEMatrix.MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.jdte_matrix"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> new ItemStack(MatrixItems.GREENHOUSE_MATRIX_CONTROLLER.get()))
            .displayItems((parameters, output) -> {
                // Controller and structure
                output.accept(MatrixItems.GREENHOUSE_MATRIX_CONTROLLER.get());
                output.accept(MatrixItems.GREENHOUSE_MATRIX_CASING.get());
                output.accept(MatrixItems.GREENHOUSE_MATRIX_ITEM_INPUT.get());
                output.accept(MatrixItems.GREENHOUSE_MATRIX_ITEM_OUTPUT.get());
                output.accept(MatrixItems.GREENHOUSE_MATRIX_FLUID_INPUT.get());
                output.accept(MatrixItems.GREENHOUSE_MATRIX_ENERGY_INPUT.get());

                // Enhancers
                output.accept(MatrixItems.GREENHOUSE_MATRIX_SPEED.get());
                output.accept(MatrixItems.GREENHOUSE_MATRIX_EFFICIENCY.get());
                output.accept(MatrixItems.GREENHOUSE_MATRIX_SEED.get());
                output.accept(MatrixItems.GREENHOUSE_MATRIX_ESSENCE.get());
                output.accept(MatrixItems.GREENHOUSE_MATRIX_AUTO_CRAFTING.get());

                // Upgrade item
                output.accept(MatrixItems.GREENHOUSE_MATRIX_QUICK_INSTALL_UPGRADE.get());

                // Tiered Solar Panels
                output.accept(MatrixItems.CONCENTRATED_SOLAR_PANEL.get());
                output.accept(MatrixItems.SINGULARITY_SOLAR_PANEL.get());
                output.accept(MatrixItems.STELLAR_FUSION_SOLAR_PANEL.get());
                output.accept(MatrixItems.DIMENSIONAL_COLLAPSE_SOLAR_PANEL.get());
                output.accept(MatrixItems.CREATIVE_SOLAR_PANEL.get());
            })
            .build());
}
