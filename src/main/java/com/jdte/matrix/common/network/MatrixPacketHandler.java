package com.jdte.matrix.common.network;

import com.jdte.matrix.JDTEMatrix;
import com.jdte.matrix.common.network.data.GreenhouseMatrixControlPayload;
import com.jdte.matrix.common.network.data.GreenhouseMatrixPatternPagePayload;
import com.jdte.matrix.common.network.handler.GreenhouseMatrixControlPacket;
import com.jdte.matrix.common.network.handler.GreenhouseMatrixPatternPagePacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class MatrixPacketHandler {
    public static void registerNetworking(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(JDTEMatrix.MODID);
        registrar.playToServer(GreenhouseMatrixControlPayload.TYPE, GreenhouseMatrixControlPayload.STREAM_CODEC,
                GreenhouseMatrixControlPacket::handle);
        registrar.playToServer(GreenhouseMatrixPatternPagePayload.TYPE,
                GreenhouseMatrixPatternPagePayload.STREAM_CODEC, GreenhouseMatrixPatternPagePacket::handle);
    }
}
