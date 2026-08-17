# Migration of the Greenhouse Matrix from JDTE into the JDTE-GreenhouseMatrix mod.
# Copies moved Java files and resources and rewrites packages / namespaces.
# Usage: pwsh -File tools/migrate.ps1
$ErrorActionPreference = 'Stop'

$jdt = (Resolve-Path '..\JDTE').Path
$dst = (Resolve-Path '.').Path

function Rewrite-Text([string]$text) {
    # ---- 1. Protect classes that STAY in JDTE (no rewrite) ----
    $protect = @(
        'com.jdte.common.greenhouse.GreenhouseMatrixMember',
        'com.jdte.common.greenhouse.GreenhouseMatrixMemberState',
        'com.jdte.common.greenhouse.GreenhouseMatrixProductionProfile',
        'com.jdte.common.greenhouse.GreenhouseMatrixRuntime',
        'com.jdte.common.greenhouse.GreenhouseMatrixRenderRegistry',
        'com.jdte.common.greenhouse.GreenhouseFluidPolicy',
        'com.jdte.common.blockentities.GreenhouseBE',
        'com.jdte.common.blockentities.LargeGreenhouseBE',
        'com.jdte.common.blockentities.GreenhouseEssenceConversionHelper',
        'com.jdte.common.blockentities.GreenhouseProductionEngine',
        'com.jdte.common.blockentities.FixedSizeItemStackHandlerSerialization',
        'com.jdte.common.blockentities.CoalescedAcceleratedMachine',
        'com.jdte.common.items.UpgradeCardItem',
        'com.jdte.common.upgrades.',
        'com.jdte.common.recipes.',
        'com.jdte.common.autoioconfig.',
        'com.jdte.setup.JDTEBlocks',
        'com.jdte.setup.JDTEConfig'
    )
    for ($i = 0; $i -lt $protect.Count; $i++) {
        $text = $text.Replace($protect[$i], "@@PROTECT$i@@")
    }

    # ---- 2. Package declarations ----
    $text = $text.Replace('package com.jdte.common.blocks;', 'package com.jdte.matrix.common.blocks;')
    $text = $text.Replace('package com.jdte.common.blockentities;', 'package com.jdte.matrix.common.blockentities;')
    $text = $text.Replace('package com.jdte.common.containers;', 'package com.jdte.matrix.common.containers;')
    $text = $text.Replace('package com.jdte.common.greenhouse;', 'package com.jdte.matrix.common.greenhouse;')
    $text = $text.Replace('package com.jdte.common.items;', 'package com.jdte.matrix.common.items;')
    $text = $text.Replace('package com.jdte.common.network.data;', 'package com.jdte.matrix.common.network.data;')
    $text = $text.Replace('package com.jdte.common.network.handler;', 'package com.jdte.matrix.common.network.handler;')
    $text = $text.Replace('package com.jdte.client.screens;', 'package com.jdte.matrix.client.screens;')
    $text = $text.Replace('package com.jdte.common.integrations.ae2;', 'package com.jdte.matrix.common.integrations.ae2;')

    # ---- 3. Package prefix rewrites (imports / inline FQNs) ----
    $prefixes = @(
        'com.jdte.common.blocks.',
        'com.jdte.common.blockentities.',
        'com.jdte.common.containers.',
        'com.jdte.common.greenhouse.',
        'com.jdte.common.items.',
        'com.jdte.common.network.',
        'com.jdte.common.integrations.ae2.'
    )
    foreach ($p in $prefixes) {
        $text = $text.Replace($p, 'com.jdte.matrix.common.' + $p.Substring('com.jdte.common.'.Length))
    }
    $text = $text.Replace('com.jdte.client.screens.', 'com.jdte.matrix.client.screens.')

    # ---- 4. Setup class renames (imports and usages) ----
    $text = $text.Replace('com.jdte.setup.JDTEBlockEntities', 'com.jdte.matrix.setup.MatrixBlockEntities')
    $text = $text.Replace('com.jdte.setup.JDTEItems', 'com.jdte.matrix.setup.MatrixItems')
    $text = $text.Replace('com.jdte.setup.JDTEMenus', 'com.jdte.matrix.setup.MatrixMenus')
    $text = $text.Replace('JDTEBlocks.', 'MatrixBlocks.')
    $text = $text.Replace('JDTEItems.', 'MatrixItems.')
    $text = $text.Replace('JDTEBlockEntities.', 'MatrixBlockEntities.')
    $text = $text.Replace('JDTEMenus.', 'MatrixMenus.')
    $text = $text.Replace('com.jdte.JDTE', 'com.jdte.matrix.JDTEMatrix')
    $text = $text.Replace('JDTE.', 'JDTEMatrix.')

    # ---- 5. Matrix config values -> new mod config ----
    $text = $text.Replace('JDTEConfig.COMMON.greenhouseMatrixProfileScanBudget', 'MatrixConfig.COMMON.greenhouseMatrixProfileScanBudget')
    $text = $text.Replace('JDTEConfig.COMMON.greenhouseMatrixDynamicSamplesPerGroup', 'MatrixConfig.COMMON.greenhouseMatrixDynamicSamplesPerGroup')
    $text = $text.Replace('JDTEConfig.COMMON.greenhouseMatrixAEOutputTypeBudget', 'MatrixConfig.COMMON.greenhouseMatrixAEOutputTypeBudget')

    # ---- 6. AE output transfer ----
    $text = $text.Replace('AEOutputManager.tickMatrix(this)', 'MatrixAEOutputTransfer.tickMatrix(this)')
    $text = $text.Replace('AEOutputManager.MatrixState', 'MatrixAEOutputTransfer.MatrixState')

    # ---- 7. Lang key namespace ----
    $text = $text.Replace('"jdte.screen.greenhouse_matrix.', '"jdte_matrix.screen.greenhouse_matrix.')
    $text = $text.Replace('"block.jdte.greenhouse_matrix_', '"block.jdte_matrix.greenhouse_matrix_')
    $text = $text.Replace('"item.jdte.greenhouse_matrix_', '"item.jdte_matrix.greenhouse_matrix_')
    $text = $text.Replace('"tooltip.jdte.greenhouse_matrix_', '"tooltip.jdte_matrix.greenhouse_matrix_')
    $text = $text.Replace('"jade.jdte.greenhouse_matrix.', '"jade.jdte_matrix.greenhouse_matrix.')

    # ---- 8. Restore protected classes ----
    for ($i = 0; $i -lt $protect.Count; $i++) {
        $text = $text.Replace("@@PROTECT$i@@", $protect[$i])
    }
    return $text
}

