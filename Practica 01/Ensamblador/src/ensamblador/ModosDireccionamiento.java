/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ensamblador;

import java.util.*;

/**
 *
 * @author Usuario
 */
public class ModosDireccionamiento {
    boolean formaCorrecta, opeCorrecto, errorEtiqueta, formaIMM, formaEXT, formaIDX, formaIDX1, formaIDX2, formaIDX2C, formaIDX_ID, formaIDX_ACU, formaIDX_ACU_IND, formaREL; 
    String error, tipoModo, auxBin, auxOpe;
    int val, valBit;
    
    public int ValorOperando(String op, int numBit) {        
        val = 0;
        valBit = numBit;
        auxOpe = "";
        auxBin = "";
        try {
            if(op.charAt(0) == '@') {
                if(op.toUpperCase().charAt(1) == '7') {
                    int i = 1;
                    while(i < op.length()){
                    if(op.toUpperCase().charAt(i) == '7' && i+1 != op.length() && op.toUpperCase().charAt(i+1) == '7') 
                        auxOpe = auxOpe;
                    else
                        auxOpe = auxOpe + op.charAt(i);
                    i++;
                    }
                    val = Integer.parseInt(auxOpe, 8);
                    auxBin = Integer.toBinaryString(val);
                    auxOpe = "";
                    if(auxBin.charAt(0) == '1' || auxBin.charAt(0) == '0') {
                        for (i = 0; i < auxBin.length(); i++) {
                        if(auxBin.charAt(i) == '1') 
                            auxOpe = auxOpe + "0";
                        else
                            auxOpe = auxOpe + "1";
                    }
                    val = Integer.parseInt(auxOpe, 2);
                    return ~val;
                    }
                }
                else
                    val = Integer.parseInt(op.toUpperCase().substring(1, op.length()), 8);
                
                if(val >= Math.pow(2, valBit - 1) && val < Math.pow(2, valBit)){
                    val <<= 32 - valBit;
                    val = ~valBit;
                    val >>= 32 - valBit;
                    val += 1;
                    return (~val) + 1;
                }                
            }        
            else if(op.charAt(0) == '$') {
                if(op.toUpperCase().charAt(1) == 'F') {
                    int i = 1;
                    while(i < op.length()){
                    if(op.toUpperCase().charAt(i) == 'F' && i+1 != op.length() && op.toUpperCase().charAt(i+1) == 'F') 
                        auxOpe = auxOpe;
                    else
                        auxOpe = auxOpe + op.charAt(i);
                    i++;
                    }
                    val = Integer.parseInt(auxOpe, 16);
                    auxBin = Integer.toBinaryString(val);
                    auxOpe = "";
                    if(auxBin.charAt(0) == '1' || auxBin.charAt(0) == '0') {
                        for (i = 0; i < auxBin.length(); i++) {
                        if(auxBin.charAt(i) == '1') 
                            auxOpe = auxOpe + "0";
                        else 
                            auxOpe = auxOpe + "1";
                    }
                    val = Integer.parseInt(auxOpe, 2);
                    return ~val;
                    }
                }
                else
                    val = Integer.parseInt(op.toUpperCase().substring(1, op.length()), 16);
                
                if(val >= Math.pow(2, valBit - 1) && val < Math.pow(2, valBit)){
                    val <<= 32 - valBit;
                    val = ~valBit;
                    val >>= 32 - valBit;
                    val += 1;
                    return (~val) + 1;
                }
            }
            else if(op.charAt(0) == '%') {                
                if(op.charAt(1) == '1') {
                    for (int i = 1; i < op.length(); i++) {
                    if(op.charAt(i) == '1') 
                        auxOpe = auxOpe + "0";
                    else 
                        auxOpe = auxOpe + "1";
                    }
                    val = Integer.parseInt(auxOpe, 2);
                    return ~val;
                }
                else
                    val = Integer.parseInt(op.toUpperCase().substring(1, op.length()), 2);
                if(val >= Math.pow(2, valBit - 1) && val < Math.pow(2, valBit)){
                    val <<= 32 - valBit;
                    val = ~valBit;
                    val >>= 32 - valBit;
                    val += 1;
                    return (~val) + 1;
                }
                
            }
            return val;
        }
        catch(Exception e) {
            return val = 65536;
        }
    }
    
