package main.java.Model;

import java.sql.Timestamp;
import java.util.Date;

public class ModificaUtente extends OperazioneUtente{


    public ModificaUtente(int id_operazione,Timestamp DataA, Timestamp DataR, String Testo, boolean accettata, boolean visionata, boolean modifica, boolean link, int link_pagina,String Utente){
        super(id_operazione, DataA,DataR,Testo,accettata,visionata,modifica, link,link_pagina, Utente);

    }

    public ModificaUtente(int id_operazione, Timestamp DataR, String Testo, boolean accettata, boolean visionata, boolean modifica, boolean link, int link_pagina,String Utente){
        super(id_operazione,DataR,Testo,accettata,visionata,modifica, link,link_pagina, Utente);

    }

    public ModificaUtente(int id_operazione,Timestamp DataA, Timestamp DataR, String Testo, boolean accettata, boolean visionata, boolean modifica, boolean link, int link_pagina,String Utente,String Autore, String Titolo_pagina_link, String titolo){
        super(id_operazione, DataA,DataR,Testo,accettata,visionata,modifica, link,link_pagina, Utente, Autore, Titolo_pagina_link, titolo);

    }

}
