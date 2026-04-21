import java.util.*;
import java.io.*;

public class ManejadorErrores {
    private List<String> errores;
    private PrintWriter errorWriter;
    
    public ManejadorErrores() throws Exception {
        this.errores = new ArrayList<>();
        this.errorWriter = new PrintWriter(new FileWriter("errores.txt"));
        this.errorWriter.println("=== REPORTE DE ERRORES ===");
        this.errorWriter.println("Tipo\t\tLínea\tColumna\tMensaje");
        this.errorWriter.println("----------------------------------------");
    }
    
    public void agregarErrorLexico(String mensaje, int linea, int columna) {
        String error = String.format("LÉXICO\t\t%d\t%d\t%s", 
            linea + 1, columna + 1, mensaje);
        errores.add(error);
        errorWriter.println(error);
        errorWriter.flush();
        System.err.println("ERROR LÉXICO [Línea " + (linea + 1) + ", Columna " + (columna + 1) + "]: " + mensaje);
    }
    
    public void agregarErrorSintactico(String mensaje, int linea, int columna) {
        String error = String.format("SINTÁCTICO\t%d\t%d\t%s", 
            linea + 1, columna + 1, mensaje);
        errores.add(error);
        errorWriter.println(error);
        errorWriter.flush();
        System.err.println("ERROR SINTÁCTICO [Línea " + (linea + 1) + ", Columna " + (columna + 1) + "]: " + mensaje);
    }
    
    public void cerrar() {
        errorWriter.println("----------------------------------------");
        errorWriter.println("Total de errores: " + errores.size());
        if (errorWriter != null) errorWriter.close();
    }
    
    public boolean hayErrores() {
        return !errores.isEmpty();
    }
    
    public int getTotalErrores() {
        return errores.size();
    }
}
