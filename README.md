# Open Serial Console

Consola de comandos para dispositivos serie construida en Java 21 con [jSerialComm-v2.11.4](https://github.com/fazecast/jserialcomm).

- Discord comunitario: [Discord](https://discord.gg/).

- Sitio web: [https://xabyserver.ddns.net/OpenSerialConsole](https://xabyserver.ddns.net/OpenSerialConsole).

> Sitio web actualmente en desarrollo y el discord está en construcción.

![OpenSerialConsole gif demo](media/Screen-Recording-v0.0.1.gif)

## Características

- Comunicación Serial/UART
- Selección de puertos dinámicos
- Configuración de baud rate
- Interfaz ligera y fácil de usar en Swing
- Cross-platform (Linux / Windows / macOS)

## Estructura

```text
OpenSerialConsole.java   → Punto de entrada de la aplicación

ui/
 ├─ AppFrame.java        → Ventana principal
 ├─ TopBar.java          → Controles seriales
 └─ TerminalPanel.java   → Terminal RX

utils/
 └─ SerialManager.java   → Lógica de conexión serie

config/
 └─ Theme.java           → UI colores, fuentes, helpers de widgets

libs/                    → Librerías externas
 └─ jSerialComm-2.11.4.jar
```

## Descarga de librerías

- Descarga la librería [jSerialComm-v2.11.4](https://github.com/fazecast/jserialcomm) y cópiala en la carpeta `libs` del proyecto.

- Alternativamente, puedes usar el *custom script* `maven_downloader.py` para descargar las librerías desde Maven Central o cualquier otro repositorio configurable desde `dependencies.xml`.

## Entorno de desarrollo recomendado:

Es recomendable usar `Java 21` y `VSCode` con la extensión [Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) para compilar y depurar el código.

> El proyecto está configurado para ser trabajado desde `VSCode` con `Java 21` aunque se puede compilar con cualquier IDE compatible con Java.

## Ejecutar y Compilar:

> NOTA: Si estás usando `VSCode`, debes configurar el proyecto y la extensión de java para que reconozca la ruta de las librerías externas y las clases de la consola. Puedes configurarlo manualmente en `.vscode/settings.json`.

**Windows**

````powershell
javac -cp "libs/*" OpenSerialConsole.java
java -cp ".;libs/*" OpenSerialConsole
````

**Linux / macOS:**

````bash
javac -cp "libs/*" OpenSerialConsole.java
java -cp ".:libs/*" OpenSerialConsole
````

### Licencia

- Este proyecto está licenciado bajo la [Apache License 2.0](LICENSE).