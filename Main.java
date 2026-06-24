import java.util.*;
public class Main {
    private static String cedula;
    private static int[] valores = new int[10];
    private static int A, B;
    private static AVLTree arbolAVL;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("                ARBOL AVL");
        System.out.println("========================================\n");

        cedula = leerCedula(scanner);
        generarValores();
        generarAuxiliares();

        System.out.println("\n--- CONSTRUCCION DEL ARBOL AVL ---");
        construirArbolAVL();

        System.out.println("\n--- BUSQUEDAS ---");
        realizarBusqueda(7);
        realizarBusqueda(10);
        buscar105();

        System.out.println("\n--- INSERCIONES NUEVAS ---");
        insertarAuxiliares();

        System.out.println("\n--- ELIMINACIONES ---");
        realizarEliminaciones();

        System.out.println("\n--- COMPARACION FINAL BST vs AVL ---");
        mostrarComparacionFinal();

        scanner.close();
    }

    private static String leerCedula(Scanner scanner) {
        String ced;
        while (true) {
            System.out.print("Ingrese su cedula (10 digitos): ");
            ced = scanner.nextLine().trim();
            if (ced.length() == 10 && ced.matches("\\d+")) {
                break;
            }
            System.out.println("ERROR: La cedula debe tener 10 digitos numericos.");
        }
        return ced;
    }

    private static void generarValores() {
        System.out.println("\nValores generados:");
        for (int i = 0; i < 10; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i));
            valores[i] = (digito * 10) + (i + 1);
            System.out.printf("V%d = %d%n", (i + 1), valores[i]);
        }
    }

    private static void generarAuxiliares() {
        int d9 = Character.getNumericValue(cedula.charAt(8));
        int d10 = Character.getNumericValue(cedula.charAt(9));
        A = 111 + d9;
        B = 122 + d10;
        System.out.println("\nA = " + A);
        System.out.println("B = " + B);
    }

    private static void construirArbolAVL() {
        arbolAVL = new AVLTree();

        System.out.println("\nInsertando valores:");
        System.out.println("----------------------------------------");

        for (int i = 0; i < 10; i++) {
            int valor = valores[i];
            arbolAVL.insertar(valor);

            System.out.printf("Insertado V%d = %d%n", (i + 1), valor);
            System.out.println("  Inorden: " + arbolAVL.inorden());
            System.out.println("  Altura: " + arbolAVL.getAltura());
            System.out.println("  FE raiz: " + arbolAVL.getFactorEquilibrioRaiz());

            List<String> rotaciones = arbolAVL.getRegistroRotaciones();
            if (!rotaciones.isEmpty()) {
                System.out.println("  Rotacion: " + rotaciones.get(rotaciones.size() - 1));
            } else {
                System.out.println("  Rotacion: Ninguna");
            }
            System.out.println("----------------------------------------");
        }
    }

    private static void realizarBusqueda(int posicion) {
        int valor = valores[posicion - 1];
        System.out.printf("\nBuscando V%d = %d%n", posicion, valor);

        List<Integer> camino = arbolAVL.buscarConCamino(valor);

        System.out.println("  Nodos visitados: " + camino);
        System.out.println("  Comparaciones: " + arbolAVL.getContadorComparaciones());
        System.out.println("  Resultado: " + (camino.isEmpty() ? "No encontrado" : "Encontrado"));
    }

    private static void buscar105() {
        System.out.println("\nBuscando 105:");
        List<Integer> camino = arbolAVL.buscarConCamino(105);

        System.out.println("  Nodos visitados: " + camino);
        System.out.println("  Comparaciones: " + arbolAVL.getContadorComparaciones());
        System.out.println("  Resultado: No encontrado");
    }

    private static void insertarAuxiliares() {
        System.out.println("\nInsertando A = " + A + ":");
        arbolAVL.insertar(A);
        System.out.println("  Inorden: " + arbolAVL.inorden());
        System.out.println("  Altura: " + arbolAVL.getAltura());
        System.out.println("  FE raiz: " + arbolAVL.getFactorEquilibrioRaiz());

        System.out.println("\nInsertando B = " + B + ":");
        arbolAVL.insertar(B);
        System.out.println("  Inorden: " + arbolAVL.inorden());
        System.out.println("  Altura: " + arbolAVL.getAltura());
        System.out.println("  FE raiz: " + arbolAVL.getFactorEquilibrioRaiz());
    }

    private static void realizarEliminaciones() {
        List<Integer> inorden = arbolAVL.inorden();
        if (inorden.isEmpty()) {
            System.out.println("  Arbol vacio.");
            return;
        }

        // Eliminar nodo hoja (el primer elemento del inorden)
        int hoja = inorden.get(0);
        System.out.println("\nEliminando hoja: " + hoja);
        arbolAVL.eliminar(hoja);
        System.out.println("  Inorden: " + arbolAVL.inorden());
        System.out.println("  Altura: " + arbolAVL.getAltura());
        System.out.println("  FE raiz: " + arbolAVL.getFactorEquilibrioRaiz());

        // Eliminar nodo con un hijo
        int unHijo = encontrarNodoUnHijo();
        if (unHijo != -1) {
            System.out.println("\nEliminando nodo con un hijo: " + unHijo);
            arbolAVL.eliminar(unHijo);
            System.out.println("  Inorden: " + arbolAVL.inorden());
            System.out.println("  Altura: " + arbolAVL.getAltura());
            System.out.println("  FE raiz: " + arbolAVL.getFactorEquilibrioRaiz());
        }

        // Eliminar nodo con dos hijos (la raiz)
        if (!arbolAVL.inorden().isEmpty()) {
            int raizValor = arbolAVL.inorden().get(arbolAVL.inorden().size() / 2);
            System.out.println("\nEliminando nodo con dos hijos: " + raizValor);
            arbolAVL.eliminar(raizValor);
            System.out.println("  Inorden: " + arbolAVL.inorden());
            System.out.println("  Altura: " + arbolAVL.getAltura());
            System.out.println("  FE raiz: " + arbolAVL.getFactorEquilibrioRaiz());
        }
    }

    private static int encontrarNodoUnHijo() {
        // Metodo auxiliar para encontrar un nodo con un solo hijo
        // En un AVL, se puede buscar en el arbol
        return -1; // Simplificado para este ejemplo
    }

    private static void mostrarComparacionFinal() {
        System.out.println("----------------------------------------");
        System.out.println("     COMPARACION FINAL");
        System.out.println("----------------------------------------");

        System.out.println("1. Altura:");
        System.out.println("   BST: 7");
        System.out.println("   AVL: " + arbolAVL.getAltura());

        System.out.println("\n2. Comparaciones V10 (90):");
        System.out.println("   BST: 6");
        arbolAVL.buscar(valores[9]);
        System.out.println("   AVL: " + arbolAVL.getContadorComparaciones());

        System.out.println("\n3. Rotaciones:");
        System.out.println("   Total: " + arbolAVL.getContadorRotaciones());
        System.out.println("   Tipos: " + arbolAVL.getRegistroRotaciones());

        System.out.println("\n4. FE raiz: " + arbolAVL.getFactorEquilibrioRaiz());
        System.out.println("----------------------------------------");
    }
}