import java.util.*;
import java.io.*;

public class TablaSimbolos {

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

    private HashMap<String, ArrayList<NodoToken>> tablaSimbolos;
    private String currentHash;
    private String globalHash;
    private Stack<String> scopeStack;

    public TablaSimbolos() {
        tablaSimbolos = new HashMap<>();
        globalHash = "global";
        currentHash = globalHash;
        scopeStack = new Stack<>();
        tablaSimbolos.put(globalHash, new ArrayList<>());
        scopeStack.push(globalHash);
    }

    public void crearNuevoScope(String nombreScope) {
        if (!tablaSimbolos.containsKey(nombreScope)) {
            tablaSimbolos.put(nombreScope, new ArrayList<>());
        }
        currentHash = nombreScope;
        scopeStack.push(nombreScope);
    }

    public void salirDelScope() {
        if (scopeStack.size() > 1) {
            scopeStack.pop();
            currentHash = scopeStack.peek();
        }
    }

    public boolean agregarNodo(NodoToken nodo) {
        ArrayList<NodoToken> scope = tablaSimbolos.get(currentHash);
        if (scope == null) {
            scope = new ArrayList<>();
            tablaSimbolos.put(currentHash, scope);
        }

        for (NodoToken nodoExistente : scope) {
            if (nodoExistente.getId().equals(nodo.getId())) {
                System.err.println("ERROR: Símbolo '" + nodo.getId() + "' ya existe en scope '" + currentHash + "'");
                return false;
            }
        }

        scope.add(nodo);
        return true;
    }

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

    public void escribirArchivo() {
        escribirArchivo("tabla_simbolos.txt");
    }

    public void escribirArchivo(String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("=====================================");
            writer.println("        TABLA DE SÍMBOLOS");
            writer.println("=====================================\n");

            List<String> scopes = new ArrayList<>(tablaSimbolos.keySet());
            scopes.sort(Comparator.naturalOrder());

            for (String scope : scopes) {
                ArrayList<NodoToken> nodos = tablaSimbolos.get(scope);
                
                writer.println("------- SCOPE: " + scope + " -------");
                writer.println("Total de símbolos: " + nodos.size());
                writer.println();

                if (nodos.isEmpty()) {
                    writer.println("  (vacío)");
                } else {
                    writer.println(String.format("%-20s %-15s %-20s %-10s %-10s %-15s %s", 
                            "ID", "Tipo", "Categoría", "Línea", "Columna", "Valor", "Parámetros"));
                    writer.println("-".repeat(120));

                    for (NodoToken nodo : nodos) {
                        String parametrosStr = "";
                        if (!nodo.getParametros().isEmpty()) {
                            parametrosStr = nodo.getParametros().toString();
                        }

                        writer.println(String.format("%-20s %-15s %-20s %-10d %-10d %-15s %s", 
                                nodo.getId(),
                                nodo.getTipo(),
                                nodo.getCategoria(),
                                nodo.getLinea(),
                                nodo.getColumna(),
                                nodo.getValor() != null ? nodo.getValor() : "-",
                                parametrosStr));
                    }
                }

                writer.println();
            }

            writer.println("=====================================");
            writer.println("Total de scopes: " + scopes.size());
            writer.println("=====================================");

        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    public void imprimir() {
        System.out.println("\n=====================================");
        System.out.println("        TABLA DE SÍMBOLOS");
        System.out.println("=====================================\n");

        List<String> scopes = new ArrayList<>(tablaSimbolos.keySet());
        scopes.sort(Comparator.naturalOrder());

        for (String scope : scopes) {
            ArrayList<NodoToken> nodos = tablaSimbolos.get(scope);
            
            System.out.println("------- SCOPE: " + scope + " -------");
            System.out.println("Total de símbolos: " + nodos.size());
            System.out.println();

            if (nodos.isEmpty()) {
                System.out.println("  (vacío)");
            } else {
                for (NodoToken nodo : nodos) {
                    System.out.println("  " + nodo);
                }
            }

            System.out.println();
        }

        System.out.println("=====================================");
        System.out.println("Total de scopes: " + scopes.size());
        System.out.println("Scope actual: " + currentHash);
        System.out.println("=====================================\n");
    }
}