    public boolean INH(String ope) {
        formaCorrecta = true;
        opeCorrecto = true;
        error = null;
        tipoModo = " ";
        if(ope != "NULL") {
            error = "El modo de Direccionamiento INHERENTE no Debe de Llevar Operando";
            formaCorrecta = false;
            opeCorrecto = false;
        }
        else tipoModo = "INH";
        return formaCorrecta;
    }
    
    public boolean IMM(String ope, String mod) {
        formaCorrecta = true;
        opeCorrecto = true;
        formaIMM = false;
        error = null;
        tipoModo = " ";
        int valor = 0;
        if(ope.charAt(0) == '#') {
            formaIMM = true;
            if(ope.charAt(1) == '@'  && ope.substring(2, ope.length()).matches("[0-7]+"))        
                valor = ValorOperando(ope.substring(1, ope.length()), 8);                
            else if(ope.charAt(1) == '$' && ope.substring(2, ope.length()).matches("[A-Fa-f0-9]+"))
                valor = ValorOperando(ope.substring(1, ope.length()), 8);
            else if(ope.charAt(1) == '%' && ope.substring(2, ope.length()).matches("[0-1]+"))
                valor = ValorOperando(ope.substring(1, ope.length()), 8);
            else if(ope.substring(1, ope.length()).matches("(-)?[0-9]+"))
                valor = Integer.parseInt(ope.substring(1, ope.length()));
            else {
                error = "Formato Erroneo de Operando para para Direccionamiento IMM";
                formaCorrecta = false;
                opeCorrecto = false; 
            }
        }
        else if(ope.equals("NULL"))
               formaCorrecta = true; 
        else if(ope.charAt(0) != '#'){ 
            error = "Formato Erroneo de Operando para Direccionamiento IMM";
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null) {
            if(mod.equals("IMM") && ope.equals("NULL"))
                tipoModo = "IMM";
            else if(valor >= -256 && valor <= 255 && mod.equals("IMM8"))
                tipoModo = "IMM8";
            else if(valor >= -32768 && valor <= 65535 && mod.equals("IMM16"))
                tipoModo = "IMM16";
            else {
                    error = "Operando Fuera de Rango para Direccionamiento IMM";
                    formaCorrecta = false;
                    opeCorrecto = false;
                }
        }
        return formaCorrecta;
    }      
    
