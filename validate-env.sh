#!/usr/bin/env bash
#
# Script de validación de entorno para thread-safe-collections
# Verifica Java 17+, Maven y sugiere SDKMAN si faltan dependencias
#

set -euo pipefail

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Variables de estado
JAVA_OK=false
MAVEN_OK=false
EXIT_CODE=0

echo "🔍 Validando entorno de desarrollo para thread-safe-collections..."
echo ""

# Función para verificar Java
check_java() {
    echo -n "☕ Verificando Java... "
    
    if ! command -v java &> /dev/null; then
        echo -e "${RED}NO ENCONTRADO${NC}"
        return 1
    fi
    
    # Obtener versión de Java
    local java_version
    java_version=$(java -version 2>&1 | grep -oP 'version "?(1\.)?\K\d+' || echo "0")
    
    if [[ "$java_version" -ge "17" ]]; then
        local full_version
        full_version=$(java -version 2>&1 | head -n1 | cut -d'"' -f2)
        echo -e "${GREEN}OK${NC} (versión $full_version)"
        JAVA_OK=true
        return 0
    elif [[ "$java_version" -ge "1" ]]; then
        local full_version
        full_version=$(java -version 2>&1 | head -n1 | cut -d'"' -f2)
        echo -e "${YELLOW}VERSIÓN ANTIGUA${NC} (versión $full_version, se requiere 17+)"
        return 1
    else
        echo -e "${RED}VERSIÓN NO RECONOCIDA${NC}"
        return 1
    fi
}

# Función para verificar Maven
check_maven() {
    echo -n "🪶 Verificando Maven... "
    
    if command -v mvn &> /dev/null; then
        local mvn_version
        mvn_version=$(mvn -version 2>&1 | head -n1 | grep -oP '\d+\.\d+\.\d+' || echo "desconocida")
        echo -e "${GREEN}OK${NC} (versión $mvn_version)"
        MAVEN_OK=true
        return 0
    elif [[ -f "./mvnw" ]]; then
        echo -e "${GREEN}OK${NC} (usando Maven Wrapper)"
        MAVEN_OK=true
        return 0
    else
        echo -e "${RED}NO ENCONTRADO${NC}"
        return 1
    fi
}

# Verificar dependencias
check_java || true
check_maven || true

echo ""

# Mostrar resumen
if [[ "$JAVA_OK" == "true" && "$MAVEN_OK" == "true" ]]; then
    echo -e "${GREEN}✅ Entorno listo para desarrollar!${NC}"
    echo ""
    echo "Comandos disponibles:"
    echo "  make test          - Ejecutar todos los tests"
    echo "  make demo          - Ejecutar todas las demos"
    echo "  make demo-single DEMO=<id>  - Ejecutar una demo específica"
    echo "  ./mvnw exec:java   - Ejecutar la aplicación"
    exit 0
fi

# Hay problemas
EXIT_CODE=1
echo -e "${YELLOW}⚠️  Entorno incompleto. Se requieren las siguientes acciones:${NC}"
echo ""

if [[ "$JAVA_OK" == "false" ]]; then
    echo -e "${RED}❌ Java 17+ no encontrado${NC}"
    echo ""
    echo "Opciones de instalación:"
    echo ""
    echo "1️⃣  Usar SDKMAN (recomendado):"
    echo "   $ curl -s 'https://get.sdkman.io' | bash"
    echo "   $ source ~/.sdkman/bin/sdkman-init.sh"
    echo "   $ sdk install java 17.0.14-tem"
    echo ""
    echo "2️⃣  Instalar desde el gestor de paquetes del sistema:"
    echo "   # Ubuntu/Debian:"
    echo "   $ sudo apt install openjdk-17-jdk"
    echo ""
    echo "   # macOS con Homebrew:"
    echo "   $ brew install openjdk@17"
    echo ""
    echo "   # Arch Linux:"
    echo "   $ sudo pacman -S jdk17-openjdk"
    echo ""
fi

if [[ "$MAVEN_OK" == "false" ]]; then
    echo -e "${RED}❌ Maven no encontrado${NC}"
    echo ""
    echo "Opciones de instalación:"
    echo ""
    echo "1️⃣  Usar SDKMAN (si ya lo tienes instalado):"
    echo "   $ sdk install maven"
    echo ""
    echo "2️⃣  Instalar desde el gestor de paquetes:"
    echo "   # Ubuntu/Debian:"
    echo "   $ sudo apt install maven"
    echo ""
    echo "   # macOS con Homebrew:"
    echo "   $ brew install maven"
    echo ""
    echo "   # Arch Linux:"
    echo "   $ sudo pacman -S maven"
    echo ""
fi

echo -e "${BLUE}💡 Nota:${NC} Este proyecto incluye un archivo .sdkmanrc configurado."
echo "   Después de instalar SDKMAN y Java/Maven, ejecuta:"
echo "   $ sdk env"
echo ""

exit $EXIT_CODE
