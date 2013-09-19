/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ensamblador;

/**
 *
 * @author JAVIER
 */

import javax.swing.*;
import java.io.*;

public class Archivos {
    File archivo, archivo2;
    FileReader fr;
    FileWriter fw;
    BufferedReader br;
    BufferedWriter bw;
    String buffer;   
    
    public void AbrirASM() {        
        JFileChooser seleccArchivo = new JFileChooser();
        if(seleccArchivo.showOpenDialog(seleccArchivo) == JFileChooser.APPROVE_OPTION) {
            if(seleccArchivo.getSelectedFile().getName().substring(seleccArchivo.getSelectedFile().getName().length()-4).toUpperCase().equals(".ASM")){
                try {
                    archivo = seleccArchivo.getSelectedFile();
                    fr = new FileReader(archivo);
                    br = new BufferedReader(fr);
                }
                catch(IOException ioe) {
                    System.out.println("\nNo se pudo abrir el archivo");
                }
            }
        }
        else
            System.out.println("\nNo selecciono nada");
    }
    
    public void CreaINST(File archivo2) {
        try {     
            archivo = new File(archivo2.getPath().substring(0, archivo2.getPath().length()-4)+".INST");
            archivo.createNewFile();
        }
        catch(IOException ioe) {
            System.out.println("\nNo se pudo crear el archivo");
        }
    }
    
    public void CreaERR(File archivo2) {
        try {
            archivo = new File(archivo2.getPath().substring(0, archivo2.getPath().length()-4)+".ERR");
            archivo.createNewFile();
        }
        catch(IOException ioe) {
            System.out.println("\nNo se pudo crear el archivo");
        }
    }
    
    public void AbrirINST() {
        try {
            fw = new FileWriter(archivo);
            bw = new BufferedWriter(fw);
            bw.write("LINEA\tETQ\tCODOP\tOPER\tMODOS");
            bw.newLine();
            bw.write("------------------------------------------------------------");
            bw.newLine();
        }
        catch(IOException ioe) {
            System.out.println("\nNo se pudo abrir el archivo");            
        }
     }
    
    public void AbrirTABOP() {
        JFileChooser seleccArchivo = new JFileChooser();
        if(seleccArchivo.showOpenDialog(seleccArchivo) == JFileChooser.APPROVE_OPTION) {
            if(seleccArchivo.getSelectedFile().getName().substring(seleccArchivo.getSelectedFile().getName().length()-9).toUpperCase().equals("TABOP.TXT")){
                try {
                    archivo2 = seleccArchivo.getSelectedFile();
                    fr = new FileReader(archivo2);
                    br = new BufferedReader(fr);
                }
                catch(IOException ioe) {
                    System.out.println("\nNo se pudo abrir el archivo");
                }
            }
        }
        else
            System.out.println("\nNo selecciono nada");
    }
    
    public void AbrirERR() {
        try {
            fw = new FileWriter(archivo);
            bw = new BufferedWriter(fw);
            bw.write("LINEA\tERROR");
            bw.newLine();
            bw.write("------------------------------------------------------------");
            bw.newLine();
        }
        catch(IOException ioe) {
            System.out.println("\nNo se pudo abrir el archivo");            
        }
     }
    
    public void EscribeLineaINST(Linea linea) {
        try {
            if(linea.codop.compareTo("END") != 0) {
            buffer = linea.numLinea + "\t" + linea.etiqueta + "\t" + linea.codop + "\t" + linea.operando + "\t" + linea.modos;
            bw.write(buffer);
            bw.newLine();
            }
        }
        catch(IOException ioe) {
            System.out.println("\nNo se pudo abrir el archivo");
        }
    }
    
    
    public void EscribeLineaERR(Linea linea) {
        try {
            buffer = linea.numLinea + ". " + linea.tipoError;
            bw.write(buffer);
            bw.newLine();
        }
        catch(IOException ioe) {
            System.out.println("\nNo se pudo abrir el archivo");
        }
    }
    
    
    public String LeeLineaASM() {
        buffer = null;
        try{
            buffer = br.readLine();
        }
        catch(IOException ioe){
            System.out.println("\nNo se pudo abrir el archivo");
        }
        return buffer;
    }
    
    public String LeeLineaTABOP() {
        buffer = null;
        try{
            buffer = br.readLine();
        }
        catch(IOException ioe){
            System.out.println("\nNo se pudo abrir el archivo");
        }
        return buffer;
    }
    
    public void CerrarLectura() {
        try{
            br.close();
            fr.close();
        }
        catch(IOException ioe) {
            System.out.println("\nNo se pudo cerrar el archivo");
        }
    }
    
    public void CerrarEscritura() {
        try{
            bw.close();
            fw.close();
        }
        catch(IOException ioe) {
            System.out.println("\nNo se pudo cerrar el archivo");
        }
    }    
}