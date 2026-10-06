param(
    [ValidateSet('build', 'test', 'run', 'screenshots')]
    [string]$Task = 'build'
)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    if (-not (Get-Command javac -ErrorAction SilentlyContinue)) {
        throw 'Instale um JDK 8 ou superior e adicione sua pasta bin ao PATH.'
    }
    New-Item -ItemType Directory -Force build/classes, build/test | Out-Null
    $sources = @(Get-ChildItem src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
    & javac -encoding UTF-8 -source 8 -target 8 -d build/classes @sources
    if ($LASTEXITCODE -ne 0) { throw 'Falha na compilação.' }
    if ($Task -eq 'test') {
        $tests = @(Get-ChildItem test -Recurse -Filter *.java | ForEach-Object { $_.FullName })
        & javac -encoding UTF-8 -source 8 -target 8 -cp build/classes -d build/test @tests
        if ($LASTEXITCODE -ne 0) { throw 'Falha na compilação dos testes.' }
        & java -cp 'build/classes;build/test' calculadora.CalculadoraTest
    } elseif ($Task -eq 'run') {
        & java -cp build/classes calculadora.App
    } elseif ($Task -eq 'screenshots') {
        & javac -encoding UTF-8 -source 8 -target 8 -cp build/classes -d build/test tools/Capturas.java
        if ($LASTEXITCODE -ne 0) { throw 'Falha na compilação das capturas.' }
        & java -Djava.awt.headless=true -cp 'build/classes;build/test' Capturas
    } else {
        New-Item -ItemType Directory -Force dist | Out-Null
        & jar cfe dist/calculadora.jar calculadora.App -C build/classes .
    }
    if ($LASTEXITCODE -ne 0) { throw "Falha na tarefa $Task." }
} finally { Pop-Location }
