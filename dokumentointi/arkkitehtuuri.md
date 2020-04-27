# Arkkitehtuurikuvaus

## Rakenne
Ohjelma koostuu kolmesta pakkauksesta noudattaen tehtävänannon esimerkkisovelluksen tapaan kolmitasoista kerrosarkkitehtuuria (kts. kuva alla). Pakkaukset ovat i) woj.ui, ii) woj.domain ja iii) woj.dao. Näistä ensimmäisen, woj.ui, sisältää käyttöliittymän toiminnallisuuden (toteutuksessa käytetty JavaFX-kirjastoa). Koodi woj.domain-pakkauksen alla sen sijaan vastaa ohjelman logiikasta. woj.dao-pakkauksen koodi vastaa tiedon pysyväistallennuksesta ja saatavuudesta.

![pakkausdiagrammi](./arkkitehtuuri/wojPackageDiagram.png)

## Käyttöliittymä

Käyttöliittymä koostuu viidestä näkymästä
- kirjautumisnäkymä
- näkymä uuden käyttäjän luomiseen
- päänäkymä käyttäjän kirjauduttua, josta näkee käyttäjän kohteet ja kohdekohtaiset tiedot
- näkymä uuden havaintokohteen luomiseen
- näkymä uuden kohdekohtaisen havainnon luomiseen

joiden lisäksi käyttöliittymä tarjoaa popup-ikkunoita käyttäjän tiedottamiseen. Nämä erilliset ikkunat on toteutettu luokassa woj.ui.PopUpBox ja itse käyttöliittymä luokassa woj.ui.GUI.

## Sovelluslogiikka

Sovelluksen logiikka rakentuu kolmen luokan ympärille: User, Site ja Observation, jotka yhdessä muodostavat ohjelman datamallin. Luokat kuvaavat käyttäjiä (User), käyttäjien havaintokohteita (Site) sekä käyttäjien havaintokohteissa tekemiin havaintoihin (Observation). 

![datamalli](./arkkitehtuuri/wojDataModelDiagram.png)

Sovelluksen JournalService-luokka vastaa toimintalogiikasta tarjoten ohjelman ylemmäntason toiminnallisuuksille (eli käyttöliittymälle) seuraavat metodit:

- login(String username)
- getLoggedUser()
- logout()
- createUser(String username, String name)
- createSite(Site site)
- getSitesOfLoggedUser()
- createObservation(Observation observation)
- getObservationsOfLoggedUserAndChosenSite(Site site)

JournalService-olioon liitetään woj.dao-pakkauksen luokkien UserDao-, SiteDao- ja ObservationDao-rajapinnat toteuttavat dao-luokat, joiden avulla JournalService pääsee pysyväistallennettuun tietoon käsiksi. Tallennettava tieto koskee datamallin mukaisesti käyttäjiä, kohteita ja havaintoja. Muiden ohjelman luokkien ja JournalServicen suhde on esitetty kaaviossa alla.

![luokkadiagrammi](./arkkitehtuuri/wojClassDiagram.png)

## Tietojen pysyväistallennus

Tietojen pysyväistallennuksesta vastaa pakkaus woj.dao ja tämän luokat SQLiteUserDao, SQLiteSiteDao sekä SQLiteObservationDao. Tieto tallennetaan toteutuksessa luokkien nimen mukaisesti SQLite-tietokantaan. Kukin luokka toteuttaa nimeä vastaavan rajapinnan (SQLiteUserDao rajapinnan UserDao, SQLiteSiteDao rajapinnan SiteDao ja SQLiteObservationDao rajapinnan ObservationDao) Data Access Object-suunnittelumallin mukaisesti. Muulla sovelluslogiikalla ei ole pääsyä suoraan rajapintoja toteuttaviin luokkiin, vaan vuorovaikutus tapahtuu rajapintojen kautta. Näin tallennuksesta vastaavat luokkia voidaan tarvittaessa muuttaa rikkomatta yhteensopivuutta muun sovelluslogiikan kanssa.

Suunnittelumallia noudattelemalla on mahdollistettu myös sovelluslogiikan testaaminen eristettynä varsinaisista tallennustavoista.

### Tietokanta

Sovellus tallentaa käyttäjien, kohteiden ja havaintojen tiedot samaan SQLite-tietokantaan kunkin omille tauluilleen.

Tietokannan skeema on seuraava:

```
CREATE TABLE Users(id INTEGER PRIMARY KEY, username TEXT UNIQUE, name TEXT);
CREATE TABLE Sites(id INTEGER PRIMARY KEY, sitename TEXT UNIQUE, address TEXT, description TEXT, user_id INTEGER REFERENCES Users);
CREATE TABLE Observations(timestamp DATETIME, temperature REAL, rh REAL, rainfall REAL, pressure REAL, description TEXT, comment TEXT, site_id INTEGER REFERENCES Sites,user_id INTEGER REFERENCES Users);
```

Tietokantakaavio on esitetty alla:

![tietokantakaavio](./arkkitehtuuri/wojDbDiagram.png)

## Päätoiminnallisuudet

### Uuden käyttäjän luominen

### Käyttäjän sisäänkirjaantuminen

### Käyttäjän kohteen valinta ja havaintotietojen esittäminen

### Uuden havaintokohteen luominen
![sekvenssikaavio havaintokohteen luomisesta](./arkkitehtuuri/createSiteSequenceDiagram.png)

### Uuden havainnon luominen

### Käyttäjän uloskirjautuminen

### Ohjelman sulkeminen
