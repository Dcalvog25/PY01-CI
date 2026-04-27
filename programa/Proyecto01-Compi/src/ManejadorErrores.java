import java.util.*;
import java.io.*;

public class ManejadorErrores {
    private List<String> errores;
    private PrintWriter errorWriter;
    
    public ManejadorErrores() throws Exception {
        this.errores = new ArrayList<>();
        this.errorWriter = new PrintWriter(new FileWriter("errores.txt"));
        errorWriter.println("======================================================");
        errorWriter.println("                REPORTE DE ERRORES");
        errorWriter.println("======================================================");
        errorWriter.println(String.format("%-12s %-8s %-8s %s",
                "Tipo", "Línea", "Columna", "Mensaje"));
        errorWriter.println("------------------------------------------------------");
    }
    
    public void agregarErrorLexico(String mensaje, int linea, int columna) {
        registrarError("Léxico", mensaje, linea, columna);
    }

    public void agregarErrorSintactico(String mensaje, int linea, int columna) {
        registrarError("Sintáctico", mensaje, linea, columna);
    }

    private void registrarError(String tipo, String mensaje, int linea, int columna) {
        int l = linea + 1;
        int c = columna + 1;

        String error = String.format("%-12s %-8d %-8d %s", tipo, l, c, mensaje);
        errores.add(error);
        errorWriter.println(error);
        errorWriter.flush();

        imprimirEnConsola(tipo, mensaje, l, c);
    }

    private void imprimirEnConsola(String tipo, String mensaje, int linea, int columna) {
        System.err.println("Error " + tipo);
        System.err.println("  Línea   : " + linea);
        System.err.println("  Columna : " + columna);
        System.err.println("  Mensaje : " + mensaje);
        System.err.println();
    }

    public void cerrar() {
        errorWriter.println("------------------------------------------------------");
        errorWriter.println("Total de Errores: " + errores.size());
        errorWriter.println("======================================================");
        errorWriter.close();
    }

    public boolean hayErrores() {
        return !errores.isEmpty();
    }

    public int getTotalErrores() {
        return errores.size();
    }
}
