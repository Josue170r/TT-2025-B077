# Script de Deploy a Azure Container Registry

# Colores para output
$Green = "`e[32m"
$Red = "`e[31m"
$Yellow = "`e[33m"
$Reset = "`e[0m"

# Configuracion
$REGISTRY = "huellasormexico"
$REGISTRY_URL = "huellasormexico.azurecr.io"
$SPRING_DOCKERFILE = "compose\production\spring\Dockerfile"
$FRONT_DOCKERFILE = "compose\production\front\Dockerfile"
$SPRING_IMAGE_NAME = "huellas-backend"
$FRONT_IMAGE_NAME = "huellas-frontend"

# Funcion para obtener timestamp unico como version
function Get-Version {
    return (Get-Date -Format "yyyyMMdd.HHmmss")
}

# Funcion para imprimir con color
function Write-ColorOutput {
    param($Message, $Color)
    Write-Host "$Color$Message$Reset"
}

Write-ColorOutput "========================================" $Yellow
Write-ColorOutput "SCRIPT DE DEPLOY A AZURE" $Yellow
Write-ColorOutput "========================================" $Yellow

# Paso 1: Cambiar a rama master y hacer pull
Write-ColorOutput "`n[1/6] Cambiando a rama master y haciendo pull..." $Green
try {
    git checkout master
    if ($LASTEXITCODE -ne 0) {
        Write-ColorOutput "Error: No se pudo cambiar a rama master" $Red
        exit 1
    }
    
    git pull origin master
    if ($LASTEXITCODE -ne 0) {
        Write-ColorOutput "Error: No se pudo hacer pull de master" $Red
        exit 1
    }
    Write-ColorOutput "OK Git pull completado" $Green
}
catch {
    Write-ColorOutput "Error en operacion git: $_" $Red
    exit 1
}

# Paso 2: Login en Azure
Write-ColorOutput "`n[2/6] Iniciando sesion en Azure..." $Green
try {
    az login
    if ($LASTEXITCODE -ne 0) {
        Write-ColorOutput "Error: No se pudo iniciar sesion en Azure" $Red
        exit 1
    }
    Write-ColorOutput "OK Sesion de Azure iniciada" $Green
}
catch {
    Write-ColorOutput "Error en Azure login: $_" $Red
    exit 1
}

# Paso 3: Login en Container Registry
Write-ColorOutput "`n[3/6] Iniciando sesion en Container Registry..." $Green
try {
    az acr login --name $REGISTRY
    if ($LASTEXITCODE -ne 0) {
        Write-ColorOutput "Error: No se pudo iniciar sesion en ACR" $Red
        exit 1
    }
    Write-ColorOutput "OK Sesion de ACR iniciada" $Green
}
catch {
    Write-ColorOutput "Error en ACR login: $_" $Red
    exit 1
}

# Obtener version
$VERSION = Get-Version
Write-ColorOutput "`n[VERSION] Imagen: $VERSION" $Yellow

# Paso 4: Build y push de imagen Backend
Write-ColorOutput "`n[4/6] Buildeando imagen Backend..." $Green
try {
    $SPRING_FULL_IMAGE = "$REGISTRY_URL/$($SPRING_IMAGE_NAME):$VERSION"
    $SPRING_LATEST = "$REGISTRY_URL/$($SPRING_IMAGE_NAME):latest"
    
    Write-ColorOutput "Buildeando: $SPRING_FULL_IMAGE" $Yellow
    
    # Cambiar al directorio del Dockerfile
    $SPRING_DIR = Split-Path -Parent $SPRING_DOCKERFILE
    Push-Location $SPRING_DIR
    
    docker buildx build --platform linux/amd64,linux/arm64 `
        -t $SPRING_FULL_IMAGE `
        -t $SPRING_LATEST `
        --push .
    
    Pop-Location
    
    if ($LASTEXITCODE -ne 0) {
        Write-ColorOutput "Error: Build de Backend fallo" $Red
        exit 1
    }
    Write-ColorOutput "OK Backend buildeado y pusheado: $SPRING_FULL_IMAGE" $Green
}
catch {
    Write-ColorOutput "Error en build de Backend: $_" $Red
    exit 1
}

# Paso 5: Build y push de imagen Frontend
Write-ColorOutput "`n[5/6] Buildeando imagen Frontend..." $Green
try {
    $FRONT_FULL_IMAGE = "$REGISTRY_URL/$($FRONT_IMAGE_NAME):$VERSION"
    $FRONT_LATEST = "$REGISTRY_URL/$($FRONT_IMAGE_NAME):latest"
    
    Write-ColorOutput "Buildeando: $FRONT_FULL_IMAGE" $Yellow
    
    # Cambiar al directorio del Dockerfile
    $FRONT_DIR = Split-Path -Parent $FRONT_DOCKERFILE
    Push-Location $FRONT_DIR
    
    docker buildx build --platform linux/amd64,linux/arm64 `
        -t $FRONT_FULL_IMAGE `
        -t $FRONT_LATEST `
        --push .
    
    Pop-Location
    
    if ($LASTEXITCODE -ne 0) {
        Write-ColorOutput "Error: Build de Frontend fallo" $Red
        exit 1
    }
    Write-ColorOutput "OK Frontend buildeado y pusheado: $FRONT_FULL_IMAGE" $Green
}
catch {
    Write-ColorOutput "Error en build de Frontend: $_" $Red
    exit 1
}

# Paso 6: Resumen
Write-ColorOutput "`n[6/6] Deploy completado exitosamente" $Green
Write-ColorOutput "`n========================================" $Yellow
Write-ColorOutput "RESUMEN DEL DEPLOY" $Yellow
Write-ColorOutput "========================================" $Yellow
Write-ColorOutput "Version: $VERSION" $Green
Write-ColorOutput "Backend:  $SPRING_FULL_IMAGE" $Green
Write-ColorOutput "Frontend: $FRONT_FULL_IMAGE" $Green
Write-ColorOutput "========================================" $Yellow
Write-ColorOutput "`nOK Todas las imagenes en Azure Container Registry" $Green