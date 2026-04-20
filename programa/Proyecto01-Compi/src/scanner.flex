import java_cup.runtime.*;
import java.io.*;

%%

%class Lexer
%public
%unicode
%line
%column
%cup

%{
    TabSimb tablaSimbolos = new TabSimb();

    PrintWriter tokenWriter;

    {
        try {
            tokenWriter = new PrintWriter(new FileWriter("tokens.txt"));
        } catch (IOException e) {
            System.err.println("Error al abrir tokens.txt: " + e.getMessage());
        }
    }

    public void cerrar() {
        if (tokenWriter != null) tokenWriter.close();
        tablaSimbolos.escribirArchivo("tabla_simbolos.txt");
    }

    StringBuffer string = new StringBuffer();

    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

letra        = [a-zA-Z_]
digito       = [0-9]
digitoNoCero = [1-9]
id           = {letra}({digito}|{letra})*
entero       = 0|{digitoNoCero}{digito}*
flotante     = (0\.0|((0|-?{digitoNoCero}{digito}*)\.{digito}*{digitoNoCero}0*))
exponencial  = {entero}e{entero}
fraccion     = -?{entero}\/{entero}
charLiteral  = \' [^\'\n] \'

LineTerminator   = \r|\n|\r\n
InputCharacter   = [^\r\n]
Comentario1Linea = "ii" {InputCharacter}* {LineTerminator}?
espacio          = {LineTerminator} | [ \t\f]

%state CADENA
%state COMENTARIO

%%

<YYINITIAL> {

    {Comentario1Linea}  { /* ignorar */ }
    "{-"                { yybegin(COMENTARIO); }

    "if"          
    { 
        tokenWriter.println("Token: IF\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.IF, yyline, yycolumn, yytext()); 
    }

    "else"        
    { 
        tokenWriter.println("Token: ELSE\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.ELSE, yyline, yycolumn, yytext()); 
    }
    "do"          
    { 
        tokenWriter.println("Token: DO\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.DO, yyline, yycolumn, yytext()); 
    }
    "while"       
    { 
        tokenWriter.println("Token: WHILE\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.WHILE, yyline, yycolumn, yytext()); 
    }
    "switch"      
    { 
        tokenWriter.println("Token: SWITCH\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.SWITCH, yyline, yycolumn, yytext()); 
    }
    "case"        
    { 
        tokenWriter.println("Token: CASE\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.CASE, yyline, yycolumn, yytext()); 
    }
    "default"     
    { 
        tokenWriter.println("Token: DEFAULT\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.DEFAULT, yyline, yycolumn, yytext()); 
    }
    "break"       
    { 
        tokenWriter.println("Token: BREAK\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.BREAK, yyline, yycolumn, yytext()); 
    }
    "return"      
    { 
        tokenWriter.println("Token: RETURN\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.RETURN, yyline, yycolumn, yytext()); 
        }
    "cin"         
    { 
        tokenWriter.println("Token: CIN\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.CIN, yyline, yycolumn, yytext()); 
    }
    "cout"        
    { 
        tokenWriter.println("Token: COUT\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.COUT, yyline, yycolumn, yytext()); 
    }
    "empty"       
    { 
        tokenWriter.println("Token: EMPTY\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.EMPTY, yyline, yycolumn, yytext()); 
    }
    "__main__"    
    { 
        tokenWriter.println("Token: MAIN\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.MAIN, yyline, yycolumn, yytext()); 
    }
    "string"      
    { 
        tokenWriter.println("Token: STRING\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.STRING, yyline, yycolumn, yytext()); 
    }
    "char"        
    { 
        tokenWriter.println("Token: CHAR\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.CHAR, yyline, yycolumn, yytext()); 
    }
    "float"       
    { 
        tokenWriter.println("Token: FLOAT\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.FLOAT, yyline, yycolumn, yytext()); 
    }
    "bool"        
    { 
        tokenWriter.println("Token: BOOL\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.BOOL, yyline, yycolumn, yytext()); 
    }
    "int"         
    { 
        tokenWriter.println("Token: INT\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.INT, yyline, yycolumn, yytext()); 
    }
    "true"        
    { 
        tokenWriter.println("Token: TRUE\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.TRUE, yyline, yycolumn, yytext()); 
        }
    "false"       
    { 
        tokenWriter.println("Token: FALSE\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.FALSE, yyline, yycolumn, yytext()); 
    }
    "equal"       
    { 
        tokenWriter.println("Token: EQUAL\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.EQUAL, yyline, yycolumn, yytext()); 
    }
    "n_equal"     
    { 
        tokenWriter.println("Token: N_EQUAL\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.N_EQUAL, yyline, yycolumn, yytext()); 
    }
    "less_t"      
    { 
        tokenWriter.println("Token: LESS_T\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.LESS_T, yyline, yycolumn, yytext()); 
    }
    "less_te"     
    { 
        tokenWriter.println("Token: LESS_TE\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.LESS_TE, yyline, yycolumn, yytext()); 
    }
    "greather_t"  
    { 
        tokenWriter.println("Token: GREATHER_T\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.GREATHER_T, yyline, yycolumn, yytext()); 
    }
    "greather_te" 
    { 
        tokenWriter.println("Token: GREATHER_TE\tLexema: " + yytext() + "\tTabla: keywords\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.GREATHER_TE, yyline, yycolumn, yytext()); 
    }

    {id}  {
                if (!tablaSimbolos.existe(yytext())) {
                    tablaSimbolos.agregar(yytext(), "-", "identificador", yyline + 1, yycolumn + 1);
                }
                tokenWriter.println("Token: ID\tLexema: " + yytext() + "\tTabla: tablaSimbolos\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1));
                return new Symbol(sym.ID, yyline, yycolumn, yytext());
          }

    {exponencial}  
    { 
        tokenWriter.println("Token: EXPONENCIAL\tLexema: " + yytext() + "\tTabla: constantes\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.EXPONENCIAL, yyline, yycolumn, yytext()); 
    }
    {fraccion}     
    { 
        tokenWriter.println("Token: FRACCION\tLexema: " + yytext() + "\tTabla: constantes\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.FRACCION, yyline, yycolumn, yytext()); 
    }
    {flotante}     
    { 
        tokenWriter.println("Token: FLOTANTE\tLexema: " + yytext() + "\tTabla: constantes\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.FLOTANTE, yyline, yycolumn, yytext()); 
    }
    {entero}       
    { 
        tokenWriter.println("Token: ENTERO\tLexema: " + yytext() + "\tTabla: constantes\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.ENTERO, yyline, yycolumn, yytext()); 
    }
    {charLiteral}  
    { 
        tokenWriter.println("Token: CHAR_LIT\tLexema: " + yytext() + "\tTabla: constantes\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.CHAR_LIT, yyline, yycolumn, yytext()); 
    }

    \"  { string.setLength(0); yybegin(CADENA); }

    "<-"  
    { 
        tokenWriter.println("Token: ASIGNAR\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.ASIGNAR, yyline, yycolumn, yytext()); 
    }
    "<<"  
    { 
        tokenWriter.println("Token: INICIO_INDICES\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.INICIO_INDICES, yyline, yycolumn, yytext()); 
    }
    ">>"  
    { 
        tokenWriter.println("Token: FINAL_INDICES\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.FINAL_INDICES, yyline, yycolumn, yytext()); 
    }
    "<|"  
    { 
        tokenWriter.println("Token: INICIO_PAREN\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.INICIO_PAREN, yyline, yycolumn, yytext()); 
    }
    "|>"  
    { 
        tokenWriter.println("Token: FINAL_PAREN\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.FINAL_PAREN, yyline, yycolumn, yytext()); 
    }
    "|:"  
    { 
        tokenWriter.println("Token: INICIO_BLOQUE\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.INICIO_BLOQUE, yyline, yycolumn, yytext()); 
    }
    ":|"  
    { tokenWriter.println("Token: FINAL_BLOQUE\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
    return new Symbol(sym.FINAL_BLOQUE, yyline, yycolumn, yytext()); 
    }
    "++"  
    { 
        tokenWriter.println("Token: MASMAS\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.MASMAS, yyline, yycolumn, yytext()); 
    }
    "--"  
    { 
        tokenWriter.println("Token: MENOSMENOS\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.MENOSMENOS, yyline, yycolumn, yytext()); 
    }
    "+"   
    { 
        tokenWriter.println("Token: SUMA\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.SUMA, yyline, yycolumn, yytext()); 
    }
    "-"   
    { 
        tokenWriter.println("Token: RESTA\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.RESTA, yyline, yycolumn, yytext()); 
    }
    "*"   
    { 
        tokenWriter.println("Token: MULTI\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.MULTI, yyline, yycolumn, yytext()); 
    }
    "/"   
    { 
        tokenWriter.println("Token: DIV\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.DIV, yyline, yycolumn, yytext()); 
    }
    "%"   
    { 
        tokenWriter.println("Token: MOD\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.MOD, yyline, yycolumn, yytext()); 
    }
    "^"   
    { 
        tokenWriter.println("Token: POT\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.POT, yyline, yycolumn, yytext()); 
    }
    "#"   
    { 
        tokenWriter.println("Token: OR\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.OR, yyline, yycolumn, yytext()); 
    }
    "@"   
    { 
        tokenWriter.println("Token: AND\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.AND, yyline, yycolumn, yytext()); 
    }
    "$"   
    { 
        tokenWriter.println("Token: NOT\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.NOT, yyline, yycolumn, yytext()); 
    }
    "~"   
    { 
        tokenWriter.println("Token: SEPARADOR\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.SEPARADOR, yyline, yycolumn, yytext()); 
    }
    "!"   
    { 
        tokenWriter.println("Token: FIN_EXPR\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.FIN_EXPR, yyline, yycolumn, yytext()); 
    }
    ","   
    { 
        tokenWriter.println("Token: COMA\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.COMA, yyline, yycolumn, yytext()); 
        }
    ":"   
    { 
        tokenWriter.println("Token: DOS_PUNTOS\tLexema: " + yytext() + "\tTabla: operadores\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1)); 
        return new Symbol(sym.DOS_PUNTOS, yyline, yycolumn, yytext()); 
    }

    {espacio}  { /* ignorar */ }

    .  { System.out.println("Error lexico: '" + yytext() + "' en linea " + (yyline + 1) + ", columna " + (yycolumn + 1)); }
}

<COMENTARIO> {
    "-}"             { yybegin(YYINITIAL); }
    [^-\r\n]+        { }
    "-"              { }
    {LineTerminator} { }
}

<CADENA> {
    \"               { yybegin(YYINITIAL);
                       String valor = string.toString();
                       tokenWriter.println("Token: STRING_LITERAL\tLexema: \"" + valor + "\"\tTabla: constantes\tLinea: " + (yyline+1) + "\tCol: " + (yycolumn+1));
                       return new Symbol(sym.STRING_LITERAL, yyline, yycolumn, valor); }
    [^\n\r\"\\]+     { string.append(yytext()); }
    \\t              { string.append('\t'); }
    \\n              { string.append('\n'); }
    \\r              { string.append('\r'); }
    \\\"             { string.append('\"'); }
    \\\\             { string.append('\\'); }
    {LineTerminator} { System.out.println("Error lexico: cadena no cerrada en linea " + (yyline + 1) + ", columna " + (yycolumn + 1));
                       yybegin(YYINITIAL); }
}