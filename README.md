# User Profile Service (Dev)

Microservicio REST desarrollado con Spring Boot 4 y Java 25 para la gestión de perfiles de usuario autenticados.

## Requisitos

- **Java 25**
- **Gradle**

## Cómo ejecutar

Para iniciar la aplicación, ejecuta el siguiente comando en la raíz del proyecto:

```bash
./gradlew bootRun
```

La aplicación estará disponible por defecto en `http://localhost:8080`.

## Documentación de la API

### 1. Obtención de Token (Modo Dev)

Para facilitar las pruebas, el microservicio incluye un endpoint de login que genera tokens válidos sin necesidad de validación de contraseña compleja.

**Endpoint:** `POST /api/v1/auth/login`

**Ejemplo curl:**

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "juan_perez"}'
```

**Respuesta:**

```json
{
  "access_token": "eyJhbGci...",
  "token_type": "Bearer",
  "expires_in_seconds": 1800
}
```

---

### 2. Gestión del Perfil del Usuario

Todos los endpoints bajo `/api/v1/profile` requieren el token obtenido anteriormente en la cabecera `Authorization`.

#### Crear Perfil (POST)

Permite crear el perfil asociado al usuario del token. Si ya existe, devuelve un error 409 Conflict.

**Ejemplo curl:**

```bash
curl -X POST http://localhost:8080/api/v1/profile \
     -H "Authorization: Bearer <TU_TOKEN>" \
     -H "Content-Type: application/json" \
     -d '{
           "email": "juan.perez@example.com",
           "firstName": "Juan",
           "lastName": "Perez",
           "birthDate": "1990-05-15",
           "phoneNumber": "600000000",
           "street": "Gran Vía 1",
           "city": "Madrid",
           "country": "España",
           "postalCode": "28013"
         }'
```

#### Obtener Mi Perfil (GET)

Devuelve los datos del perfil del usuario autenticado.

**Ejemplo curl:**

```bash
curl -X GET http://localhost:8080/api/v1/profile \
     -H "Authorization: Bearer <TU_TOKEN>"
```

#### Actualizar Perfil (PUT - Full Replace)

Realiza un reemplazo completo del perfil. Los campos no incluidos en la petición se establecerán a nulo (si son opcionales).

**Ejemplo curl:**

```bash
curl -X PUT http://localhost:8080/api/v1/profile \
     -H "Authorization: Bearer <TU_TOKEN>" \
     -H "Content-Type: application/json" \
     -d '{
           "email": "juan.perez.updated@example.com",
           "firstName": "Juan Carlos",
           "lastName": "Perez Garcia",
           "birthDate": "1990-05-15"
         }'
```

---

## Notas y Decisiones de Diseño

- **Identificación de Usuario**: El `userId` no se envía nunca en el cuerpo o la URL; se extrae exclusivamente de la claim `sub` del JWT.
- **Base de Datos H2**: Se utiliza una base de datos en memoria para desarrollo.
  - **Consola H2**: `http://localhost:8080/h2-console`
  - **JDBC URL**: `jdbc:h2:mem:testdb`
  - **Credenciales**: `sa` / `password`
- **Validaciones**: Se utiliza Bean Validation para asegurar la integridad de los datos (`@Email`, `@Past`, `@NotBlank`).
- **Gestión de Errores**: Se ha implementado un `@RestControllerAdvice` para devolver respuestas de error consistentes (400, 404, 409) con detalles de los campos en caso de errores de validación.
- **PUT (Full Replace)**: Siguiendo la semántica HTTP PUT, el endpoint de actualización reemplaza la entidad completa. Es responsabilidad del cliente enviar todos los campos que desea conservar.

## Suposiciones y Tradeoffs

### Suposiciones

- **birthDate opcional**: Se asume que la fecha de nacimiento es un campo opcional, ya que no todos los usuarios pueden querer proporcionarla por privacidad.
- **Email único global**: Se asume que el email debe ser único en todo el sistema, no solo por usuario.
- **Autenticación simplificada**: El endpoint de login genera tokens sin validación de contraseña, asumiendo un entorno de desarrollo/pruebas.
- **Un perfil por usuario**: Se asume que cada usuario autenticado solo puede tener un perfil asociado a su `userId`.

### Tradeoffs

- **PUT full replace vs PATCH**: Se optó por PUT con reemplazo completo en lugar de PATCH parcial por simplicidad de implementación y límite de tiempo. El cliente debe enviar todos los campos en cada actualización.
- **H2 en memoria vs BD persistente**: Se priorizó la facilidad de setup y pruebas sobre la persistencia real de datos. En producción se recomendaría PostgreSQL o similar.
- **Validación en capa DTO**: Las validaciones se realizan en DTOs en lugar de en la entidad para tener control granular sobre qué se valida en creación vs actualización.
- **Mensajes i18n hardcodeados en inglés**: Por límite de tiempo, solo se incluye el archivo de mensajes en inglés, aunque la infraestructura soporta múltiples idiomas.
- **Manejo genérico de DataIntegrityViolationException**: Se asume que cualquier violación de integridad es por email duplicado. En un sistema más robusto se analizaría el mensaje de error para dar feedback más específico.

