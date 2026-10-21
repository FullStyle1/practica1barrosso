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

Esta etapa incluye la base de Spring Boot, reglas de Spring Security, autenticación JWT y HTTPS local con TLS 1.2/1.3.

## Configuración local

El certificado de desarrollo y sus claves no se guardan en Git. En PowerShell, crea un keystore PKCS12 local:

```powershell
New-Item -ItemType Directory -Force certs
keytool -genkeypair `
  -alias localhost `
  -keyalg RSA `
  -keysize 2048 `
  -storetype PKCS12 `
  -keystore certs\localhost.p12 `
  -validity 365 `
  -dname "CN=localhost" `
  -ext "SAN=dns:localhost,ip:127.0.0.1"
```

Cuando `keytool` solicite la contraseña de clave, pulsa Enter para reutilizar la del keystore. Usa esa contraseña en `SERVER_SSL_KEY_STORE_PASSWORD`. Configura además las credenciales locales y un secreto JWT de al menos 32 caracteres:

```powershell
$env:APP_AUTH_USERNAME = "usuario"
$env:APP_AUTH_PASSWORD = Read-Host "Contraseña local"
$env:APP_AUTH_ROLE = "USER"
$env:APP_JWT_SECRET = Read-Host "Secreto JWT (32 caracteres o más)"
$env:APP_CORS_ALLOWED_ORIGIN = "http://localhost:4200"
$env:SERVER_SSL_KEY_STORE_PASSWORD = Read-Host "Contraseña del keystore"
mvn spring-boot:run
```

El servicio escucha únicamente en HTTPS en el puerto `8443`; no se inicia un conector HTTP.
Por defecto CORS permite solo `http://localhost:4200`; cambia `APP_CORS_ALLOWED_ORIGIN` al origen exacto de Angular si usa otro puerto o está desplegado. No se permite cualquier origen (`*`).

## Probar autenticación

Solicita un token con `POST /api/auth/token` y JSON `{"username":"usuario","password":"..."}`. El token dura 15 minutos.

```powershell
$body = @{ username = $env:APP_AUTH_USERNAME; password = $env:APP_AUTH_PASSWORD } | ConvertTo-Json
$tokenResponse = $body | curl.exe -k -sS -X POST https://localhost:8443/api/auth/token -H "Content-Type: application/json" --data-binary "@-" | ConvertFrom-Json
$token = $tokenResponse.accessToken
curl.exe -k https://localhost:8443/api/private -H "Authorization: Bearer $token"
```

El certificado autofirmado es solo para desarrollo local; no desactives la validación de certificados en clientes de producción. `/api/auth/**` es público; las demás rutas requieren un Bearer token válido. `/api/admin/**` además requiere el rol `ADMIN`.
El usuario en memoria tiene un único rol configurable: `USER` (predeterminado) o `ADMIN`. Para probar la ruta administrativa, cambia `APP_AUTH_ROLE` a `ADMIN`; una aplicación real debe asignar roles por usuario desde su persistencia.

Los errores de acceso devuelven JSON con `status`, `error`, `message` y `timestamp`. Se registran los intentos de login fallidos y los rechazos 401/403 sin guardar contraseñas ni tokens. Los errores de handshake TLS ocurren antes de HTTP, por lo que no pueden incluir una respuesta JSON de la API.

Comprueba el handshake con TLS 1.2 y TLS 1.3:

```powershell
curl.exe -k --tlsv1.2 --tls-max 1.2 https://localhost:8443/api/private
curl.exe -k --tlsv1.3 --tls-max 1.3 https://localhost:8443/api/private
```
