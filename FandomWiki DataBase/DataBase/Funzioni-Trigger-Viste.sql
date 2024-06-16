

--1
CREATE OR REPLACE FUNCTION Modifica_Proposta() RETURNS TRIGGER 
AS $Modifica_Proposta$
BEGIN
    RAISE EXCEPTION 'E07: La proposta è gia stata visionata';
    RETURN NEW;
END;
$Modifica_Proposta$
LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER modifica_proposta
BEFORE UPDATE OF testo ON operazione_utente 
FOR EACH ROW
WHEN (old.visionata = 1::bit(1))
EXECUTE FUNCTION modifica_proposta();




--2

set time zone 'Europe/Rome';

CREATE OR REPLACE FUNCTION AggiornamentoDataMod() RETURNS TRIGGER 
AS $Aggiornamento_data_mod$
BEGIN
    new.DataR = localtimestamp(0);
    RETURN NEW;
END;
$Aggiornamento_data_mod$
LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER AggiornamentoDataMod
BEFORE UPDATE OF testo ON operazione_utente 
FOR EACH ROW
EXECUTE FUNCTION AggiornamentoDataMod();


--3

CREATE OR REPLACE FUNCTION Utente_No_Delete()
RETURNS TRIGGER
AS $Utente_No_Delete$
BEGIN
	RAISE EXCEPTION 'E06: Un utente non puo essere cancellato';
	RETURN NEW;
END;
$Utente_No_Delete$ LANGUAGE plpgsql;

CREATE TRIGGER Utente_No_delete
BEFORE DELETE ON Utente
FOR EACH ROW
EXECUTE FUNCTION Utente_No_Delete();

--4

CREATE OR REPLACE FUNCTION Aggiornamento_Generalita_Pagina()
RETURNS TRIGGER
AS $Aggiornamento_Generalita_Pagina$
DECLARE
    generalita VARCHAR (41);
    cur CURSOR FOR SELECT id_pagina FROM PAGINA WHERE emailautore=NEW.email;
	idPagina INTEGER;
BEGIN
    
    generalita := NEW.Nome || ';' || NEW.Cognome;
    OPEN cur;
	LOOP
		FETCH cur INTO idPagina;
        EXIT WHEN NOT FOUND;
        UPDATE PAGINA SET generalita_autore=generalita WHERE id_pagina = idPagina;
		
    END LOOP;
    CLOSE cur;
    
    RETURN NEW;
END;
$Aggiornamento_Generalita_Pagina$ LANGUAGE plpgsql;

CREATE TRIGGER Aggiornamento_Generalita_Pagina
AFTER UPDATE OF nome, cognome ON Utente
FOR EACH ROW
WHEN (NEW.autore=1::BIT(1))
EXECUTE FUNCTION Aggiornamento_Generalita_Pagina();

-- 5

CREATE OR REPLACE FUNCTION SwitchAutore() RETURNS trigger 
as $SwitchAutore$
begin
 	IF NOT EXISTS(SELECT email FROM utente where new.emailautore = email and autore = 1::BIT(1)) THEN
		UPDATE utente set autore = 1::BIT(1) where new.emailautore = email;
	END IF;
	RETURN NEW;
end;
$SwitchAutore$  LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER SwitchAutore
BEFORE INSERT ON pagina
FOR EACH ROW
EXECUTE FUNCTION SwitchAutore();

-- 6

CREATE OR REPLACE FUNCTION SwitchUtente() RETURNS trigger 
as $SwitchUtente$
begin
 	IF NOT EXISTS(SELECT p1.Id_pagina FROM (pagina as p1 join pagina on old.emailautore = p1.emailautore)) THEN
		UPDATE utente set autore = 0::BIT(1) where old.emailautore = email;
	END IF;
	RETURN NEW;
end;
$SwitchUtente$  LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER SwitchUtente
AFTER DELETE ON pagina
FOR EACH ROW
EXECUTE FUNCTION SwitchUtente();


-- 7


CREATE OR REPLACE PROCEDURE InsertFraseAutore(Id_pagina_in INTEGER, EmailAutore VARCHAR(255), Testo TEXT, link BIT(1), titolo_pagina_link varchar(255), posizione INTEGER)
as $InsertFraseAutore$ 
DECLARE
	idpagina_link INTEGER;
BEGIN 
	CALL CheckAutore(EmailAutore, id_pagina_in);
	IF(link = 1::BIT(1)) then
		SELECT id_pagina INTO idpagina_link FROM pagina where  titolo_pagina_link = pagina.titolo;
		IF FOUND THEN
			INSERT INTO frase VALUES (Id_pagina_in, posizione,Testo,link,idpagina_link);
			
		else
			RAISE EXCEPTION 'E04: La pagina non esiste(link non valido)';
			ROLLBACK;
		END IF;
	ELSE
		INSERT INTO frase VALUES(Id_pagina_in, posizione,Testo,link,null);	
	END IF;

