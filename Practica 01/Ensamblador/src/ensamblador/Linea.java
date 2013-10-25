/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ensamblador;

/**
 *
 * @author JAVIER
 */

import java.util.*;

public class Linea {    
    int numLinea, byteOperando;
    String etiqueta, codop, operando, tipoError, instruccion, modo, modos, codMaquina, byteCalculado, byteporCalcular, totalBytes, contLoc, auxCont;
    boolean esComentario, fin, encontrado, unORG;
    
    public Linea(int numeroLinea) {        
        numLinea = numeroLinea;
        contLoc = "0";
        etiqueta = "NULL";
        codop = "NULL";
        operando = "NULL";
        tipoError = null;
        esComentario = false;
        fin = false;
        modo = "";
    }
    
    public boolean SepararTokens(String linea) {
        boolean formatoCorrecto = true, rompe = false, rompe2 = false, sinEti = false, conEti = false;
        Vector<String> vect = new Vector<String>();
        StringTokenizer token = new StringTokenizer(linea, ";", true);
        if(token.countTokens() >= 1) { 
            linea = token.nextToken();
            if(linea.charAt(0) != ';') {
                token = new StringTokenizer(linea);
                if(linea.charAt(0) == ' ' || linea.charAt(0) == '\t') {
                    if(token.countTokens() == 0)
                        esComentario = true;  
                    else if(token.countTokens() == 1) {                        
                        codop = token.nextToken();                                                 
                    }                     
                    else if(token.countTokens() == 2) {                        
                        codop = token.nextToken();
                        operando = token.nextToken();
                    }
                    else if(token.countTokens() > 2) {  
                        sinEti = true;
                        token = new StringTokenizer(linea, " \t ;");                        
                        while(token.hasMoreTokens() && !rompe) {
                            vect.add(token.nextToken());
                            if(!vect.get(0).toUpperCase().equals("FCC"))
                                rompe = true;
                        }
                    }
                    else {
                        tipoError = "Formato de Linea Invalido";
                        formatoCorrecto = false;
                    }                    
                }
                else{
                    if(token.countTokens() == 0)
                        esComentario = true;
                    else if(token.countTokens() == 1) {
                        tipoError = "Formato de Linea Invalido";
                        formatoCorrecto = false;
                    }
                    else if(token.countTokens() == 2) {
                        etiqueta = token.nextToken();                        
                        codop = token.nextToken();
                    }
                    else if(token.countTokens() == 3) {                        
                        etiqueta = token.nextToken();                        
                        codop = token.nextToken();                        
                        operando = token.nextToken();
                    }
                    else if(token.countTokens() > 3) {
                        conEti = true;
                        int cont = 0;
                        token = new StringTokenizer(linea, " \t ;");                        
                        while(token.hasMoreTokens() && !rompe2) {
                            vect.add(token.nextToken());
                            cont++;                            
                            if(cont == 2 && !vect.get(1).toUpperCase().equals("FCC"))
                                rompe2 = true;                            
                        }
                    }
                    else {
                        tipoError = "Formato de Linea Invalido";
                        formatoCorrecto = false;
                    }
                  }
                }
             else esComentario = true;   
            }
            else esComentario = true;
            
        if(rompe || rompe2) {
                tipoError = "Formato de Linea Invalido";
                formatoCorrecto = false;
            }
        else if(!rompe && sinEti) {
                String auxOpe = "";
                codop = vect.get(0);
                for (int i = 1; i < vect.size(); i++)
                    auxOpe = auxOpe + vect.get(i) + " ";
                operando = auxOpe.substring(0, auxOpe.length()-1);
            }
        else if(!rompe2 && conEti) {
                String auxOpe = "";
                etiqueta = vect.get(0);
                codop = vect.get(1);
                for (int i = 2; i < vect.size(); i++)
                    auxOpe = auxOpe + vect.get(i) + " ";
                operando = auxOpe.substring(0, auxOpe.length()-1);
            }
            return formatoCorrecto;
    }  
    
