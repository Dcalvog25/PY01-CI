import java_cup.runtime.*;
import java.io.*;

%%

/*
 * =========================================================
 *  ANALIZADOR LÉXICO (JFlex)
 * =========================================================
 * Este archivo define el lexer del lenguaje.
 * Su función es leer el código fuente y convertirlo en tokens
 * que luego serán utilizados por el parser (CUP).
 */

%class Lexer
%public
%unicode
%line
%column
%cup

%{
    // Manejador de errores léxicos
    ManejadorErrores manejadorErrores;

    // Archivo donde se guardan los tokens generados
    PrintWriter tokenWriter;

    /*
     * Bloque de inicialización:
     * Se ejecuta cuando se crea el lexer.
     * Aquí se abren los archivos de salida.
     */

    {
        try {
            tokenWriter = new PrintWriter(new FileWriter("tokens.txt"));
            manejadorErrores = new ManejadorErrores();
        } catch (IOException e) {
            System.err.println("Error al abrir archivos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al inicializar ManejadorErrores: " + e.getMessage());
        }
    }

    /*
     * Se debe llamar al finalizar el análisis léxico para asegurar que los archivos se cierren correctamente.
     */
    public void cerrar() {
        if (tokenWriter != null) tokenWriter.close();
        if (manejadorErrores != null) manejadorErrores.cerrar();
    }

    // Buffer para construir cadenas de texto
    StringBuffer string = new StringBuffer();

    // Crea un símbolo sin valor asociado
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    // Crea un símbolo con valor asociado
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }

    // Permite acceder al manejador de errores desde el parser
    public ManejadorErrores getManejadorErrores() {
        return manejadorErrores;
    }
%}

/*
 * ==========================
 *  EXPRESIONES REGULARES
 * ==========================
 */

letra        = [a-zA-Z_]
digito       = [0-9]
digitoNoCero = [1-9]
id           = {letra}({digito}|{letra})*
entero       = 0|{digitoNoCero}{digito}*
flotante     = (0\.0|((0|-?{digitoNoCero}{digito}*)\.{digito}*{digitoNoCero}0*))
exponencial  = {entero}e{entero}
fraccion     = -?{entero}(\/\/?){entero}
charLiteral  = \' [^\'\n] \'

LineTerminator   = \r|\n|\r\n
InputCharacter   = [^\r\n]
Comentario1Linea = "ii" {InputCharacter}* {LineTerminator}?
espacio          = {LineTerminator} | [ \t\f]

%state CADENA
%state COMENTARIO

%%

/*
 * ================================
 *  ESTADO INICIAL (YYINITIAL)
 * ================================
 */

