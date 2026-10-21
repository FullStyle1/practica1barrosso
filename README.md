# practica1barrosso

Proyecto base de la API backend para la historia HU-SEC-001.

## Requisitos

- Java 21
- Maven 3.9 o posterior

## Ejecutar

```powershell
mvn spring-boot:run
```

## Probar

```powershell
mvn test
```

Esta etapa incluye la base de Spring Boot, reglas de Spring Security y autenticación JWT. TLS/SSL se agregará en un paso posterior.

## Configuración local

En PowerShell, configura las credenciales locales y un secreto JWT de al menos 32 caracteres:

```powershell
$env:APP_AUTH_USERNAME = "usuario"
$env:APP_AUTH_PASSWORD = Read-Host "Contraseña local"
$env:APP_JWT_SECRET = Read-Host "Secreto JWT (32 caracteres o más)"
mvn spring-boot:run
```

## Probar autenticación

Solicita un token con `POST /api/auth/token` y JSON `{"username":"usuario","password":"..."}`. El token dura 15 minutos.

```powershell
$body = @{ username = $env:APP_AUTH_USERNAME; password = $env:APP_AUTH_PASSWORD } | ConvertTo-Json
$token = (Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/auth/token -ContentType "application/json" -Body $body).accessToken
Invoke-RestMethod -Uri http://localhost:8080/api/private -Headers @{ Authorization = "Bearer $token" }
```

`/api/auth/**` es público; las demás rutas requieren un Bearer token válido. `/api/admin/**` además requiere el rol `ADMIN`. La API sigue usando HTTP en esta etapa; no envíes credenciales por una red no confiable hasta que se configure TLS/SSL.