    public boolean DIR(String ope) {
        formaCorrecta = true;
        opeCorrecto = true;
        error = null;
        tipoModo = " ";
        int valor = 0;
        if(ope.charAt(0) == '@' || ope.charAt(0) == '$' || ope.charAt(0) == '%' || ope.matches("(-)?[0-9]+")) {    
            if(ope.charAt(0) == '@' && ope.substring(1, ope.length()).matches("[0-7]+"))
                valor = ValorOperando(ope, 8);                 
            else if(ope.charAt(0) == '$' && ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+"))
                valor = ValorOperando(ope, 8);
            else if(ope.charAt(0) == '%' && ope.substring(1, ope.length()).matches("[0-1]+"))
                valor = ValorOperando(ope, 8);
            else if(ope.matches("(-)?[0-9]+"))
                valor = Integer.parseInt(ope);  
            else {
                error = "Formato Erroneo de Operando para Direccionamiento DIR";
                formaCorrecta = false;
            }
        }
        else {
            error = "Formato Erroneo de Operando para Direccionamiento DIR";
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null) {
            if(valor >= 0 && valor <= 255)
                tipoModo = "DIR";
            else {
                error = "Operando Fuera de Rango para Direccionamiento DIR";
                formaCorrecta = false;
                }            
        }
        return formaCorrecta;
    }
    
    public boolean EXT(String ope) {
        formaCorrecta = true;
        opeCorrecto = true;
        formaEXT = false;
        errorEtiqueta = false;
        error = null;
        tipoModo = " ";
        int valor = 0;
        if(ope.charAt(0) == '@' || ope.charAt(0) == '$' || ope.charAt(0) == '%' || ope.matches("(-)?[0-9]+") || ope.matches("[A-Za-z0-9]+")) {         
            formaEXT = true;
            if(ope.charAt(0) == '@'  && ope.substring(1, ope.length()).matches("[0-7]+"))
                valor = ValorOperando(ope, 16);                
            else if(ope.charAt(0) == '$' && ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+"))
                valor = ValorOperando(ope, 16);
            else if(ope.charAt(0) == '%' && ope.substring(1, ope.length()).matches("[0-1]+"))
                valor = ValorOperando(ope, 16);
            else if(ope.matches("(-)?[0-9]+"))
                valor = Integer.parseInt(ope); 
            else if(ope.matches("[A-Za-z]{1}[A-Za-z0-9]{0,7}"))
                formaCorrecta = true;
            else if(!ope.matches("[A-Za-z]{1}[A-Za-z0-9]{0,7}") && (ope.charAt(0) != '@' && ope.charAt(0) != '$' && ope.charAt(0) != '%' && !ope.matches("(-)?[0-9]+"))) {
                errorEtiqueta = true;
                error = "Formato Erroneo de Etiqueta para Direccionamiento EXT";
                formaCorrecta = false;
            }
            else {
                error = "Formato Erroneo de Operando para Direccionamiento EXT";
                formaCorrecta = false;
            }
        }
        else {
            error = "Formato Erroneo de Operando para Direccionamiento EXT";
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null) {
            if(ope.matches("[A-Za-z]+")) {
                tipoModo = "EXT";
                formaCorrecta = true;
            }
            else if(valor >= -32768 && valor <= 65535)
                tipoModo = "EXT";
            else {
                error = "Operando Fuera de Rango para Direccionamiento EXT";
                formaCorrecta = false;
                opeCorrecto = false;
                }            
        }
        return formaCorrecta;
    }
    
    public boolean IDX(String ope) {
        formaCorrecta = true;
        opeCorrecto = true;
        formaIDX = false;
        error = null;
        tipoModo = " ";
        int valor = 0;
        boolean mod1 = false, mod2 = false;
        String numero = null, registro = null;
        
        if(ope.charAt(0) == ',') {
            formaIDX = true;
            registro = ope.substring(1, ope.length()); 
            mod1 = true;
        }
        else if(ope.matches("(-)?[0-9]+(,)[A-Za-z]+") || ope.matches("\\@[0-9]+(,)[A-Za-z]+") || ope.matches("\\%[0-1]+(,)[A-Za-z]+")){
            StringTokenizer token = new StringTokenizer(ope, "\n", true);
            numero = token.nextToken(",");
            token.nextToken(",");
            registro = token.nextToken(); 
            mod2 = true;            
        }
        else if(ope.charAt(0) == '$' && ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+(,)[A-Za-z]+")){
            StringTokenizer token = new StringTokenizer(ope, "\n", true);
            numero = token.nextToken(",");
            token.nextToken(",");
            registro = token.nextToken(); 
            mod2 = true;            
        }
        else mod1 = mod2 = false;
        
        if(mod1) {
            if(!registro.toUpperCase().equals("X") && !registro.toUpperCase().equals("Y") && !registro.toUpperCase().equals("SP") && !registro.toUpperCase().equals("PC")) {
                error = "Registro no Valido para Direccionamiento IDX";
                formaCorrecta = false;
                opeCorrecto = false;
            }           
        }
        else if(mod2) {
            if(numero.charAt(0) == '@'  && numero.substring(1, numero.length()).matches("[0-7]+"))
                valor = ValorOperando(numero, 5);                
            else if(numero.charAt(0) == '$' && numero.substring(1, numero.length()).matches("[A-Fa-f0-9]+"))
                valor = ValorOperando(numero, 5);
            else if(numero.charAt(0) == '%' && numero.substring(1, numero.length()).matches("[0-1]+"))
                valor = ValorOperando(numero, 5);
            else if(numero.matches("(-)?[0-9]+"))
                valor = Integer.parseInt(numero); 
            else {
                error = "Formato Erroneo de Operando para Direccionamiento IDX";
                formaCorrecta = false;
            }
            if(!registro.toUpperCase().equals("X") && !registro.toUpperCase().equals("Y") && !registro.toUpperCase().equals("SP") && !registro.toUpperCase().equals("PC") && formaCorrecta) {
                error = "Registro no Valido para Direccionamiento IDX";
                formaCorrecta = false;
            }
        }
        else {
            error = "Formato Erroneo de Operando para Direccionamiento IDX";
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null) {
            if(valor >= -16 && valor <= 15 && (mod1 || mod2))
                tipoModo = "IDX";
            else {
                error = "Operando Fuera de Rango para Direccionamiento IDX";
                formaCorrecta = false;
                opeCorrecto = false;
                }            
        }
        return formaCorrecta;
    }
    
    public boolean IDX1(String ope) {
        formaCorrecta = true;
        opeCorrecto = true;
        formaIDX1 = false;
        error = null;
        tipoModo = " ";
        int valor = 0;
        boolean mod2 = false;
        String numero = null, registro = null;
        
        if(ope.matches("(-)?[0-9]+(,)[A-Za-z]+") || ope.matches("\\@[0-9]+(,)[A-Za-z]+") || ope.matches("\\%[0-1]+(,)[A-Za-z]+")){
            StringTokenizer token = new StringTokenizer(ope, "\n", true);
            numero = token.nextToken(",");
            token.nextToken(",");
            registro = token.nextToken(); 
            mod2 = true;            
        }
        else if(ope.charAt(0) == '$' && ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+(,)[A-Za-z]+")){
            StringTokenizer token = new StringTokenizer(ope, "\n", true);
            numero = token.nextToken(",");
            token.nextToken(",");
            registro = token.nextToken(); 
            mod2 = true;            
        }
        else mod2 = false;
        
        if(mod2) {
            if(numero.charAt(0) == '@' && numero.substring(1, numero.length()).matches("[0-7]+"))
                valor = ValorOperando(numero, 9);                
            else if(numero.charAt(0) == '$' && numero.substring(1, numero.length()).matches("[A-Fa-f0-9]+"))
                valor = ValorOperando(numero, 9);
            else if(numero.charAt(0) == '%' && numero.substring(1, numero.length()).matches("[0-1]+"))
                valor = ValorOperando(numero, 9);
            else if(numero.matches("(-)?[0-9]+"))
                valor = Integer.parseInt(numero);  
            else {
                error = "Formato Erroneo de Operando para Direccionamiento IDX1";
                formaCorrecta = false;
            }
            if(!registro.toUpperCase().equals("X") && !registro.toUpperCase().equals("Y") && !registro.toUpperCase().equals("SP") && !registro.toUpperCase().equals("PC") && formaCorrecta) {
                error = "Registro no Valido para Direccionamiento IDX1";
                formaCorrecta = false;
                opeCorrecto = false;
            }   
        }
        else {
            error = "Formato Erroneo de Operando para Direccionamiento IDX1";
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null) {
            if(valor >= -256 && valor <= 255 && mod2)
                tipoModo = "IDX1";
            else {
                error = "Operando Fuera de Rango para Direccionamiento IDX1";
                formaCorrecta = false;
                opeCorrecto = false;
                formaIDX1 = true;
                }            
        }
        return formaCorrecta;
    }
    
    public boolean IDX2(String ope) {
        formaCorrecta = true;
        opeCorrecto = true;
        formaIDX2 = false;
        error = null;
        tipoModo = " ";
        int valor = 0;
        boolean mod2 = false;
        String numero = null, registro = null;
        
        if(ope.matches("(-)?[0-9]+(,)[A-Za-z]+") || ope.matches("\\@[0-9]+(,)[A-Za-z]+") || ope.matches("\\%[0-1]+(,)[A-Za-z]+")){
            StringTokenizer token = new StringTokenizer(ope, "\n", true);
            numero = token.nextToken(",");
            token.nextToken(",");
            registro = token.nextToken(); 
            mod2 = true;            
        }
        else if(ope.charAt(0) == '$' && ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+(,)[A-Za-z]+")){
            StringTokenizer token = new StringTokenizer(ope, "\n", true);
            numero = token.nextToken(",");
            token.nextToken(",");
            registro = token.nextToken(); 
            mod2 = true;            
        }
        else mod2 = false;
        
        if(mod2) {
            formaIDX2 = true;
            if(numero.charAt(0) == '@' && numero.substring(1, numero.length()).matches("[0-7]+"))
                valor = ValorOperando(numero, 16);                
            else if(numero.charAt(0) == '$' && numero.substring(1, numero.length()).matches("[A-Fa-f0-9]+"))
                valor = ValorOperando(numero, 16);
            else if(numero.charAt(0) == '%' && numero.substring(1, numero.length()).matches("[0-1]+"))
                valor = ValorOperando(numero, 16);
            else if(numero.matches("(-)?[0-9]+"))
                valor = Integer.parseInt(numero);  
            else  {
                error = "Formato Erroneo de Operando para Direccionamiento IDX2";
                formaCorrecta = false;                   
            }
            if(!registro.toUpperCase().equals("X") && !registro.toUpperCase().equals("Y") && !registro.toUpperCase().equals("SP") && !registro.toUpperCase().equals("PC") && formaCorrecta) {
                error = "Registro no Valido para Direccionamiento IDX2";
                formaCorrecta = false;
                opeCorrecto = false;
            }   
        }
        else {
            error = "Formato Erroneo de Operando para Direccionamiento IDX2";
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null) {
            if(valor >= 0 && valor <= 65535 && mod2)
                tipoModo = "IDX2";
            else {
                error = "Operando Fuera de Rango para Direccionamiento IDX2";
                formaCorrecta = false;
                opeCorrecto = false;
                }            
        }
        return formaCorrecta;
    }
    
    public boolean IDX2C(String ope) {
        formaCorrecta = true;
        opeCorrecto = true;
        formaIDX2C = false;
        error = null;
        tipoModo = " ";
        int valor = 0;
        boolean mod2 = false;
        String numero = null, registro = null;
        
        if(ope.charAt(0) == '[' && (ope.substring(1, ope.length()).matches("(-)?[0-9]+(,)[A-Za-z]+]$") || ope.substring(1, ope.length()).matches("\\@[0-9]+(,)[A-Za-z]+]$")|| ope.substring(1, ope.length()).matches("\\%[0-1]+(,)[A-Za-z]+]$"))){
            StringTokenizer token = new StringTokenizer(ope, "\n", true);
            numero = token.nextToken(",");
            token.nextToken(",");
            registro = token.nextToken(); 
            mod2 = true;            
        }
        else if(ope.charAt(0) == '[' && ope.charAt(1) == '$' && ope.substring(2, ope.length()).matches("[A-Fa-f0-9]+(,)[A-Za-z]+]$")){
            StringTokenizer token = new StringTokenizer(ope, "\n", true);
            numero = token.nextToken(",");
            token.nextToken(",");
            registro = token.nextToken(); 
            mod2 = true;            
        }        
        else mod2 = false;
        if(ope.charAt(0) == '[' || ope.charAt(ope.length()-1) == ']')
            formaIDX2C = true;
        
        if(mod2) {
            formaIDX2C = true;
            if(numero.charAt(1) == '@' && numero.substring(2, numero.length()).matches("[0-7]+"))
                valor = ValorOperando(numero.substring(1, numero.length()), 16);                
            else if(numero.charAt(1) == '$' && numero.substring(2, numero.length()).matches("[A-Fa-f0-9]+"))
                valor = ValorOperando(numero.substring(1, numero.length()), 16);
            else if(numero.charAt(1) == '%' && numero.substring(2, numero.length()).matches("[0-1]+"))
                valor = ValorOperando(numero.substring(1, numero.length()), 16);
            else if(numero.matches("\\[(-)?[0-9]+"))
                valor = Integer.parseInt(numero.substring(1, numero.length()));  
            else  {
                error = "Formato Erroneo de Operando para Direccionamiento [IDX2]";
                formaCorrecta = false;                   
            }
            if(!registro.substring(0, registro.length()-1).toUpperCase().equals("X") && !registro.substring(0, registro.length()-1).toUpperCase().equals("Y") && !registro.substring(0, registro.length()-1).toUpperCase().equals("SP") && !registro.substring(0, registro.length()-1).toUpperCase().equals("PC") && formaCorrecta) {
                error = "Registro no Valido para Direccionamiento [IDX2]";
                formaCorrecta = false;
            }   
        }
        else {
            error = "Formato Erroneo de Operando para Direccionamiento [IDX2]";
            formaCorrecta = false;
        }
        if(formaCorrecta && error == null) {
            if(valor >= 0 && valor <= 65535 && mod2)
                tipoModo = "[IDX2]";
            else {
                error = "Operando Fuera de Rango para Direccionamiento [IDX2]";
                formaCorrecta = false;
                opeCorrecto = false;
            }            
        }
        return formaCorrecta;
    }
    
    public boolean IDX_ID(String ope) {
        formaCorrecta = true;
        opeCorrecto = true;
        formaIDX_ID = false;
        error = null;
        tipoModo = " ";
        int valor = 0;
        boolean mod2 = false;
        String numero = null, registro = null;
        
        if(ope.matches("(@|-)?[0-9]+,-[A-Za-z]+") || ope.matches("\\%[0-1]+,-[A-Za-z]+") || ope.matches("(@|-)?[0-9]+,\\+[A-Za-z]+") || ope.matches("\\%[0-1]+,\\+[A-Za-z]+") || ope.matches("(@|-)?[0-9]+,[A-Za-z]+\\-") || ope.matches("\\%[0-1]+,[A-Za-z]+\\-") || ope.matches("(@|-)?[0-9]+,[A-Za-z]+\\+") || ope.matches("\\%[0-1]+,[A-Za-z]+\\+")){
            StringTokenizer token = new StringTokenizer(ope, "\n", true);
            numero = token.nextToken(",");
            token.nextToken(",");
            registro = token.nextToken(); 
            mod2 = true;            
        }
        else if(ope.charAt(0) == '$' && (ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+,-[A-Za-z]+") || ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+,\\+[A-Za-z]+") || ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+,[A-Za-z]+\\-") || ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+,[A-Za-z]+\\+"))){
            StringTokenizer token = new StringTokenizer(ope, "\n", true);
            numero = token.nextToken(",");
            token.nextToken(",");
            registro = token.nextToken(); 
            mod2 = true;            
        }
        else mod2 = false;
        
        if(mod2) {
            formaIDX_ID = true;
           if(numero.charAt(0) == '@' && numero.substring(1, numero.length()).matches("[0-7]+")) 
                valor = ValorOperando(numero, 3);                
            else if(numero.charAt(0) == '$' && numero.substring(1, numero.length()).matches("[A-Fa-f0-9]+"))
                valor = ValorOperando(numero, 3);
            else if(numero.charAt(0) == '%' && numero.substring(1, numero.length()).matches("[0-1]+"))
                valor = ValorOperando(numero, 3);
            else if(numero.matches("(-)?[0-9]+"))
                valor = Integer.parseInt(numero);  
            else {
                error = "Formato Fuera de Rango para Direccionamiento IDX+/IDX-";
                formaCorrecta = false;         
                opeCorrecto = false;
            }
            if(!registro.toUpperCase().equals("+X") && !registro.toUpperCase().equals("+Y") && !registro.toUpperCase().equals("+SP") && !registro.toUpperCase().equals("X+") && !registro.toUpperCase().equals("Y+") && !registro.toUpperCase().equals("SP+") && !registro.toUpperCase().equals("-X") && !registro.toUpperCase().equals("-Y") && !registro.toUpperCase().equals("-SP") && !registro.toUpperCase().equals("X-") && !registro.toUpperCase().equals("Y-") && !registro.toUpperCase().equals("SP-") && formaCorrecta) {
                error = "Registro no Valido para Direccionamiento IDX+/IDX-";
                formaCorrecta = false;
                opeCorrecto = false;
            }   
        }
        else {
            error = "Formato Erroneo de Operando para Direccionamiento IDX+/IDX-";
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null) {
            if(valor >= 1 && valor <= 8 && mod2)
                tipoModo = "IDX";
            else {
                error = "Operando Fuera de Rango para Direccionamiento IDX+/IDX-";
                formaCorrecta = false;
                opeCorrecto = false;
                }            
        }
        return formaCorrecta;
    }
    
