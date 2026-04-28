import java.util.*;
import java.io.*;

/*
 * =========================================================
 *  CLASE: TablaSimbolos
 * =========================================================
 *
 * Objetivo:
 * Gestionar todos los identificadores del programa (variables,
 * funciones, parámetros y arreglos) organizados por scopes.
 *
 * Entradas:
 * - Información proveniente del parser (tipo, nombre, línea, etc.)
 *
 * Salidas:
 * - Estructura interna de símbolos por scope
 * - Archivo tabla_simbolos.txt con el reporte final
 * - Impresión en consola opcional
 *
 * Restricciones:
 * - No valida duplicados 
 * - Depende de que el parser maneje correctamente los scopes
 */
public class TablaSimbolos {

    /*
     * =========================================================
     *  CLASE INTERNA: Parametro
     * =========================================================
     *
     * Objetivo:
     * Representar los parámetros de una función.
     *
     * Entradas:
     * - tipo del parámetro
     * - nombre del parámetro
     *
     * Salidas:
     * - Objeto que describe un parámetro
     */
    public static class Parametro {
        private String tipo;
        private String nombre;

        public Parametro(String tipo, String nombre) {
            this.tipo = tipo;
            this.nombre = nombre;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String toString() {
            return tipo + " " + nombre;
        }
    }

    /*
     * =========================================================
     *  CLASE INTERNA: NodoToken
     * =========================================================
     *
     * Objetivo:
     * Representar un símbolo dentro de la tabla (variable, función, etc.)
     *
     * Entradas:
     * - tipo (int, float, etc.)
     * - identificador
     * - línea y columna
     *
     * Salidas:
     * - Objeto que contiene toda la información del símbolo
     *
     * Restricciones:
     * - El valor puede ser null si no aplica
     */
    public static class NodoToken {
        private String tipo;
        private String id;
        private String valor;
        private int linea;
        private int columna;
        private List<Parametro> parametros;
        private String categoria;

        public NodoToken(String tipo, String id, int linea, int columna) {
            this.tipo = tipo;
            this.id = id;
            this.linea = linea;
            this.columna = columna;
            this.valor = null;
            this.parametros = new ArrayList<>();
            this.categoria = "variable";
        }

        public String getTipo() {
            return tipo;
        }

        public String getId() {
            return id;
        }

        public String getValor() {
            return valor;
        }

        public int getLinea() {
            return linea;
        }

        public int getColumna() {
            return columna;
        }

        public List<Parametro> getParametros() {
            return parametros;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }

        public void setLinea(int linea) {
            this.linea = linea;
        }

        public void setColumna(int columna) {
            this.columna = columna;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public void agregarParametro(Parametro param) {
            parametros.add(param);
        }

        public void agregarParametro(String tipo, String nombre) {
            parametros.add(new Parametro(tipo, nombre));
        }

        public String toString() {
            String params = "";
            if (!parametros.isEmpty()) {
                params = ", parametros=" + parametros;
            }
            return "NodoToken{tipo='" + tipo + "', id='" + id + "', valor='" + valor + 
                   "', linea=" + linea + ", columna=" + columna + ", categoria='" + categoria + "'" + params + "}";
        }
    }

    /*
     * =========================================================
     *  ATRIBUTOS PRINCIPALES
     * =========================================================
     *
     * tablaSimbolos:
     * Mapa donde cada clave es un scope y su valor es la lista de símbolos.
     *
     * currentHash:
     * Scope actual donde se están agregando símbolos.
     *
     * scopeStack:
     * Pila para manejar scopes anidados.
     */
    private HashMap<String, ArrayList<NodoToken>> tablaSimbolos;
    private String currentHash;
    private String globalHash;
    private Stack<String> scopeStack;

    /*
     * Constructor
     *
     * Objetivo:
     * Inicializar la tabla con el scope global.
     */
    public TablaSimbolos() {
        tablaSimbolos = new HashMap<>();
        globalHash = "global";
        currentHash = globalHash;
        scopeStack = new Stack<>();
        tablaSimbolos.put(globalHash, new ArrayList<>());
        scopeStack.push(globalHash);
    }

    /*
     * Crea un nuevo scope
     *
     * Entrada:
     * - nombre del scope
     *
     * Salida:
     * - cambia el scope actual
     */
    public void crearNuevoScope(String nombreScope) {
        if (!tablaSimbolos.containsKey(nombreScope)) {
            tablaSimbolos.put(nombreScope, new ArrayList<>());
        }
        currentHash = nombreScope;
        scopeStack.push(nombreScope);
    }

    /*
     * Sale del scope actual y regresa al anterior
     */
    public void salirDelScope() {
        if (scopeStack.size() > 1) {
            scopeStack.pop();
            currentHash = scopeStack.peek();
        }
    }

    /*
     * Agrega un símbolo al scope actual
     *
     * Entrada:
     * - NodoToken con la información del símbolo
     *
     * Salida:
     * - símbolo agregado a la tabla
     */
    public boolean agregarNodo(NodoToken nodo) {
        ArrayList<NodoToken> scope = tablaSimbolos.get(currentHash);
        if (scope == null) {
            scope = new ArrayList<>();
            tablaSimbolos.put(currentHash, scope);
        }
        scope.add(nodo);
        return true;
    }

    /*
     * Busca un símbolo en el scope actual
     */
    public NodoToken buscarSimbolo(String nombre) {
        ArrayList<NodoToken> scope = tablaSimbolos.get(currentHash);
        if (scope == null) {
            return null;
        }

        for (NodoToken nodo : scope) {
            if (nodo.getId().equals(nombre)) {
                return nodo;
            }
        }
        return null;
    }

