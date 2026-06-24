# AVL Tree - Java Implementation

## Descripción

Implementación completa de un **Árbol AVL** (Adelson-Velsky y Landis) en Java, con operaciones de inserción, eliminación y búsqueda con balanceo automático mediante rotaciones.

---

## 👥 Integrantes

- Ambar Salazar
- Brenda Veintimilla 

---
## Características

- **Inserción** con rebalanceo automático (LL, RR, LR, RL)
- **Eliminación** con rebalanceo posterior
- **Búsqueda** con conteo de comparaciones
- **Recorridos**: Inorden, Preorden, Postorden
- **Registro** de todas las rotaciones realizadas
- **Comparación** explícita BST vs AVL
- **Factor de equilibrio** para cada nodo
- **Validación** de cédula (10 dígitos)

---

## 📁 Estructura del Proyecto
avl-tree-java/
├── src/
│ ├── Nodo.java # Clase Nodo del árbol
│ ├── AVLTree.java # Implementación del AVL
│ └── Main.java # Programa principal
├── README.md # Documentación
└── .gitignore

### Clases

| Clase | Descripción |
|-------|-------------|
| `Nodo` | Representa un nodo con valor, altura e hijos |
| `AVLTree` | Implementación del árbol AVL con rotaciones |
| `Main` | Programa principal con menú y lógica |

---

## Generación de Valores

### Cédula: **1751167998**

| Variable | Cálculo | Valor |
|----------|---------|-------|
| V₁ | (1 × 10) + 1 | **11** |
| V₂ | (7 × 10) + 2 | **72** |
| V₃ | (5 × 10) + 3 | **53** |
| V₄ | (1 × 10) + 4 | **14** |
| V₅ | (1 × 10) + 5 | **15** |
| V₆ | (6 × 10) + 6 | **66** |
| V₇ | (7 × 10) + 7 | **77** |
| V₈ | (9 × 10) + 8 | **98** |
| V₉ | (9 × 10) + 9 | **99** |
| V₁₀ | (8 × 10) + 10 | **90** |

**Valores auxiliares:**
- A = 111 + d₉ = 111 + 9 = **120**
- B = 122 + d₁₀ = 122 + 8 = **130**

---

## Resultados

### Rotaciones Realizadas

| Inserción | Valor | Nodo Desbalanceado | Rotación |
|-----------|-------|-------------------|----------|
| 4 | 14 | 72 | **RL** |
| 5 | 15 | 11 | **RR** |
| 9 | 99 | 77 | **RR** |

**Total: 3 rotaciones**

### Comparación BST vs AVL

| Métrica | BST | AVL | Mejora |
|---------|-----|-----|--------|
| **Altura del árbol** | 7 | 4 | **42.8%** |
| **Comparaciones V₁₀ (90)** | 6 | 4 | **33.3%** |

---
## Estructura del Código

El proyecto está estructurado con las siguientes clases principales para la implementación del árbol AVL:

* [Nodo.java](Nodo.java): Define la estructura básica de cada nodo del árbol (clave, altura, e hijos izquierdo/derecho).
* [AVLTree.java](AVLTree.java): Contiene la lógica principal del árbol AVL, incluyendo las rotaciones, inserciones y el balanceo automático.
* [Main.java](Main.java): Punto de entrada del programa donde se ejecutan las pruebas y la comparación de rendimiento.

---

## Compilación y Ejecución

### Requisitos
- Java 8 o superior instalado
- `javac` disponible en el PATH

### Compilación

```bash
# Navegar al directorio del proyecto
cd avl-tree-java/src

# Compilar todos los archivos
javac *.java

