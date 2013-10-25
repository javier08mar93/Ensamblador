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
        Archivos TABOP = new Archivos(); 
        Lista listaTABOP = new Lista();
        ModosDireccionamiento dir = new ModosDireccionamiento();
        
        File auxFile;
        String linea;
        int numLinea = 1;
        Linea linea1, lineaAux;
        linea1 = new Linea(numLinea);     
        ArrayList<Linea> lista = new ArrayList<Linea>();
       
        ASM.AbrirASM();
        auxFile = ASM.archivo;
        TABOP.AbrirTABOP(auxFile);               
        while ((linea = TABOP.LeeLineaTABOP()) != null) {
            linea1.SeparaTABOP(linea, listaTABOP);
        }
        TABOP.CerrarLectura();        
              
        while((linea = ASM.LeeLineaASM()) != null && !linea1.fin) {
            linea1.ValidaToken(linea, listaTABOP, dir);
            lista.add(linea1);
            if(!linea1.fin) {
                numLinea++;
                linea1 = new Linea(numLinea);
            }
        }
        ASM.CerrarLectura();       
        
        auxFile = ASM.archivo;
        INST.CreaINST(auxFile);
        ERR.CreaERR(auxFile);
        INST.AbrirINST();
        ERR.AbrirERR();
        
        while(!lista.isEmpty()){
            lineaAux = lista.remove(0);    
            if(!lineaAux.esComentario && lineaAux.tipoError == null)
                INST.EscribeLineaINST(lineaAux);
            else if(lineaAux.tipoError != null)
                ERR.EscribeLineaERR(lineaAux);                      
        }
        
        if(linea1.fin == false) {
            linea1.numLinea = linea1.numLinea - 1;
            linea1.tipoError = "No se Encontro la Etiqueta END";
            ERR.EscribeLineaERR(linea1);
        }
        
        INST.CerrarEscritura();
        ERR.CerrarEscritura();        
    }
}