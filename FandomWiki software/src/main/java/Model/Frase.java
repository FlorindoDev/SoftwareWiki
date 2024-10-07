package main.java.Model;

import java.util.ArrayList;

public class Frase {

    private String Testo;
    private final int posizione;
    private InserimentoAutore inserimentoAutore = null;
    private ArrayList<ModificaAutore> ModificheAutore = new ArrayList<ModificaAutore>();
    private InserimentoUtente inserimentoUtente = null;
    private ArrayList<ModificaUtente> ModificheUtente = new ArrayList<ModificaUtente>();

    public Frase(String Testo, int posizione){
        this.Testo = Testo;
        this.posizione = posizione;
    }

    public void AddModificaAutore(ModificaAutore m){
        ModificheAutore.add(m);
    }
    public void AddModificaUtente(ModificaUtente m){
        ModificheUtente.add(m);
    }
    public void AddInserimentoAutore(InserimentoAutore i){
        this.inserimentoAutore = i;
    }
    public void AddInserimentoUtente(InserimentoUtente i){
        this.inserimentoUtente = i;
    }

    public ArrayList<String> getData(){

        ArrayList<String> Data = new ArrayList<>();
        Data.add(String.valueOf(posizione));
        Data.add(Testo);

        return Data;
    }

    public int getPosizione(){
        return posizione;
    }

    public String getTesto(){
        return Testo;
    }

}
