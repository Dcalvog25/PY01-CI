import java_cup.runtime.*;

%%

%class Lexer
%public
%unicode
%line
%column
%cup

%{
    StringBuffer string = new StringBuffer();

    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

letra = [a-zA-Z_]
digito = [0-9]
digitoNoCero = [1-9]
id = {letra}({digito}|{letra})*
entero = 0|{digitoNoCero}{digito}*
flotante = (0\.0|((0|-?{digitoNoCero}{digito}*)\.{digito}*{digitoNoCero}0*))
exponencial = {entero}e{entero}
fraccion = -?{entero}/{entero}

// Comentario Linea
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Comentario1Linea = "ii" {InputCharacter}* {LineTerminator}?

//Espacio
espacio = {LineTerminator} | [ \t\f]

%state CADENA
%state COMENTARIO

%%

/* palabras reservadas */
<YYINITIAL> "if"        { return symbol(sym.IF); }
<YYINITIAL> "else"      { return symbol(sym.ELSE); }
<YYINITIAL> "do"        { return symbol(sym.DO); }
<YYINITIAL> "while"     { return symbol(sym.WHILE); }
<YYINITIAL> "switch"    { return symbol(sym.SWITCH); }
<YYINITIAL> "case"      { return symbol(sym.CASE); }
<YYINITIAL> "default"   { return symbol(sym.DEFAULT); }
<YYINITIAL> "break"     { return symbol(sym.BREAK); }
<YYINITIAL> "return"    { return symbol(sym.RETURN); }
<YYINITIAL> "cin"       { return symbol(sym.CIN); }
<YYINITIAL> "cout"      { return symbol(sym.COUT); }
<YYINITIAL> "empty"     { return symbol(sym.EMPTY); }
<YYINITIAL> "__main__"  { return symbol(sym.MAIN); }
<YYINITIAL> "string"    { return symbol(sym.STRING); }
<YYINITIAL> "char"      { return symbol(sym.CHAR); }
<YYINITIAL> "float"     { return symbol(sym.FLOAT); }
<YYINITIAL> "bool"      { return symbol(sym.BOOL); }
<YYINITIAL> "int"       { return symbol(sym.INT); }
<YYINITIAL> "true"      { return symbol(sym.TRUE); }
<YYINITIAL> "false"     { return symbol(sym.FALSE); }

<YYINITIAL> "equal"        { return symbol(sym.EQUAL); }
<YYINITIAL> "n_equal"      { return symbol(sym.N_EQUAL); }
<YYINITIAL> "less_t"       { return symbol(sym.LESS_T); }
<YYINITIAL> "less_te"      { return symbol(sym.LESS_TE); }
<YYINITIAL> "greather_t"   { return symbol(sym.GREATHER_T); }
<YYINITIAL> "greather_te"  { return symbol(sym.GREATHER_TE); }

<YYINITIAL> {
    /* identificadores */
    {id}    { return symbol(sym.ID, yytext()); }

    /* literales */
    {exponencial}  { return symbol(sym.EXPONENCIAL, yytext()); }
    {fraccion}     { return symbol(sym.FRACCION, yytext()); }
    {flotante}     { return symbol(sym.FLOTANTE, yytext()); }
    {entero}       { return symbol(sym.ENTERO, yytext()); }
    \"  { string.setLength(0); yybegin(CADENA); }

    /* operadores */
    "<-"  { return symbol(sym.ASIGNAR); }
    "<<"  { return symbol(sym.INICIO_INDICES); }
    ">>"  { return symbol(sym.FINAL_INDICES); }
    "<|"  { return symbol(sym.INICIO_PAREN); }
    "|>"  { return symbol(sym.FINAL_PAREN); }
    "|:"  { return symbol(sym.INICIO_BLOQUE); }
    ":|"  { return symbol(sym.FINAL_BLOQUE); }
    "++"  { return symbol(sym.MASMAS); }
    "--"  { return symbol(sym.MENOSMENOS); }
    "~"   { return symbol(sym.SEPARADOR); }
    "!"   { return symbol(sym.FIN_EXPR); }
    "+"   { return symbol(sym.SUMA); }
    "-"   { return symbol(sym.RESTA); }
    "*"   { return symbol(sym.MULTI); }
    "/"   { return symbol(sym.DIV); }
    "%"   { return symbol(sym.MOD); }
    "^"   { return symbol(sym.POT); }
    "#"   { return symbol(sym.OR); }
    "@"   { return symbol(sym.AND); }
    "$"   { return symbol(sym.NOT); }
    ","   { return symbol(sym.COMA); }
    ":"   { return symbol(sym.DOS_PUNTOS); }
}

/* Comentario 1 Linea */
<YYINITIAL> {Comentario1Linea} { /* se ignora */ }

/* Comentario en bloque */
<YYINITIAL> "{-" { yybegin(COMENTARIO); }
<COMENTARIO> "-}" { yybegin(YYINITIAL); }
<COMENTARIO> [^-\r\n]+ { }
<COMENTARIO> "-" { }
<COMENTARIO> {LineTerminator} { } 

/* Espacio */
<YYINITIAL> {espacio} { /* se ignora */ }

<CADENA> {
      \"                  { yybegin(YYINITIAL); 
                            return symbol(sym.STRING_LITERAL, 
                            string.toString()); }
      [^\n\r\"\\]+        { string.append( yytext() ); }
      \\t                 { string.append('\t'); }
      \\n                 { string.append('\n'); }

      \\r                 { string.append('\r'); }
      \\\"                { string.append('\"'); }
      \\                  { string.append('\\'); }
    }

/* Manejo de error */
<YYINITIAL> . { System.out.println("Error léxico: " + yytext() + " en línea " + yyline); }