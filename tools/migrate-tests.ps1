# Moves the Greenhouse Matrix unit tests from JDTE into the JDTE-GreenhouseMatrix mod.
$ErrorActionPreference = 'Stop'
$jdt = (Resolve-Path '..\JDTE').Path
$dst = (Resolve-Path '.').Path

function Rewrite-Test([string]$text) {
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
    $text = $text.Replace('package com.jdte.common.greenhouse;', 'package com.jdte.matrix.common.greenhouse;')
    $text = $text.Replace('package com.jdte.common.blockentities;', 'package com.jdte.matrix.common.blockentities;')
    $text = $text.Replace('package com.jdte.common.network;', 'package com.jdte.matrix.common.network;')
    $text = $text.Replace('package com.jdte.common.integrations.jade;', 'package com.jdte.matrix.common.integrations.jade;')
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
    # Jade provider test: JDTEJadePlugin -> JDTEMatrixJadePlugin
    $text = $text.Replace('com.jdte.common.integrations.jade.JDTEJadePlugin', 'com.jdte.matrix.common.integrations.jade.JDTEMatrixJadePlugin')
    $text = $text.Replace('JDTEJadePlugin.GreenhouseMatrixFluidStorageProvider', 'JDTEMatrixJadePlugin.GreenhouseMatrixFluidStorageProvider')
    # Resource namespace rewrites used by the AutoCraftingResourcesTest
    $text = $text.Replace('jdte:block/greenhouse_matrix', 'jdte_matrix:block/greenhouse_matrix')
    $text = $text.Replace('jdte:greenhouse_matrix', 'jdte_matrix:greenhouse_matrix')
    $text = $text.Replace('data/jdte/recipe/greenhouse_matrix', 'data/jdte_matrix/recipe/greenhouse_matrix')
    $text = $text.Replace('assets/jdte/blockstates/greenhouse_matrix', 'assets/jdte_matrix/blockstates/greenhouse_matrix')
    $text = $text.Replace('assets/jdte/models/block/greenhouse_matrix', 'assets/jdte_matrix/models/block/greenhouse_matrix')
    $text = $text.Replace('assets/jdte/lang/', 'assets/jdte_matrix/lang/')
    $text = $text.Replace('"jdte.screen.greenhouse_matrix.', '"jdte_matrix.screen.greenhouse_matrix.')
    for ($i = 0; $i -lt $protect.Count; $i++) {
        $text = $text.Replace("@@PROTECT$i@@", $protect[$i])
    }
    return $text
}

$tests = Get-ChildItem (Join-Path $jdt 'src\test') -Recurse -File | Where-Object { $_.Name -like '*Matrix*' }
foreach ($t in $tests) {
    $relative = $t.FullName.Substring((Join-Path $jdt 'src\test\java').Length + 1)
    $target = Join-Path $dst "src\test\java\$relative"
    New-Item -ItemType Directory -Force -Path (Split-Path $target) | Out-Null
    $text = [System.IO.File]::ReadAllText($t.FullName)
    $text = Rewrite-Test $text
    [System.IO.File]::WriteAllText($target, $text, [System.Text.UTF8Encoding]::new($false))
    Remove-Item $t.FullName -Force
    Write-Host "test: $relative"
}
Write-Host "moved $($tests.Count) tests"