    public boolean ValidaToken(String linea, Lista lista, ModosDireccionamiento dir, Directivas direct, String auxC, boolean esORG, Vector<String> directiva, Vector<String> etiq) {
        boolean formatoCorrecto = true, esDirectiva = false;
        if(SepararTokens(linea)) {
        if(codop != null && formatoCorrecto) {               
            if(!codop.matches("[a-zA-Z]((\\.[a-zA-Z]{0,3}|[a-zA-Z]{0,1}\\.[a-zA-Z]{0,2}|[a-zA-Z]{0,2}\\.[a-zA-Z]{0,1}|[a-zA-Z]{0,3}\\.)|[a-zA-Z]{0,4})")) {
                tipoError = "Codigo de Operacion Invalido";
                formatoCorrecto = false;
            }
            else {                
                if(esDirectivaValida(etiqueta, codop, operando, dir, direct, auxC, esORG, directiva)) {
                    esDirectiva = true;                
                    if(etiqueta != null && formatoCorrecto) {
                                  if(!etiqueta.matches("[A-Za-z][\\w]{0,7}")){
                                        tipoError = "Etiqueta Invalida";
                                        formatoCorrecto = false;
                                    }
                                    if(formatoCorrecto && tipoError == null) {
                                        if(ValidaEtiq(etiqueta, etiq))
                                            formatoCorrecto = false;                                      
                                    }
                            }
                
                }
                else if(!esDirectiva && tipoError == null) {
                        modos = lista.Buscar(codop, operando);
                        if(lista.expCorrecta) {      
                            if(SeparaModos(modos, operando, dir, lista)) {
                                if(etiqueta != null && formatoCorrecto) {
                                    if(!etiqueta.matches("[A-Za-z][\\w]{0,7}")){
                                        tipoError = "Etiqueta Invalida";
                                        formatoCorrecto = false;
                                    }
                                    if(formatoCorrecto && tipoError == null) {
                                        if(ValidaEtiq(etiqueta, etiq))
                                            formatoCorrecto = false;
                                        else {
                                            modo = dir.tipoModo;
                                            contLoc = auxC.toUpperCase();
                                            auxC = Integer.toHexString(Integer.parseInt(auxC, 16) + byteOperando);
                                            auxCont = auxC.toUpperCase(); 
                                        }                                        
                                    }
                                }
                            }
                            else
                                tipoError = dir.error;                
                        }
                        else if(!esComentario) { 
                            tipoError = lista.error; 
                            formatoCorrecto = false; 
                     }
                }
                for(int ceros = contLoc.length(); ceros < 4; ceros++) {
                    contLoc = "0" + contLoc;
                }
            }
        }
    }
    else formatoCorrecto = false;
    return formatoCorrecto;
    }

    public void SeparaTABOP(String linea, Lista lista) {
        StringTokenizer token = new StringTokenizer(linea, "\n", true);
        if(token.countTokens() <= 6){            
            instruccion = token.nextToken("|");
            token.nextToken("|");
            modo = token.nextToken("|");
            token.nextToken("|");
            codMaquina = token.nextToken("|");
            token.nextToken("|");
            byteCalculado = token.nextToken("|");
            token.nextToken("|");
            byteporCalcular = token.nextToken("|");
            token.nextToken("|");
            totalBytes = token.nextToken();             
            lista.Insertar(instruccion, modo, codMaquina, byteCalculado, byteporCalcular, totalBytes);            
        }   
     }
    
