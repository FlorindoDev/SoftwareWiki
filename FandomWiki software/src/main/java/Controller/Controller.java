package main.java.Controller;

import main.java.Database.ConnessionePostges;
import main.java.ImplementazionePostgresDAO.*;
import main.java.Model.*;

import javax.swing.*;
import java.security.spec.ECField;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

public class Controller {

    Utente utenteLoggato = null;

    private HashMap<Integer, Pagina> Pagine = new HashMap<>(); //inseriti quando carico la getwiki selezionata //Integer:IdPagina
    private ArrayList<OperazioneUtente> Operazioni_utente = new ArrayList<>();

    /**
     * Funzione per effettuare il login
     * Chiama il metodo Login di LoginDAO
     * se Login di Login DAO ritorna 1 allora significa che l'utnete è un autore
     * se Login di Login DAO ritorna 0 allora significa che l'utente è un utente semplice
     * se Login di Login DAO ritorna null allora email e password non combaciano.
     * @return true se il login è effettuato con successo altrimenti
     * false in caso di problemi o di credenziali errate.
     */

    public boolean Login(String email, String password) {
        this.logOut();
        try {
            ArrayList<String> Contenuto =  new LoginPostgresDAO().Login(email, password);

            if(Contenuto !=null) {

                if(Contenuto.get(0).equals("1")){
                    utenteLoggato = new Autore(Contenuto.get(1),Contenuto.get(2),Contenuto.get(3),Contenuto.get(4),Contenuto.get(5).charAt(0));
                }else{
                    utenteLoggato = new Utente(Contenuto.get(1),Contenuto.get(2),Contenuto.get(3),Contenuto.get(4),Contenuto.get(5).charAt(0));
                }

                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }catch (Exception e){
            System.out.println("Errore con è null");
            return false;
        }


    }

    /**
     * Controlliamo se la connessione al db è presente
     * Lancia eccezzione se non è presente
     */
    public void CheckConnection() throws Exception {
        Connection con = new ConnessionePostges().openConnection();
        if(con == null) throw new Exception("Errore di connessione al database. Tutte le azioni future potrebbero essere compromesse.");
        con.close();
    }

    /**
     * Funzione per cercare le pagine all'interno del DaatBase in base al titolo
     * @return Matrice contenente per ogni riga tutti i dati di pagina che combacia con la ricerca
    */
    public ArrayList<ArrayList<String>> searchPages(String ricerca) {

        try {
            return new PaginaPostgresDAO().SearchPage(ricerca);
        } catch (SQLException e) {
            return null;
        }catch (Exception e){
            System.out.println("Errore con è null");
            return null;
        }


    }

    /**
     *
     * @return oggetto del utente loggato
     */
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    /**
     * Funzione che permette di caricare il contenuto di una pagina wiki.
     * Crea un oggetto pagina e ogni frase ritornata dal DB viene aggiunta all'oggetto pagina.
     * Inoltre l'oggetto pagina viene aggiunto a una HashMap, in modo tale che la prossima volta che viene
     * selezionata tale pagina non venga effettuata nuovamente una richiesta al DB
     * @param idPagina id della pagina che si vuole caricare
     * @return HashMap contenente per ogni chiave un array di dati cosi formato
     * (idPagia, posizione reale, testo, link, idLink) che costituisce un oggetto "Frase"
     */
    public HashMap<Integer, ArrayList<String>> getWikiPage(int idPagina) {

        if (Pagine.get(idPagina) != null) { //gia sta una pagina nell'hashmap

            Pagina StoredPage = Pagine.get(idPagina);
            HashMap<Integer, ArrayList<String>> frasi = new HashMap<>();
            if (StoredPage.getFrasi()!=null){
            for (Map.Entry<Integer, Frase> entry : StoredPage.getFrasi().entrySet()){
                ArrayList<String> temp = new ArrayList<>();
                temp.add(String.valueOf(idPagina));
                temp.add(String.valueOf(entry.getValue().getPosizione()));
                temp.add(entry.getValue().getTesto());

                if (entry.getValue() instanceof Link){
                    temp.add(String.valueOf(1));
                    temp.add(String.valueOf(((Link) entry.getValue()).getPaginaId()));
                }else{
                    temp.add(String.valueOf(0));
                    temp.add("null");
                }
                frasi.put(entry.getKey(), temp);
            }

            return frasi;
            }else {
                return null;
            }



        } else { //devo caricare dal DB
            try {
                PaginaPostgresDAO p = new PaginaPostgresDAO();
                HashMap<Integer, ArrayList<String>> Frasi = p.getWikiPage(idPagina);
                ArrayList<String> paginaCercata = p.getWikiInfo(idPagina);
                if (paginaCercata != null) {
                    Pagina pagina_cercata = new Pagina(paginaCercata.get(0), paginaCercata.get(1), paginaCercata.get(2), paginaCercata.get(3), paginaCercata.get(4), Integer.parseInt(paginaCercata.get(5)) );

                    for (Map.Entry<Integer, ArrayList<String>> entry : Frasi.entrySet()) {
                        //0 idPagina
                        //1 1
                        //2 testo
                        //3 link
                        //4 idLink
                        Frase frase;
                        if (entry.getValue().get(3).equals("1")){ //se è un link
                            ArrayList<String> PaginaLinkRow = p.getWikiInfo(Integer.parseInt(entry.getValue().get(4)));
                            Pagina PaginaLink = new Pagina(PaginaLinkRow.get(0), PaginaLinkRow.get(1), PaginaLinkRow.get(2), PaginaLinkRow.get(3), PaginaLinkRow.get(4), Integer.parseInt(PaginaLinkRow.get(5)));
                            frase = new Link(entry.getValue().get(2), Integer.parseInt(entry.getValue().get(1)), PaginaLink);//testo //posizione
                        }else{
                            frase = new Frase(entry.getValue().get(2), Integer.parseInt(entry.getValue().get(1)));
                        }

                        pagina_cercata.AddFrase(frase, entry.getKey());

                    }

                    Pagine.put(idPagina, pagina_cercata);
                }
                return Frasi;

            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

    }

    /**
     * Funzione che permette di effettuare la registrazione di un utente.
     * Tramite la chiamata del metodo RegisterUser di RegisterDAO è possibile registrare l'utente nel DB
     * e controllare tramite la Stringa di ritorno, se la registrazione è avvenuta con successo oppure no
     * @return Stringa che contiene un messaggio che può essere sia di errore che si successo
     */

    public String register(String Nome, String Cognome, Object Genere, String Email, String Password) {

        String messageError = "<html>";
        try{
            messageError += new RegisterPostgerDAO().RegisterUser(Nome, Cognome, Genere.toString(), Email, Password);
        }catch (Exception e){
            System.out.println("[-] "+e.getMessage());
            messageError += "problema sconosciuto<br>";
        }

        return messageError+"</html>";
    }

    /**
     * Controlla se l'utente loggato è un autore
     *
     * @return True se è autore, False se è un Utente
     */
    public boolean isAutore(){
        return utenteLoggato instanceof Autore;
    }

    /**
     * Controlla se l'utente loggato è un autore tramite isAutore().
     * Crea un oggetto di NotifichePostgresDAO.
     * Richiama la funzione LoadNotifiche(Email Autore) che restituisce una Matrice con tutti i dati restituiti dal DataBase
     * Per ogni colonna della matrice creiamo un oggetto ModificaUtente se il parametro a riga 5 colonna i(quindi i-esima Operazione) è true
     * altrimenti un inserimento
     */
    public  void LoadNotifiche() throws SQLException {

        if(this.isAutore()){
            NotifichePostgresDAO NotificheDao = new NotifichePostgresDAO();
            ArrayList<ArrayList> Dati = NotificheDao.LoadNotifiche(utenteLoggato.getEmail());
            Autore utenteLoggato1 = (Autore) utenteLoggato;
            utenteLoggato1.ResetNotifiche();

            //System.out.print((Dati.get(0).get(1)));
            for (int i = 0 ; i < Dati.get(0).size(); i++){
                if(!((Boolean) Dati.get(5).get(i))){
                    InserimentoUtente ToAdd = new InserimentoUtente((int) Dati.get(0).get(i),(Timestamp) Dati.get(1).get(i),(String) Dati.get(2).get(i),(Boolean)Dati.get(3).get(i),(Boolean)Dati.get(4).get(i),(Boolean)Dati.get(5).get(i),(Boolean)Dati.get(6).get(i),(int) Dati.get(7).get(i),(int) Dati.get(8).get(i),(String) Dati.get(9).get(i));
                    //System.out.printf(String.valueOf((Dati.get(0).get(i)).getClass()) + String.valueOf((Dati.get(1).get(i)).getClass()) + String.valueOf((Dati.get(2).get(i)).getClass())+ String.valueOf((Dati.get(3).get(i)).getClass())+ String.valueOf((Dati.get(4).get(i)).getClass())+ String.valueOf((Dati.get(5).get(i)).getClass())+ String.valueOf((Dati.get(6).get(i)).getClass())+ String.valueOf((Dati.get(7).get(i)).getClass())+ String.valueOf((Dati.get(8).get(i)).getClass())+ String.valueOf((Dati.get(9).get(i)).getClass()));
                    utenteLoggato1.addNotifica(new Notifica(ToAdd));
                    Operazioni_utente.add(ToAdd);
                    //new Notifica(new InserimentoUtente((Date) Dati.get(0).get(i), (Boolean) Dati.get(1).get(i),(Boolean) Dati.get(2).get(i)));
                    //System.out.println("io");
                }else{
                    ModificaUtente ToAdd = new ModificaUtente((int) Dati.get(0).get(i) ,(Timestamp) Dati.get(1).get(i),(String) Dati.get(2).get(i),(Boolean)Dati.get(3).get(i),(Boolean)Dati.get(4).get(i),(Boolean)Dati.get(5).get(i),(Boolean)Dati.get(6).get(i),(int) Dati.get(7).get(i),(String) Dati.get(9).get(i));
                    utenteLoggato1.addNotifica(new Notifica(ToAdd));
                    Operazioni_utente.add(ToAdd);
                }

            }

        }
    }

    /**
     * Carica una matrice con tutti i dati utili di una notifica, come:
     * Id_operazionme, DataR(data Richiesta), Testo, accettata, Visionata, Modifica, Link, Link_pagina, posizione e utente
     * dopo che ha caricato la matrice la restituisce, i dati sono presi dal model.
     *
     * @return Una Matrice con i dati di ogni Notifica nel model. Dove La riga Corrisponde a un tipo di dato del model
     * e un a colonna a un oggetto. Es: matrice 5x5 ho 5 attributi e 5 oggetti. Es: matrice 5x7 ho 5 attributi e sette oggetti
     * {riga}x{colonna}
     */
    public ArrayList<ArrayList> GetNotifiche(){

        ArrayList<ArrayList> s = null;
       if(this.isAutore()){
           s = new ArrayList<>();
           s.add(new ArrayList<Integer>());
           s.add(new ArrayList<Timestamp>());
           s.add(new ArrayList<String>());
           s.add(new ArrayList<Boolean>());
           s.add(new ArrayList<Boolean>());
           s.add(new ArrayList<Boolean>());
           s.add(new ArrayList<Boolean>());
           s.add(new ArrayList<Integer>());
           s.add(new ArrayList<Integer>());
           s.add(new ArrayList<String>());
           Autore utenteLoggato1 = (Autore) utenteLoggato;
           for(Notifica n: utenteLoggato1.getNotifiche()){
               s.get(0).add(n.getOperazioni_notificate().getIdOperazione());
               s.get(1).add(n.getOperazioni_notificate().getDataR());
               s.get(2).add(n.getOperazioni_notificate().getTesto());
               s.get(3).add(n.getOperazioni_notificate().getAccettata());
               s.get(4).add(n.getOperazioni_notificate().getVisionata());
               s.get(5).add(n.getOperazioni_notificate().getModifica());
               s.get(6).add(n.getOperazioni_notificate().getLink());
               s.get(7).add(n.getOperazioni_notificate().getLink_pagina());
               if(n.getOperazioni_notificate() instanceof InserimentoUtente){
                   s.get(8).add( ((InserimentoUtente)(n.getOperazioni_notificate())).getPosizione());
               }

               s.get(9).add(n.getOperazioni_notificate().getUtente());

           }

       }
        return s;
    }

    /**
     *  Crea un Oggetto ConfrontaPostgersDAO() e richiamo una sua funzione di nome Confronti LoadConfronto(id_operazione, email Autore)
     *  LoadConfronto ritornerà un stringa formattata nel seguente modo: Titolo+testo-posizione-link-
     * id pagina riferita-titolo pagina riferita | proposta testo-proposta posizione-
     * proposta link-proposta id pagina riferita-proposta titolo pagina riferita
     * @param id_operazione
     * @return Restituisce un array di stringe[](matrice di stringe o array 3D) dove in posizione zero ci sono le informazioni sulla frase in uno nella wiki e
     * nella posizione 1 le informazione della nuova frase.
     */
    public ArrayList<String[]> LoadConfronto(int id_operazione){
        ConfrontaPostgersDAO c = new ConfrontaPostgersDAO();
        try {
            ArrayList<String[]> Confronti = new ArrayList<>();
            String Confronto = c.LoadConfronto(id_operazione,utenteLoggato.getEmail());
            int index = Confronto.indexOf('+');
            Confronto = Confronto.substring(index+1);
            if(Confronto.contains("|")){
                String [] s = Confronto.split("\\|");
                Confronti.add(s[0].split("-"));
                Confronti.add(s[1].split("-"));
            }else{
                Confronti.add(Confronto.split("-"));
            }

            return Confronti;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea un oggetto di ModifichePostgresDAO.
     * Richiama la funzione LoadModifiche(Email Autore) che restituisce una Matrice con tutti i dati restituiti dal DataBase
     * Per ogni colonna della matrice creiamo un oggetto ModificaUtente se il parametro a riga 5 colonna i(quindi i-esima Operazione) è true
     * altrimenti un inserimento.
     *
     */
    public void loadModifiche(){
        ModifichePostgresDAO ModificheDao = new ModifichePostgresDAO();
        ArrayList<ArrayList> Dati;
        try {
            Dati = ModificheDao.LoadModifiche(utenteLoggato.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        utenteLoggato.ClearModifiche();
        //System.out.printf(String.valueOf(Dati));

        for (int i = 0 ; i < Dati.get(0).size(); i++){
            /*
            if(this.AggiornamentoModifiche((int) Dati.get(0).get(i),(String) Dati.get(11).get(i),(String) Dati.get(12).get(i),(Timestamp) Dati.get(10).get(i),(String) Dati.get(13).get(i), i,Dati)){
                continue;
            }

             */

            if(!((Boolean) Dati.get(5).get(i))){
                InserimentoUtente ToAdd = new InserimentoUtente((int) Dati.get(0).get(i),(Timestamp) Dati.get(10).get(i),(Timestamp) Dati.get(1).get(i),(String) Dati.get(2).get(i),(Boolean)Dati.get(3).get(i),(Boolean)Dati.get(4).get(i),(Boolean)Dati.get(5).get(i),(Boolean)Dati.get(6).get(i),(int) Dati.get(7).get(i),(int) Dati.get(8).get(i),(String) Dati.get(9).get(i),(String) Dati.get(11).get(i),(String) Dati.get(12).get(i),(String) Dati.get(13).get(i));
                utenteLoggato.addOperazione_Utente(ToAdd);
            }else{
                ModificaUtente ToAdd = new ModificaUtente((int) Dati.get(0).get(i),(Timestamp) Dati.get(10).get(i) ,(Timestamp) Dati.get(1).get(i),(String) Dati.get(2).get(i),(Boolean)Dati.get(3).get(i),(Boolean)Dati.get(4).get(i),(Boolean)Dati.get(5).get(i),(Boolean)Dati.get(6).get(i),(int) Dati.get(7).get(i),(String) Dati.get(9).get(i),(String) Dati.get(11).get(i),(String) Dati.get(12).get(i),(String) Dati.get(13).get(i));
                utenteLoggato.addOperazione_Utente(ToAdd);

            }

        }

    }


    /**
     * Carica una matrice con tutti i dati utili di una Modifica, come:
     * Id_operazionme, DataR(data Richiesta), Testo, accettata, Visionata, Modifica, Link, Link_pagina, posizione, utente,
     * DataA(data Accettazione), utenteNotificato, titolo pagina link(titolo della pagina in cui mi riferisco) e titolo pagina
     * dopo che ha caricato la matrice la restituisce, i dati sono presi dal model.
     *
     * @return Una Matrice con i dati di ogni Modifica nel model. Dove La riga Corrisponde a un tipo di dato del model
     * e un a colonna a un oggetto. Es: matrice 5x5 ho 5 attributi e 5 oggetti. Es: matrice 5x7 ho 5 attributi e sette oggetti
     * {riga}x{colonna}
     */
    public ArrayList<ArrayList> GetModifiche(){

        ArrayList<ArrayList> s = null;
        if(this.isAutore()){
            s = new ArrayList<>();
            s.add(new ArrayList<Integer>());
            s.add(new ArrayList<Timestamp>());
            s.add(new ArrayList<String>());
            s.add(new ArrayList<Boolean>());
            s.add(new ArrayList<Boolean>());
            s.add(new ArrayList<Boolean>());
            s.add(new ArrayList<Boolean>());
            s.add(new ArrayList<Integer>());
            s.add(new ArrayList<Integer>());
            s.add(new ArrayList<String>());
            s.add(new ArrayList<Timestamp>());
            s.add(new ArrayList<String>());
            s.add(new ArrayList<String>());
            s.add(new ArrayList<String>());

            //Autore utenteLoggato1 = (Autore) utenteLoggato;
            for(OperazioneUtente n: utenteLoggato.getOperazioni_Utente()){
                s.get(0).add(n.getIdOperazione());
                s.get(1).add(n.getDataR());
                s.get(2).add(n.getTesto());
                s.get(3).add(n.getAccettata());
                s.get(4).add(n.getVisionata());
                s.get(5).add(n.getModifica());
                s.get(6).add(n.getLink());
                s.get(7).add(n.getLink_pagina());
                if(n instanceof InserimentoUtente){
                    s.get(8).add( ((InserimentoUtente)(n)).getPosizione());
                }else{
                    s.get(8).add(0);
                }


                s.get(9).add(n.getUtente());
                s.get(10).add(n.getDataA());
                s.get(11).add(n.getUtenteNotificato());
                s.get(12).add(n.getTitoloPaginaLink());
                s.get(13).add(n.getTitolo());

            }

        }
        return s;
    }

    /*
    public boolean AggiornamentoModifiche(int id_operazione, String Autore, String Titolo, Timestamp Data, String TitoloPagina, int i, ArrayList<ArrayList> Dati){for(OperazioneUtente u: utenteLoggato.getOperazioni_Utente()){
            if(u.getIdOperazione() == id_operazione){
                ((OperazioneUtente) u).SetAutore(Autore);
                ((OperazioneUtente) u).SetTitoloLink(TitoloPagina);
                ((OperazioneUtente) u).SetDataA(Data);
                ((OperazioneUtente) u).SetTitolo(Titolo);
                for (int j = 0 ; j < Dati.size(); j++) {
                    Dati.get(j).remove(i);
                }
                return true;
            }

        }
        return false;
    }

     */

    /**
     * Se l'utente è un atuore imposta visonata a true nell'operazione utente che ha id_operazione = a id_operazione ingresso
     * @param id_operazione
     */

    public void SetVisionataNotificheModel(int id_operazione){
        Autore utenteLoggato1 = null;
        if(isAutore()){
            utenteLoggato1 = (Autore) utenteLoggato;
        }
        for(Notifica u: utenteLoggato1.getNotifiche()){

            if(u.getOperazioni_notificate().getIdOperazione() == id_operazione){
                u.getOperazioni_notificate().SetVisionata(true);
                return;
            }
        }
    }

    /**
     * Funzione che permette l'inserimento/proposta di inserimento di una frase.
     * Attraverso la chiamata proponiInserimento() di WikiPageDAO l'inserimento/proposta viene inserita nel DB.
     * Prima di chiamare proponiInserimento() viene controllato se l'utente loggato è un auotre oppure no, cosi da capire de il tipo di
     * operazione è una proposta o inserimento diretto della frase nel DB
     * @param idPagina pagina su cui si sta effettuando un insrimento
     * @param posizione posizione dove si sta inserendo una nuova frase
     * @param text testo della nuova frase
     * @param selected valore che specifica se la frase è un link oppure no
     * @param RiferimentoLink titolo della pagina a cui il link fa riferimento
     * @return Stringa che contiene un messaggio di successo o di errore
     */
    public String ProponiInserimento(int idPagina, String posizione, String text, boolean selected, String RiferimentoLink) {

        String messageError = "<html>";
        int posizioneInt=-1;

        if (posizione.isEmpty()){
            messageError += "posizione non valida<br>";
        }

        if (text.isEmpty()){
            return messageError += "Il testo è vuoto<br>";
        }

        try{
            posizioneInt = Integer.parseInt(posizione);
        }catch (NumberFormatException e){
            messageError += "La posizione deve esssere un numero<br>";
            return messageError;
        }

        Pagina pagina = Pagine.get(idPagina);
        Frase frase = pagina.getFrase(Integer.parseInt(posizione));

        Integer posizioneDB = posizioneInt;
        if (frase==null){ //la frase non è nell'hashmap
            //messageError += "posizione non valida<br>";
            int lastIdxFrase=pagina.getLastIdxFrase(); //puo essere l'ultima
            if (lastIdxFrase<posizioneInt){
                //System.out.println("stai inserendo in coda");
                //posizioneDB= Integer.valueOf(pagina.getFrase(lastIdxFrase).get(0))+1;
                posizioneDB= pagina.getFrase(lastIdxFrase).getPosizione()+1;
            }
        }else{
            //posizioneDB = Integer.parseInt(frase.get(0));  //0 : posizione
            posizioneDB = frase.getPosizione();
        }


            Boolean isAutore;
            Pagina currentPage = Pagine.get(idPagina);
            String email = utenteLoggato.getEmail();
            if ( email.equals(currentPage.getEmailAutore())){
                isAutore=true;
            }else{
                isAutore=false;
            }

            try {
                messageError += new WikiPagePostgresDAO().proponiInserimento(isAutore, idPagina, email, text, posizioneDB, selected, RiferimentoLink);
            }catch (Exception e){
                e.printStackTrace();
            }


        return messageError+"</html>";
    }

    /**
     * Funzione che permette la modifica/proposta di modifica di una frase.
     * Attraverso la chiamata proponiModifica() di WikiPageDAO la modifica/proposta viene inserita nel DB.
     * Prima di chiamare proponiModifica() viene controllato se l'utente loggato è un auotre oppure no, cosi da capire de il tipo di
     * operazione è una proposta o una modifica diretta della frase nel DB
     * @param idPagina pagina su cui si sta effettuando un insrimento
     * @param posizione posizione dove si sta inserendo una nuova frase
     * @param text testo della nuova frase
     * @param link valore che specifica se la frase è un link oppure no
     * @param titoloLink titolo della pagina a cui il link fa riferimento
     * @return Stringa che contiene un messaggio di successo o di errore
     */
    public String ProponiModifica(int idPagina, int posizione, String text, boolean link, String titoloLink) {

        String Message = "<html>";
        if (text.isEmpty()){
            Message+="Il testo è vuoto<br>";
        }else {

            Boolean isAutore;
            String email = utenteLoggato.getEmail();
            Pagina currentPage = Pagine.get(idPagina);
            if (email.equals(currentPage.getEmailAutore())){
                isAutore=true;
            }else{
                isAutore=false;
            }

            Pagina pagina = Pagine.get(idPagina);
            //ArrayList<String> frase = pagina.getFrase(posizione);
            Frase frase = pagina.getFrase(posizione);
            //posizione = Integer.parseInt(frase.get(0));
            posizione = frase.getPosizione();


            try{
                Message+= new WikiPagePostgresDAO().proponiModifica(isAutore, idPagina, email, text, posizione, link, titoloLink);
            }catch (Exception e){
                e.printStackTrace();
                Message+="problema sconosciuto<br>";
            }
        }

        return Message+"</html>";
    }


    /**
     * Crea un oggetto VisualizzaPostgersDAO()
     * e richiamiamo la funzione Visionata(id_operazione) che aggiorenera L'operazione utente come vidionata = true
     * @param id_operazione
     */
    public void SetVisionata(int id_operazione){
        VisualizzaPostgersDAO v = new VisualizzaPostgersDAO();
        try {
            v.Visionata(id_operazione);
        } catch (SQLException e) {
            //System.out.printf("visionata");
        }
    }

    /**
     * Funzione che permette di controllare se l'utente ha effettuato il login
     * @return True se ha effettuato il login, altrimenti False.
     */
    public boolean isUserLogged(){

        if (utenteLoggato==null){
            return false;
        }else{
            return true;
        }

    }


    /**
     * Crea un oggetto ModificaPorpostaPostgresDAO()
     * e richiama la funzione AggironamentoProposta(id_proposta, email utente loggato, Testo) e aggiorna il testo e dataR(data richiesta)
     */
    public void ModificaPropsostaEffetuata(int id_proposta,String Testo){
        ModificaPorpostaPostgresDAO m = new ModificaPorpostaPostgresDAO();
        m.AggironamentoProposta(id_proposta,utenteLoggato.getEmail(),Testo);
    }


    /**
     * Crea un oggetto NotifichePostgresDAO()
     * e richiama NumberOfNotiche(email utente loggato)
     * @return restituisce il numero di notifiche per l'utente loggato
     */
    public int NumerOfNotifiche(){
        NotifichePostgresDAO n = new NotifichePostgresDAO();
        try {
            //System.out.printf("\n%d", n.NumberOfNotiche(utenteLoggato.getEmail()));
            return n.NumberOfNotiche(utenteLoggato.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea un oggetto ModificaPorpostaPostgresDAO()
     * e richiama NumberOfModifiche(email utente loggato)
     * @return restituisce il numero di Modifiche per l'utente loggato
     */
    public int NumerOfModifiche(){
        ModificaPorpostaPostgresDAO m = new ModificaPorpostaPostgresDAO();
        try {
            return m.NumeroModifiche(utenteLoggato.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Crea un oggetto RisultatoConfrontoPostgresDAO()
     * Richiama Accettazione(id_operazione, email utente loggato) se accettata e 1 altrimenti Rifiuto(id_operazione, email utente loggato)
     * @param id_operazione
     * @param accettata
     */
    public void ModificaProposta(int id_operazione, int accettata){
        RisultatoConfrontoPostgresDAO r = new RisultatoConfrontoPostgresDAO();
        if(accettata == 1){
            r.Accettazione(id_operazione, utenteLoggato.getEmail());
        }else{
            r.Rifiuto(id_operazione, utenteLoggato.getEmail());
        }


    }



    /**
     * Funzione che permette di prendere le pagine wiki associate all'utente corrente.
     * @return Una Matrice di pagine wiki associate all'utente corrente.
     *         Se l'utente non ha pagine associate o si verifica un'eccezione durante l'accesso al database, restituisce null.
     */
    public ArrayList<ArrayList<String>> getMyPage(){

        String email = utenteLoggato.getEmail();
        ArrayList<ArrayList<String>> pages;
        try{
            pages = new PaginaPostgresDAO().getMyPage(email);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        if (pages.isEmpty()){
            return null;
        }else{
            return pages;
        }
    }

    /**
     * Funzione che permette di ottiene la storia specifica di una pagina wiki identificata dall'ID della pagina e dalla data specificata.
     * Restituisce una lista di frasi associate alla pagina per la data specificata.
     * @param idPage L'ID della pagina wiki di cui si desidera ottenere la storia.
     * @param Data La data per la quale si desidera ottenere la storia della pagina.
     * @return Matrice di frasi associate alla pagina per la data specificata.
     *         Se si verifica un'eccezione durante l'accesso al database, restituisce null.
     */

    public ArrayList<ArrayList<String>> getStoricitaPage(int idPage, String Data){

        ArrayList<ArrayList<String>> frasi;
        try {
            frasi = new PaginaPostgresDAO().getStroicitaSpecifica(idPage, Data);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


        return frasi;
    }

    /**
     * @param idPagina ID della pagina su cui si vuole conoscere tutte le date su cui si sono effettuate delle operazioni
     * @return Array che contiene tutte le date su cui si è accettata una Modifica o Inserimento, di una pagina specifica.
     */

    public ArrayList<String> getDateAvailable(int idPagina){

        try {
            return new PaginaPostgresDAO().getDateAvailable(idPagina);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Funzione utilizzata quando viene effettuato il log out di un utente dall'applicativo.
     * Setta l'oggetto utenteLoggato a null, cosi da cancellare i dati del vecchio utente e ripetere il log in
     */

    public void logOut(){

        this.utenteLoggato= null;
        this.Operazioni_utente = null;
        this.Operazioni_utente = new ArrayList<>();
    }

    /**
     * Crea una nuova pagina wiki con il titolo e la frase specificati.
     * Se il titolo o la frase sono vuoti, restituisce un messaggio di errore corrispondente.
     * Altrimenti, invia una richiesta al database per creare la pagina e restituisce un messaggio di successo o di errore.
     * @param titolo Il titolo della pagina wiki da creare.
     * @param frase La frase iniziale da inserire nella pagina wiki.
     * @param selected True se la pagina deve essere selezionata, false altrimenti.
     * @param TitoloPaginaLink Il titolo della pagina a cui collegare questa pagina, se applicabile.
     * @return Stringa che contiene un messaggio che indica se la creazione della pagina è avvenuta con successo o se si è verificato un errore.
     */

    public String creaPagina(String titolo, String frase, boolean selected, String TitoloPaginaLink) {

        String messageError = "<html>";
        if (titolo.isEmpty()) {
            messageError += "Il titolo è vuoto<br>";
        } else if (frase.isEmpty()) {
            messageError += "La frase è vuota<br>";
        } else {

            String email = utenteLoggato.getEmail();
            try {
                messageError += new PaginaPostgresDAO().createPage(email, titolo, frase, selected, TitoloPaginaLink);
            } catch (Exception e) {
                //e.printStackTrace();
                messageError += "Problema sconosciuto<br>";
            }

        }

        return messageError += "</html>";

    }

    /**
     * Fa un resize della Frame per far aggiornare il contenuto
     * @param W
     * @param H
     * @param frame
     */
    public void Resize(int W, int H, JFrame frame){
        frame.setSize(W+1,H);
        frame.setSize(W,H);
    }

    /**
     * Funzione che rimuove dall HashMap "Pagine" la pagina con id corrispondente.
     * Utilizzata prima delle operazioni di "Refresh" delle pagine Wiki.
     * @param idPagina numero che identifica la pagina da eliminare
     */
    public void removePage(int idPagina) {
        Pagine.remove(idPagina);
    }
}