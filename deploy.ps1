$REGISTRY = "huellasormexico"
$REGISTRY_URL = "huellasormexico.azurecr.io"
$SPRING_DOCKERFILE = "compose/production/spring/Dockerfile"
$FRONT_DOCKERFILE = "compose/production/front/Dockerfile"
$SPRING_IMAGE_NAME = "huellas-backend"
$FRONT_IMAGE_NAME = "huellas-frontend"

$RESOURCE_GROUP = "TT-2025-B077"
$LOCATION = "westus2"
$CPU = "2"
$MEMORY = "3.5"
$CONTAINER_PORT_BACKEND = "443"
$CONTAINER_PORT_FRONTEND = "80"
$DNS_LABEL_BACKEND = "huellas-backend-prod"
$DNS_LABEL_FRONTEND = "huellas-frontend-prod"

function Get-Version {
    return (Get-Date -Format "yyyyMMdd.HHmmss")
}

Write-Host "========================================"
Write-Host "SCRIPT DE DEPLOY A AZURE"
Write-Host "========================================"

Write-Host "`n[1/7] Git pull..."
try {
    git pull origin master
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Error: No se pudo hacer pull de master"
        exit 1
    }
    Write-Host "OK"
}
catch {
    Write-Host "Error: $_"
    exit 1
}

Write-Host "`n[2/7] Azure login..."
try {
    az login
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Error: No se pudo iniciar sesion en Azure"
        exit 1
    }
    Write-Host "OK"
}
catch {
    Write-Host "Error: $_"
    exit 1
}

Write-Host "`n[3/7] ACR login..."
try {
    az acr login --name $REGISTRY
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Error: No se pudo iniciar sesion en ACR"
        exit 1
    }
    Write-Host "OK"
}
catch {
    Write-Host "Error: $_"
    exit 1
}

$VERSION = Get-Version
Write-Host "`nVersion: $VERSION"

Write-Host "`n[4/7] Build Backend..."
try {
    $SPRING_FULL_IMAGE = "$REGISTRY_URL/$($SPRING_IMAGE_NAME):$VERSION"
    $SPRING_LATEST = "$REGISTRY_URL/$($SPRING_IMAGE_NAME):latest"

    Write-Host "Compilando: $SPRING_FULL_IMAGE"

    docker buildx build --platform linux/amd64 `
        -f $SPRING_DOCKERFILE `
        -t $SPRING_FULL_IMAGE `
        -t $SPRING_LATEST `
        --push .

    if ($LASTEXITCODE -ne 0) {
        Write-Host "Error: Build Backend fallo"
        exit 1
    }
    Write-Host "OK"
}
catch {
    Write-Host "Error: $_"
    exit 1
}

Write-Host "`n[5/7] Build Frontend..."
if (-not (Test-Path ".env_prod_frontend")) {
    Write-Host "Error: No se encontro .env_prod_frontend"
    exit 1
}

$envVarsFront = @{}
Get-Content ".env_prod_frontend" | ForEach-Object {
    $line = $_.Trim()
    if ($line -and -not $line.StartsWith("#")) {
        $parts = $line -split "=", 2
        if ($parts.Count -eq 2) {
            $key = $parts[0].Trim()
            $value = $parts[1].Trim()
            $envVarsFront[$key] = $value
        }
    }
}

