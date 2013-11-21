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
    File archivo, archivo2, archivo3;
    FileReader fr;
    FileWriter fw;
    BufferedReader br;
    BufferedWriter bw;
    String buffer;   
        
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
    
    public void CreaTDS(File archivo2) {
        try {
            archivo = new File(archivo2.getPath().substring(0, archivo2.getPath().length()-4)+".TDS");
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
            bw.write("LINEA\tCONLOC\tETQ\tCODOP\tOPER\tMODIR\tCODMAQ");
            bw.newLine();
            bw.write("--------------------------------------------------------------------------------------------------");
            bw.newLine();
        }
        catch(IOException ioe) {
            System.out.println("\nNo se pudo abrir el archivo");            
        }
     }
    
    public void AbrirERR() {
        try {
            fw = new FileWriter(archivo);
            bw = new BufferedWriter(fw);
            bw.write("LINEA\tERROR");
            bw.newLine();
            bw.write("--------------------------------------------------------------------------------------------------");
            bw.newLine();
        }
        catch(IOException ioe) {
            System.out.println("\nNo se pudo abrir el archivo");            
        }
     }
    
    public void AbrirTDS() {
        try {
            fw = new FileWriter(archivo);
            bw = new BufferedWriter(fw);
            bw.write("ETQ\tVALOR");
            bw.newLine();
            bw.write("-----------------------------");
            bw.newLine();
        }
        catch(IOException ioe) {
            System.out.println("\nNo se pudo abrir el archivo");            
        }
     }
    
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
    
    public void AbrirTABOP(File archivo) {
        archivo2 = new File(archivo.getParentFile(), "TABOP.txt");
        try {
                fr = new FileReader(archivo2);
                br = new BufferedReader(fr);
            }
        catch(IOException ioe) {
                System.out.println("\nNo se pudo abrir el archivo");
            }
    }
    
    public void EscribeLineaINST(Linea linea) {
        try {
            buffer = linea.numLinea + "." + "\t" + linea.contLoc  + "\t" + linea.etiqueta + "\t" + linea.codop + "\t" + linea.operando + "\t" + linea.modo + "\t" + linea.codMaquina;
            bw.write(buffer);
            bw.newLine();
        }
        catch(IOException ioe) {
            System.out.println("\nNo se pudo abrir el archivo");
        }
    }    
    
    public void EscribeLineaERR(Linea linea) {
        try {
            if(linea.numLinea > 0)
                buffer = linea.numLinea + "." + "\t" + linea.tipoError;
            else 
                buffer = "\t" + linea.tipoError; 
            bw.write(buffer);
            bw.newLine();
            
        }
        catch(IOException ioe) {
            System.out.println("\nNo se pudo abrir el archivo");
        }
    }  
    
    public void EscribeLineaTDS(Linea linea) {
        try {
            buffer = linea.etiqueta + "\t" + linea.contLoc;
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
    
    public String LeeLineaTDS() {
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