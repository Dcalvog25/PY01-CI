import java.util.*;
import java.io.*;

/*
 * Clase: ManejadorErrores
 *
 * Objetivo:
 * Centralizar el registro de errores léxicos y sintácticos encontrados
 * durante el análisis del archivo fuente.
 *
 * Entradas:
 * - Mensajes de error enviados por el lexer o el parser.
 * - Línea y columna donde ocurre el error.
 *
 * Salidas:
 * - Archivo errores.txt con el reporte de errores.
 * - Mensajes en consola para facilitar la revisión durante la ejecución.
 *
 * Restricciones:
 * - La línea y columna recibidas se manejan desde cero, por eso se suma 1
 *   antes de mostrarlas al usuario.
 * - El archivo debe cerrarse al finalizar el análisis para escribir el total
 *   de errores correctamente.
 */
public class ManejadorErrores {
    /*
     * Lista interna donde se almacenan todos los errores encontrados.
     * Permite saber si hubo errores y calcular el total al final.
     */
    private List<String> errores;

    /*
     * Objeto encargado de escribir el reporte de errores en un archivo externo.
     */
    private PrintWriter errorWriter;

    /*
     * Constructor de la clase.
     *
     * Objetivo:
     * Inicializar la lista de errores y crear el archivo errores.txt.
     *
     * Entradas:
     * - No recibe parámetros.
     *
     * Salidas:
     * - Crea el archivo errores.txt con el encabezado del reporte.
     *
     * Restricciones:
     * - Puede lanzar una excepción si el archivo no puede crearse o abrirse.
     */
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
    
    /*
     * Registra un error léxico.
     *
     * Objetivo:
     * Guardar errores producidos por caracteres o lexemas no válidos.
     *
     * Entradas:
     * - mensaje: descripción del error.
     * - linea: línea donde ocurre el error.
     * - columna: columna donde ocurre el error.
     *
     * Salidas:
     * - Envía el error al método general de registro.
     *
     * Restricciones:
     * - Se utiliza únicamente para errores detectados por el lexer.
     */
    public void agregarErrorLexico(String mensaje, int linea, int columna) {
        registrarError("Léxico", mensaje, linea, columna);
    }

    /*
     * Registra un error sintáctico.
     *
     * Objetivo:
     * Guardar errores producidos cuando la secuencia de tokens no cumple
     * con la gramática definida.
     *
     * Entradas:
     * - mensaje: descripción del error.
     * - linea: línea donde ocurre el error.
     * - columna: columna donde ocurre el error.
     *
     * Salidas:
     * - Envía el error al método general de registro.
     *
     * Restricciones:
     * - Se utiliza únicamente para errores detectados por el parser.
     */
    public void agregarErrorSintactico(String mensaje, int linea, int columna) {
        registrarError("Sintáctico", mensaje, linea, columna);
    }

    /*
     * Registra un error en la lista interna, en el archivo y en consola.
     *
     * Objetivo:
     * Evitar repetir código entre errores léxicos y sintácticos.
     *
     * Entradas:
     * - tipo: tipo de error, por ejemplo Léxico o Sintáctico.
     * - mensaje: descripción del problema encontrado.
     * - linea: línea recibida desde el lexer/parser.
     * - columna: columna recibida desde el lexer/parser.
     *
     * Salidas:
     * - Agrega el error a la lista.
     * - Escribe el error en errores.txt.
     * - Imprime el error en consola.
     *
     * Restricciones:
     * - La línea y columna se ajustan sumando 1 para mostrarlas en formato humano.
     */
    private void registrarError(String tipo, String mensaje, int linea, int columna) {
        int l = linea + 1;
        int c = columna + 1;

        String error = String.format("%-12s %-8d %-8d %s", tipo, l, c, mensaje);
        errores.add(error);
        errorWriter.println(error);
        errorWriter.flush();

        imprimirEnConsola(tipo, mensaje, l, c);
    }

    /*
     * Imprime un error en consola.
     *
     * Objetivo:
     * Mostrar el error de forma clara durante la ejecución del programa.
     *
     * Entradas:
     * - tipo: tipo de error.
     * - mensaje: descripción del error.
     * - linea: línea ya ajustada para mostrarse al usuario.
     * - columna: columna ya ajustada para mostrarse al usuario.
     *
     * Salidas:
     * - Mensaje de error en la consola.
     *
     * Restricciones:
     * - Solo imprime información; no modifica la lista de errores.
     */
    private void imprimirEnConsola(String tipo, String mensaje, int linea, int columna) {
        System.err.println("Error " + tipo);
        System.err.println("  Línea   : " + linea);
        System.err.println("  Columna : " + columna);
        System.err.println("  Mensaje : " + mensaje);
        System.err.println();
    }

    /*
     * Cierra el archivo de errores.
     *
     * Objetivo:
     * Finalizar correctamente el reporte escribiendo el total de errores.
     *
     * Entradas:
     * - No recibe parámetros.
     *
     * Salidas:
     * - Agrega el total de errores al archivo.
     * - Cierra errores.txt.
     *
     * Restricciones:
     * - Debe llamarse al finalizar el análisis para evitar que el archivo quede incompleto.
     */
    public void cerrar() {
        errorWriter.println("------------------------------------------------------");
        errorWriter.println("Total de Errores: " + errores.size());
        errorWriter.println("======================================================");
        errorWriter.close();
    }

    /*
     * Indica si se registraron errores.
     *
     * Objetivo:
     * Permitir que el programa principal sepa si el análisis terminó con errores.
     *
     * Entradas:
     * - No recibe parámetros.
     *
     * Salidas:
     * - true si hay errores registrados.
     * - false si no hay errores.
     *
     * Restricciones:
     * - Solo consulta la lista, no modifica datos.
     */
    public boolean hayErrores() {
        return !errores.isEmpty();
    }

    /*
     * Retorna la cantidad total de errores encontrados.
     *
     * Objetivo:
     * Facilitar el resumen del análisis.
     *
     * Entradas:
     * - No recibe parámetros.
     *
     * Salidas:
     * - Número total de errores almacenados.
     *
     * Restricciones:
     * - Solo consulta la lista de errores.
     */
    public int getTotalErrores() {
        return errores.size();
    }
}
