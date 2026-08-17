package com.jdte.matrix.common.blockentities;

import com.jdte.matrix.setup.MatrixConfig;
import com.jdte.common.integrations.ae2.AEOutputNetwork;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

/**
 * AE2 upload path for the Greenhouse Matrix central output buffer.
 *
 * <p>Split out of JDTE's {@code AEOutputManager} when the matrix became its own mod;
 * the controller-independent machine upload logic remains in JDTE.</p>
 */
public final class MatrixAEOutputTransfer {
    private MatrixAEOutputTransfer() {
    }

    public static void tickMatrix(GreenhouseMatrixControllerBE controller) {
        if (!(controller.getLevel() instanceof ServerLevel level) || !controller.isFormed()) return;
        ItemStack upgrade = controller.getAEOutputUpgrade();
        if (upgrade.isEmpty() || !AEOutputNetwork.isLinked(upgrade)) return;
        MatrixState state = controller.getAEOutputState();
        if (level.getGameTime() < state.nextAttemptTick) return;

        var buffer = controller.getOutputBuffer();
        long moved = 0L;
        int entry = 0;
        int typeBudget = MatrixConfig.COMMON.greenhouseMatrixAEOutputTypeBudget.get();
        while (entry < buffer.distinctTypes() && typeBudget-- > 0) {
            ItemStack prototype = buffer.prototypeAt(entry);
            long remaining = buffer.amountAt(entry);
            boolean removedEntry = false;
            while (!prototype.isEmpty() && remaining > 0L) {
                int offered = (int) Math.min(Integer.MAX_VALUE, remaining);
                int simulated = AEOutputNetwork.insertItem(level, upgrade,
                        prototype.copyWithCount(offered), true);
                if (simulated <= 0) break;
                int accepted = AEOutputNetwork.insertItem(level, upgrade,
                        prototype.copyWithCount(Math.min(offered, simulated)), false);
                if (accepted <= 0) break;
                long removed = buffer.removeAmount(entry, accepted);
                if (removed != accepted) throw new IllegalStateException("Matrix output changed during AE transfer");
                moved += removed;
                remaining -= removed;
                removedEntry = remaining == 0L;
                if (accepted < offered) break;
            }
            if (!removedEntry) entry++;
        }
        if (moved > 0L) {
            state.failureBackoff = 0;
            state.nextAttemptTick = level.getGameTime() + 1L;
            controller.setChanged();
        } else {
            state.failureBackoff = state.failureBackoff <= 0 ? 5 : Math.min(MAX_BACKOFF, state.failureBackoff * 2);
            state.nextAttemptTick = level.getGameTime() + state.failureBackoff;
        }
    }

    private static final int MAX_BACKOFF = 20;

    /** Per-controller backoff state for AE upload attempts. */
    public static final class MatrixState {
        int failureBackoff;
        long nextAttemptTick;
    }
}
