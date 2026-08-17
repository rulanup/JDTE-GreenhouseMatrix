package com.jdte.matrix;

import com.jdte.matrix.common.capabilities.MatrixCapabilities;
import com.jdte.matrix.common.network.MatrixPacketHandler;
import com.jdte.matrix.setup.MatrixBlockEntities;
import com.jdte.matrix.setup.MatrixBlocks;
import com.jdte.matrix.setup.MatrixConfig;
import com.jdte.matrix.setup.MatrixCreativeTabs;
import com.jdte.matrix.setup.MatrixItems;
import com.jdte.matrix.setup.MatrixMenus;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@Mod(JDTEMatrix.MODID)
public class JDTEMatrix {
    public static final String MODID = "jdte_matrix";

    public JDTEMatrix(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, MatrixConfig.COMMON_SPEC, JDTEMatrix.MODID + "/jdte_matrix.toml");
        MatrixBlocks.BLOCKS.register(modEventBus);
        MatrixItems.ITEMS.register(modEventBus);
        MatrixBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        MatrixMenus.MENUS.register(modEventBus);
        MatrixCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(MatrixPacketHandler::registerNetworking);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        MatrixCapabilities.register(event);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
