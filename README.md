# 📱 PedidosApp

**PedidosApp** es una aplicación móvil desarrollada como proyecto final para la asignatura **Desarrollo de Aplicaciones Móviles I (DAM-135)** en la Universidad de El Salvador. El objetivo principal es crear una app funcional para la administración de un catálogo de pedidos, implementando gestión de datos locales, autenticación de usuarios y una experiencia de usuario moderna y personalizable.

## 👥  Integrantes
- Alas Hernández, Milton Obed - AH09062 
- Martínez Valladares, David Salomón MV12013

## 🎯 Objetivo

Desarrollar y desplegar en un dispositivo físico una aplicación móvil con gestión de datos locales, que permita la administración de pedidos y productos, integrando operaciones CRUD, autenticación, preferencias de usuario y una interfaz adaptable a modo claro/oscuro.

## ✨ Características principales

- 🔐 **Gestión de acceso:** Registro e inicio de sesión de usuarios, validando credenciales contra una base de datos local
- 📊 **Administración de datos maestro:** Gestión de entidades como Usuario, Producto, Pedido y DetallePedido
- 🔄 **Operaciones CRUD:** Crear, leer, actualizar y eliminar pedidos y productos
- 💾 **Persistencia local:** Uso de Room (SQLite) para almacenamiento de datos
- ⚙️ **Shared Preferences:** Guardado de sesión de usuario y preferencias de tema
- 🌙 **Temas claro/oscuro:** Soporte completo para ambos modos, con opción de cambio desde la configuración
- 🧭 **Navegación moderna:** Uso de Navigation Drawer y navegación entre fragments
- 💬 **Diálogos y listas personalizadas:** Confirmaciones y acciones mediante AlertDialog, personalización de ítems en RecyclerView
- 🖼️ **Gestión de imágenes:** Permite agregar imágenes a productos desde URL o galería, mostrándolas en catálogos y detalles
- 📱 **Pruebas en dispositivo físico:** La app es probada y demostrada en un dispositivo real

## 🚀 Plan de Desarrollo

### 1. 🏗️ Estructura y Navegación
- Ajuste de la estructura de carpetas
- Implementación y verificación del Navigation Drawer y navegación entre fragments

### 2. 🗄️ Base de Datos Room
- Definición y creación de entidades: Usuario, Producto, Pedido, DetallePedido
- Creación de DAOs para cada entidad
- Implementación de AppDatabase y Converters

### 3. 📝 CRUD de Pedidos
- Implementación de Repository y ViewModel para pedidos
- Creación de Fragments y Adapters para listar, agregar, editar y eliminar pedidos
- Pruebas visuales del CRUD

### 4. 🔑 Sistema de Login/Registro
- Pantallas de registro y login de usuario
- Validación de credenciales contra la base de datos
- Navegación segura: solo usuarios autenticados acceden a la app

### 5. 💾 Shared Preferences
- Persistencia de sesión de usuario
- Guardado de preferencias de tema (oscuro/claro)

### 6. 🎨 Temas claro/oscuro
- Soporte para ambos temas
- Opción para cambiar el tema desde la configuración

### 7. 🎭 Diálogos y listas personalizadas
- Uso de AlertDialog para confirmaciones
- Personalización de ítems en RecyclerView (colores, imágenes, botones)

### 8. 🖼️ Imágenes en catálogos
- Agregar imágenes a productos (URL o galería)
- Mostrar imágenes en listas y detalles

### 9. 🧪 Pruebas y Entrega
- Pruebas en dispositivo físico
- Grabación de video demostrativo (máx. 5 min)
- Empaquetado y entrega del proyecto (ZIP y video)

## 📊 Criterios de Evaluación

| Criterio | Peso |
|----------|------|
| 🎨 Aplicación de temas y diseño en componentes de la UI | 10% |
| 🔐 Funcionamiento de login y registro | 10% |
| 🗄️ Uso de Room para almacenamiento local | 30% |
| 🧭 Navegación entre actividades/fragments | 10% |
| ⚙️ Uso de Shared Preferences para sesión y preferencias | 10% |
| 💬 Diálogos y listas personalizadas con RecyclerView | 10% |
| 🖼️ Gestión de imágenes en catálogos | 10% |
| 🎛️ Uso de controles avanzados como NavigationView | 10% |

## 🛠️ Tecnologías y herramientas

- **Lenguaje:** ☕ Java
- **Framework:** 🤖 Android SDK
- **Persistencia:** 🗄️ Room (SQLite)
- **UI:** 🧭 Navigation Drawer, Fragments, RecyclerView, AlertDialog
- **Preferencias:** ⚙️ SharedPreferences

## 📦 Entrega

- 🎤 Presentación grupal
- 📁 Archivo ZIP con el proyecto funcional
- 🎥 Video demostrativo (máx. 5 minutos)

---

*Proyecto desarrollado como trabajo final de la asignatura DAM-135* 🎓