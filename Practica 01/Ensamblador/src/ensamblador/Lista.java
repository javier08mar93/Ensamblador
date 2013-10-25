/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ensamblador;

/**
 *
 * @author JAVIER
 */

public class Lista {    
    String modos, error, cambModo;       
    boolean expCorrecta, bandera, bandera2;
    int tokens, t, x;
    
    class Nodo {
        String instruccion, modo, codMaquina, byteCalculado, byteporCalcular, totalBytes;;
        Nodo sig;
    }
    
    private Nodo primero, ultimo;
    
    Lista() {
        primero = null;
        ultimo = null;
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
        nuevo.sig = null;
        if(primero == null){
            primero = nuevo;
            ultimo = nuevo;
        }
        else{
            ultimo.sig = nuevo;
            ultimo = nuevo;
        }
    }
    
    public String Buscar(String inst, String op) {
        Nodo reco = primero;
        bandera = true;
        bandera2 = true;
        modos = "";
        cambModo = "";
        error = "";
        expCorrecta = false;
        while(reco != null) {
            if(inst.toUpperCase().equals(reco.instruccion) && reco.byteporCalcular.compareTo("0") > 0 && op != "NULL") {
                modos = modos + reco.modo + " " +reco.totalBytes;
                modos = modos + "|";
                expCorrecta = true;
            }
            else if(inst.toUpperCase().equals(reco.instruccion) && reco.byteporCalcular.equals("0") && op == "NULL") {
                modos = modos + reco.modo + " " +reco.totalBytes;
                modos = modos + "|";
                expCorrecta = true;
            } 
            else if(inst.toUpperCase().equals(reco.instruccion) && reco.byteporCalcular.equals("0") && op != "NULL") {
                expCorrecta = false;
                bandera = false;
            }
            else if(inst.toUpperCase().equals(reco.instruccion) && reco.modo.equals("INH") && reco.byteporCalcular.compareTo("0") > 0 && op == "NULL") {
                modos = modos + reco.modo + " " +reco.totalBytes;
                modos = modos + "|";
                expCorrecta = true;
            }
            else if(inst.toUpperCase().equals(reco.instruccion) && !reco.modo.equals("INH") && reco.byteporCalcular.compareTo("0") > 0 && op == "NULL") {
                expCorrecta = false;
                bandera2 = false;
            }
          reco = reco.sig;
        } 
        if(inst.toUpperCase().equals("ORG")) 
            expCorrecta = true;

        if(expCorrecta) 
            return modos;
        else if(!bandera && !expCorrecta) return error = "No puede tener Operando";
        else if(!bandera2 && !expCorrecta) return error = "Tiene que llevar un Operando";
        else return error = "No se encontro Codigo de Operacion";        
    }        
}