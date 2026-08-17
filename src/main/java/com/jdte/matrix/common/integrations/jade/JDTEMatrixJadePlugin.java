package com.jdte.matrix.common.integrations.jade;

import com.jdte.matrix.JDTEMatrix;
import com.jdte.matrix.common.blockentities.GreenhouseMatrixControllerBE;
import com.jdte.matrix.common.blockentities.GreenhouseMatrixPortBE;
import com.jdte.matrix.common.blockentities.SolarPanelBE;
import com.jdte.matrix.common.blocks.GreenhouseMatrixControllerBlock;
import com.jdte.matrix.common.blocks.GreenhouseMatrixPortBlock;
import com.jdte.matrix.common.blocks.SolarPanelBlock;
import com.jdte.matrix.common.greenhouse.GreenhouseMatrixPortType;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.FluidView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ViewGroup;

import java.util.List;
import java.util.Locale;

/** Jade integration for the Greenhouse Matrix: controller status and port storage views. */
@WailaPlugin(JDTEMatrix.MODID)
public class JDTEMatrixJadePlugin implements IWailaPlugin {
    private static final ResourceLocation STATUS_UID = JDTEMatrix.id("greenhouse_matrix_status");
    private static final ResourceLocation ITEM_STORAGE_UID = JDTEMatrix.id("greenhouse_matrix_item_storage");
    private static final ResourceLocation FLUID_STORAGE_UID = JDTEMatrix.id("greenhouse_matrix_fluid_storage");
    private static final ResourceLocation SOLAR_PANEL_STATUS_UID = JDTEMatrix.id("solar_panel_status");
    private static final String TAG_PORT_TYPE = "jdte_matrix_port_type";
    private static final String TAG_LINKED = "jdte_matrix_linked";
    private static final String TAG_FORMED = "jdte_matrix_formed";
    private static final String TAG_ENABLED = "jdte_matrix_enabled";
    private static final String TAG_RENDER = "jdte_matrix_render";
    private static final String TAG_AUTO_IO = "jdte_matrix_auto_io";
    private static final String TAG_GREENHOUSES = "jdte_matrix_greenhouses";
    private static final String TAG_SIZE_X = "jdte_matrix_size_x";
    private static final String TAG_SIZE_Y = "jdte_matrix_size_y";
    private static final String TAG_SIZE_Z = "jdte_matrix_size_z";
    private static final String TAG_SPEED = "jdte_matrix_speed";
    private static final String TAG_EFFICIENCY = "jdte_matrix_efficiency";
    private static final String TAG_SEED = "jdte_matrix_seed";
    private static final String TAG_ESSENCE = "jdte_matrix_essence";
    private static final String TAG_ERROR = "jdte_matrix_error";
    private static final String TAG_QUICK_INSTALL = "jdte_matrix_quick_install";
    private static final String TAG_QUEUED_UPGRADES = "jdte_matrix_queued_upgrades";
    private static final String TAG_GROUPS = "jdte_matrix_groups";
    private static final String TAG_REBUILDING = "jdte_matrix_rebuilding";
    private static final String TAG_BUFFER_TYPES = "jdte_matrix_buffer_types";
    private static final String TAG_BUFFER_ITEMS = "jdte_matrix_buffer_items";
    private static final String TAG_SOLAR_ACTIVE = "jdte_solar_active";
    private static final String TAG_SOLAR_CREATIVE = "jdte_solar_creative";
    private static final String TAG_SOLAR_BASE = "jdte_solar_base";
    private static final String TAG_SOLAR_GENERATION = "jdte_solar_generation";
    private static final String TAG_SOLAR_STORED = "jdte_solar_stored";
    private static final String TAG_SOLAR_CAPACITY = "jdte_solar_capacity";
    private static final GreenhouseMatrixStatusProvider STATUS_PROVIDER =
            new GreenhouseMatrixStatusProvider();
    private static final GreenhouseMatrixItemStorageProvider ITEM_STORAGE_PROVIDER =
            new GreenhouseMatrixItemStorageProvider();
    private static final GreenhouseMatrixFluidStorageProvider FLUID_STORAGE_PROVIDER =
            new GreenhouseMatrixFluidStorageProvider();
    private static final SolarPanelStatusProvider SOLAR_PANEL_STATUS_PROVIDER =
            new SolarPanelStatusProvider();

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(STATUS_PROVIDER, GreenhouseMatrixControllerBE.class);
        registration.registerBlockDataProvider(STATUS_PROVIDER, GreenhouseMatrixPortBE.class);
        registration.registerBlockDataProvider(SOLAR_PANEL_STATUS_PROVIDER, SolarPanelBE.class);
        registration.registerItemStorage(ITEM_STORAGE_PROVIDER, GreenhouseMatrixPortBE.class);
        registration.registerFluidStorage(FLUID_STORAGE_PROVIDER, GreenhouseMatrixPortBE.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(STATUS_PROVIDER, GreenhouseMatrixControllerBlock.class);
        registration.registerBlockComponent(STATUS_PROVIDER, GreenhouseMatrixPortBlock.class);
        registration.registerBlockComponent(SOLAR_PANEL_STATUS_PROVIDER, SolarPanelBlock.class);
        registration.registerFluidStorageClient(FLUID_STORAGE_PROVIDER);
    }

