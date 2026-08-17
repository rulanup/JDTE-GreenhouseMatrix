# Migrates the Tiered Solar Panels from JDTE into the JDTE-GreenhouseMatrix mod.
$ErrorActionPreference = 'Stop'
$jdt = (Resolve-Path '..\JDTE').Path
$dst = (Resolve-Path '.').Path
$utf8 = [System.Text.UTF8Encoding]::new($false)

function Rewrite-Java([string]$text) {
    # Solar panels are standalone: every JDTE reference moves except none.
    $text = $text.Replace('package com.jdte.common.solar;', 'package com.jdte.matrix.common.solar;')
    $text = $text.Replace('package com.jdte.common.blocks;', 'package com.jdte.matrix.common.blocks;')
    $text = $text.Replace('package com.jdte.common.blockentities;', 'package com.jdte.matrix.common.blockentities;')
    $text = $text.Replace('com.jdte.common.solar.', 'com.jdte.matrix.common.solar.')
    $text = $text.Replace('com.jdte.common.blocks.', 'com.jdte.matrix.common.blocks.')
    $text = $text.Replace('com.jdte.common.blockentities.', 'com.jdte.matrix.common.blockentities.')
    $text = $text.Replace('com.jdte.setup.JDTEBlockEntities', 'com.jdte.matrix.setup.MatrixBlockEntities')
    $text = $text.Replace('JDTEBlockEntities.SOLAR_PANEL', 'MatrixBlockEntities.SOLAR_PANEL')
    $text = $text.Replace('com.jdte.setup.JDTEConfig', 'com.jdte.matrix.setup.MatrixConfig')
    $text = $text.Replace('JDTEConfig.COMMON.solarPanel.', 'MatrixConfig.COMMON.solarPanel.')
    return $text
}

function Copy-Java($fileName, $packageSuffix, $base = 'common') {
    $src = Join-Path $jdt "src\main\java\com\jdte\$base\$packageSuffix\$fileName"
    $targetDir = Join-Path $dst "src\main\java\com\jdte\matrix\$base\$packageSuffix"
    New-Item -ItemType Directory -Force -Path $targetDir | Out-Null
    $text = [System.IO.File]::ReadAllText($src)
    $text = Rewrite-Java $text
    [System.IO.File]::WriteAllText((Join-Path $targetDir $fileName), $text, $utf8)
    Write-Host "java: $packageSuffix/$fileName"
}

$javaFiles = @(
    'SolarPanelBlock.java', 'blocks', 'common',
    'SolarPanelBE.java', 'blockentities', 'common',
    'SolarEnergyTransfer.java', 'solar', 'common',
    'SolarGenerationPolicy.java', 'solar', 'common',
    'SolarPanelEnergyExportCapability.java', 'solar', 'common',
    'SolarPanelEnergyStorage.java', 'solar', 'common',
    'SolarPanelTier.java', 'solar', 'common'
)
for ($i = 0; $i -lt $javaFiles.Count; $i += 3) {
    Copy-Java $javaFiles[$i] $javaFiles[$i + 1] $javaFiles[$i + 2]
}

# Tests
function Copy-Test($fileName, $packageSuffix) {
    $src = Join-Path $jdt "src\test\java\com\jdte\common\$packageSuffix\$fileName"
    $targetDir = Join-Path $dst "src\test\java\com\jdte\matrix\common\$packageSuffix"
    New-Item -ItemType Directory -Force -Path $targetDir | Out-Null
    $text = [System.IO.File]::ReadAllText($src)
    $text = $text.Replace('package com.jdte.common.solar;', 'package com.jdte.matrix.common.solar;')
    $text = $text.Replace('com.jdte.common.solar.', 'com.jdte.matrix.common.solar.')
    $text = $text.Replace('com.jdte.common.blocks.', 'com.jdte.matrix.common.blocks.')
    $text = $text.Replace('com.jdte.common.blockentities.', 'com.jdte.matrix.common.blockentities.')
    $text = $text.Replace('com.jdte.setup.JDTEBlocks', 'com.jdte.matrix.setup.MatrixBlocks')
    $text = $text.Replace('JDTEBlocks.CONCENTRATED_SOLAR_PANEL', 'MatrixBlocks.CONCENTRATED_SOLAR_PANEL')
    $text = $text.Replace('data/jdte/', 'data/jdte_matrix/')
    $text = $text.Replace('assets/jdte/', 'assets/jdte_matrix/')
    [System.IO.File]::WriteAllText((Join-Path $targetDir $fileName), $text, $utf8)
    Write-Host "test: $packageSuffix/$fileName"
}
$tests = @(
    'SolarEnergyTransferTest.java', 'solar',
    'SolarGenerationPolicyTest.java', 'solar',
    'SolarPanelBlockTest.java', 'solar',
    'SolarPanelEnergyExportCapabilityTest.java', 'solar',
    'SolarPanelEnergyStorageTest.java', 'solar',
    'SolarPanelOutputPolicyTest.java', 'solar',
    'SolarPanelResourceContractTest.java', 'solar'
)
for ($i = 0; $i -lt $tests.Count; $i += 2) {
    Copy-Test $tests[$i] $tests[$i + 1]
}

# Resources
function Copy-Resource($relative) {
    $src = Join-Path $jdt "src\main\resources\$relative"
    $target = $relative.Replace('assets\jdte\', 'assets\jdte_matrix\').Replace('data\jdte\', 'data\jdte_matrix\')
    $target = $target.Replace('guides\jdte\guide', 'guides\jdte_matrix\guide')
    $targetPath = Join-Path $dst "src\main\resources\$target"
    New-Item -ItemType Directory -Force -Path (Split-Path $targetPath) | Out-Null
    $text = [System.IO.File]::ReadAllText($src)
    $text = $text.Replace('jdte:solar', 'jdte_matrix:solar')
    $text = $text.Replace('jdte:block/solar', 'jdte_matrix:block/solar')
    $text = $text.Replace('jdte:item/solar', 'jdte_matrix:item/solar')
    [System.IO.File]::WriteAllText($targetPath, $text, $utf8)
    Write-Host "resource: $target"
}
$resources = Get-ChildItem (Join-Path $jdt 'src\main\resources') -Recurse -File | Where-Object {
    $_.FullName -like '*solar_panel*' -or $_.FullName -like '*solar-panels*'
}
foreach ($res in $resources) {
    $relative = $res.FullName.Substring((Join-Path $jdt 'src\main\resources').Length + 1)
    if ($relative -like 'assets\jdte\lang\*') {
        Write-Host "lang handled separately: $relative"
    } else {
        Copy-Resource $relative
    }
}
Write-Host 'Solar migration done.'