try {
    $FRONT_FULL_IMAGE = "$REGISTRY_URL/$($FRONT_IMAGE_NAME):$VERSION"
    $FRONT_LATEST = "$REGISTRY_URL/$($FRONT_IMAGE_NAME):latest"

    Write-Host "Compilando: $FRONT_FULL_IMAGE"

    # Extrae variables de entorno para pasar al build
    $viteApiBack = $envVarsFront['VITE_API_BACK']
    $viteAcoApi = $envVarsFront['VITE_ACO_API']
    $viteGoogleMapKey = $envVarsFront['VITE_GOOGLE_MAP_KEY']

    docker buildx build --platform linux/amd64 `
        -f $FRONT_DOCKERFILE `
        --build-arg VITE_API_BACK="$viteApiBack" `
        --build-arg VITE_ACO_API="$viteAcoApi" `
        --build-arg VITE_GOOGLE_MAP_KEY="$viteGoogleMapKey" `
        -t $FRONT_FULL_IMAGE `
        -t $FRONT_LATEST `
        --push .

    if ($LASTEXITCODE -ne 0) {
        Write-Host "Error: Build Frontend fallo"
        exit 1
    }
    Write-Host "OK"
}
catch {
    Write-Host "Error: $_"
    exit 1
}

Write-Host "`n[6/7] Create Backend Container..."
if (-not (Test-Path ".env_prod_backend")) {
    Write-Host "Error: No se encontro .env_prod_backend"
    exit 1
}

$envVars = @{}
Get-Content ".env_prod_backend" | ForEach-Object {
    $line = $_.Trim()
    if ($line -and -not $line.StartsWith("#")) {
        $parts = $line -split "=", 2
        if ($parts.Count -eq 2) {
            $key = $parts[0].Trim()
            $value = $parts[1].Trim()
            $envVars[$key] = $value
        }
    }
}

try {
    $BACKEND_CONTAINER_NAME = "$REGISTRY-backend"
    $acrUsername = az acr credential show --name $REGISTRY --query username -o tsv
    $acrPassword = az acr credential show --name $REGISTRY --query "passwords[0].value" -o tsv

    $envArray = @()
    foreach ($key in $envVars.Keys) {
        $envArray += "$key=$($envVars[$key])"
    }

    az container create `
        --resource-group $RESOURCE_GROUP `
        --name $BACKEND_CONTAINER_NAME `
        --image $SPRING_FULL_IMAGE `
        --dns-name-label $DNS_LABEL_BACKEND `
        --ports $CONTAINER_PORT_BACKEND `
        --ip-address public `
        --registry-login-server $REGISTRY_URL `
        --registry-username $acrUsername `
        --registry-password $acrPassword `
        --environment-variables $envArray `
        --os-type Linux `
        --cpu $CPU `
        --memory $MEMORY `
        --location $LOCATION

    if ($LASTEXITCODE -ne 0) {
        Write-Host "Error: Creacion Backend fallo"
        exit 1
    }

    $backendInfo = az container show `
        --resource-group $RESOURCE_GROUP `
        --name $BACKEND_CONTAINER_NAME `
        -o json | ConvertFrom-Json

    $BACKEND_FQDN = $backendInfo.ipAddress.fqdn
    Write-Host "OK: http://$BACKEND_FQDN`:$CONTAINER_PORT_BACKEND"
}
catch {
    Write-Host "Error: $_"
    exit 1
}

Write-Host "`n[7/7] Create Frontend Container..."
try {
    $FRONTEND_CONTAINER_NAME = "$REGISTRY-frontend"
    $acrUsername = az acr credential show --name $REGISTRY --query username -o tsv
    $acrPassword = az acr credential show --name $REGISTRY --query "passwords[0].value" -o tsv

    $envArrayFront = @()
    foreach ($key in $envVarsFront.Keys) {
        $envArrayFront += "$key=$($envVarsFront[$key])"
    }

    az container create `
        --resource-group $RESOURCE_GROUP `
        --name $FRONTEND_CONTAINER_NAME `
        --image $FRONT_FULL_IMAGE `
        --dns-name-label $DNS_LABEL_FRONTEND `
        --ports $CONTAINER_PORT_FRONTEND `
        --ip-address public `
        --registry-login-server $REGISTRY_URL `
        --registry-username $acrUsername `
        --registry-password $acrPassword `
        --environment-variables $envArrayFront `
        --os-type Linux `
        --cpu $CPU `
        --memory $MEMORY `
        --location $LOCATION

    if ($LASTEXITCODE -ne 0) {
        Write-Host "Error: Creacion Frontend fallo"
        exit 1
    }

    $frontendInfo = az container show `
        --resource-group $RESOURCE_GROUP `
        --name $FRONTEND_CONTAINER_NAME `
        -o json | ConvertFrom-Json

    $FRONTEND_FQDN = $frontendInfo.ipAddress.fqdn
    Write-Host "OK: http://$FRONTEND_FQDN`:$CONTAINER_PORT_FRONTEND"
}
catch {
    Write-Host "Error: $_"
    exit 1
}

Write-Host "`n========================================"
Write-Host "DEPLOY COMPLETADO"
Write-Host "========================================"
Write-Host "Backend:  http://$BACKEND_FQDN`:$CONTAINER_PORT_BACKEND"
Write-Host "Frontend: http://$FRONTEND_FQDN`:$CONTAINER_PORT_FRONTEND"
Write-Host "========================================"