    public boolean IDX_ACU(String ope) {
        formaCorrecta = true;
        opeCorrecto = true;
        formaIDX_ACU = false;
        error = null;
        tipoModo = " ";
        boolean mod2 = false;
        String acumulador = null, registro = null;
        
        if(ope.matches("[AaBbDd]{1},[A-Za-z]+")){
            StringTokenizer token = new StringTokenizer(ope, "\n", true);
            acumulador = token.nextToken(",");
            token.nextToken(",");
            registro = token.nextToken(); 
            mod2 = true;            
        }
        else mod2 = false;
        
        if(mod2) {
            formaIDX_ACU = true;
            if(acumulador.toUpperCase().charAt(0) == 'A' || acumulador.toUpperCase().charAt(0) == 'B' || acumulador.toUpperCase().charAt(0) == 'D')
                formaCorrecta = true;                
            else {
                error = "Acumulador Erroneo para Direccionamiento IDX";
                formaCorrecta = false;    
                opeCorrecto = false;
            }
            if(!registro.toUpperCase().equals("X") && !registro.toUpperCase().equals("Y") && !registro.toUpperCase().equals("SP") && !registro.toUpperCase().equals("PC") && formaCorrecta) {
                error = "Registro no Valido para Direccionamiento IDX";
                formaCorrecta = false;
                opeCorrecto = false;
            }   
        }
        else {
            error = "Formato Erroneo de Operando para Direccionamiento IDX";
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null)
                tipoModo = "IDX";
        else {
            error = "Formato Erroneo de Operando para Direccionamiento IDX";
            formaCorrecta = false;
            opeCorrecto = false;
        }    
        return formaCorrecta;
    }
    