END;
$InsertFraseAutore$ LANGUAGE plpgsql;
/*
as $InsertFraseAutore$
DECLARE
	idpagina_link INTEGER;
BEGIN 
	CALL CheckAutore(EmailAutore_in, id_pagina_in);
	IF(link = 1::BIT(1)) then
		SELECT id_pagina INTO idpagina_link FROM pagina where  titolo_pagina_link = pagina.titolo;
		IF FOUND THEN
			INSERT INTO frase VALUES (Id_pagina_in, posizione,Testo,link,idpagina_link);
			INSERT INTO operazione_autore(data,testo,posizioneins,modifica,link,link_pagina,posizione_frase,pagina_frase,autore) 
				VALUES(localtimestamp(0),testo,posizione,0::BIT(1),link,idpagina_link,posizione,id_pagina_in,(select emailautore from pagina where id_pagina_in = id_pagina));
		else
			RAISE EXCEPTION 'La pagina non esiste(link non valido)';
			ROLLBACK;
		END IF;
	ELSE
		INSERT INTO operazione_autore(data,testo,posizioneins,modifica,link,link_pagina,posizione_frase,pagina_frase,autore) 
				VALUES(localtimestamp(0),testo,posizione,0::BIT(1),link,null,posizione,id_pagina_in,(select emailautore from pagina where id_pagina_in = id_pagina));
		INSERT INTO frase VALUES(Id_pagina_in, posizione,Testo,link,null);
		
		
	END IF;

END;
$InsertFraseAutore$ LANGUAGE plpgsql;
*/

CREATE OR REPLACE PROCEDURE CreazionePagina(Titolo_in VARCHAR(255), Emailautore VARCHAR(255), Testo TEXT, link BIT(1), titolo_pagina_link varchar(255), posizione INTEGER)
as $CreazionePagina$
DECLARE
	Nome VARCHAR(20);
	Cognome VARCHAR(20);
	Generalita VARCHAR(41);
	id_page integer;

BEGIN
	IF EXISTS(SELECT email FROM Utente where Emailautore = utente.email) THEN	
			SELECT u.cognome , u.nome into Cognome, nome FROM Utente as u where Emailautore = u.email;
			Generalita := nome || ';' || cognome;
			INSERT INTO Pagina (Titolo, Generalita_Autore, DataUltimaModifica, DataCreazione, emailAutore) VALUES(Titolo_in, Generalita, localtimestamp(0), localtimestamp(0), Emailautore);
			SELECT id_pagina into id_page FROM pagina where Titolo_in = pagina.titolo;
			CALL InsertFraseAutore(id_page, Emailautore , Testo, link , titolo_pagina_link , posizione);
	else
		RAISE EXCEPTION 'E05: Utente non trovato';
		ROLLBACK;	
	END IF;

END;
$CreazionePagina$ LANGUAGE plpgsql;


-- prova


CREATE OR REPLACE VIEW  Operazione_autore_ordered AS
SELECT data, pagina_frase,testo,posizione_frase,link,link_pagina from Operazione_autore order by pagina_frase,data ASC;

CREATE OR REPLACE VIEW  Operazione_utente_ordered AS
SELECT dataA as data,pagina_frase,testo,posizione_frase,link,link_pagina  from Operazione_utente WHERE dataa IS NOT NULL AND accettata = 1::bit(1) order by pagina_frase,data ASC;


CREATE OR REPLACE VIEW log_page AS
SELECT data, pagina_frase,testo,posizione_frase,link,link_pagina  from (
  SELECT data, pagina_frase, testo, posizione_frase,link,link_pagina 
  FROM Operazione_autore_ordered
  
  UNION
  
  SELECT data, pagina_frase, testo, posizione_frase,link,link_pagina 
  FROM Operazione_utente_ordered
) order by pagina_frase, data ASC;



-- 8

CREATE OR REPLACE FUNCTION DataModficaPagina() RETURNS TRIGGER
AS $DataModficaPagina$
BEGIN
	UPDATE pagina set dataultimamodifica = localtimestamp(0) where id_pagina = new.pagina; 
	RETURN NEW;
END;
$DataModficaPagina$ LANGUAGE PLPGSQL;

CREATE OR REPLACE TRIGGER DataModficaPagina
AFTER INSERT ON frase
FOR EACH ROW
EXECUTE FUNCTION DataModficaPagina();



-- 9