     public boolean SeparaModos(String mod, String ope, ModosDireccionamiento dir, Lista lista) {
        Vector<String> tipoModo = new Vector<String>();
        Vector<String> byteC = new Vector<String>();
        boolean formatoCorrecto = true;
        byteOperando = 0;
        encontrado = false;
        StringTokenizer token = new StringTokenizer(mod, "|");
        while(token.hasMoreTokens()) {
            tipoModo.add(token.nextToken("| "));     
            byteC.add(token.nextToken("|"));
        }
        
        dir.errorEtiqueta = dir.formaIMM = dir.formaEXT = dir.formaIDX = dir.formaIDX1 = dir.formaIDX2 = dir.formaIDX2C = dir.formaIDX_ID = dir.formaIDX_ACU = dir.formaIDX_ACU_IND = dir.formaREL = false; 
        int x = 0;
        while(x < tipoModo.size() && !encontrado && !dir.errorEtiqueta) {
            if(tipoModo.get(x).equals("INH")) { 
                if(dir.INH(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                   byteOperando = Integer.parseInt(byteC.get(x).substring(1, byteC.get(x).length()));
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("IMM8") || tipoModo.get(x).equals("IMM16") || tipoModo.get(x).equals("IMM") && !encontrado) { 
                if(dir.IMM(ope, tipoModo.get(x))) {
                   formatoCorrecto = true;
                   encontrado = true;
                   byteOperando = Integer.parseInt(byteC.get(x).substring(1, byteC.get(x).length()));
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("DIR") && !encontrado && !dir.formaIMM) { 
                if(dir.DIR(ope)) { 
                   formatoCorrecto = true;
                   encontrado = true;
                   byteOperando = Integer.parseInt(byteC.get(x).substring(1, byteC.get(x).length()));
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("EXT") && !encontrado && !dir.formaIMM) { 
                if(dir.EXT(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                   byteOperando = Integer.parseInt(byteC.get(x).substring(1, byteC.get(x).length()));
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("IDX") && !encontrado && !dir.formaIMM) { 
                if(dir.IDX(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                   byteOperando = Integer.parseInt(byteC.get(x).substring(1, byteC.get(x).length()));
                }
                else if(!dir.formaIDX) {
                        if(dir.IDX_ID(ope)) {
                        formatoCorrecto = true;
                        encontrado = true;
                        byteOperando = Integer.parseInt(byteC.get(x).substring(1, byteC.get(x).length()));
                    }
                }
                else if(!dir.formaIDX && !dir.formaIDX_ID) {
                        if(dir.IDX_ACU(ope) && !dir.formaIDX && !dir.formaIDX_ID) {
                            formatoCorrecto = true;
                            encontrado = true;
                            byteOperando = Integer.parseInt(byteC.get(x).substring(1, byteC.get(x).length()));
                    }
                }
                else formatoCorrecto = false;
            }                 
            else if(tipoModo.get(x).equals("IDX1") && !encontrado && !dir.formaIMM && !dir.formaEXT && !dir.formaIDX && !dir.formaIDX_ID && !dir.formaIDX_ACU) { 
                if(dir.IDX1(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                   byteOperando = Integer.parseInt(byteC.get(x).substring(1, byteC.get(x).length()));
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("IDX2") && !encontrado && !dir.formaIMM && !dir.formaEXT && !dir.formaIDX && !dir.formaIDX_ID && !dir.formaIDX_ACU) { 
                if(dir.IDX2(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                   byteOperando = Integer.parseInt(byteC.get(x).substring(1, byteC.get(x).length()));
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("[IDX2]") && !encontrado && !dir.formaIMM && !dir.formaEXT && !dir.formaIDX && !dir.formaIDX1 && !dir.formaIDX2 && !dir.formaIDX_ID && !dir.formaIDX_ACU) { 
                if(dir.IDX2C(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                   byteOperando = Integer.parseInt(byteC.get(x).substring(1, byteC.get(x).length()));
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("[D,IDX]") && !encontrado && !dir.formaIMM && !dir.formaEXT && !dir.formaIDX && !dir.formaIDX1 && !dir.formaIDX2 && !dir.formaIDX2C && !dir.formaIDX_ID && !dir.formaIDX_ACU) { 
                if(dir.IDX_ACU_IND(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                   byteOperando = Integer.parseInt(byteC.get(x).substring(1, byteC.get(x).length()));
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("REL8") || tipoModo.get(x).equals("REL9") || tipoModo.get(x).equals("REL16") && !encontrado && !dir.formaIMM) { 
                if(dir.REL(ope, tipoModo.get(x))) { 
                   formatoCorrecto = true;
                   encontrado = true;
                   byteOperando = Integer.parseInt(byteC.get(x).substring(1, byteC.get(x).length()));
                }
                else formatoCorrecto = false;
            }            

            if(!dir.opeCorrecto && ((tipoModo.get(x).equals("DIR") && tipoModo.get(x+1).equals("EXT")) || (tipoModo.get(x).equals("IDX") && tipoModo.get(x+1).equals("IDX1")) || (tipoModo.get(x).equals("IDX1") && tipoModo.get(x+1).equals("IDX2")) || (tipoModo.get(x).equals("[D,IDX]") && tipoModo.get(x+1).equals("[IDX2]")))) 
                x++;          
            else if(!dir.opeCorrecto) 
                x = tipoModo.size();
            else
                x++;            
        }
        if(!encontrado && !dir.formaIMM && !dir.formaEXT && !dir.formaIDX && !dir.formaIDX1 && !dir.formaIDX2 && !dir.formaIDX2C && !dir.formaIDX_ID && !dir.formaIDX_ACU && !dir.formaIDX_ACU_IND && !dir.formaREL)
            dir.error = "Formato de Operando no Valido para Ningun Modo de Direccionamiento";
        return formatoCorrecto; 
     }
                              
     public boolean esDirectivaValida(String etiqueta, String codop, String operando, ModosDireccionamiento dir,  Directivas direct, String auxC, boolean esORG, Vector<String> directiva) {
         boolean esDirectiva = false, formatoCorrecto = true;
         if(codop.toUpperCase().equals("ORG")) {
                    if(etiqueta.toUpperCase().equals("NULL") && !operando.toUpperCase().equals("NULL")) {
                        if(!esORG) {
                            unORG = true;
                            if(direct.ORG(operando, dir)) {
                                esDirectiva = true;
                                auxC = Integer.toHexString(direct.valor);
                                auxCont = contLoc = auxC.toUpperCase();                           
                            }
                            else
                                tipoError = direct.error;                            
                        } 
                        else {
                            tipoError = "Solo Puede Existir una Directiva ORG";
                            formatoCorrecto = false;
                        }
                    }
                    else {
                        if(!esORG) {
                            tipoError = "La Directiva ORG no Debe de Llevar Etiqueta y Debe de Llevar Operando";
                            formatoCorrecto = false; 
                        }
                        else {
                            tipoError = "Solo Puede Existir una Directiva ORG";
                            formatoCorrecto = false; 
                        }
                    }
                }                
                else if(codop.toUpperCase().equals("EQU")) {
                    if(!etiqueta.toUpperCase().equals("NULL") && !operando.toUpperCase().equals("NULL")) {
                        if(direct.EQU(operando, dir)) {
                            esDirectiva = true;
                            contLoc = Integer.toHexString(direct.valor);                         
                        }
                        else
                            tipoError = direct.error;                            
                    }
                    else {
                        tipoError = "La Directiva EQU Debe de Llevar Etiqueta y Operando";
                        formatoCorrecto = false; 
                    }
                }                
                else if(codop.toUpperCase().equals("END")) {
                    if(operando.toUpperCase().equals("NULL")) {
                        fin = true;                           
                        esDirectiva = true;
                        contLoc = auxC.toUpperCase();
                    }
                    else 
                        tipoError = "La Directiva END no Puede Llevar Operando"; 
                }
                else if(codop.toUpperCase().equals("DB") || codop.toUpperCase().equals("DC.B") || codop.toUpperCase().equals("FCB")) {
                                if(!operando.toUpperCase().equals("NULL")) {
                                    if(direct.ONEBYTE(operando, dir, codop.toUpperCase())) {
                                        esDirectiva = true;
                                        contLoc = auxC.toUpperCase();
                                        auxC = Integer.toHexString(Integer.parseInt(auxC, 16) + 1);
                                        auxCont = auxC.toUpperCase();                        
                                    }
                                    else
                                        tipoError = direct.error; 
                                }
                                else
                                     tipoError = "La Directiva " + codop + " Debe Llevar Operando";                               
                     }
                else if(codop.toUpperCase().equals("DW") || codop.toUpperCase().equals("DC.W") || codop.toUpperCase().equals("FDB")) {
                                if(!operando.toUpperCase().equals("NULL")) {
                                    if(direct.TWOBYTES(operando, dir, codop)) {
                                        esDirectiva = true;
                                        contLoc = auxC.toUpperCase();
                                        auxC = Integer.toHexString(Integer.parseInt(auxC, 16) + 2);
                                        auxCont = auxC.toUpperCase();                     
                                    }
                                    else
                                        tipoError = direct.error; 
                                }
                                else
                                     tipoError = "La Directiva " + codop + " Debe Llevar Operando"; 
                     }
                else if(codop.toUpperCase().equals("FCC")) {
                                if(!operando.toUpperCase().equals("NULL")) {
                                    if(operando.length() == 1) 
                                        tipoError = "Formato Erroneo de Operando para Directiva FCC"; 
                                    else if(operando.charAt(0) == '"' && operando.charAt(operando.length()-1) == '"' && operando.substring(1, operando.length()-2).matches(".+")) {
                                    if(direct.FCC(operando.substring(1, operando.length()-1), dir, codop)) {
                                            esDirectiva = true;
                                            contLoc = auxC.toUpperCase();
                                            auxC = Integer.toHexString(Integer.parseInt(auxC, 16) + direct.valor);
                                            auxCont = auxC.toUpperCase();                   
                                        }
                                        else
                                            tipoError = direct.error; 
                                    }
                                    else
                                        tipoError = "Formato Erroneo de Operando para Directiva FCC";    
                                }
                                else
                                     tipoError = "La Directiva " + codop + " Debe Llevar Operando"; 
                     }
                else if(codop.toUpperCase().equals("DS") || codop.toUpperCase().equals("DS.B") || codop.toUpperCase().equals("RMB")) {
                                if(!operando.toUpperCase().equals("NULL")) {
                                    if(direct.TWOBYTES(operando, dir, codop)) {
                                        esDirectiva = true;
                                        contLoc = auxC.toUpperCase();
                                        auxC = Integer.toHexString(Integer.parseInt(auxC, 16) + direct.valor);
                                        auxCont = auxC.toUpperCase();                     
                                    }
                                    else
                                        tipoError = direct.error; 
                                }
                                else
                                     tipoError = "La Directiva " + codop + " Debe Llevar Operando"; 
                     }
                else if(codop.toUpperCase().equals("DS.W") || codop.toUpperCase().equals("RMW")) {
                                if(!operando.toUpperCase().equals("NULL")) {
                                    if(direct.TWOBYTES(operando, dir, codop)) {
                                        esDirectiva = true;
                                        contLoc = auxC.toUpperCase();
                                        auxC = Integer.toHexString(Integer.parseInt(auxC, 16) + (direct.valor * 2));
                                        auxCont = auxC.toUpperCase();                     
                                    }
                                    else
                                        tipoError = direct.error; 
                                }
                                else
                                     tipoError = "La Directiva " + codop + " Debe Llevar Operando"; 
                     }
          return esDirectiva;
     }   
     
     public boolean ValidaEtiq(String etiqueta, Vector<String> etiq) {
         boolean etqRepetida = false;;
         if(numLinea == 1)
         {
             etiq.add(etiqueta);
         }
         else if(numLinea > 1){
                    int et = 0;
                    while(et < etiq.size() && !etqRepetida) {
                        if(etiqueta.toUpperCase().equals(etiq.get(et).toUpperCase()) && !etiqueta.toUpperCase().equals("NULL")) {
                            etqRepetida = true;
                        }
                        else
                            et++;
                    }
                    if(etqRepetida) 
                        tipoError = "Etiqueta \"" + etiq.get(et) + "\" Repetida";
                    else 
                        etiq.add(etiqueta);
         }
         return etqRepetida;
     }
}