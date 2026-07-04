# ADS Generator

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=JCPrieto_ADS-Generator&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=JCPrieto_ADS-Generator)

Asistente para crear guias ADS libres de errores y ayudar al CAU en la resolucion de incidencias. Automatiza la
generacion de guias para el interprete ADS, reduciendo errores manuales.

## Caracteristicas

- Asistente guiado para crear guias ADS coherentes y sin errores manuales.
- Editor visual basado en Swing y JGraphX para construir el arbol de la guia.
- Gestion de atributos, campos, validaciones y siguientes por nodo.
- Guardado del proyecto en `.pds` (XML) en la carpeta de datos del usuario y exportacion a `.ads` para el interprete
  ADS.
- Interfaz dividida con panel grafico y editor de propiedades para uso en CAU.
- Comprobacion y descarga de nuevas versiones desde GitHub.
- Registro de errores en ficheros de log rotativos.

## Requisitos

- JDK 21
- Maven 3.9+

## Compilar

```bash
mvn -q clean package
```

El JAR se genera en `target/`.

## Paquetes de distribucion

Genera un paquete con doble click usando `jpackage` (debe ejecutarse en el SO destino):
Nota: no es posible generar el paquete de Windows o macOS desde Linux (y viceversa).

Linux:

```bash
mvn -q -Pjpackage-linux clean package
```

Linux `.deb` instalable:

```bash
mvn -q -Pjpackage-linux-deb clean package
```

Windows:

```bash
mvn -q -Pjpackage-windows clean package
```

macOS:

```bash
mvn -q -Pjpackage-mac clean package
```

Los paquetes se generan en `target/dist/` y los assets esperados para releases son:

- `ads-generator-<version>-linux.zip`
- `ads-generator-<version>-linux-ubuntu22.04.deb`
- `ads-generator-<version>-linux-ubuntu24.04.deb`
- `ads-generator-<version>-windows.zip`
- `ads-generator-<version>-mac.zip`

En Linux, el `.deb` es el paquete recomendado para Ubuntu/GNOME porque instala la entrada `.desktop` y el icono de la
aplicacion en el sistema. El ZIP de Linux es portable y no registra automaticamente la aplicacion en el dock.
El `.deb` de Ubuntu 22.04 se publica tambien en el repositorio APT configurado por el workflow de release.

## Ejecutar

```bash
mvn -q exec:java -Dexec.mainClass=gui.Principal
```

## Formatos de archivo

- `.pds`: XML del proyecto, usado para guardar/abrir guias en la GUI.
- `.ads`: guia generada para el interprete ADS.

## Datos de usuario

La aplicacion guarda sus datos fuera de la carpeta de instalacion:

- Windows: `%LOCALAPPDATA%\ADS Generator`
- macOS: `~/Library/Application Support/ADS Generator`
- Linux: `~/.local/share/ADS Generator`

Dentro de esa carpeta se usan:

- `projects`: proyectos `.pds`.
- `logs`: ficheros de log rotativos.

Se pueden sobrescribir estas rutas con propiedades de sistema:

- `-Dads.projects.dir=/ruta/proyectos`
- `-Dads.logs.dir=/ruta/logs`

## Ejemplo de guia `.ads`

```ads
Arbol {

	titulo "Alta de usuario"

	nodo Inicio{

		atributo usuario
			descripcion "Identificador del usuario"
			valor "jdoe"

		campo nombre
			tipo texto
			etiqueta "Nombre completo"

		validacion {
			si nombre == "" mensaje "El nombre es obligatorio"
		}

		siguiente {
			entonces Fin
		}

	}

	nodo Fin{

		campo resumen
			tipo fijo
			etiqueta "Proceso finalizado"

	}

}
```

## Flujo recomendado

1. Crear una nueva guia ADS y definir el arbol de decisiones.
2. Revisar atributos, campos, validaciones y siguientes para evitar errores.
3. Guardar el proyecto (`.pds`) y exportar la guia (`.ads`).

## Perfil de uso (CAU)

Orientado al equipo CAU para crear y mantener guias ADS consistentes que agilicen la resolucion de incidencias.

## Estructura del proyecto

- `src/main/java/gui`: interfaz grafica y flujo principal del asistente.
- `src/main/java/arbol`: logica de dominio del arbol de guias ADS.
- `src/main/java/data`: carga/guardado de proyectos `.pds`.
- `src/main/java/auxiliar`: utilidades y filtros de archivos.
- `src/main/resources`: recursos (iconos).

## Dependencias destacadas

- JGraphX (grafo)
- JDOM (XML)
- SwingX (componentes)
- Gson (GitHub API)
- Apache Commons IO (descargas)

## Changelog

Consulta `CHANGELOG.md` para ver los cambios por version.