CREATE OR REPLACE PROCEDURE ModificaFraseAutore(Id_pagina_in INTEGER, Emailautore VARCHAR(255), Testo_in TEXT, link_in BIT(1), titolo_pagina_link varchar(255), posizione_in INTEGER)
AS $ModificaFraseAutore$
DECLARE
	idpagina_link INTEGER;
	
BEGIN 

	CALL CheckAutore(EmailAutore, id_pagina_in);
	IF(link_in = 1::BIT(1)) then
		SELECT id_pagina INTO idpagina_link FROM pagina where  titolo_pagina_link = pagina.titolo;
		IF FOUND THEN
			UPDATE frase SET testo = Testo_in , link = link_in, linkpagina = idpagina_link  where Id_pagina_in = pagina and posizione_in = posizione;   
		else
			RAISE EXCEPTION 'E04: La pagina non esiste(link non valido)';
			ROLLBACK;
		END IF;
	ELSE
		UPDATE frase SET testo = Testo_in , link = link_in, linkpagina = NULL where Id_pagina_in = pagina and posizione_in = posizione;
	END IF;	
	
END;
$ModificaFraseAutore$ LANGUAGE PLPGSQL;


-- 10


CREATE OR REPLACE PROCEDURE CheckAutore(EmailAutore_in VARCHAR(255), id_pagina_in INTEGER) AS 
$CheckAutore$

BEGIN
	IF EXISTS(SELECT id_pagina FROM pagina where id_pagina_in = pagina.id_pagina) THEN
		IF EXISTS(SELECT email FROM Utente where EmailAutore_in = utente.email and Utente.autore = 1::BIT(1)) THEN
			IF EXISTS(SELECT email from utente join pagina on pagina.emailautore = utente.email where EmailAutore_in like utente.email and id_pagina_in = pagina.id_pagina) THEN
				RETURN;
			ELSE
				RAISE EXCEPTION 'E02: Questo utente non è autore della pagina';
				ROLLBACK;		
			END IF;
		ELSE
			RAISE EXCEPTION 'E03: Utente non trovato o utente non è autore';
			ROLLBACK;		
		END IF;
	ELSE
		RAISE EXCEPTION 'E09: La pagina non esiste';
		ROLLBACK;
	END IF;
END;
$CheckAutore$ LANGUAGE PLPGSQL;

-- 11


CREATE OR REPLACE FUNCTION PositionControll() RETURNS TRIGGER AS 
$PositionControll$
DECLARE
	max_pos integer;
	min_pos integer;
	max_int_pos integer;

BEGIN

	SELECT MAX(posizione) into max_pos from frase where pagina = new.pagina;
	IF NOT FOUND THEN
		max_pos = 0;
	END IF;


	IF max_pos < new.posizione THEN
		new.posizione = max_pos + 200;


	ELSEIF max_pos >= new.posizione then

		IF new.posizione = 1 THEN
			CALL UpadatePosition(new.posizione,new.pagina,1::BIT(1));
			RETURN NEW;
		END IF;

		SELECT MIN(posizione) into min_pos  from frase where pagina = new.pagina and new.posizione <= posizione;
		SELECT MAX(posizione) into max_int_pos  from frase where pagina = new.pagina and new.posizione > posizione;
		new.posizione := (min_pos + max_int_pos)/2;
		--new.posizione := new.posizione/2;
		--RAISE NOTICE 'pos %|min % | max %', new.posizione, min_pos,max_int_pos;
		IF EXISTS(SELECT posizione from frase where new.pagina = pagina and new.posizione = posizione) THEN

			CALL UpadatePosition(new.posizione,new.pagina,0::BIT(1));
			new.posizione := (min_pos + 100);
		END IF;
	END IF;	

	RETURN NEW;
END;
$PositionControll$ LANGUAGE PLPGSQL;



CREATE OR REPLACE TRIGGER PositionControll
BEFORE INSERT ON Frase
FOR EACH ROW
EXECUTE FUNCTION PositionControll();



CREATE OR REPLACE FUNCTION OperazioneAutoreIntegritaIns() RETURNS TRIGGER AS 
$OperazioneAutoreIntegritaIns$

BEGIN
	IF NOT EXISTS(SELECT id_operazione from operazione_utente where new.pagina = pagina_frase and posizioneins = new.posizione) THEN
		INSERT INTO operazione_autore(data,testo,posizioneins,modifica,link,link_pagina,posizione_frase,pagina_frase,autore) VALUES(localtimestamp(0),new.testo,new.posizione,0::BIT(1),new.link,new.linkpagina,new.posizione,new.pagina,(select emailautore from pagina where new.pagina = id_pagina));
	END IF;
	RETURN NEW;
END;
$OperazioneAutoreIntegritaIns$ LANGUAGE PLPGSQL;


