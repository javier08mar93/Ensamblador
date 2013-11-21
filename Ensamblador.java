/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ensamblador;

/**
 *
 * @author JAVIER
 */

import java.io.*;
import java.util.*;

public class Ensamblador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Archivos ASM = new Archivos();
        Archivos INST = new Archivos();
        Archivos ERR = new Archivos();
        Archivos TDS = new Archivos();
        Archivos TABOP = new Archivos(); 
        Lista listaTABOP = new Lista();
        ModosDireccionamiento dir = new ModosDireccionamiento();
        Directivas direct = new Directivas();
        ArrayList<Linea> lista = new ArrayList<Linea>();
        ArrayList<Linea> listaETIQ = new ArrayList<Linea>();
        ArrayList<Linea> listaAux = new ArrayList<Linea>();
        ArrayList<Linea> listaTDS = new ArrayList<Linea>();
        ArrayList<Linea> listaAux2 = new ArrayList<Linea>();
        ArrayList<Linea> listaTDS2 = new ArrayList<Linea>();
        Vector<String> etiq = new Vector<String>();
        
        File auxFile;
        String linea;
        int numLinea = 1, num;
        String cont = "0", aux, aux2;
        boolean auxORG = false, correcto = false, recalcula = false, paso2Correcto = true;;
        Linea linea1, lineaAux;
        linea1 = new Linea(numLinea);          
        
        ASM.AbrirASM();
        auxFile = ASM.archivo;
        
        TABOP.AbrirTABOP(auxFile);         
        while ((linea = TABOP.LeeLineaTABOP()) != null) {
            linea1.SeparaTABOP(linea, listaTABOP);
        }
        TABOP.CerrarLectura();  
             
        while((linea = ASM.LeeLineaASM()) != null && !linea1.fin) {
            linea1.auxCont = cont;
            linea1.unORG = auxORG;
            linea1.ValidaToken(linea, listaTABOP, dir, direct, linea1.auxCont, linea1.unORG, etiq);
            lista.add(linea1);
            if(!linea1.fin) {
                cont = linea1.auxCont;
                auxORG = linea1.unORG;
                numLinea++;
                linea1 = new Linea(numLinea);                
            }            
        }
        ASM.CerrarLectura();       
        
        auxFile = ASM.archivo;
        INST.CreaINST(auxFile);
        ERR.CreaERR(auxFile);
        TDS.CreaTDS(auxFile);
        INST.AbrirINST();
        ERR.AbrirERR();
        TDS.AbrirTDS();
        
        num = 0;
        while(num < lista.size()) {
            lineaAux = lista.get(num);    
            if(!lineaAux.esComentario && lineaAux.tipoError == null) {
                if(!lineaAux.etiqueta.equals("NULL")) {
                   listaETIQ.add(lineaAux);
                }
            } 
            num++;
        }        
        
        num = 0;
        aux = aux2 = "";
        recalcula = false; 
        while(!lista.isEmpty()) {
            correcto = false;
            lineaAux = lista.remove(0); 
            if(lineaAux.fin)
                linea1.fin = true;
            
            if(lineaAux.operando.matches("[A-Za-z][\\w]{0,7}") && lineaAux.tipoError == null && !lineaAux.operando.equals("NULL")) {
                num = 0;
                while(num < listaETIQ.size() && !correcto) {
                    linea1 = listaETIQ.get(num);
                    if(lineaAux.operando.equals(linea1.etiqueta)) 
                        correcto = true;                    
                    else 
                        lineaAux.tipoError = "Etiqueta \"" + lineaAux.operando + "\" No Encontrada en Archivo .TDS";
                   num++;
                }              
            }
            
            if(correcto)
                lineaAux.tipoError = null;
            else if(!correcto && num == listaETIQ.size())
                recalcula = true;
            
            if(recalcula && lineaAux.tipoError == null) {
                if(!lineaAux.codop.toUpperCase().equals("ORG") && !lineaAux.codop.toUpperCase().equals("EQU"))
                    lineaAux.contLoc = Integer.toHexString(Integer.parseInt(aux, 16) + Integer.parseInt(aux2, 16));
                
                else if(lineaAux.codop.toUpperCase().equals("ORG") || lineaAux.codop.toUpperCase().equals("EQU"))
                    lineaAux.contLoc = Integer.toHexString(lineaAux.byteOperando);
                
                for(int ceros = lineaAux.contLoc.length(); ceros < 4; ceros++) 
                    lineaAux.contLoc = "0" + lineaAux.contLoc.toUpperCase();
            }
        
        if(!lineaAux.esComentario && lineaAux.tipoError == null) {
                if(!lineaAux.etiqueta.equals("NULL")) { 
                    listaTDS.add(lineaAux);
                    listaAux.add(lineaAux);
                }
                else      
                   listaAux.add(lineaAux);
                
                if(!lineaAux.codop.toUpperCase().equals("EQU")) {
                    aux = lineaAux.contLoc;
                    aux2 = Integer.toHexString(lineaAux.byteOperando);
                }                
            }
            else if(lineaAux.tipoError != null)
                listaAux.add(lineaAux);
        }
        
        num = 0;
        aux = aux2 = "";
        recalcula = false;  
        while(!listaAux.isEmpty()) {
            correcto = false;
            lineaAux = listaAux.remove(0); 
            if(lineaAux.fin)
                linea1.fin = true;
            
            if(lineaAux.operando.matches("[A-Za-z][\\w]{0,7}") && lineaAux.tipoError == null && !lineaAux.operando.equals("NULL")) {
                num = 0;
                while(num < listaTDS.size() && !correcto) {
                    linea1 = listaTDS.get(num);
                    if(lineaAux.operando.equals(linea1.etiqueta)) 
                        correcto = true;                    
                    else 
                        lineaAux.tipoError = "Etiqueta \"" + lineaAux.operando + "\" No Encontrada en Archivo .TDS";
                   num++;
                }              
            }
            
            if(correcto)
                lineaAux.tipoError = null;
            else if(!correcto && num == listaTDS.size())
                recalcula = true;
            
            if(recalcula && lineaAux.tipoError == null) {
                if(!lineaAux.codop.toUpperCase().equals("ORG") && !lineaAux.codop.toUpperCase().equals("EQU"))
                    lineaAux.contLoc = Integer.toHexString(Integer.parseInt(aux, 16) + Integer.parseInt(aux2, 16));
                
                else if(lineaAux.codop.toUpperCase().equals("ORG") || lineaAux.codop.toUpperCase().equals("EQU"))
                    lineaAux.contLoc = Integer.toHexString(lineaAux.byteOperando);
                
                for(int ceros = lineaAux.contLoc.length(); ceros < 4; ceros++) 
                    lineaAux.contLoc = "0" + lineaAux.contLoc.toUpperCase();
            }
        
        if(!lineaAux.esComentario && lineaAux.tipoError == null) {
            if(lineaAux.modo.equals("INH") && lineaAux.byteporCalcular.equals("0") && lineaAux.codMaquina.length() %2 != 0)
                lineaAux.codMaquina = "0" + lineaAux.codMaquina;
            lineaAux.codMaquina = lineaAux.codMaquina + lineaAux.valorOperando.toUpperCase();
                if(!lineaAux.etiqueta.equals("NULL")) {
                    TDS.EscribeLineaTDS(lineaAux);  
                    listaTDS2.add(lineaAux);
                    listaAux2.add(lineaAux);
                }
                else
                   listaAux2.add(lineaAux);
                
                if(!lineaAux.codop.toUpperCase().equals("EQU")) {
                    aux = lineaAux.contLoc;
                    aux2 = Integer.toHexString(lineaAux.byteOperando);
                }                
            }
            else if(lineaAux.tipoError != null) {
                ERR.EscribeLineaERR(lineaAux); 
                paso2Correcto = false;
            }
        }
              
        while(!listaAux2.isEmpty()) {
            correcto = false;            
            lineaAux = listaAux2.remove(0); 
            lineaAux.contLoc = lineaAux.contLoc.toUpperCase();  
            if(lineaAux.fin)
                linea1.fin = true;
            if(!paso2Correcto)
                lineaAux.codMaquina = "";
            if(!lineaAux.esComentario && lineaAux.tipoError == null) {
                
                if(!lineaAux.operando.equals("NULL")) {
                    num = 0;
                    while(num < listaTDS2.size() && !correcto) {
                        linea1 = listaTDS2.get(num);
                        if(lineaAux.operando.equals(linea1.etiqueta)) {
                            lineaAux.codMaquina = lineaAux.codMaquina + linea1.contLoc;
                            correcto = true;                    
                        }
                       num++;
                    }   
                   if(!paso2Correcto)
                       lineaAux.codMaquina = ""; 
                   if(correcto) 
                     INST.EscribeLineaINST(lineaAux);
                   else    
                     INST.EscribeLineaINST(lineaAux);
                }
                else    
                    INST.EscribeLineaINST(lineaAux);
            }    
        }
        
        if(!linea1.fin || !paso2Correcto) {
            if(!linea1.fin && !paso2Correcto) {
                linea1.numLinea = -1;
                linea1.tipoError = "No se Encontro la Etiqueta END";
                ERR.EscribeLineaERR(linea1);
                linea1.numLinea = -1;
                linea1.tipoError = "No se Puede Pasar al Paso 2";
                ERR.EscribeLineaERR(linea1);
            }
            else if(!linea1.fin) {
                linea1.numLinea = -1;
                linea1.tipoError = "No se Encontro la Etiqueta END";
                ERR.EscribeLineaERR(linea1);
            }
            else if(!paso2Correcto) {
                linea1.numLinea = -1;
                linea1.tipoError = "No se Puede Pasar al Paso 2";
                ERR.EscribeLineaERR(linea1);
            }
        }
        
        INST.CerrarEscritura();
        ERR.CerrarEscritura();
        TDS.CerrarEscritura();  
    
    }
}