    /** Prevents Jade's generic item provider from expanding every internal greenhouse slot. */
    private static class GreenhouseMatrixItemStorageProvider implements IServerExtensionProvider<ItemStack> {
        @Override
        public List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
            return List.of();
        }

        @Override
        public ResourceLocation getUid() {
            return ITEM_STORAGE_UID;
        }
    }

    /** Publishes the fluid input port as the controller's single aggregate virtual tank. */
    static class GreenhouseMatrixFluidStorageProvider implements IServerExtensionProvider<CompoundTag>,
            IClientExtensionProvider<CompoundTag, FluidView> {
        @Override
        public List<ViewGroup<CompoundTag>> getGroups(Accessor<?> accessor) {
            if (!(accessor instanceof BlockAccessor blockAccessor)
                    || !(blockAccessor.getBlock() instanceof GreenhouseMatrixPortBlock portBlock)
                    || portBlock.portType() != GreenhouseMatrixPortType.FLUID_INPUT
                    || !(blockAccessor.getBlockEntity() instanceof GreenhouseMatrixPortBE port)) {
                return List.of();
            }
            GreenhouseMatrixControllerBE controller = port.controller();
            IFluidHandler handler = controller == null ? null : controller.getFluidHandler();
            if (handler == null || handler.getTanks() == 0) return List.of();
            FluidStack stored = handler.getFluidInTank(0);
            JadeFluidObject fluid = stored.isEmpty()
                    ? JadeFluidObject.empty()
                    : JadeFluidObject.of(stored.getFluid(), stored.getAmount(), stored.getComponentsPatch());
            CompoundTag view = FluidView.writeDefault(fluid, handler.getTankCapacity(0));
            return List.of(new ViewGroup<>(List.of(view)));
        }

        @Override
        public List<ClientViewGroup<FluidView>> getClientGroups(Accessor<?> accessor,
                                                                List<ViewGroup<CompoundTag>> groups) {
            return ClientViewGroup.map(groups, FluidView::readDefault, null);
        }

        @Override
        public ResourceLocation getUid() {
            return FLUID_STORAGE_UID;
        }
    }

    private static class GreenhouseMatrixStatusProvider
            implements IServerDataProvider<BlockAccessor>, IBlockComponentProvider {
        @Override
        public void appendServerData(CompoundTag data, BlockAccessor accessor) {
            int portType = accessor.getBlock() instanceof GreenhouseMatrixPortBlock port
                    ? port.portType().ordinal() : -1;
            data.putInt(TAG_PORT_TYPE, portType);

            GreenhouseMatrixControllerBE controller = accessor.getBlockEntity() instanceof GreenhouseMatrixControllerBE direct
                    ? direct
                    : accessor.getBlockEntity() instanceof GreenhouseMatrixPortBE port ? port.controller() : null;
            data.putBoolean(TAG_LINKED, controller != null);
            if (controller == null) return;

            var matrix = controller.getMatrixData();
            data.putBoolean(TAG_FORMED, controller.isFormed());
            data.putBoolean(TAG_ENABLED, controller.isEnabled());
            data.putBoolean(TAG_RENDER, controller.isRenderEnabled());
            data.putBoolean(TAG_AUTO_IO, controller.isAutoIoEnabled());
            data.putInt(TAG_GREENHOUSES, controller.getGreenhouseCount());
            data.putInt(TAG_SIZE_X, matrix.get(8));
            data.putInt(TAG_SIZE_Y, matrix.get(9));
            data.putInt(TAG_SIZE_Z, matrix.get(10));
            data.putInt(TAG_SPEED, matrix.get(4));
            data.putInt(TAG_EFFICIENCY, matrix.get(5));
            data.putInt(TAG_SEED, matrix.get(6));
            data.putInt(TAG_ESSENCE, matrix.get(7));
            data.putInt(TAG_ERROR, matrix.get(11));
            data.putBoolean(TAG_QUICK_INSTALL, controller.hasQuickInstallUpgrade());
            data.putInt(TAG_QUEUED_UPGRADES, controller.getQueuedUpgradeCount());
            data.putInt(TAG_GROUPS, controller.getSimulation().groupCount());
            data.putBoolean(TAG_REBUILDING, controller.getSimulation().rebuilding());
            data.putInt(TAG_BUFFER_TYPES, controller.getOutputBuffer().distinctTypes());
            data.putLong(TAG_BUFFER_ITEMS, controller.getOutputBuffer().totalCount());
        }

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            CompoundTag data = accessor.getServerData();
            int portOrdinal = data.getInt(TAG_PORT_TYPE);
            boolean port = portOrdinal >= 0;
            if (port) {
                GreenhouseMatrixPortType[] types = GreenhouseMatrixPortType.values();
                GreenhouseMatrixPortType type = types[Math.floorMod(portOrdinal, types.length)];
                tooltip.add(Component.translatable("jade.jdte_matrix.greenhouse_matrix.port",
                        Component.translatable("jade.jdte_matrix.greenhouse_matrix.port."
                                + type.name().toLowerCase(Locale.ROOT))).withStyle(ChatFormatting.GRAY));
                boolean linked = data.getBoolean(TAG_LINKED);
                tooltip.add(Component.translatable(linked
                                ? "jade.jdte_matrix.greenhouse_matrix.linked"
                                : "jade.jdte_matrix.greenhouse_matrix.unlinked")
                        .withStyle(linked ? ChatFormatting.GREEN : ChatFormatting.RED));
                if (!linked) return;
            }

            boolean formed = data.getBoolean(TAG_FORMED);
            if (formed) {
                tooltip.add(Component.translatable("jade.jdte_matrix.greenhouse_matrix.formed",
                                data.getInt(TAG_GREENHOUSES))
                        .withStyle(ChatFormatting.GREEN));
                tooltip.add(Component.translatable("jade.jdte_matrix.greenhouse_matrix.size",
                                data.getInt(TAG_SIZE_X), data.getInt(TAG_SIZE_Y),
                                data.getInt(TAG_SIZE_Z))
                        .withStyle(ChatFormatting.GRAY));
            } else {
                tooltip.add(Component.translatable("jade.jdte_matrix.greenhouse_matrix.invalid",
                                Component.translatable("jdte_matrix.screen.greenhouse_matrix.error."
                                        + data.getInt(TAG_ERROR)))
                        .withStyle(ChatFormatting.RED));
            }

            tooltip.add(Component.translatable(data.getBoolean(TAG_ENABLED)
                            ? "jade.jdte_matrix.greenhouse_matrix.operation.on"
                            : "jade.jdte_matrix.greenhouse_matrix.operation.off")
                    .withStyle(data.getBoolean(TAG_ENABLED) ? ChatFormatting.GREEN : ChatFormatting.RED));
            tooltip.add(Component.translatable(data.getBoolean(TAG_AUTO_IO)
                            ? "jade.jdte_matrix.greenhouse_matrix.auto_io.on"
                            : "jade.jdte_matrix.greenhouse_matrix.auto_io.off")
                    .withStyle(data.getBoolean(TAG_AUTO_IO) ? ChatFormatting.GREEN : ChatFormatting.GRAY));
            tooltip.add(Component.translatable("jade.jdte_matrix.greenhouse_matrix.simulation",
                            data.getInt(TAG_GROUPS), data.getInt(TAG_BUFFER_TYPES),
                            data.getLong(TAG_BUFFER_ITEMS))
                    .withStyle(data.getBoolean(TAG_REBUILDING)
                            ? ChatFormatting.YELLOW : ChatFormatting.GRAY));

            if (!port) {
                tooltip.add(Component.translatable(data.getBoolean(TAG_RENDER)
                                ? "jade.jdte_matrix.greenhouse_matrix.render.on"
                                : "jade.jdte_matrix.greenhouse_matrix.render.off")
                        .withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.translatable("jade.jdte_matrix.greenhouse_matrix.enhancements",
                                data.getInt(TAG_SPEED), data.getInt(TAG_EFFICIENCY),
                                data.getInt(TAG_SEED), data.getInt(TAG_ESSENCE))
                        .withStyle(ChatFormatting.GRAY));
                boolean quickInstall = data.getBoolean(TAG_QUICK_INSTALL);
                tooltip.add(Component.translatable(quickInstall
                                ? "jade.jdte_matrix.greenhouse_matrix.quick_install.on"
                                : "jade.jdte_matrix.greenhouse_matrix.quick_install.off",
                                data.getInt(TAG_QUEUED_UPGRADES))
                        .withStyle(quickInstall ? ChatFormatting.GREEN : ChatFormatting.GRAY));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return STATUS_UID;
        }
    }

    /** Reports day/sky generation, stored FE, and inactivity reasons for Tiered Solar Panels. */
    private static class SolarPanelStatusProvider
            implements IServerDataProvider<BlockAccessor>, IBlockComponentProvider {
        @Override
        public void appendServerData(CompoundTag data, BlockAccessor accessor) {
            if (!(accessor.getBlockEntity() instanceof SolarPanelBE panel)) return;
            boolean creative = panel.tier().creative();
            data.putBoolean(TAG_SOLAR_ACTIVE, panel.canGenerate());
            data.putBoolean(TAG_SOLAR_CREATIVE, creative);
            data.putLong(TAG_SOLAR_BASE, creative ? Long.MAX_VALUE : panel.baseGeneration());
            data.putLong(TAG_SOLAR_GENERATION, creative ? Long.MAX_VALUE : panel.currentGeneration());
            data.putInt(TAG_SOLAR_STORED, panel.getEnergyStorage().getEnergyStored());
            data.putInt(TAG_SOLAR_CAPACITY, panel.getEnergyStorage().getMaxEnergyStored());
        }

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            CompoundTag data = accessor.getServerData();
            if (data.getBoolean(TAG_SOLAR_CREATIVE)) {
                tooltip.add(Component.translatable("jade.jdte_matrix.solar_panel.infinite")
                        .withStyle(ChatFormatting.LIGHT_PURPLE));
                return;
            }
            tooltip.add(Component.translatable("jade.jdte_matrix.solar_panel.base_generation",
                    data.getLong(TAG_SOLAR_BASE)).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("jade.jdte_matrix.solar_panel.generation",
                    data.getLong(TAG_SOLAR_GENERATION)).withStyle(
                    data.getBoolean(TAG_SOLAR_ACTIVE) ? ChatFormatting.GREEN : ChatFormatting.RED));
            tooltip.add(Component.translatable("jade.jdte_matrix.solar_panel.storage",
                    data.getInt(TAG_SOLAR_STORED), data.getInt(TAG_SOLAR_CAPACITY))
                    .withStyle(ChatFormatting.GRAY));
            if (!data.getBoolean(TAG_SOLAR_ACTIVE)) {
                tooltip.add(Component.translatable("jade.jdte_matrix.solar_panel.inactive",
                        Component.translatable("jade.jdte_matrix.solar_panel.reason.no_sun")).withStyle(ChatFormatting.RED));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return SOLAR_PANEL_STATUS_UID;
        }
    }
}
