# Définition des chemins
$SRC_DIR = "src/main/java"
$BIN_DIR = "target"
$MAIN_CLASS = "ui.POOphonia"
$JAR_NAME = "POOphonia.jar"

# Compilation
if (!(Test-Path $BIN_DIR)) { New-Item -ItemType Directory -Path $BIN_DIR }
Write-Host "Compilation en cours..."
javac -d $BIN_DIR (Get-ChildItem -Path $SRC_DIR -Recurse -Filter "*.java").FullName

# Exécution
Write-Host "Exécution de l'application..."
java -cp $BIN_DIR $MAIN_CLASS
