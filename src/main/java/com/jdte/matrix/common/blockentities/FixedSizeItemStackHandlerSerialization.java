package com.jdte.matrix.common.blockentities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.items.ItemStackHandler;

/** Migrated from JDTE with the Greenhouse Matrix controller. */
final class FixedSizeItemStackHandlerSerialization {
    private FixedSizeItemStackHandlerSerialization() {
    }

    static void deserialize(ItemStackHandler handler, HolderLookup.Provider provider,
                            CompoundTag tag, int expectedSlots) {
        CompoundTag normalized = tag.copy();
        normalized.putInt("Size", expectedSlots);
        handler.deserializeNBT(provider, normalized);
    }
}
