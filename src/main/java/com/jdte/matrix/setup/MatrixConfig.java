package com.jdte.matrix.setup;

import com.jdte.matrix.setup.config.SolarPanelConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MatrixConfig {
    public static final ModConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        Pair<Common, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON = pair.getLeft();
        COMMON_SPEC = pair.getRight();
    }

    public static class Common {
        public final SolarPanelConfig solarPanel;
        public final ModConfigSpec.IntValue greenhouseMatrixProfileScanBudget;
        public final ModConfigSpec.IntValue greenhouseMatrixDynamicSamplesPerGroup;
        public final ModConfigSpec.IntValue greenhouseMatrixAEOutputTypeBudget;

        public Common(ModConfigSpec.Builder builder) {
            builder.comment("Greenhouse Matrix Settings")
                    .translation("config.jdte_matrix.jdte_matrix.greenhouse")
                    .push("greenhouse");
            greenhouseMatrixProfileScanBudget = builder
                    .comment("Maximum matrix members whose production profiles are rebuilt per server tick")
                    .translation("config.jdte_matrix.jdte_matrix.greenhouse.matrixProfileScanBudget")
                    .defineInRange("matrixProfileScanBudget", 64, 1, 4096);
            greenhouseMatrixDynamicSamplesPerGroup = builder
                    .comment("Maximum real loot/dynamic harvest calls per matrix production group settlement")
                    .translation("config.jdte_matrix.jdte_matrix.greenhouse.matrixDynamicSamplesPerGroup")
                    .defineInRange("matrixDynamicSamplesPerGroup", 8, 1, 256);
            greenhouseMatrixAEOutputTypeBudget = builder
                    .comment("Maximum distinct matrix output types uploaded to AE per server tick")
                    .translation("config.jdte_matrix.jdte_matrix.greenhouse.matrixAEOutputTypeBudget")
                    .defineInRange("matrixAEOutputTypeBudget", 64, 1, 4096);
            builder.pop();

            solarPanel = new SolarPanelConfig(builder);
        }
    }
}
