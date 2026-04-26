import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Lexer lexer = null;
        parser p = null;

        try {
            System.out.print("Ingrese el nombre del archivo: ");
            String nombreArchivo = input.nextLine();

            File archivo = new File(nombreArchivo);

            if (!archivo.exists()) {
                System.err.println("El archivo no existe.");
                return;
            }

            lexer = new Lexer(new FileReader(archivo));
            p = new parser(lexer);

            p.parse();

            if (lexer.getManejadorErrores().hayErrores()) {
                System.out.println("Análisis finalizado con errores.");
            } else {
                System.out.println("Compilación exitosa.");
            }

        } catch (Exception e) {
            System.err.println("Análisis finalizado con errores.");
        } finally {
            if (p != null) {
                p.getTabla().escribirArchivo();
            }

            if (lexer != null) {
                lexer.cerrar();
            }

            input.close();
        }
    }
}