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

        /*
         * Constructor de Parametro
         *
         * Objetivo:
         * Crear instancia de un parámetro.
         *
         * Entrada:
         * - tipo: tipo de dato.
         * - nombre: identificador.
         *
         * Salida:
         * - Parametro inicializado.
         */
        public Parametro(String tipo, String nombre) {
            this.tipo = tipo;
            this.nombre = nombre;
        }

        /*
         * Obtiene el tipo del parámetro
         *
         * Salida:
         * - String con el tipo de dato.
         */
        public String getTipo() {
            return tipo;
        }

        /*
         * Asigna el tipo del parámetro
         *
         * Entrada:
         * - tipo: nuevo tipo de dato.
         */
        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        /*
         * Obtiene el nombre del parámetro
         *
         * Salida:
         * - String con el identificador.
         */
        public String getNombre() {
            return nombre;
        }

        /*
         * Asigna el nombre del parámetro
         *
         * Entrada:
         * - nombre: nuevo identificador.
         */
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        /*
         * Retorna representación en String
         *
         * Salida:
         * - Formato: "tipo nombre".
         */
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

        /*
         * Constructor de NodoToken
         *
         * Objetivo:
         * Crear nodo para almacenar un símbolo.
         *
         * Entrada:
         * - tipo: tipo de dato.
         * - id: identificador.
         * - linea: línea en el código.
         * - columna: columna en el código.
         *
         * Salida:
         * - NodoToken listo. Categoría por defecto: "variable".
         */

        public NodoToken(String tipo, String id, int linea, int columna) {
            this.tipo = tipo;
            this.id = id;
            this.linea = linea;
            this.columna = columna;
            this.valor = null;
            this.parametros = new ArrayList<>();
            this.categoria = "variable";
        }

        /*
         * Obtiene el tipo del símbolo
         *
         * Salida:
         * - String con el tipo de dato.
         */
        public String getTipo() {
            return tipo;
        }

        /*
         * Obtiene el identificador
         *
         * Salida:
         * - String con el id del símbolo.
         */
        public String getId() {
            return id;
        }

        /*
         * Obtiene el valor del símbolo
         *
         * Salida:
         * - String con el valor. Puede ser null.
         */
        public String getValor() {
            return valor;
        }

        /*
         * Obtiene la línea de aparición
         *
         * Salida:
         * - Entero con la línea.
         */
        public int getLinea() {
            return linea;
        }

        /*
         * Obtiene la columna de aparición
         *
         * Salida:
         * - Entero con la columna.
         */
        public int getColumna() {
            return columna;
        }

        /*
         * Obtiene la lista de parámetros
         *
         * Salida:
         * - Lista con los parámetros de la función.
         */
        public List<Parametro> getParametros() {
            return parametros;
        }

        /*
         * Obtiene la categoría del símbolo
         *
         * Salida:
         * - String: "variable", "función", etc.
         */
        public String getCategoria() {
            return categoria;
        }

        /*
         * Asigna el tipo del símbolo
         *
         * Entrada:
         * - tipo: nuevo tipo de dato.
         */
        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        /*
         * Asigna el valor del símbolo
         *
         * Entrada:
         * - valor: nuevo valor.
         */
        public void setValor(String valor) {
            this.valor = valor;
        }

        /*
         * Asigna la línea de aparición
         *
         * Entrada:
         * - linea: nueva línea.
         */
        public void setLinea(int linea) {
            this.linea = linea;
        }

        /*
         * Asigna la columna de aparición
         *
         * Entrada:
         * - columna: nueva columna.
         */
        public void setColumna(int columna) {
            this.columna = columna;
        }

        /*
         * Asigna la categoría del símbolo
         *
         * Entrada:
         * - categoria: nueva categoría.
         */
        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        /*
         * Agrega un parámetro al símbolo
         *
         * Objetivo:
         * Registrar parámetro de función.
         *
         * Entrada:
         * - param: objeto Parametro.
         *
         * Salida:
         * - Parámetro agregado a la lista.
         */
        public void agregarParametro(Parametro param) {
            parametros.add(param);
        }

        /*
         * Agrega un parámetro con tipo y nombre
         *
         * Objetivo:
         * Registrar parámetro creando el objeto.
         *
         * Entrada:
         * - tipo: tipo del parámetro.
         * - nombre: id del parámetro.
         *
         * Salida:
         * - Parametro nuevo en la lista.
         */
        public void agregarParametro(String tipo, String nombre) {
            parametros.add(new Parametro(tipo, nombre));
        }

        /*
         * Retorna representación en String
         *
         * Salida:
         * - Formato: NodoToken con todos los atributos.
         */
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
    private HashMap<String, String> scopePadres = new HashMap<>();

    /*
     * Constructor
     *
     * Objetivo:
     * Inicializar la tabla con el scope global.
     *
     * Entrada:
     * - No recibe parámetros.
     *
     * Salida:
     * - TablaSimbolos lista para usar.
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
     * Objetivo:
     * Abrir contexto nuevo para símbolos (función, bloque if, etc).
     *
     * Entrada:
     * - nombreScope: identificador del nuevo scope.
     *
     * Salida:
     * - Scope registrado y activado como actual.
     *
     * Restricciones:
     * - Registra el scope padre automáticamente.
     */
    public void crearNuevoScope(String nombreScope) {
        if (!tablaSimbolos.containsKey(nombreScope)) {
            tablaSimbolos.put(nombreScope, new ArrayList<>());
        }
        scopePadres.put(nombreScope, currentHash);
        currentHash = nombreScope;
        scopeStack.push(nombreScope);
    }

    /*
     * Sale del scope actual y regresa al anterior
     *
     * Objetivo:
     * Cerrar contexto actual y volver al scope padre.
     *
     * Entrada:
     * - No recibe parámetros.
     *
     * Salida:
     * - Scope actual actualizado al anterior.
     *
     * Restricciones:
     * - No sale del scope global; siempre hay al menos uno.
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
     * Objetivo:
     * Registrar variable, función o parámetro.
     *
     * Entrada:
     * - nodo: información del identificador (tipo, id, línea, columna).
     *
     * Salida:
     * - true si se agregó. Símbolo en el scope actual.
     *
     * Restricciones:
     * - No valida duplicados; lo hace el parser.
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
     *
     * Objetivo:
     * Localizar un identificador en el contexto activo.
     *
     * Entrada:
     * - nombre: id del símbolo buscado.
     *
     * Salida:
     * - NodoToken si existe; null si no.
     *
     * Restricciones:
     * - Solo busca en scope actual, no en padres.
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
     *
     * Objetivo:
     * Consultar identificador en un scope particular.
     *
     * Entrada:
     * - nombre: id del símbolo.
     * - scope: nombre del scope donde buscar.
     *
     * Salida:
     * - NodoToken si existe; null si no.
     */
    public NodoToken buscarSimboloEnScope(String nombre, String scope) {
        ArrayList<NodoToken> scopeList = tablaSimbolos.get(scope);
        if (scopeList == null) {
            return null;
        }

        NodoToken result = null;
        for(NodoToken nodo : scopeList) {
            if (nodo.getId().equals(nombre)) {
                result = nodo; 
            }
        }
        return result;
    }

    /*
     * Obtiene el scope padre del scope actual
     *
     * Objetivo:
     * Retornar el contexto padre en la jararquía.
     *
     * Entrada:
     * - No recibe parámetros.
     *
     * Salida:
     * - Nombre del scope padre o "global" si no hay.
     */
    public String getParentScope() {
        if (scopeStack.size() >= 2) {
            return scopeStack.get(scopeStack.size() - 2);
        }
        return globalHash;
    }
    /*
     * Obtiene el scope padre de un scope específico
     *
     * Objetivo:
     * Consultar quién es el padre de otro scope.
     *
     * Entrada:
     * - scope: nombre del scope consultado.
     *
     * Salida:
     * - Nombre del padre o "global" si no existe.
     */
    public String getPadreScope(String scope) {
        
        return scopePadres.getOrDefault(scope, globalHash);
    }

    /*
     * Retorna el scope actual
     *
     * Objetivo:
     * Consultar el contexto activo.
     *
     * Entrada:
     * - No recibe parámetros.
     *
     * Salida:
     * - Nombre del scope donde se agregan símbolos.
     */
    public String getCurrentScope() {
        return currentHash;
    }

    /*
     * Retorna el scope global
     *
     * Objetivo:
     * Obtener referencia al contexto raíz.
     *
     * Entrada:
     * - No recibe parámetros.
     *
     * Salida:
     * - "global".
     */
    public String getGlobalScope() {
        return globalHash;
    }

    /*
     * Obtiene todos los scopes registrados
     *
     * Objetivo:
     * Iterar sobre todos los contextos.
     *
     * Entrada:
     * - No recibe parámetros.
     *
     * Salida:
     * - Set con nombres de todos los scopes.
     */
    public Set<String> getScopes() {
        return tablaSimbolos.keySet();
    }

    /*
     * Obtiene todos los símbolos de un scope específico
     *
     * Objetivo:
     * Recuperar lista completa de identificadores.
     *
     * Entrada:
     * - scope: nombre del contexto.
     *
     * Salida:
     * - ArrayList con los símbolos. Lista vacía si no existe.
     */
    public ArrayList<NodoToken> getSimbolosDelScope(String scope) {
        return tablaSimbolos.getOrDefault(scope, new ArrayList<>());
    }

    /*
     * Escribe la tabla de símbolos en archivo por defecto
     *
     * Objetivo:
     * Generar reporte formateado.
     *
     * Entrada:
     * - No recibe parámetros.
     *
     * Salida:
     * - Archivo tabla_simbolos.txt.
     */
    public void escribirArchivo() {
        escribirArchivo("tabla_simbolos.txt");
    }

    /*
     * Escribe la tabla de símbolos en archivo especificado
     *
     * Objetivo:
     * Exportar tabla con formato tabular.
     *
     * Entrada:
     * - nombreArchivo: ruta y nombre del archivo.
     *
     * Salida:
     * - Archivo con estructura de scopes y símbolos.
     */
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
                writer.println("SCOPE: " + scope + "  (padre: " + scopePadres.getOrDefault(scope, "-") + ")");
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
     * Mostrar tabla en pantalla para depuraci\u00f3n.\n     *
     * Entrada:
     * - No recibe par\u00e1metros.
     *
     * Salida:
     * - Impresi\u00f3n formateada en consola.
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
