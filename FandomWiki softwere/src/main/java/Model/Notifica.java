package main.java.Model;

import java.util.ArrayList;

public class Notifica {
    private OperazioneUtente Operazioni_notificate;

    public Notifica(OperazioneUtente Operazioni_notificate){
        this.Operazioni_notificate = Operazioni_notificate;
    }

    public OperazioneUtente getOperazioni_notificate() {
        return Operazioni_notificate;
    }
}
