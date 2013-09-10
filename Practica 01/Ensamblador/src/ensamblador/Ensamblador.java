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
        File auxFile;
        String linea;
        int numLinea = 1;
        Linea linea1, lineaAux;
        ArrayList<Linea> lista = new ArrayList<Linea>();
        ASM.AbrirASM();
        linea1 = new Linea(numLinea);
        while((linea = ASM.LeeLineaASM()) != null && !linea1.fin) {
            linea1.ValidaToken(linea);
            lista.add(linea1);
            if(!linea1.fin) {
                numLinea++;
                linea1 = new Linea(numLinea);
            }
        }
        ASM.CerrarASM();
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
            linea1.tipoError = "NO SE ENCONTRO LA ETIQUETA... *END*";
            ERR.EscribeLineaERR(linea1);
        }
        INST.Cerrar();
        ERR.Cerrar();        
        
        // TODO code application logic here
    }
}