<YYINITIAL> {

    {Comentario1Linea}  { /* ignorar */ }
    "{-"                { yybegin(COMENTARIO); }

    "if"          
    { 
        tokenWriter.println("IF\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.IF); 
    }

    "else"        
    { 
        tokenWriter.println("ELSE\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.ELSE); 
    }
    "do"          
    { 
        tokenWriter.println("DO\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.DO); 
    }
    "while"       
    { 
        tokenWriter.println("WHILE\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.WHILE); 
    }
    "switch"      
    { 
        tokenWriter.println("SWITCH\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.SWITCH); 
    }
    "case"        
    { 
        tokenWriter.println("CASE\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.CASE); 
    }
    "default"     
    { 
        tokenWriter.println("DEFAULT\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.DEFAULT); 
    }
    "break"       
    { 
        tokenWriter.println("BREAK\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.BREAK); 
    }
    "return"      
    { 
        tokenWriter.println("RETURN\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.RETURN); 
    }
    "cin"         
    { 
        tokenWriter.println("CIN\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.CIN); 
    }
    "cout"        
    { 
        tokenWriter.println("COUT\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.COUT); 
    }
    "empty"       
    { 
        tokenWriter.println("EMPTY\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.EMPTY); 
    }
    "__main__"    
    { 
        tokenWriter.println("MAIN\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.MAIN); 
    }
    "string"      
    { 
        tokenWriter.println("STRING\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.STRING, yytext()); 
    }
    "char"        
    { 
        tokenWriter.println("CHAR\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.CHAR, yytext()); 
    }
    "float"       
    { 
        tokenWriter.println("FLOAT\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.FLOAT, yytext()); 
    }
    "bool"        
    { 
        tokenWriter.println("BOOL\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.BOOL, yytext()); 
    }
    "int"         
    { 
        tokenWriter.println("INT\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.INT, yytext()); 
    }
    "true"        
    { 
        tokenWriter.println("TRUE\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.TRUE); 
    }
    "false"       
    { 
        tokenWriter.println("FALSE\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.FALSE); 
    }
    "equal"       
    { 
        tokenWriter.println("EQUAL\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.EQUAL); 
    }
    "n_equal"     
    { 
        tokenWriter.println("N_EQUAL\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.N_EQUAL); 
    }
    "less_t"      
    { 
        tokenWriter.println("LESS_T\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.LESS_T); 
    }
    "less_te"     
    { 
        tokenWriter.println("LESS_TE\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.LESS_TE); 
    }
    "greather_t"  
    { 
        tokenWriter.println("GREATHER_T\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.GREATHER_T); 
    }
    "greather_te" 
    { 
        tokenWriter.println("GREATHER_TE\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.GREATHER_TE); 
    }

    {id}  
    {
        tokenWriter.println("ID\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1));
        return symbol(sym.ID, yytext());
    }

    {exponencial}  
    { 
        tokenWriter.println("EXPONENCIAL\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1));
        return symbol(sym.EXPONENCIAL, yytext()); 
    }

    {fraccion}     
    { 
        tokenWriter.println("FRACCION\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1));
        return symbol(sym.FRACCION, yytext()); 
    }

    {flotante}     
    { 
        tokenWriter.println("FLOTANTE\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1));
        return symbol(sym.FLOTANTE, yytext()); 
    }

    {entero}       
    { 
        tokenWriter.println("ENTERO\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1));
        return symbol(sym.ENTERO, yytext()); 
    }

    {charLiteral}  
    { 
        tokenWriter.println("CHAR_LIT\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1));
        return symbol(sym.CHAR_LIT, yytext()); 
    }

    \"  { string.setLength(0); yybegin(CADENA); }

    "<-"  
    { 
        tokenWriter.println("ASIGNAR\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1));
        return symbol(sym.ASIGNAR); 
    }
    "<<"  
    { 
        tokenWriter.println("INICIO_INDICES\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1));
        return symbol(sym.INICIO_INDICES); 
    }
    ">>"  
    { 
        tokenWriter.println("FINAL_INDICES\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1));
        return symbol(sym.FINAL_INDICES); 
    }
    "<|"  
    { 
        tokenWriter.println("INICIO_PAREN\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1));
        return symbol(sym.INICIO_PAREN); 
    }
    "|>"  
    { 
        tokenWriter.println("FINAL_PAREN\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1));
        return symbol(sym.FINAL_PAREN); 
    }
    "|:"  
    { 
        tokenWriter.println("INICIO_BLOQUE\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1));
        return symbol(sym.INICIO_BLOQUE); 
    }
    ":|"  
    { 
        tokenWriter.println("FINAL_BLOQUE\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1));
        return symbol(sym.FINAL_BLOQUE); 
    }
    "++"  
    { 
        tokenWriter.println("MASMAS\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.MASMAS); 
    }
    "--"  
    { 
        tokenWriter.println("MENOSMENOS\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.MENOSMENOS); 
    }
    "+"   
    { 
        tokenWriter.println("SUMA\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.SUMA); 
    }
    "-"   
    { 
        tokenWriter.println("RESTA\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.RESTA); 
    }
    "*"   
    { 
        tokenWriter.println("MULTI\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.MULTI); 
    }
    "/"   
    { 
        tokenWriter.println("DIV\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.DIV); 
    }
    "%"   
    { 
        tokenWriter.println("MOD\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.MOD); 
    }
    "^"   
    { 
        tokenWriter.println("POT\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.POT); 
    }
    "#"   
    { 
        tokenWriter.println("OR\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.OR); 
    }
    "@"   
    { 
        tokenWriter.println("AND\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.AND); 
    }
    "$"   
    { 
        tokenWriter.println("NOT\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.NOT); 
    }
    "~"   
    { 
        tokenWriter.println("SEPARADOR\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.SEPARADOR); 
    }
    "!"   
    { 
        tokenWriter.println("FIN_EXPR\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.FIN_EXPR); 
    }
    ","   
    { 
        tokenWriter.println("COMA\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.COMA); 
    }
    ":"   
    { 
        tokenWriter.println("DOS_PUNTOS\t" + yytext() + "\t" + (yyline+1) + "\t" + (yycolumn+1)); 
        return symbol(sym.DOS_PUNTOS); 
    }

    {espacio}  { /* ignorar */ }

     /*
     * ========================
     * ERROR LÉXICO
     * ========================
     * Si no coincide con nada, se reporta error
     */
    .  { 
        String errorMsg = "Carácter no válido: '" + yytext() + "'";
        if (manejadorErrores != null) {
            manejadorErrores.agregarErrorLexico(errorMsg, yyline, yycolumn);
        } else {
            System.err.println("Error Léxico [Línea " + (yyline + 1) + ", Columna " + (yycolumn + 1) + "]: " + errorMsg);
        }
    }
}

/*
 * =========================================================
 *  ESTADO COMENTARIO
 * =========================================================
 * Ignora todo hasta encontrar -}
 */
<COMENTARIO> {
    "-}"             { yybegin(YYINITIAL); }
    [^-\r\n]+        { }
    "-"              { }
    {LineTerminator} { }
}

/*
 * =========================================================
 *  ESTADO CADENA
 * =========================================================
 * Construye el contenido de una cadena
 */
<CADENA> {
    \"              { 
                        yybegin(YYINITIAL);
                        String valor = string.toString();
                        tokenWriter.println("STRING_LITERAL\t\"" + valor + "\"\t" + (yyline+1) + "\t" + (yycolumn+1));
                        return symbol(sym.STRING_LITERAL, valor);
                    } 
    [^\n\r\"\\]+     { string.append(yytext()); }
    \\t              { string.append('\t'); }
    \\n              { string.append('\n'); }
    \\r              { string.append('\r'); }
    \\\"             { string.append('\"'); }
    \\\\             { string.append('\\'); }

    //Manejo de error para cadena no cerrada
    {LineTerminator} { 
        String errorMsg = "Cadena no cerrada";
        if (manejadorErrores != null) {
            manejadorErrores.agregarErrorLexico(errorMsg, yyline, yycolumn);
        } else {
            System.err.println("Error Léxico [Línea " + (yyline + 1) + ", Columna " + (yycolumn + 1) + "]: " + errorMsg);
        }
        yybegin(YYINITIAL); 
    }
}