CREATE OR REPLACE TRIGGER OperazioneAutoreIntegritaIns
AFTER INSERT ON Frase
FOR EACH ROW
EXECUTE FUNCTION OperazioneAutoreIntegritaIns();



CREATE OR REPLACE PROCEDURE UpadatePosition(posizione_in integer , pagina_in integer, caso BIT(1)) AS 
$UpadatePosition$
DECLARE
	f frase%ROWTYPE;

BEGIN
	IF caso = 1::BIT THEN
		FOR f IN SELECT pagina,posizione FROM frase where pagina_in = pagina and posizione >= posizione_in order by posizione DESC LOOP
			UPDATE frase set posizione = posizione + 200 where f.pagina = pagina and posizione = f.posizione;
		END LOOP;
	ELSE
		FOR f IN SELECT pagina,posizione FROM frase where pagina_in = pagina and posizione > posizione_in order by posizione DESC LOOP
			UPDATE frase set posizione = posizione + 200 where f.pagina = pagina and posizione = f.posizione;
		END LOOP;
	END IF;
END;
$UpadatePosition$ LANGUAGE PLPGSQL;



--12
CREATE OR REPLACE VIEW Notifica as
SELECT id_operazione,datar,testo,accettata,visionata,posizioneins,modifica,link,link_pagina,posizione_frase,pagina_frase,utente,autore_notificato
FROM operazione_utente
WHERE dataa IS NULL
ORDER BY datar, visionata ASC;

--13


CREATE OR REPLACE FUNCTION OperazioneAutoreIntegritaMod() RETURNS TRIGGER AS 
$OperazioneAutoreIntegritaMod$

BEGIN

	IF NOT EXISTS(SELECT id_operazione from operazione_utente where new.pagina = pagina_frase and posizione_frase = new.posizione) THEN
		INSERT INTO operazione_autore(data,testo,posizioneins,modifica,link,link_pagina,posizione_frase,pagina_frase,autore) VALUES(localtimestamp(0),new.testo,NULL,0::BIT(1),new.link,new.linkpagina,new.posizione,new.pagina,(select emailautore from pagina where new.pagina = id_pagina));
	END IF;
	RETURN NEW;
END;
$OperazioneAutoreIntegritaMod$ LANGUAGE PLPGSQL;

CREATE OR REPLACE TRIGGER OperazioneAutoreIntegritaMod
AFTER UPDATE OF testo ON Frase
FOR EACH ROW
EXECUTE FUNCTION OperazioneAutoreIntegritaMod();

-- 14

CREATE OR REPLACE VIEW Storicita_totale AS
SELECT pagina_frase AS id_pagina, dataa AS Data_Accettazione, datar AS Data_Richiesta, posizioneins AS posizione, testo, utente, accettata, modifica, link, link_pagina FROM
    (SELECT pagina_frase, null::TIMESTAMP AS dataa, datar, posizioneins, testo, utente, accettata, modifica, link, link_pagina FROM operazione_utente WHERE dataa IS NULL)
    UNION
    (SELECT pagina_frase, dataa, datar, posizioneins, testo, utente, accettata, modifica, link, link_pagina FROM operazione_utente WHERE dataa IS NOT NULL)    
    UNION
    (SELECT pagina_frase, data AS dataa, NULL AS datar, posizioneins, testo, autore AS utente, NULL AS accettata, NULL AS modifica, link, link_pagina FROM operazione_autore)    
ORDER BY id_pagina, Data_Accettazione ASC;

-- 15