function Copy-Java($fileName, $packageSuffix, $base = 'common') {
    $src = Join-Path $jdt "src\main\java\com\jdte\$base\$packageSuffix\$fileName"
    $targetDir = Join-Path $dst "src\main\java\com\jdte\matrix\$base\$packageSuffix"
    New-Item -ItemType Directory -Force -Path $targetDir | Out-Null
    $text = [System.IO.File]::ReadAllText($src)
    $text = Rewrite-Text $text
    $target = Join-Path $targetDir $fileName
    [System.IO.File]::WriteAllText($target, $text, [System.Text.UTF8Encoding]::new($false))
    Write-Host "java: $packageSuffix/$fileName"
}

function Copy-Resource($relative, [string]$rewriteKind) {
    $src = Join-Path $jdt "src\main\resources\$relative"
    if (-not (Test-Path $src)) { throw "missing source resource: $relative" }
    # map assets/jdte -> assets/jdte_matrix and data/jdte -> data/jdte_matrix, guides/jdte/guide -> guides/jdte_matrix/guide
    $target = $relative.Replace('assets\jdte\', 'assets\jdte_matrix\').Replace('data\jdte\', 'data\jdte_matrix\')
    $target = $target.Replace('guides\jdte\guide', 'guides\jdte_matrix\guide')
    $targetPath = Join-Path $dst "src\main\resources\$target"
    New-Item -ItemType Directory -Force -Path (Split-Path $targetPath) | Out-Null
    if ($rewriteKind -eq 'binary') {
        Copy-Item $src $targetPath -Force
        Write-Host "resource(bin): $target"
        return
    }
    $text = [System.IO.File]::ReadAllText($src)
    if ($rewriteKind -eq 'json' -or $rewriteKind -eq 'md') {
        # Namespace rewrite: matrix items/models only. Non-matrix jdte: references (ingredients like
        # jdte:greenhouse, jdte:overclock_upgrade, justdirethings:..., minecraft:...) stay untouched.
        $text = $text.Replace('jdte:greenhouse_matrix', 'jdte_matrix:greenhouse_matrix')
        $text = $text.Replace('jdte:block/greenhouse_matrix', 'jdte_matrix:block/greenhouse_matrix')
        $text = $text.Replace('jdte:item/greenhouse_matrix', 'jdte_matrix:item/greenhouse_matrix')
        $text = $text.Replace('jdte:data/greenhouse_matrix', 'jdte_matrix:data/greenhouse_matrix')
    }
    if ($rewriteKind -eq 'lang-zh') {
        $text = $text.Replace('"config.jdte.jdte.greenhouse.', '"config.jdte_matrix.jdte_matrix.greenhouse.')
        $text = $text.Replace('"config.jade.plugin_jdte.', '"config.jade.plugin_jdte_matrix.')
        $text = $text.Replace('"jade.jdte.greenhouse_matrix.', '"jade.jdte_matrix.greenhouse_matrix.')
        $text = $text.Replace('"jdte.screen.greenhouse_matrix.', '"jdte_matrix.screen.greenhouse_matrix.')
        $text = $text.Replace('"block.jdte.greenhouse_matrix_', '"block.jdte_matrix.greenhouse_matrix_')
        $text = $text.Replace('"item.jdte.greenhouse_matrix_', '"item.jdte_matrix.greenhouse_matrix_')
        $text = $text.Replace('"tooltip.jdte.greenhouse_matrix_', '"tooltip.jdte_matrix.greenhouse_matrix_')
        $text = $text.Replace('"item.jdte.guide', '"item.jdte_matrix.guide')
    }
    [System.IO.File]::WriteAllText($targetPath, $text, [System.Text.UTF8Encoding]::new($false))
    Write-Host "resource: $target"
}

# ---------------- Java files ----------------
# Each entry: file, packageSuffix, base ('common' unless noted)
$javaFiles = @(
    'GreenhouseMatrixAutoCraftingBlock.java', 'blocks', 'common',
    'GreenhouseMatrixCasingBlock.java', 'blocks', 'common',
    'GreenhouseMatrixControllerBlock.java', 'blocks', 'common',
    'GreenhouseMatrixEnhancementBlock.java', 'blocks', 'common',
    'GreenhouseMatrixPortBlock.java', 'blocks', 'common',
    'GreenhouseMatrixStructure.java', 'blocks', 'common',
    'GreenhouseMatrixAutoCraftingBE.java', 'blockentities', 'common',
    'GreenhouseMatrixAutoIo.java', 'blockentities', 'common',
    'GreenhouseMatrixCapabilitySnapshot.java', 'blockentities', 'common',
    'GreenhouseMatrixControllerBE.java', 'blockentities', 'common',
    'GreenhouseMatrixFluidBudget.java', 'blockentities', 'common',
    'GreenhouseMatrixPatternItemHandler.java', 'blockentities', 'common',
    'GreenhouseMatrixPortBE.java', 'blockentities', 'common',
    'GreenhouseMatrixContainer.java', 'containers', 'common',
    'GreenhouseMatrixAccelerationClock.java', 'greenhouse', 'common',
    'GreenhouseMatrixAutoCraftingCatalog.java', 'greenhouse', 'common',
    'GreenhouseMatrixAutoCraftingProcessor.java', 'greenhouse', 'common',
    'GreenhouseMatrixCraftingPlanner.java', 'greenhouse', 'common',
    'GreenhouseMatrixCraftingRecipe.java', 'greenhouse', 'common',
    'GreenhouseMatrixDropGenerator.java', 'greenhouse', 'common',
    'GreenhouseMatrixEnhancement.java', 'greenhouse', 'common',
    'GreenhouseMatrixMemberLookup.java', 'greenhouse', 'common',
    'GreenhouseMatrixOutputBuffer.java', 'greenhouse', 'common',
    'GreenhouseMatrixPatternRecipeValidator.java', 'greenhouse', 'common',
    'GreenhouseMatrixPatternSupport.java', 'greenhouse', 'common',
    'GreenhouseMatrixPortType.java', 'greenhouse', 'common',
    'GreenhouseMatrixProductionGroup.java', 'greenhouse', 'common',
    'GreenhouseMatrixSimulation.java', 'greenhouse', 'common',
    'GreenhouseMatrixAE2PatternDecoder.java', 'integrations\ae2', 'common',
    'GreenhouseMatrixQuickInstallUpgradeItem.java', 'items', 'common',
    'GreenhouseMatrixControlPayload.java', 'network\data', 'common',
    'GreenhouseMatrixPatternPagePayload.java', 'network\data', 'common',
    'GreenhouseMatrixControlPacket.java', 'network\handler', 'common',
    'GreenhouseMatrixPatternPagePacket.java', 'network\handler', 'common',
    'GreenhouseMatrixPatternPageRequestValidator.java', 'network\handler', 'common',
    'GreenhouseMatrixScreen.java', 'screens', 'client'
)
for ($i = 0; $i -lt $javaFiles.Count; $i += 3) {
    Copy-Java $javaFiles[$i] $javaFiles[$i + 1] $javaFiles[$i + 2]
}

# ---------------- Resources ----------------
$resources = Get-ChildItem (Join-Path $jdt 'src\main\resources') -Recurse -File | Where-Object {
    $_.FullName -like '*greenhouse_matrix*' -or $_.Name -like 'greenhouse-matrix*'
}
foreach ($res in $resources) {
    $relative = $res.FullName.Substring((Join-Path $jdt 'src\main\resources').Length + 1)
    if ($relative -like 'assets\jdte\textures\*') {
        Copy-Resource $relative 'binary'
    } elseif ($relative -like 'assets\jdte\lang\*') {
        # lang files are built separately (extract matrix keys)
        Write-Host "lang (handled separately): $relative"
    } elseif ($relative -like '*.json') {
        Copy-Resource $relative 'json'
    } elseif ($relative -like '*.md') {
        Copy-Resource $relative 'md'
    } else {
        Copy-Resource $relative 'plain'
    }
}
Write-Host 'Migration done.'
