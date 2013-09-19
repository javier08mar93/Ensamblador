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
    boolean esComentario, fin;
    String orden[] = new String[12];
    
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
    
    public boolean ValidaToken(String linea, Lista lista) {
        boolean formatoCorrecto = true;
        if(SepararTokens(linea)){
            if(etiqueta != null){
                if(!etiqueta.matches("[A-Za-z][\\w]{0,7}")){
                    tipoError = "Etiqueta Invalida";
                    formatoCorrecto = false;
            } 
        }
        if(codop != null && formatoCorrecto) {
            if(codop.toUpperCase().equals("END"))
                fin = true;
            else if(!codop.matches("[a-zA-Z]((\\.[a-zA-Z]{0,3}|[a-zA-Z]{0,1}\\.[a-zA-Z]{0,2}|[a-zA-Z]{0,2}\\.[a-zA-Z]{0,1}|[a-zA-Z]{0,3}\\.)|[a-zA-Z]{0,4})")) {
                tipoError = "Codigo de Operacion Invalido";
                formatoCorrecto = false;
            }
            else {
                modos = lista.Buscar(codop, operando);
                if(lista.expCorrecta) 
                    modo = modos;
                else { 
                    tipoError = lista.error; 
                    formatoCorrecto = false; 
            }
            }
        } 
        if(operando != null && formatoCorrecto){
            if(!operando.matches(".+")){
                tipoError = "Operando Invalido";
                formatoCorrecto = false;
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
    
}