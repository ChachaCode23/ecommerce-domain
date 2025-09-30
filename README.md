Proyecto Académico: E-commerce

Este repositorio forma parte del desarrollo del **proyecto académico individual** de la asignatura **Programación 2**, correspondiente al **Instituto Tecnológico de las Américas (ITLA)**.

El objetivo principal es construir la **capa de dominio** de una plataforma **E-commerce**, aplicando buenas prácticas de arquitectura y organización por paquetes.

---

Descripción General

La capa de dominio incluye las **entidades**, **enumeraciones** y **repositorios base** que representan la lógica de negocio del sistema.  
Se estructura de forma modular, siguiendo una arquitectura **limpia y organizada**.

---

Estructura del Proyecto

El proyecto se encuentra dividido en diferentes paquetes:
com.urbancollection.ecommerce.domain.base
├── BaseEntity.java
└── OperationResult.java

com.urbancollection.ecommerce.domain.entity.catalogo
├── Producto.java
└── Cupon.java

com.urbancollection.ecommerce.domain.entity.logistica
├── Direccion.java
└── Envio.java

com.urbancollection.ecommerce.domain.entity.usuarios
├── Usuario.java
└── ListaDeseos.java

com.urbancollection.ecommerce.domain.entity.ventas
├── Pedido.java
├── ItemPedido.java
└── TransaccionPago.java

com.urbancollection.ecommerce.domain.enums
├── EstadoDeEnvio.java
├── EstadoDePedido.java
└── MetodoDePago.java

com.urbancollection.ecommerce.domain.repository
├── ProductoRepository.java
├── UsuarioRepository.java
├── PedidoRepository.java
├── ItemPedidoRepository.java
├── TransaccionPagoRepository.java
├── DireccionRepository.java
├── EnvioRepository.java
└── CuponRepository.java


**Arquitectura de la Capa de Dominio:**

![Arquitectura Ecommerce](docs/Arquitectura%20Ecommerce.png)


Capa de Persistencia (Es la parte que estamos agregando)

La **capa de persistencia** se implementó para simular una base de datos **en memoria**, ideal para pruebas unitarias sin conexión real a un gestor de base de datos.

Esta capa incluye:
- **BaseRepository** → Clase genérica con las operaciones CRUD básicas.  
- **InMemoryContext** → Almacén en memoria (usa `ConcurrentHashMap`).  
- **Repositorios específicos (Impl)** → Implementan las interfaces del dominio.  
- **PruebaPersistencia.java** → Clase de prueba para validar las operaciones.

Estructura:
com.urbancollection.ecommerce.persistence
├── base
│ └── BaseRepository.java
├── context
│ └── InMemoryContext.java
├── repositories
│ ├── ProductoRepositoryImpl.java
│ ├── CuponRepositoryImpl.java
│ ├── UsuarioRepositoryImpl.java
│ ├── DireccionRepositoryImpl.java
│ ├── EnvioRepositoryImpl.java
│ ├── PedidoRepositoryImpl.java
│ ├── ItemPedidoRepositoryImpl.java
│ └── TransaccionPagoRepositoryImpl.java
└── PruebaPersistencia.java


**Arquitectura de la Capa de Persistencia:**

![Arquitectura Persistencia](docs/Arquitectura%20Persistencia.png


Ejecución de Pruebas

Para validar la funcionalidad de la capa de persistencia:

1. Ejecutar la clase `PruebaPersistencia.java`  
2. Observar en la consola las operaciones de **guardar**, **buscar**, **listar** y **eliminar** productos simulados en memoria.

Ejemplo de salida esperada:
Producto encontrado: Gorra
Total de productos: 1
Producto eliminado. Total actual: 0

Desarrollado por

Nombre: Yassil Elpidio Del Orbe Moronta
Matrícula: 2024-2536
Materia: Programación 2
Facilitador: Francis Ramírez