    /*
     * Busca un símbolo en un scope específico
     */
    public NodoToken buscarSimboloEnScope(String nombre, String scope) {
        ArrayList<NodoToken> scopeList = tablaSimbolos.get(scope);
        if (scopeList == null) {
            return null;
        }

        for (NodoToken nodo : scopeList) {
            if (nodo.getId().equals(nombre)) {
                return nodo;
            }
        }
        return null;
    }

    /*
     * Obtiene el scope padre
     */
    public String getParentScope() {
        if (scopeStack.size() >= 2) {
            return scopeStack.get(scopeStack.size() - 2);
        }
        return globalHash;
    }

    public String getCurrentScope() {
        return currentHash;
    }

    public String getGlobalScope() {
        return globalHash;
    }

    public Set<String> getScopes() {
        return tablaSimbolos.keySet();
    }

    public ArrayList<NodoToken> getSimbolosDelScope(String scope) {
        return tablaSimbolos.getOrDefault(scope, new ArrayList<>());
    }

    /*
     * Escribe la tabla de símbolos en archivo
     *
     * Salida:
     * - archivo tabla_simbolos.txt
     */
    public void escribirArchivo() {
        escribirArchivo("tabla_simbolos.txt");
    }

    public void escribirArchivo(String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {

            List<String> scopes = new ArrayList<>(tablaSimbolos.keySet());
            scopes.sort(Comparator.naturalOrder());

            writer.println("================================================================================================================");
            writer.println("                                                TABLA DE SÍMBOLOS");
            writer.println("================================================================================================================");
            writer.println("Total de scopes: " + scopes.size());
            writer.println();

            for (String scope : scopes) {
                ArrayList<NodoToken> nodos = tablaSimbolos.getOrDefault(scope, new ArrayList<>());

                writer.println("----------------------------------------------------------------------------------------------------------------");
                writer.println("SCOPE: " + scope);
                writer.println("Total de símbolos: " + nodos.size());
                writer.println("----------------------------------------------------------------------------------------------------------------");

                if (nodos.isEmpty()) {
                    writer.println("  (sin símbolos registrados)");
                } else {
                    writer.println(String.format(
                            "%-22s %-12s %-15s %-8s %-8s %-15s %s",
                            "Id", "Tipo", "Categoría", "Línea", "Columna", "Valor", "Parámetros"
                    ));
                    writer.println("-".repeat(112));

                    for (NodoToken nodo : nodos) {
                        String parametrosStr = nodo.getParametros().isEmpty()
                                ? "-"
                                : nodo.getParametros().toString();

                        writer.println(String.format(
                                "%-22s %-12s %-15s %-8d %-8d %-15s %s",
                                nodo.getId(),
                                nodo.getTipo(),
                                nodo.getCategoria(),
                                nodo.getLinea(),
                                nodo.getColumna(),
                                nodo.getValor() != null ? nodo.getValor() : "-",
                                parametrosStr
                        ));
                    }
                }

                writer.println();
            }

            writer.println("================================================================================================================");
            writer.println("Fin de la tabla de símbolos");
            writer.println("================================================================================================================");

        } catch (IOException e) {
            System.err.println("Error al escribir la tabla de símbolos en el archivo: " + e.getMessage());
        }
    }

    /*
     * Imprime la tabla en consola
     *
     * Objetivo:
     * Facilitar la visualización durante pruebas
     */
    public void imprimir() {
        List<String> scopes = new ArrayList<>(tablaSimbolos.keySet());
        scopes.sort(Comparator.naturalOrder());

        System.out.println();
        System.out.println("================================================================================================================");
        System.out.println("                                                TABLA DE SÍMBOLOS");
        System.out.println("================================================================================================================");
        System.out.println("Total de scopes: " + scopes.size());
        System.out.println("Scope actual    : " + currentHash);
        System.out.println();

        for (String scope : scopes) {
            ArrayList<NodoToken> nodos = tablaSimbolos.getOrDefault(scope, new ArrayList<>());

            System.out.println("----------------------------------------------------------------------------------------------------------------");
            System.out.println("SCOPE: " + scope);
            System.out.println("Total de símbolos: " + nodos.size());
            System.out.println("----------------------------------------------------------------------------------------------------------------");

            if (nodos.isEmpty()) {
                System.out.println("  (sin símbolos registrados)");
            } else {
                System.out.println(String.format(
                        "%-22s %-12s %-15s %-8s %-8s %-15s %s",
                        "Id", "Tipo", "Categoría", "Línea", "Columna", "Valor", "Parámetros"
                ));
                System.out.println("-".repeat(120));

                for (NodoToken nodo : nodos) {
                    String parametrosStr = nodo.getParametros().isEmpty()
                            ? "-"
                            : nodo.getParametros().toString();

                    System.out.println(String.format(
                            "%-22s %-12s %-15s %-8d %-8d %-15s %s",
                            nodo.getId(),
                            nodo.getTipo(),
                            nodo.getCategoria(),
                            nodo.getLinea(),
                            nodo.getColumna(),
                            nodo.getValor() != null ? nodo.getValor() : "-",
                            parametrosStr
                    ));
                }
            }

            System.out.println();
        }

        System.out.println("================================================================================================================");
        System.out.println("Fin de la tabla de símbolos");
        System.out.println("================================================================================================================");
        System.out.println();
    }
}
