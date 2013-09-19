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

public class Lista {    
    String modos, error;
    boolean expCorrecta, bandera, bandera2;
    String orden[], modo2;
    
    class Nodo {
        String instruccion, modo, codMaquina, byteCalculado, byteporCalcular, totalBytes;;
        Nodo sig;
    }
    
    private Nodo raiz;
    
    Lista() {
        raiz = null;
    }
    
    public void Insertar(String inst, String mod, String codM, String bCal, String bporCal, String total) {
        Nodo nuevo;
        nuevo = new Nodo();
        nuevo.instruccion = inst;
        nuevo.modo = mod;
        nuevo.codMaquina = codM;
        nuevo.byteCalculado = bCal;
        nuevo.byteporCalcular = bporCal;
        nuevo.totalBytes = total;
        if(raiz == null){
            nuevo.sig = null;
            raiz = nuevo;
        }
        else{
            nuevo.sig = raiz;
            raiz = nuevo;
        }
    }
    
    public String Buscar(String inst, String op) {
        Nodo reco = raiz;
        bandera = true;
        bandera2 = true;
        modos = "";
        error = "";
        expCorrecta = false;
        while(reco != null) {
            if(inst.toUpperCase().equals(reco.instruccion) && reco.byteporCalcular.compareTo("0") > 0 && op != "NULL") {
                modos = modos + reco.modo;
                modos = modos + ",";
                expCorrecta = true;
            }
            else if(inst.toUpperCase().equals(reco.instruccion) && reco.byteporCalcular.compareTo("0") > 0 && op == "NULL") {
                expCorrecta = false;
                bandera2 = false;
            }
            else if(inst.toUpperCase().equals(reco.instruccion) && reco.byteporCalcular.compareTo("0") == 0 && op == "NULL") {
                modos = modos + reco.modo;
                modos = modos + ",";
                expCorrecta = true;
            } 
            else if(inst.toUpperCase().equals(reco.instruccion) && reco.byteporCalcular.equals("0") && op != "NULL") {
                expCorrecta = false;
                bandera = false;
            }
          reco = reco.sig;
        }      
        if(inst.compareTo("ORG") == 0) {
            expCorrecta = true;
            return modos = "";            
        }
        
        if(expCorrecta)
            return modos.substring(0, modos.length()-2);
        else if(!bandera && !expCorrecta) return error = "No puede tener Operando";
        else if(!bandera2 && !expCorrecta) return error = "Tiene que llevar un Operando";
        else return error = "No se encontro Codigo de Operacion";        
    }     
}