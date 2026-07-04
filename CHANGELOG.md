# Changelog

Todos los cambios notables de este proyecto se documentaran en este archivo.

## Unreleased

- Se anaden paquetes `.deb` para Ubuntu con entrada de escritorio e icono instalados para GNOME.
- Se publica automaticamente el `.deb` de Ubuntu 22.04 en el repositorio APT configurado por la release.

## 1.1.0

- Se anade analisis de SonarQube al workflow de release, despues de los tests y sin bloquear la publicacion.
- Se incorporan logs de aplicacion con rotacion y captura de excepciones no controladas en la carpeta de datos del
  usuario.
- Los proyectos `.pds` pasan a guardarse y abrirse desde la carpeta de datos del usuario en lugar del directorio de
  ejecucion.
- Se renueva el dialogo "Acerca de..." con enlaces del autor, librerias utilizadas y licencia GPLv3.
- Se mejora la carga de iconos desde recursos con cache y multiples tamanos para la ventana principal.
- Se evita el error de serializacion de nodos al interactuar con el grafo.
- Se mejora la normalizacion de nombres al guardar y generar archivos, incluyendo nombres que empiezan por digito.
- Se mejora la descarga de nuevas versiones para ejecutarse en segundo plano y mostrar errores sin bloquear la interfaz.
- Se actualizan dependencias y plugins Maven, incluyendo JDOM 2.0.2, Gson 2.13.2 y plugins de empaquetado.

## 1.0.0

- Version inicial del asistente para crear guias ADS.
