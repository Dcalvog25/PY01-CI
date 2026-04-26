import java.io.FileReader;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        try {
            System.out.print("Ingrese el nombre del archivo: ");
            String nombreArchivo = input.nextLine();

            // Validar si el archivo existe
            File archivo = new File(nombreArchivo);
            if (!archivo.exists()) {
                System.err.println("El archivo no existe.");
                return;
            }

            // Crear lexer
            FileReader reader = new FileReader(archivo);
            Lexer lexer = new Lexer(reader);

            // Crear parser
            parser p = new parser(lexer);

            // Ejecutar análisis
            p.parse();

            // Cerrar manejador de errores
            lexer.cerrar();

            // Resultado final
            if (lexer.getManejadorErrores().hayErrores()) {
                System.out.println("Análisis finalizado con errores.");
            } else {
                System.out.println("Compilación exitosa.");
            }

        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
            e.printStackTrace();
        } finally {
            input.close();
        }
    }
}
