import java.util.*;
import java.io.*;

public class TabSimb {

    public static class Simbolo {
        private String nombre;
        private String tipo;
        private String categoria;
        private int linea;
        private int columna;
        private String valor;

        public Simbolo(String nombre, String tipo, String categoria, int linea, int columna) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.categoria = categoria;
            this.linea = linea;
            this.columna = columna;
            this.valor = null;
        }

        public Simbolo(String nombre, String tipo, String categoria, int linea, int columna, String valor) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.categoria = categoria;
            this.linea = linea;
            this.columna = columna;
            this.valor = valor;
        }
    }

    private HashMap<String, Simbolo> tabla;

    public TabSimb() {
        tabla = new HashMap<>();
    }

    public void agregar(String nombre, String tipo, String categoria, int linea, int columna) {
        if(!tabla.containsKey(nombre)) {
            Simbolo simbolo = new Simbolo(nombre, tipo, categoria, linea, columna);
            tabla.put(nombre, simbolo);
        }
    }

    public void agregar(String nombre, String tipo, String categoria, int linea, int columna, String valor) {
        if(!tabla.containsKey(nombre)) {
            Simbolo simbolo = new Simbolo(nombre, tipo, categoria, linea, columna, valor);
            tabla.put(nombre, simbolo);
        }
    }

    public boolean existe(String nombre) {
        return tabla.containsKey(nombre);
    }

    public Simbolo obtener(String nombre) {
        return tabla.get(nombre);
    }

    public void actualizarTipo(String nombre, String nuevoTipo) {
        if(tabla.containsKey(nombre)) {
            Simbolo simbolo = tabla.get(nombre);
            simbolo.tipo = nuevoTipo;
        }
    }

    public void actualizarValor(String nombre, String nuevoValor) {
        if(tabla.containsKey(nombre)) {
            Simbolo simbolo = tabla.get(nombre);
            simbolo.valor = nuevoValor;
        }
    }

    public void escribirenarchivo(String archivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            writer.println("=== Tabla de Símbolos ===");
            writer.println("Nombre\tTipo\tCategoría\tLínea\tColumna\tValor");

            List<Simbolo> simbolos = new ArrayList<>(tabla.values());
            simbolos.sort(Comparator.comparing(s -> s.nombre));

            for (Simbolo simbolo : simbolos) {
                writer.printf("%s\t%s\t%s\t%d\t%d\t%s%n",
                        simbolo.nombre,
                        simbolo.tipo,
                        simbolo.categoria,
                        simbolo.linea,
                        simbolo.columna,
                        simbolo.valor != null ? simbolo.valor : "null");
            }
            writer.println("============================================");
            writer.println("Total de símbolos: " + simbolos.size());
            writer.println("============================================");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
        }
            
    }
}