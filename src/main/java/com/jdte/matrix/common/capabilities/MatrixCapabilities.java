package com.jdte.matrix.common.capabilities;

import com.jdte.matrix.common.blockentities.GreenhouseMatrixControllerBE;
import com.jdte.matrix.common.blockentities.GreenhouseMatrixPortBE;
import com.jdte.matrix.common.blocks.GreenhouseMatrixPortBlock;
import com.jdte.matrix.common.greenhouse.GreenhouseMatrixPortType;
import com.jdte.matrix.setup.MatrixBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;

/** Registers the Greenhouse Matrix port capabilities that forward to their linked controller. */
public final class MatrixCapabilities {
    private MatrixCapabilities() {
    }

    private static final IBlockCapabilityProvider<IEnergyStorage, Direction> PORT_ENERGY =
            (level, pos, state, be, side) -> be instanceof GreenhouseMatrixPortBE port
                    && state.getBlock() instanceof GreenhouseMatrixPortBlock block
                    && block.portType() == GreenhouseMatrixPortType.ENERGY_INPUT && port.controller() != null
                    ? port.controller().getEnergyHandler() : null;
    private static final IBlockCapabilityProvider<IFluidHandler, Direction> PORT_FLUID =
            (level, pos, state, be, side) -> be instanceof GreenhouseMatrixPortBE port
                    && state.getBlock() instanceof GreenhouseMatrixPortBlock block
                    && block.portType() == GreenhouseMatrixPortType.FLUID_INPUT && port.controller() != null
                    ? port.controller().getFluidHandler() : null;
    private static final IBlockCapabilityProvider<IItemHandler, Direction> PORT_ITEMS =
            MatrixCapabilities::getPortItems;

    private static IItemHandler getPortItems(net.minecraft.world.level.Level level, BlockPos pos,
                                             net.minecraft.world.level.block.state.BlockState state,
                                             net.minecraft.world.level.block.entity.BlockEntity be,
                                             Direction side) {
        if (!(be instanceof GreenhouseMatrixPortBE port)
                || !(state.getBlock() instanceof GreenhouseMatrixPortBlock block)) return null;
        GreenhouseMatrixControllerBE controller = port.controller();
        return controller == null ? null
                : block.portType() == GreenhouseMatrixPortType.ITEM_INPUT ? controller.getInputHandler()
                : block.portType() == GreenhouseMatrixPortType.ITEM_OUTPUT ? controller.getOutputHandler() : null;
    }

    public static void register(RegisterCapabilitiesEvent event) {
        List<Block> itemPorts = List.of(
                MatrixBlocks.GREENHOUSE_MATRIX_ITEM_INPUT.get(),
                MatrixBlocks.GREENHOUSE_MATRIX_ITEM_OUTPUT.get());
        List<Block> fluidPorts = List.of(MatrixBlocks.GREENHOUSE_MATRIX_FLUID_INPUT.get());
        List<Block> energyPorts = List.of(MatrixBlocks.GREENHOUSE_MATRIX_ENERGY_INPUT.get());
        event.registerBlock(Capabilities.ItemHandler.BLOCK, PORT_ITEMS, itemPorts.toArray(Block[]::new));
        event.registerBlock(Capabilities.FluidHandler.BLOCK, PORT_FLUID, fluidPorts.toArray(Block[]::new));
        event.registerBlock(Capabilities.EnergyStorage.BLOCK, PORT_ENERGY, energyPorts.toArray(Block[]::new));
    }
}
