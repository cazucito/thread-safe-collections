#!/usr/bin/env bash
#
# Script interactivo para crear nuevas demos en thread-safe-collections
# Genera estructura base, registra en DemoRegistry y crea test stub
#

set -euo pipefail

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# Variables del proyecto
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$SCRIPT_DIR"
PACKAGE_PATH="io/github/cazucito/threadsafecollections"
MAIN_SRC_DIR="$PROJECT_DIR/src/main/java/$PACKAGE_PATH"
TEST_SRC_DIR="$PROJECT_DIR/src/test/java/$PACKAGE_PATH"
DEMO_PACKAGE="demo"

# Detectar Maven
MVN="${PROJECT_DIR}/mvnw"
if [[ ! -f "$MVN" ]]; then
    MVN="mvn"
fi

# Funciones auxiliares
print_header() {
    echo -e "${CYAN}"
    echo "╔════════════════════════════════════════════════════════════╗"
    echo "║       thread-safe-collections - Nueva Demo                 ║"
    echo "╚════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

print_success() {
    echo -e "${GREEN}✓${NC} $1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
}

print_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

# Validar kebab-case
validate_kebab_case() {
    local name="$1"
    if [[ ! "$name" =~ ^[a-z0-9]+(-[a-z0-9]+)*$ ]]; then
        return 1
    fi
    return 0
}

# Convertir kebab-case a PascalCase
kebab_to_pascal() {
    local kebab="$1"
    local pascal=""
    local capitalize_next=true
    
    for (( i=0; i<${#kebab}; i++ )); do
        local char="${kebab:$i:1}"
        if [[ "$char" == "-" ]]; then
            capitalize_next=true
        elif [[ "$capitalize_next" == true ]]; then
            pascal+="${char^^}"
            capitalize_next=false
        else
            pascal+="$char"
        fi
    done
    
    echo "$pascal"
}

# Convertir kebab-case a camelCase
kebab_to_camel() {
    local pascal="$(kebab_to_pascal "$1")"
    echo "${pascal,}"
}

# Generar el archivo de demo
generate_demo_file() {
    local demo_id="$1"
    local demo_title="$2"
    local learning_objective="$3"
    local pascal_name="$(kebab_to_pascal "$demo_id")"
    local file_path="$MAIN_SRC_DIR/$DEMO_PACKAGE/${pascal_name}Demo.java"
    
    cat > "$file_path" << EOF
package ${PACKAGE_PATH//\//.}.$DEMO_PACKAGE;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import io.github.cazucito.threadsafecollections.concurrency.ConcurrentTestScenario;

/**
 * ${demo_title}
 */
public final class ${pascal_name}Demo implements Demo {

    // TODO: Ajustar estos valores según la necesidad de la demo
    private static final int THREAD_COUNT = 5;
    private static final int TIMEOUT_SECONDS = 2;

    @Override
    public String id() {
        return "${demo_id}";
    }

    @Override
    public String title() {
        return "${demo_title}";
    }

    @Override
    public String learningObjective() {
        return "${learning_objective}";
    }

    @Override
    public String expectedObservation() {
        // TODO: Describir qué comportamiento conviene observar
        return "TODO: Describe qué salida o comportamiento observar durante la ejecución.";
    }

    @Override
    public String keyTakeaway() {
        // TODO: Resumir la conclusión principal
        return "TODO: Resume la conclusión principal que deja esta demo.";
    }

    @Override
    public DemoResult run() {
        DemoResult.Builder result = DemoResult.builder(id(), title());

        // TODO: Implementar la demostración
        result.add(MessageType.SUBTITLE, "Demostración: ${demo_title}");
        result.add(MessageType.INFO, "Esta es una demo generada automáticamente.");
        result.add(MessageType.INFO, "Edita el archivo ${pascal_name}Demo.java para implementar el comportamiento deseado.");

        // Ejemplo de uso de ConcurrentTestScenario:
        // ConcurrentTestScenario scenario = ConcurrentTestScenario.withoutVerification(THREAD_COUNT, TIMEOUT_SECONDS);
        // scenario.executeWithCollection(result, collection, (coll, index) -> new YourAdder(coll, String.valueOf(index)));
        // ConcurrentTestScenario.appendSizeResult(result, THREAD_COUNT, collection.size());

        return result.build();
    }
}
EOF

    echo "$file_path"
}

# Generar test stub
generate_test_file() {
    local demo_id="$1"
    local demo_title="$2"
    local pascal_name="$(kebab_to_pascal "$demo_id")"
    local file_path="$TEST_SRC_DIR/${pascal_name}DemoTest.java"
    
    cat > "$file_path" << EOF
package ${PACKAGE_PATH//\//.};

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.github.cazucito.threadsafecollections.demo.${pascal_name}Demo;
import io.github.cazucito.threadsafecollections.demo.DemoResult;
import org.junit.jupiter.api.Test;

/**
 * Pruebas para ${pascal_name}Demo.
 */
class ${pascal_name}DemoTest {

    @Test
    void demoHasRequiredMetadata() {
        ${pascal_name}Demo demo = new ${pascal_name}Demo();

        assertEquals("${demo_id}", demo.id());
        assertFalse(demo.title().isBlank());
        assertFalse(demo.learningObjective().isBlank());
        assertFalse(demo.expectedObservation().isBlank());
        assertFalse(demo.keyTakeaway().isBlank());
    }

    @Test
    void demoRunsSuccessfully() {
        ${pascal_name}Demo demo = new ${pascal_name}Demo();

        DemoResult result = demo.run();

        assertEquals(demo.id(), result.demoId());
        assertEquals(demo.title(), result.title());
        assertFalse(result.messages().isEmpty());
    }
}
EOF

    echo "$file_path"
}

# Actualizar DemoRegistry
update_demo_registry() {
    local demo_id="$1"
    local pascal_name="$(kebab_to_pascal "$demo_id")"
    local registry_file="$MAIN_SRC_DIR/$DEMO_PACKAGE/DemoRegistry.java"
    
    # Crear backup
    cp "$registry_file" "${registry_file}.bak"
    
    # Usar awk para insertar la nueva demo después de ConcurrentHashMapDemo
    # que típicamente es la última alfabéticamente
    # Nota: el último elemento de List.of() no lleva coma
    awk -v new_demo="new ${pascal_name}Demo()" '
        /new ConcurrentHashMapDemo\(\)/ {
            # Reemplazar Demo() por Demo(), para agregar coma
            sub(/Demo\(\)/, "Demo(),")
            print
            printf "                %s\n", new_demo
            next
        }
        { print }
    ' "$registry_file" > "${registry_file}.tmp"
    
    mv "${registry_file}.tmp" "$registry_file"
    
    # Limpiar backup
    rm "${registry_file}.bak"
    
    echo "$registry_file"
}

# Verificar compilación
verify_compilation() {
    print_info "Verificando que el proyecto compila..."
    
    if ! "$MVN" compile -q; then
        return 1
    fi
    
    return 0
}

# Main
main() {
    print_header
    
    # Verificar que estamos en el directorio correcto
    if [[ ! -f "$PROJECT_DIR/pom.xml" ]]; then
        print_error "No se encontró pom.xml. Ejecuta este script desde la raíz del proyecto."
        exit 1
    fi
    
    # Solicitar datos interactivamente
    echo -e "${CYAN}Configuración de la nueva demo${NC}"
    echo ""
    
    # ID de la demo
    local demo_id=""
    while true; do
        echo -en "${BLUE}?${NC} ID de la demo (kebab-case, ej: mi-nueva-demo): "
        read -r demo_id
        
        if [[ -z "$demo_id" ]]; then
            print_error "El ID no puede estar vacío."
            continue
        fi
        
        if ! validate_kebab_case "$demo_id"; then
            print_error "El ID debe estar en kebab-case (solo minúsculas, números y guiones)."
            echo "   Ejemplos válidos: my-demo, concurrent-list, test-123"
            continue
        fi
        
        # Verificar que no exista ya
        local pascal_check="$(kebab_to_pascal "$demo_id")"
        if [[ -f "$MAIN_SRC_DIR/$DEMO_PACKAGE/${pascal_check}Demo.java" ]]; then
            print_error "Ya existe una demo con ID '$demo_id'."
            continue
        fi
        
        break
    done
    
    # Título
    local demo_title=""
    while [[ -z "$demo_title" ]]; do
        echo -en "${BLUE}?${NC} Título legible (ej: Mi Nueva Colección): "
        read -r demo_title
        
        if [[ -z "$demo_title" ]]; then
            print_error "El título no puede estar vacío."
        fi
    done
    
    # Objetivo de aprendizaje
    local learning_objective=""
    while [[ -z "$learning_objective" ]]; do
        echo -en "${BLUE}?${NC} Objetivo de aprendizaje: "
        read -r learning_objective
        
        if [[ -z "$learning_objective" ]]; then
            print_error "El objetivo no puede estar vacío."
        fi
    done
    
    echo ""
    print_info "Resumen de la nueva demo:"
    echo "  ID:       $demo_id"
    echo "  Título:   $demo_title"
    echo "  Objetivo: $learning_objective"
    echo ""
    
    echo -en "${BLUE}?${NC} ¿Crear la demo? [Y/n]: "
    read -r confirm
    
    if [[ "$confirm" =~ ^[Nn]$ ]]; then
        print_warning "Operación cancelada."
        exit 0
    fi
    
    echo ""
    print_info "Generando archivos..."
    
    # Generar archivos
    local demo_file
    demo_file=$(generate_demo_file "$demo_id" "$demo_title" "$learning_objective")
    print_success "Demo creada: $demo_file"
    
    local test_file
    test_file=$(generate_test_file "$demo_id" "$demo_title")
    print_success "Test creado: $test_file"
    
    local registry_file
    registry_file=$(update_demo_registry "$demo_id")
    print_success "Registro actualizado: $registry_file"
    
    echo ""
    print_info "Verificando compilación..."
    
    if verify_compilation; then
        print_success "Proyecto compila correctamente!"
    else
        print_error "El proyecto no compila. Revisa los errores arriba."
        print_warning "Archivos creados - puedes corregir manualmente."
        exit 1
    fi
    
    echo ""
    echo -e "${GREEN}╔════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${GREEN}║  ¡Demo '$demo_id' creada exitosamente!                     ║${NC}"
    echo -e "${GREEN}╚════════════════════════════════════════════════════════════╝${NC}"
    echo ""
    echo "Pasos siguientes:"
    echo "  1. Edita: $demo_file"
    echo "  2. Implementa el método run() con tu lógica de demostración"
    echo "  3. Completa los TODOs (expectedObservation, keyTakeaway)"
    echo "  4. Ejecuta: make demo-single DEMO=$demo_id"
    echo "  5. Ejecuta tests: make test"
    echo ""
    echo "Para ver todas las demos disponibles:"
    echo "  make list"
}

# Ejecutar main
main "$@"
