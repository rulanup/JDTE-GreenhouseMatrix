package com.jdte.matrix.setup;

import com.jdte.matrix.JDTEMatrix;
import com.jdte.matrix.common.containers.GreenhouseMatrixContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MatrixMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, JDTEMatrix.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<GreenhouseMatrixContainer>> GREENHOUSE_MATRIX = MENUS.register(
            "greenhouse_matrix", () -> IMenuTypeExtension.create(GreenhouseMatrixContainer::new));
}
