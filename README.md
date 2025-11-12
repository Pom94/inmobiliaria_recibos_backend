# Sistema backend de generación de recibos para inmobiliarias

API RESTful desarrollada en **Java con Spring Boot** para la gestión de **clientes, contratos y recibos** de una inmobiliaria.  
Proyecto final para **Prácticas Supervisadas (PS)** – UTN FRBB.

**Autor:** Pom94 (Pamela Dominguez Fernandez)

## Tabla de Contenidos
- [Descripción del Proyecto](#descripción-del-proyecto)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Endpoints Principales](#endpoints-principales)
- [Colección Postman](#colección-postman)
- [Despliegue](#despliegue)
- [Ejecución Local](#ejecución-local)
- [Documentación Adicional](#documentación-adicional)
- [Repositorios](#repositorios)

### Descripción del Proyecto

API que permite:
- Gestión completa de **clientes**.
- Administración de **contratos**.
- Emisión de **recibos** con múltiples conceptos y medios de pago. (CRUD completo)
- Autenticación segura con **JWT**.
- Conexión a **base de datos SQLite** (archivo local).

> Incluye **soft delete** (`activo = false`) en lugar de borrado físico para clientes y contratos.

### Tecnologías Utilizadas

| Tecnología         | Uso |
|--------------------|-----|
| **Java 17**        | Lenguaje principal |
| **Spring Boot 3**  | Framework backend |
| **Spring Security + JWT** | Autenticación |
| **Spring Data JPA** | Persistencia |
| **SQLite**         | Base de datos embebida (archivo `inmobiliaria.db`) |
| **Lombok**         | Reducción de código boilerplate |
| **Maven**          | Gestión de dependencias |

### Endpoints Principales

| Método | Ruta | Descripción |
|--------|------|-----------|
| `POST` | `/auth/login` | Inicia sesión y devuelve JWT |
| `POST` | `/recibo/crear` | Crea un recibo |
| `GET`  | `/recibo/listar` | Lista todos los recibos |
| `DELETE` | `/recibo/{id}` | Elimina un recibo |
| `POST` | `/clientes/crear` | Crea un cliente |
| `GET`  | `/clientes/listar` | Lista todos los clientes |
| `PUT` | `/clientes/{id}/modificar` | Modifica un cliente |
| `PUT`  | `/clientes/{id}/desactivar` | Desactiva cliente |
| `POST` | `/contratos/crear` | Crea un contrato |
| `GET`  | `/contratos/listar` | Lista todos los contratos |
| `PUT` | `/contratos/{id}/modificar` | Modifica un contrato |
| `PUT`  | `/contratos/{id}/desactivar` | Desactiva contrato (soft delete) |

> Ver colección completa en Postman: [Colección Postman](#colección-postman)

### Colección Postman

Archivo exportado: [`docs/Inmobiliaria.postman_collection.json`](./docs/Inmobiliaria.postman_collection.json)

### Despliegue

**Backend en producción (Render):**  
[https://inmobiliaria-recibos-backend.onrender.com](https://inmobiliaria-recibos-backend.onrender.com)

**Frontend conectado:**  
[https://inmobiliaria-recibos-frontend.onrender.com](https://inmobiliaria-recibos-frontend.onrender.com)

> **Nota:** En Render, SQLite se guarda en el sistema de archivos efímero.  
> Los datos **no persisten entre despliegues**.  
> Para producción real, se recomienda PostgreSQL o MySQL.

### Ejecución Local

#### Requisitos
- Java 17
- Maven
- Git

#### Pasos

```bash
git clone https://github.com/Pom94/inmobiliaria_recibos_backend.git
cd inmobiliaria_recibos_backend
mvn spring-boot:run
```
> La **API** arranca en ``http://localhost:8080``
> Base de datos: ``inmobiliaria.db`` (se crea automáticamente en el directorio del proyecto)

### Documentación Adicional

| Archivo | Descripción |
|--------|-------------|
| `docs/inmobiliaria-diagrama-e-r.jpg` | Diagrama ER en imagen |
| `docs/Inmobiliaria.postman_collection.json` | Colección de pruebas |
| `inmobiliaria.db` | Base de datos SQLite (generada al iniciar) |

### Repositorios

- **Backend:** [github.com/Pom94/inmobiliaria_recibos_backend](https://github.com/Pom94/inmobiliaria_recibos_backend)
- **Frontend:** [github.com/Pom94/inmobiliaria_recibos_frontend](https://github.com/Pom94/inmobiliaria_recibos_frontend)