    public boolean IDX_ACU_IND(String ope) {
        formaCorrecta = true;
        opeCorrecto = true;
        formaIDX_ACU_IND = false;
        error = null;
        tipoModo = " ";
        boolean mod2 = false;
        String acumulador = null, registro = null;
        
        if(ope.matches("\\[[Dd]{1},[A-Za-z]+]$")){
            StringTokenizer token = new StringTokenizer(ope, "\n", true);
            acumulador = token.nextToken(",");
            token.nextToken(",");
            registro = token.nextToken(); 
            mod2 = true;            
        }
        else mod2 = false;
        
        if(mod2) {
            formaIDX_ACU_IND = true;
            if(acumulador.toUpperCase().charAt(0) == '[' && acumulador.toUpperCase().charAt(1) == 'D')
                formaCorrecta = true;                
            else {
                error = "Acumulador Erroneo para Direccionamiento [D,IDX]";
                formaCorrecta = false;    
                opeCorrecto = false;
            }
            if(!registro.substring(0, registro.length()-1).toUpperCase().equals("X") && !registro.substring(0, registro.length()-1).toUpperCase().equals("Y") && !registro.substring(0, registro.length()-1).toUpperCase().equals("SP") && !registro.substring(0, registro.length()-1).toUpperCase().equals("PC") && formaCorrecta) {
                error = "Registro no Valido para Direccionamiento [D,IDX]";
                formaCorrecta = false;
                opeCorrecto = false;
            }   
        }
        else {
            error = "Formato Erroneo de Operando para Direccionamiento [D,IDX]";
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null)
                tipoModo = "[D,IDX]";
        else {
            error = "Formato Erroneo de Operando para Direccionamiento [D,IDX]";
            formaCorrecta = false;
            opeCorrecto = false;
        }    
        return formaCorrecta;
    }
    
