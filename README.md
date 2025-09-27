# 🛒 Proyecto Académico: Capa de Dominio - E-commerce

Este repositorio forma parte del desarrollo del **proyecto académico individual** de la asignatura **Programación 2**, correspondiente al **Instituto Tecnológico de las Américas (ITLA)**.

El objetivo principal es construir la **capa de dominio** de una plataforma **E-commerce**, aplicando buenas prácticas de arquitectura y organización por paquetes.

---

## 📚 Descripción General

La capa de dominio incluye las **entidades**, **enumeraciones** y **repositorios base** que representan la lógica de negocio del sistema.  
Se estructura de forma modular, siguiendo una arquitectura **limpia y organizada**.

---

## 🧱 Estructura del Proyecto

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
├── EstadoDePedido.java
├── EstadoDeEnvio.java
└── MetodoDePago.java

com.urbancollection.ecommerce.domain.repository
├── IBaseRepository.java
├── UsuarioRepository.java
├── ProductoRepository.java
├── PedidoRepository.java
├── EnvioRepository.java
└── CuponRepository.java


---

## 🧩 Tecnologías Utilizadas

- **Lenguaje:** Java  
- **Framework:** Maven  
- **IDE:** Eclipse  
- **Versión de Java:** 1.8  
- **Control de versiones:** Git & GitHub

---

## 🖼️ Arquitectura del Proyecto

En la carpeta `/docs` se incluye una imagen representando la estructura del dominio:

📁 **docs/Arquitectura Ecommerce.png**

Vista previa de la arquitectura:

![Arquitectura Ecommerce](./docs/Arquitectura%20Ecommerce.png)

---

## 👨‍💻 Desarrollado por

**Nombre:** Yassil Elpidio Del Orbe Moronta  
**Matrícula:** 2024-2536  
**Materia:** Programación 2  
**Facilitador:** Francis Ramírez  
**Fecha:** 26/09/2025

---

## 🔗 Enlace del Repositorio

[Acceder al repositorio en GitHub](https://github.com/ChachaCode23/ecommerce-domain)


---

>Este trabajo fue realizado con fines académicos para demostrar la comprensión de la capa de dominio dentro de una arquitectura de software.

