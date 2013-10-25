/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ensamblador;

/**
 *
 * @author samsung
 */
public class Directivas {
    boolean formaCorrecta; 
    String error;
    int valor;
    
    public boolean ORG(String ope, ModosDireccionamiento dir) {
        formaCorrecta = true;
        error = null;
        valor = 0;
        if(ope.charAt(0) == '@' || ope.charAt(0) == '$' || ope.charAt(0) == '%' || ope.matches("(-)?[0-9]+")) {    
            if(ope.charAt(0) == '@' && ope.substring(1, ope.length()).matches("[0-7]+"))
                valor = dir.ValorOperando(ope, 16);                 
            else if(ope.charAt(0) == '$' && ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+"))
                valor = dir.ValorOperando(ope, 16);
            else if(ope.charAt(0) == '%' && ope.substring(1, ope.length()).matches("[0-1]+"))
                valor = dir.ValorOperando(ope, 16);
            else if(ope.matches("(-)?[0-9]+"))
                valor = Integer.parseInt(ope);  
            else {
                error = "Formato Erroneo de Operando para Directiva ORG";
                formaCorrecta = false;
            }
        }
        else {
            error = "Formato Erroneo de Operando para Directiva ORG";
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null) {
            if(valor >= 0 && valor <= 65535)
                formaCorrecta = true;
            else {
                error = "Operando Fuera de Rango para Directiva ORG";
                formaCorrecta = false;
                }            
        }
        return formaCorrecta;
    }
    
    public boolean EQU(String ope, ModosDireccionamiento dir) {
        formaCorrecta = true;
        error = null;
        valor = 0;
        if(ope.charAt(0) == '@' || ope.charAt(0) == '$' || ope.charAt(0) == '%' || ope.matches("(-)?[0-9]+")) {    
            if(ope.charAt(0) == '@' && ope.substring(1, ope.length()).matches("[0-7]+"))
                valor = dir.ValorOperando(ope, 16);                 
            else if(ope.charAt(0) == '$' && ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+"))
                valor = dir.ValorOperando(ope, 16);
            else if(ope.charAt(0) == '%' && ope.substring(1, ope.length()).matches("[0-1]+"))
                valor = dir.ValorOperando(ope, 16);
            else if(ope.matches("(-)?[0-9]+"))
                valor = Integer.parseInt(ope);  
            else {
                error = "Formato Erroneo de Operando para Directiva EQU";
                formaCorrecta = false;
            }
        }
        else {
            error = "Formato Erroneo de Operando para Directiva EQU";
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null) {
            if(valor >= 0 && valor <= 65535)
                formaCorrecta = true;
            else {
                error = "Operando Fuera de Rango para Directiva EQU";
                formaCorrecta = false;
                }            
        }
        return formaCorrecta;
    }
    
    public boolean ONEBYTE(String ope, ModosDireccionamiento dir, String directiva) {
        formaCorrecta = true;
        error = null;
        valor = 0;
        if(ope.charAt(0) == '@' || ope.charAt(0) == '$' || ope.charAt(0) == '%' || ope.matches("(-)?[0-9]+")) {    
            if(ope.charAt(0) == '@' && ope.substring(1, ope.length()).matches("[0-7]+"))
                valor = dir.ValorOperando(ope, 8);                 
            else if(ope.charAt(0) == '$' && ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+"))
                valor = dir.ValorOperando(ope, 8);
            else if(ope.charAt(0) == '%' && ope.substring(1, ope.length()).matches("[0-1]+"))
                valor = dir.ValorOperando(ope, 8);
            else if(ope.matches("(-)?[0-9]+"))
                valor = Integer.parseInt(ope);  
            else {
                error = "Formato Erroneo de Operando para Directiva " + directiva;
                formaCorrecta = false;
            }
        }
        else {
            error = "Formato Erroneo de Operando para Directiva " + directiva;
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null) {
            if(valor >= 0 && valor <= 255)
                formaCorrecta = true;
            else {
                error = "Operando Fuera de Rango para Directiva " + directiva;
                formaCorrecta = false;
                }            
        }
        return formaCorrecta;
    }
    
    public boolean TWOBYTES(String ope, ModosDireccionamiento dir, String directiva) {
        formaCorrecta = true;
        error = null;
        valor = 0;
        if(ope.charAt(0) == '@' || ope.charAt(0) == '$' || ope.charAt(0) == '%' || ope.matches("(-)?[0-9]+")) {    
            if(ope.charAt(0) == '@' && ope.substring(1, ope.length()).matches("[0-7]+"))
                valor = dir.ValorOperando(ope, 16);                 
            else if(ope.charAt(0) == '$' && ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+"))
                valor = dir.ValorOperando(ope, 16);
            else if(ope.charAt(0) == '%' && ope.substring(1, ope.length()).matches("[0-1]+"))
                valor = dir.ValorOperando(ope, 16);
            else if(ope.matches("(-)?[0-9]+"))
                valor = Integer.parseInt(ope);  
            else {
                error = "Formato Erroneo de Operando para Directiva " + directiva.toUpperCase();
                formaCorrecta = false;
            }
        }
        else {
            error = "Formato Erroneo de Operando para Directiva " + directiva.toUpperCase();
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null) {
            if(valor >= 0 && valor <= 65535)
                formaCorrecta = true;
            else {
                error = "Operando Fuera de Rango para Directiva " + directiva.toUpperCase();
                formaCorrecta = false;
                }            
        }
        return formaCorrecta;
    }
    
    public boolean FCC(String ope, ModosDireccionamiento dir, String directiva) {
        formaCorrecta = true;
        error = null;
        valor = 0;
        if(ope.matches(".+"))
                valor = ope.length();  
        return formaCorrecta;
    }   
}