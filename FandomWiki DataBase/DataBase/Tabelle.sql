CREATE TABLE Utente(
	Email d_Email,
	Nome VARCHAR(20) NOT NULL,
    Cognome VARCHAR(20) NOT NULL,
	Password_Utente VARCHAR(50) NOT NULL,
	Genere CHAR(1) NOT NULL,
	Autore BIT(1) NOT NULL,
	CONSTRAINT PK_Utente PRIMARY KEY(Email),
	CONSTRAINT CheckGenere CHECK(Genere LIKE 'F' OR Genere LIKE 'M')
);

CREATE TABLE Pagina(
    ID_Pagina SERIAL,
    Titolo VARCHAR(255) NOT NULL,
    Generalita_Autore VARCHAR(41) NOT NULL,
    DataUltimaModifica timestamp NOT NULL,
    DataCreazione timestamp NOT NULL,
    emailAutore d_email NOT NULL,
    CONSTRAINT PK_PAGINA PRIMARY KEY(ID_Pagina),
    CONSTRAINT UNIQUE_TITOLO UNIQUE(Titolo),
    CONSTRAINT FK_PAGINA_AUTORE FOREIGN KEY(emailAutore) REFERENCES Utente(email) ON UPDATE CASCADE
);

CREATE TABLE Frase(
    Pagina INTEGER,
    Posizione INTEGER,
    Testo TEXT NOT NULL,
    Link BIT(1) NOT NULL,
    LinkPagina INTEGER,
    CONSTRAINT PK_FRASE PRIMARY KEY(Pagina,Posizione),
    CONSTRAINT FK_FRASE_PAGINA_REF FOREIGN KEY(LinkPagina) REFERENCES Pagina(ID_Pagina) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT FK_FRASE_PAGINA_LINK FOREIGN KEY(Pagina) REFERENCES Pagina(ID_Pagina)  ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Operazione_Utente(
    ID_Operazione SERIAL,
    DataA timestamp,
    DataR timestamp NOT NULL,
    Testo TEXT  NOT NULL,
    Accettata BIT(1) NOT NULL,
    Visionata BIT(1) NOT NULL,
    PosizioneIns INTEGER,
    modifica BIT(1) NOT NULL,
    Link BIT(1) NOT NULL,
    link_pagina INTEGER,
    Posizione_Frase INTEGER,
    Pagina_Frase INTEGER NOT NULL,
    Utente d_Email NOT NULL,
    Autore_Notificato d_Email NOT NULL,
    CONSTRAINT CheckLink CHECK((link = 1::BIT(1) AND link_pagina IS NOT NULL) OR (link_pagina IS NULL AND link = 0::BIT(1))),
    CONSTRAINT PK_Operazione_U PRIMARY KEY(ID_Operazione),
    CONSTRAINT FK_Operazione_U_Frase_Posizione_Pagina FOREIGN KEY(Posizione_Frase,Pagina_Frase) REFERENCES frase(Posizione,pagina) ON DELETE CASCADE ON UPDATE CASCADE ,
    CONSTRAINT FK_Operazione_U_Utente  FOREIGN KEY(Utente) REFERENCES Utente(Email) ON UPDATE CASCADE,
    CONSTRAINT FK_Operazione_U_AuotrePagina  FOREIGN KEY(Autore_Notificato) REFERENCES Utente(Email) ON UPDATE CASCADE
);

CREATE TABLE Operazione_Autore(
    ID_Operazione SERIAL,
    Data timestamp NOT NULL,
    Testo TEXT  NOT NULL,
    PosizioneIns INTEGER ,
    modifica BIT(1) NOT NULL,
    Link BIT(1) NOT NULL,
    link_pagina INTEGER,
    Posizione_Frase INTEGER NOT NULL,
    Pagina_Frase INTEGER NOT NULL,
    Autore d_email NOT NULL,
    CONSTRAINT CheckLink CHECK((link = 1::BIT(1) AND link_pagina IS NOT NULL) OR (link_pagina IS NULL AND link = 0::BIT(1))),
    CONSTRAINT PK_Operazione_A PRIMARY KEY(ID_Operazione),
    CONSTRAINT FK_Operazione_A_Frase_Posizione_Pagina FOREIGN KEY(Posizione_Frase,Pagina_Frase) REFERENCES frase(Posizione,pagina) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_Operazione_A_Autore FOREIGN KEY(Autore) REFERENCES Utente(Email) ON UPDATE CASCADE);