CREATE OR REPLACE PROCEDURE OperazioneUtenteRichiesta(utente_in varchar(255),Testo_in Text,posizioneins_in integer, modifica_in BIT(1),pagina_in integer,posizione_frase_in integer, link_in BIT(1), link_pagina integer)
AS $OperazioneUtenteRichiesta$
BEGIN
	IF EXISTS(SELECT id_pagina FROM pagina where pagina_in = pagina.id_pagina) THEN
		IF link_in = 1::BIT(1) and EXISTS(SELECT id_pagina FROM pagina where link_pagina = pagina.id_pagina) THEN
			IF EXISTS(select email from utente where utente_in like email) THEN
				IF modifica_in = 0::BIT(1) THEN
					INSERT INTO operazione_utente(dataA,dataR,testo,accettata,visionata,posizioneins,modifica,link,link_pagina,posizione_frase,pagina_frase,utente,autore_notificato)
					VALUES(NULL,LOCALTIMESTAMP(0),Testo_in,0::BIT(1),0::BIT(1),posizioneins_in,modifica_in,link_in,link_pagina,NULL,pagina_in,utente_in,(Select emailautore from pagina where pagina_in = id_pagina));

				ELSEIF EXISTS(select posizione from frase where pagina = pagina_in and posizione_frase_in = frase.posizione) THEN
					INSERT INTO operazione_utente(dataA,dataR,testo,accettata,visionata,posizioneins,modifica,link,link_pagina,posizione_frase,pagina_frase,utente,autore_notificato)
					VALUES(NULL,LOCALTIMESTAMP(0),Testo_in,0::BIT(1),0::BIT(1),posizioneins_in,modifica_in,link_in,link_pagina,posizione_frase_in,pagina_in,utente_in,(Select emailautore from pagina where pagina_in = id_pagina));
				ELSE
					RAISE EXCEPTION 'E08: La frase non esiste';
					
				END IF;
			ELSE
				RAISE EXCEPTION 'E10: Utente non corretto';
			END IF;
		ELSEIF link_in = 0::BIT(1) THEN
			IF EXISTS(select email from utente where utente_in like email) THEN
				IF modifica_in = 0::BIT(1) THEN
					INSERT INTO operazione_utente(dataA,dataR,testo,accettata,visionata,posizioneins,modifica,link,link_pagina,posizione_frase,pagina_frase,utente,autore_notificato)
					VALUES(NULL,LOCALTIMESTAMP(0),Testo_in,0::BIT(1),0::BIT(1),posizioneins_in,modifica_in,link_in,NULL,NULL,pagina_in,utente_in,(Select emailautore from pagina where pagina_in = id_pagina));

				ELSEIF EXISTS(select posizione from frase where pagina = pagina_in and posizione = posizione_frase_in) THEN
					INSERT INTO operazione_utente(dataA,dataR,testo,accettata,visionata,posizioneins,modifica,link,link_pagina,posizione_frase,pagina_frase,utente,autore_notificato)
					VALUES(NULL,LOCALTIMESTAMP(0),Testo_in,0::BIT(1),0::BIT(1),posizioneins_in,modifica_in,link_in,NULL,posizione_frase_in,pagina_in,utente_in,(Select emailautore from pagina where pagina_in = id_pagina));
				ELSE
					RAISE EXCEPTION 'E08: La frase non esiste';

				END IF;
			END IF;
		ELSE
			RAISE EXCEPTION 'E04: La pagina non esiste(per il riferimento del link)';
		END IF;
	ELSE
		RAISE EXCEPTION 'E09: La pagina non esiste';
	END IF;
END;
$OperazioneUtenteRichiesta$ LANGUAGE PLPGSQL;



-- 16 

CREATE OR REPLACE FUNCTION GetUserModPage(emailIn IN VARCHAR(255), paginaIn IN INTEGER)
RETURNS TEXT
AS $GetUserModPage$
DECLARE
    ListaUtenti TEXT := '';
    Flag bit(1) := 0::bit(1);
    sql_instr TEXT := 'select utente from storicita_totale WHERE utente NOT LIKE ''%s'' AND (id_pagina = ';
    pagina_rec pagina%ROWTYPE;
    user_rec storicita_totale%ROWTYPE;    
BEGIN
IF paginaIn = 0 THEN
    FOR pagina_rec IN SELECT id_pagina FROM pagina WHERE emailautore = emailIn LOOP
         IF flag = 1::bit(1) THEN sql_instr := sql_instr || ' OR id_pagina = ' || pagina_rec.id_pagina;
         ELSE sql_instr := sql_instr || pagina_rec.id_pagina; Flag := 1::bit(1); END IF;
    END LOOP;
    sql_instr := sql_instr || ')';
ELSE
    CALL CheckAutore(emailIn, paginaIn);
    sql_instr := sql_instr || paginaIn;
END IF;

FOR user_rec IN EXECUTE format(sql_instr, emailIn)  LOOP
    ListaUtenti := ListaUtenti || user_rec.utente || ';';
    RAISE NOTICE 'utente: %', user_rec.utente;
END LOOP;

RAISE NOTICE 'This is a notice: %',sql_instr;
RETURN ListaUtenti;

END;
$GetUserModPage$ LANGUAGE plpgsql;

-- 17

