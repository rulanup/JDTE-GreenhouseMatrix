package com.jdte.matrix.client;

import com.jdte.matrix.JDTEMatrix;
import com.jdte.matrix.client.screens.GreenhouseMatrixScreen;
import com.jdte.matrix.setup.MatrixMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = JDTEMatrix.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class JDTEMatrixClientSetup {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MatrixMenus.GREENHOUSE_MATRIX.get(), GreenhouseMatrixScreen::new);
    }
}