    public boolean REL(String ope, String mod) {
        formaCorrecta = true;
        opeCorrecto = true;
        formaREL = false;
        errorEtiqueta = false;
        error = null;
        tipoModo = " ";
        int valor = 0;
        if(ope.charAt(0) == '@' || ope.charAt(0) == '$' || ope.charAt(0) == '%' || ope.matches("(-)?[0-9]+") || ope.matches("[A-Za-z0-9]+")) {    
            formaREL = true;
            if(ope.charAt(0) == '@' && ope.substring(1, ope.length()).matches("[0-7]+"))
                valor = ValorOperando(ope, 16);                
            else if(ope.charAt(0) == '$' && ope.substring(1, ope.length()).matches("[A-Fa-f0-9]+"))
                valor = ValorOperando(ope, 16);
            else if(ope.charAt(0) == '%' && ope.substring(1, ope.length()).matches("[0-1]+"))
                valor = ValorOperando(ope, 16);
            else if(ope.matches("(-)?[0-9]+"))
                valor = Integer.parseInt(ope); 
            else if(ope.matches("[A-Za-z]{1}[A-Za-z0-9]{0,7}"))
                formaCorrecta = true;
            else if(!ope.matches("[A-Za-z]{1}[A-Za-z0-9]{0,7}")) {
                errorEtiqueta = true;
                error = "Formato Erroneo de Etiqueta para Direccionamiento REL";
                formaCorrecta = false;
            }
            else {
                error = "Formato Erroneo de Operando para Direccionamiento REL";
                formaCorrecta = false;
            }
        }
        else {
            error = "Formato Erroneo de Operando para Direccionamiento REL";
            formaCorrecta = false;
        }
        
        if(formaCorrecta && error == null) {
            if(ope.matches("[A-Za-z]+")  && mod.equals("REL8")) {
                tipoModo = "REL8";
                formaCorrecta = true;
            }
            else if(ope.matches("[A-Za-z]+")  && mod.equals("REL9")) {
                tipoModo = "REL9";
                formaCorrecta = true;
            }
            else if(ope.matches("[A-Za-z]+")  && mod.equals("REL16")) {
                tipoModo = "REL16";
                formaCorrecta = true;
            }
            else if(valor >= -128 && valor <= 127 && mod.equals("REL8"))
                tipoModo = "REL8";
            else if(valor >= -256 && valor <= 255 && mod.equals("REL9"))
                tipoModo = "REL9";
            else if(valor >= -32768 && valor <= 65535 && mod.equals("REL16"))
                tipoModo = "REL16";
            else {
                error = "Operando Fuera de Rango para Direccionamiento REL";
                formaCorrecta = false;
                opeCorrecto = false;
                }            
        }
        return formaCorrecta;
    }
}