CREATE OR REPLACE FUNCTION GetUserModPage(emailIn IN VARCHAR(255), paginaIn IN INTEGER)
RETURNS TEXT
AS $GetUserModPage$
DECLARE
    ListaUtenti TEXT := '';
	Flag bit(1) := 0::bit(1);
    sql_instr TEXT := 'select distinct utente from storicita_totale WHERE utente NOT LIKE ''' ||emailIn || ''' AND (id_pagina = ';
	pagina_rec pagina%ROWTYPE;
	user_rec storicita_totale.utente%TYPE;	
BEGIN
IF paginaIn = 0 THEN
    FOR pagina_rec IN SELECT id_pagina FROM pagina WHERE emailautore = emailIn LOOP
         IF flag = 1::bit(1) THEN sql_instr := sql_instr || ' OR id_pagina = ' || pagina_rec.id_pagina;
		 ELSE sql_instr := sql_instr || pagina_rec.id_pagina; Flag := 1::bit(1); END IF;
    END LOOP;
ELSE
	CALL CheckAutore(emailIn, paginaIn);
    sql_instr := sql_instr || paginaIn;
END IF;

sql_instr := sql_instr || ')';

FOR user_rec IN EXECUTE sql_instr LOOP
    ListaUtenti := ListaUtenti || user_rec || ';';
END LOOP;

RETURN ListaUtenti;

END;
$GetUserModPage$ LANGUAGE plpgsql;


--18


CREATE OR REPLACE PROCEDURE AccettaProposta (EmailIn IN d_Email, IdOperazione IN INTEGER, AccettataIn IN  bit(1))
AS $AccettaProposta$
DECLARE
    
    notifica_rec notifica%ROWTYPE;

BEGIN
    select * into notifica_rec from notifica where autore_notificato = EmailIn AND id_operazione = IdOperazione;
    IF FOUND THEN
        UPDATE operazione_utente SET accettata = AccettataIn, dataa = LOCALTIMESTAMP(0), visionata = 1::BIT(1) WHERE id_operazione = IdOperazione AND autore_notificato = EmailIn;
    ELSE
        RAISE EXCEPTION 'E01: Non esiste una proposta per % con id_operazione %', EmailIn, IdOperazione; 
    END IF;

END;
$AccettaProposta$ LANGUAGE plpgsql;

-- 19

CREATE OR REPLACE FUNCTION GetLimtUserModPage(Ids_page VARCHAR(255), emailIn VARCHAR(255)) RETURNS VARCHAR AS $GetLimtUserModPage$
DECLARE
	pos INTEGER := 0;
	oldpos INTEGER := 1;
	parola varchar(10000) := '';
	commando varchar(10000) := 'SELECT distinct(utente) FROM operazione_utente where utente NOT LIKE ''' || emailIn || ''' AND (pagina_frase = ';
	u operazione_utente.utente%TYPE;

BEGIN
	
	LOOP
		pos := POSITION('+' IN SUBSTRING(Ids_page FROM oldpos));
		
		RAISE NOTICE '% | %', pos, oldpos;
		EXIT WHEN pos = 0;
		parola := SUBSTRING(Ids_page FROM oldpos FOR pos-1);
		oldpos := oldpos + pos;
		call CheckAutore(emailin,parola::INTEGER);
		commando := commando || parola || ' OR pagina_frase = ';
		
	END LOOP;
	
	parola := SUBSTRING(Ids_page FROM oldpos);
	commando := commando || parola || ');';
	--RAISE NOTICE '%', parola;
	--RAISE NOTICE '%', commando;
	parola := '';
	FOR u in EXECUTE commando LOOP
		parola := parola || '+' || u;
	END LOOP;
	
	
	RETURN parola;
	
END;
$GetLimtUserModPage$ LANGUAGE PLPGSQL;


-- 20
CREATE OR REPLACE FUNCTION GetNotifiche(Ids_page VARCHAR(255), emailIn VARCHAR(255)) RETURNS VARCHAR AS $GetNotifiche$
DECLARE
	pos INTEGER := 0;
	oldpos INTEGER := 1;
	parola varchar(10000) := '';
	commando varchar(10000) := 'SELECT * FROM notifica where autore_notificato LIKE ''' || emailIn || ''' AND (pagina_frase = ';
	u notifica%ROWTYPE;

BEGIN
	
	LOOP
		pos := POSITION('+' IN SUBSTRING(Ids_page FROM oldpos));
		
		RAISE NOTICE '% | %', pos, oldpos;
		EXIT WHEN pos = 0;
		parola := SUBSTRING(Ids_page FROM oldpos FOR pos-1);
		oldpos := oldpos + pos;
		call CheckAutore(emailin,parola::INTEGER);
		commando := commando || parola || ' OR pagina_frase = ';
		
	END LOOP;
	
	parola := SUBSTRING(Ids_page FROM oldpos);
	commando := commando || parola || ');';
	--RAISE NOTICE '%', parola;
	--RAISE NOTICE '%', commando;
	parola := '';
	FOR u in EXECUTE commando LOOP
		--RAISE NOTICE '%', parola;
		IF(u.link_pagina is not null) THEN
			parola := parola || '+' || u.id_operazione || ';' || u.datar || ';' || u.posizioneins || ';' || u.modifica || ';' || u.link|| ';' || u.link_pagina || ';' || u.utente;
		ELSE
			parola := parola || '+' || u.id_operazione || ';' || u.datar || ';' || u.posizioneins || ';' || u.modifica || ';' || u.link|| ';' || 'NULL' || ';' || u.utente;
		END IF;
	END LOOP;
	
	
	RETURN parola;
	
END;
$GetNotifiche$ LANGUAGE PLPGSQL;

-- 21

CREATE OR REPLACE FUNCTION visualizzaPropostaAndConfronta (idOp IN INTEGER, emailAutore IN VARCHAR(255) )
RETURNS TEXT
AS $visualizzaProposta$
DECLARE

	confronto TEXT := '';
	notifica_rec notifica%ROWTYPE;
	frase_rec frase%ROWTYPE;
	
	NotificaLinkPagina INTEGER := 0;
	FraseLinkPagina INTEGER := 0;
	
	FraseTitolo VARCHAR(255);
	NotificaRefTitolo VARCHAR(255) := 'null' ;
	FraseRefTitolo VARCHAR(255) := 'null' ;
	
BEGIN
	
	select * into notifica_rec from notifica where id_operazione = idOp; -- se esiste quella notifica
	raise notice 'notifica rec %', notifica_rec;
	if found then
	
		if emailAutore != (Select autore_notificato from notifica where id_operazione  = idop) then
			raise exception 'l''autore inserito non e autorizzato a visualizzare questa notifica';
		end if;
		
		CALL checkautore(emailAutore, notifica_rec.pagina_frase);
			
		update operazione_utente set visionata = 1::bit(1) where id_operazione = idOp; --aggiorno il visualizzato
		
		if notifica_rec.link_pagina is not null then NotificaLinkPagina := notifica_rec.link_pagina; select titolo into NotificaRefTitolo from pagina where id_pagina = notifica_rec.link_pagina; end if;
		
		select titolo into FraseTitolo from pagina where id_pagina = notifica_rec.pagina_frase;
		
		if notifica_rec.modifica = 1::bit(1) then
			select * into frase_rec from frase where pagina = notifica_rec.pagina_frase AND posizione = notifica_rec.posizione_frase;
			raise notice '%', frase_rec;
			
			if notifica_rec.link_pagina is not null then FraseLinkPagina := frase_rec.linkpagina; select titolo into FraseRefTitolo from pagina where id_pagina = notifica_rec.link_pagina;end if;
            raise notice 'FraseRefTitolo: %', FraseRefTitolo;
			confronto := FraseTitolo ||'+'||frase_rec.testo ||'-'|| frase_rec.posizione ||'-' || frase_rec.link || '-'|| FraseLinkPagina || '-' || FraseRefTitolo ||'|'|| notifica_rec.testo || '-' || notifica_rec.posizioneins || '-' || notifica_rec.link || '-' || NotificaLinkPagina || '-' || NotificaRefTitolo;
		
		else
			confronto := FraseTitolo ||'+'||notifica_rec.testo || '-' || notifica_rec.posizioneins || '-' || notifica_rec.link || '-' || NotificaLinkPagina || '-' || NotificaRefTitolo;
		end if;

	else
		raise exception 'non esisete una notifica con l''identificativo proposto';
	end if;
	raise notice 'confronto hh: %', confronto;

	RETURN confronto;
END;
$visualizzaProposta$
LANGUAGE plpgsql;
/*
DECLARE

	confronto TEXT := '';
	notifica_rec notifica%ROWTYPE;
	frase_rec frase%ROWTYPE;
	
	NotificaLinkPagina INTEGER := 0;
	FraseLinkPagina INTEGER := 0;
	
	FraseTitolo VARCHAR(255);
	NotificaRefTitolo VARCHAR(255) := 'null' ;
	FraseRefTitolo VARCHAR(255) := 'null' ;
	
BEGIN
	
	select * into notifica_rec from notifica where id_operazione = idOp; -- se esiste quella notifica
	raise notice 'notifica rec %', notifica_rec;
	if found then
	
		if emailAutore != (Select autore_notificato from notifica where id_operazione  = idop) then
			raise exception 'l''autore inserito non e autorizzato a visualizzare questa notifica';
		end if;
		
		CALL checkautore(emailAutore, notifica_rec.pagina_frase);
			
		update operazione_utente set visionata = 1::bit(1) where id_operazione = idOp; --aggiorno il visualizzato
		
		if notifica_rec.link_pagina is not null then NotificaLinkPagina := notifica_rec.link_pagina; select titolo into NotificaRefTitolo from pagina where id_pagina = notifica_rec.link_pagina; end if;
		
		select titolo into FraseTitolo from pagina where id_pagina = notifica_rec.pagina_frase;
		
		if notifica_rec.modifica = 1::bit(1) then
			select * into frase_rec from frase where pagina = notifica_rec.pagina_frase AND posizione = notifica_rec.posizione_frase;
			raise notice '%', frase_rec;
			
			if frase_rec.linkpagina is not null then FraseLinkPagina := frase_rec.linkpagina; select titolo into FraseRefTitolo from pagina where id_pagina = notifica_rec.link_pagina;end if;
			confronto := FraseTitolo ||'+'||frase_rec.testo ||','|| frase_rec.posizione ||',' || frase_rec.link || ','|| FraseLinkPagina || ',' || FraseRefTitolo ||';'|| notifica_rec.testo || ',' || notifica_rec.posizioneins || ',' || notifica_rec.link || ',' || NotificaLinkPagina || ',' || NotificaRefTitolo;
		
		else
			confronto := FraseTitolo ||'+'||notifica_rec.testo || ',' || notifica_rec.posizioneins || ',' || notifica_rec.link || ',' || NotificaLinkPagina || ',' || NotificaRefTitolo;
		end if;

	else
		raise exception 'non esisete una notifica con l''identificativo proposto';
	end if;
	raise notice 'confronto: %', confronto;

	RETURN confronto;
END;
$visualizzaProposta$
LANGUAGE plpgsql;
*/


-- 23


CREATE OR REPLACE FUNCTION trovaPagina (testoRicerca IN TEXT) 
RETURNS TEXT AS
$trovaPagine$
DECLARE

    stringaRicerca TEXT  := '%';--||testoRicerca||'%';
    
    pos INTEGER;
    oldPos INTEGER:= 1;
    parola TEXT;
    page pagina%ROWTYPE;
    
    idTrovati TEXT:= '';

BEGIN
    
    LOOP
    
        pos := POSITION(' ' IN SUBSTRING(testoRicerca FROM oldPos));
        EXIT WHEN pos = 0;
        parola := SUBSTRING (testoRicerca FROM oldPos FOR pos-1);
        stringaRicerca := stringaRicerca || parola ||'%';
        oldPos := oldPos + pos;
        
    END LOOP;
    
    parola := SUBSTRING(testoRicerca FROM oldPos);
    stringaRicerca := stringaRicerca || parola ||'%';
    
    FOR page IN SELECT id_pagina FROM pagina WHERE titolo LIKE stringaRicerca LOOP
        idTrovati:=idTrovati||page.id_pagina||'-';
    END LOOP;
    idTrovati:=RTRIM(idTrovati, '-');
    
    IF page IS NULL THEN
        idTrovati := '-1';
    END IF;
    
    RETURN idTrovati;

END;
$trovaPagine$
LANGUAGE plpgsql;

-- 24 

CREATE OR REPLACE FUNCTION CheckAggiornamentoPagina() 
RETURNS TRIGGER 
AS $CheckAggiornamentoPagina$
BEGIN
    IF NEW.accettata = 1::bit(1) THEN
        IF NEW.modifica = 1::bit(1) THEN
            UPDATE frase SET testo = NEW.testo, link = NEW.link, linkpagina = NEW.link_pagina WHERE pagina = NEW.pagina_frase AND posizione = NEW.posizione_frase;
        ELSE
            INSERT INTO frase VALUES (NEW.pagina_frase, NEW.posizioneins, NEW.testo, NEW.link, NEW.link_pagina);
        END IF;
    END IF;
    
    RETURN NEW;
END;
$CheckAggiornamentoPagina$ 
LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER CheckAggiornamentoPagina
AFTER UPDATE OF Dataa ON operazione_utente
FOR EACH ROW
EXECUTE FUNCTION CheckAggiornamentoPagina();

-- 25
CREATE OR REPLACE PROCEDURE ModificaRichestaProposta(Id_operazione_in INTEGER, Email_in VARCHAR(255), Testo_in TEXT) AS $ModificaRichestaProposta$
BEGIN
	
	IF EXISTS(SELECT utente from operazione_utente where Email_in = utente and Id_operazione_in = id_operazione) THEN
		UPDATE operazione_utente SET testo = Testo_in where id_operazione = id_operazione_in;
	
	ELSE
		RAISE EXCEPTION 'utente non ha questa proposta';
	
	END IF;


END;
$ModificaRichestaProposta$ LANGUAGE PLPGSQL;

-- 26

CREATE OR REPLACE FUNCTION DeleteFraseOpUtente() RETURNS TRIGGER
AS $DeleteFraseOpUtente$
BEGIN

    DELETE FROM operazione_utente WHERE pagina_frase=OLD.pagina AND posizione_frase is NULL;
 
    RETURN NEW;
END;
$DeleteFraseOpUtente$ LANGUAGE PLPGSQL;

CREATE OR REPLACE TRIGGER DeleteFraseOpUtente
AFTER DELETE ON Pagina
FOR EACH ROW
EXECUTE FUNCTION DeleteFraseOpUtente();