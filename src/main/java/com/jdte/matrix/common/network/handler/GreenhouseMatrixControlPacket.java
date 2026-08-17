package com.jdte.matrix.common.network.handler;

import com.jdte.matrix.common.blockentities.GreenhouseMatrixControllerBE;
import com.jdte.matrix.common.containers.GreenhouseMatrixContainer;
import com.jdte.matrix.common.network.data.GreenhouseMatrixControlPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class GreenhouseMatrixControlPacket {
    private GreenhouseMatrixControlPacket() {}

    public static void handle(GreenhouseMatrixControlPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player().containerMenu instanceof GreenhouseMatrixContainer menu)
                    || !menu.getPos().equals(payload.blockPos())
                    || !(context.player().level().getBlockEntity(payload.blockPos()) instanceof GreenhouseMatrixControllerBE controller)) return;
            if (payload.action() == 0) controller.setEnabled(payload.value());
            else if (payload.action() == 1) controller.setRenderEnabled(payload.value());
            else if (payload.action() == 2) controller.setAutoIoEnabled(payload.value());
        });
    }
}
