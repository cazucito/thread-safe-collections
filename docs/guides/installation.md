# Instalación

## Requisitos

- **Java Development Kit (JDK)** 17 o superior
  - Recomendado: [Eclipse Temurin](https://adoptium.net/) (OpenJDK)
  - Alternativas: Oracle JDK, Amazon Corretto, Azul Zulu

- **Maven** 3.9+ (opcional, el proyecto incluye Maven Wrapper)

## Verificar Java

```bash
java -version
```

Deberías ver algo como:
```
openjdk version "21.0.5" 2024-10-15 LTS
OpenJDK Runtime Environment Temurin-21.0.5+11
```

## Opción 1: Instalación Rápida (SDKMAN)

[SDKMAN](https://sdkman.io/) es el gestor de SDKs más popular para Java.

```bash
# Instalar SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Instalar Java 21
sdk install java 21.0.5-tem

# Verificar
java -version
```

## Opción 2: Instalación Manual

### Linux/macOS

Descarga el tarball desde [Adoptium](https://adoptium.net/temurin/releases/) y:

```bash
# Extraer
tar -xzf OpenJDK21U-jdk_x64_linux_hotspot_21.0.5_11.tar.gz

# Mover a ubicación estándar
sudo mv jdk-21.0.5+11 /opt/java/

# Configurar JAVA_HOME
echo 'export JAVA_HOME=/opt/java/jdk-21.0.5+11' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

### Windows

1. Descarga el instal MSI desde [Adoptium](https://adoptium.net/temurin/releases/)
2. Ejecuta el instalador
3. Añade al PATH si no lo hace automáticamente

## Verificar Entorno

El proyecto incluye un validador de entorno:

```bash
./mvnw validate -Pcheck-env
```

## Compilar Proyecto

```bash
# Descargar dependencias y compilar
./mvnw clean compile

# Ejecutar tests
./mvnw test

# Verificar todo (tests + cobertura)
./mvnw clean verify
```

## Solución de Problemas

### Permission denied al ejecutar mvnw

```bash
chmod +x mvnw
```

### JAVA_HOME no está configurado

**Linux/macOS:**
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)  # macOS
# o
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk     # Linux
```

**Windows:**
```powershell
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Eclipse Adoptium\jdk-21", "User")
```

### Error: Maven no encontrado

El proyecto incluye Maven Wrapper (`./mvnw`). No necesitas instalar Maven separadamente.

---

¿Listo? Continúa con la [Guía Rápida](quickstart.md).
