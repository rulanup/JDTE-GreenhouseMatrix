package com.jdte.matrix.common.blockentities;

import com.jdte.common.blockentities.CoalescedAcceleratedMachine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GreenhouseMatrixControllerAccelerationTest {
    @Test
    void controllerUsesCoalescedAccelerationPath() {
        assertTrue(CoalescedAcceleratedMachine.class.isAssignableFrom(GreenhouseMatrixControllerBE.class));
    }
}
