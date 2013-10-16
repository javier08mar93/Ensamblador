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
    int numLinea;
    String etiqueta, codop, operando, tipoError, instruccion, modo, modos, codMaquina, byteCalculado, byteporCalcular, totalBytes;
    boolean esComentario, fin, encontrado;
    
    public Linea(int numeroLinea) {        
        numLinea = numeroLinea;
        etiqueta = "NULL";
        codop = "NULL";
        operando = "NULL";
        tipoError = null;
        esComentario = false;
        fin = false;
        modo = "";
    }
    
    public boolean SepararTokens(String linea) {
        boolean formatoCorrecto = true;
        StringTokenizer token = new StringTokenizer(linea, ";", true);
        if(token.countTokens() >= 1 && token.countTokens() <= 3){
            linea = token.nextToken();
            if(linea.charAt(0) != ';'){
                token = new StringTokenizer(linea);
                if(linea.charAt(0) == ' ' || linea.charAt(0) == '\t'){
                    if(token.countTokens() == 0)
                        esComentario = true;  
                    else if(token.countTokens() == 1) {                        
                        codop = token.nextToken();                                                 
                    }                     
                    else if(token.countTokens() == 2){                        
                        codop = token.nextToken();
                        operando = token.nextToken();
                    }
                    else {
                        tipoError = "Formato de Linea Invalido";
                        formatoCorrecto = false;
                    }                    
                }
                else{
                    if(token.countTokens() == 0)
                        esComentario = true;
                    else if(token.countTokens() == 1){
                        tipoError = "Formato de Linea Invalido";
                        formatoCorrecto = false;
                    }
                    else if(token.countTokens() == 2){
                        etiqueta = token.nextToken();                        
                        codop = token.nextToken();
                    }
                    else if(token.countTokens() == 3){                        
                        etiqueta = token.nextToken();                        
                        codop = token.nextToken();                        
                        operando = token.nextToken();
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
            return formatoCorrecto;
    }
    
    public boolean ValidaToken(String linea, Lista lista, ModosDireccionamiento dir) {
        boolean formatoCorrecto = true;
        if(SepararTokens(linea)){
            if(etiqueta != null){
                if(!etiqueta.matches("[A-Za-z][\\w]{0,7}")){
                    tipoError = "Etiqueta Invalida";
                    formatoCorrecto = false;
            } 
        }
        if(codop != null && formatoCorrecto) {
            if(codop.toUpperCase().equals("END") && operando.toUpperCase().equals("NULL"))
                fin = true;
            else if(codop.toUpperCase().equals("END") && !operando.toUpperCase().equals("NULL")) {
                fin = true;
                tipoError = "END no Puede Llevar Operando";
            }
                
            else if(!codop.matches("[a-zA-Z]((\\.[a-zA-Z]{0,3}|[a-zA-Z]{0,1}\\.[a-zA-Z]{0,2}|[a-zA-Z]{0,2}\\.[a-zA-Z]{0,1}|[a-zA-Z]{0,3}\\.)|[a-zA-Z]{0,4})")) {
                tipoError = "Codigo de Operacion Invalido";
                formatoCorrecto = false;
            }
            else {
                modos = lista.Buscar(codop, operando);
                if(lista.expCorrecta) {      
                    if(SeparaModos(modos, operando, dir)) 
                        modo = dir.tipoModo;
                    else
                        tipoError = dir.error;                
                }
                else if(!esComentario){ 
                    tipoError = lista.error; 
                    formatoCorrecto = false; 
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
    
     public boolean SeparaModos(String mod, String ope, ModosDireccionamiento dir) {
        Vector<String> tipoModo = new Vector<String>();
        boolean formatoCorrecto = true;
        encontrado = false;
        StringTokenizer token = new StringTokenizer(mod, "|");
        while(token.hasMoreTokens())
            tipoModo.add(token.nextToken());     
        
        dir.errorEtiqueta = dir.formaIMM = dir.formaEXT = dir.formaIDX = dir.formaIDX1 = dir.formaIDX2 = dir.formaIDX2C = dir.formaIDX_ID = dir.formaIDX_ACU = dir.formaIDX_ACU_IND = dir.formaREL = false; 
        int x = 0;
        while(x < tipoModo.size() && !encontrado && !dir.errorEtiqueta) {
            if(tipoModo.get(x).equals("INH")) { 
                if(dir.INH(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("IMM8") || tipoModo.get(x).equals("IMM16") || tipoModo.get(x).equals("IMM") && !encontrado) { 
                if(dir.IMM(ope, tipoModo.get(x))) {
                   formatoCorrecto = true;
                   encontrado = true;
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("DIR") && !encontrado && !dir.formaIMM) { 
                if(dir.DIR(ope)) { 
                   formatoCorrecto = true;
                   encontrado = true;
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("EXT") && !encontrado && !dir.formaIMM) { 
                if(dir.EXT(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("IDX") && !encontrado && !dir.formaIMM) { 
                if(dir.IDX(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                }
                else if(!dir.formaIDX) {
                        if(dir.IDX_ID(ope)) {
                        formatoCorrecto = true;
                        encontrado = true;
                    }
                }
                else if(!dir.formaIDX && !dir.formaIDX_ID) {
                        if(dir.IDX_ACU(ope) && !dir.formaIDX && !dir.formaIDX_ID) {
                            formatoCorrecto = true;
                            encontrado = true;
                    }
                }
                else formatoCorrecto = false;
            }                 
            else if(tipoModo.get(x).equals("IDX1") && !encontrado && !dir.formaIMM && !dir.formaEXT && !dir.formaIDX && !dir.formaIDX_ID && !dir.formaIDX_ACU) { 
                if(dir.IDX1(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("IDX2") && !encontrado && !dir.formaIMM && !dir.formaEXT && !dir.formaIDX && !dir.formaIDX_ID && !dir.formaIDX_ACU) { 
                if(dir.IDX2(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("[IDX2]") && !encontrado && !dir.formaIMM && !dir.formaEXT && !dir.formaIDX && !dir.formaIDX1 && !dir.formaIDX2 && !dir.formaIDX_ID && !dir.formaIDX_ACU) { 
                if(dir.IDX2C(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("[D,IDX]") && !encontrado && !dir.formaIMM && !dir.formaEXT && !dir.formaIDX && !dir.formaIDX1 && !dir.formaIDX2 && !dir.formaIDX2C && !dir.formaIDX_ID && !dir.formaIDX_ACU) { 
                if(dir.IDX_ACU_IND(ope)) {
                   formatoCorrecto = true;
                   encontrado = true;
                }
                else formatoCorrecto = false;
            }
            else if(tipoModo.get(x).equals("REL8") || tipoModo.get(x).equals("REL9") || tipoModo.get(x).equals("REL16") && !encontrado && !dir.formaIMM) { 
                if(dir.REL(ope, tipoModo.get(x))) { 
                   formatoCorrecto = true;
                   encontrado = true;
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
}