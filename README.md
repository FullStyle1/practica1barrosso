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

Esta etapa incluye la base de Spring Boot y reglas iniciales de Spring Security. La autenticación con tokens y TLS/SSL se agregarán en pasos posteriores.

## Reglas de acceso iniciales

- `/api/auth/**` es público.
- `GET /api/health` es público.
- `/api/admin/**` requiere el rol `ADMIN`.
- Las demás rutas requieren autenticación.
