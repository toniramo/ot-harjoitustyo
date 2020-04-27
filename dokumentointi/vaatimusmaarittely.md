# Vaatimusmäärittely

## Sovelluksen tarkoitus
Sovelluksen avulla käyttäjät voivat pitää henkilökohtaista säähavaintopäiväkirjaa lisäämällä havaintokohteilleen päiväkohtaisia merkintöjä ja selata jo tehtyjä muistiinpanoja. Sovellus mahdollistaa merkintöjen tarkastelun visuaalisessa muodossa.

## Käyttäjät
Sovelluksen perustoiminnallisuuden kannalta olennaista on, että siitä löytyy peruskäyttäjärooli, joka mahdollistaa sovelluksen päätoiminnallisuuksien käytön. Jatkokehitystavoitteena on lisätä myös admin-tason käyttäjärooli, joka voi hallinnoida sovelluksen käyttäjiä ja heidän tietojaan sekä tarkastella jokaisen käyttäjän lisäämiä merkintöjä. Peruskäyttäjä voi tarkastella ja hallinnoida vain omia tietojaan - ellei käyttäjähallintaa tehdä edistyneemmäksi jatkossa.

## Suunnitellut toiminnallisuudet
:heavy_check_mark:-merkki tarkoittaa, että toiminto on toteutettu. Mahdolliset viitteet lisähuomioille toteutukseen liittyen lisätty :heavy_check_mark:-merkin perään.
### Ennen kirjautumista
- :heavy_check_mark: käyttäjä voi luoda käyttäjätunnuksen sovellusta varten
  - :heavy_check_mark: Tunnuksen on oltava uniikki ja sisältää vähintään yhden merkin
- :heavy_check_mark: käyttäjä voi kirjautua järjestelmään jo olemassa olevalla tunnuksella
  - :heavy_check_mark: salasanan käyttöä ei vaadita perustoiminnallisuuden näkökulmasta

### Kirjauduttua
- :heavy_check_mark: käyttäjä näkee omat säähavaintokohteensa
- :heavy_check_mark: käyttäjä voi luoda uuden havaintokohteen
  - :heavy_check_mark: kohteen nimen tulee olla uniikki ja vähintään yhden merkkin pituinen
- :heavy_check_mark: käyttäjä voi valita olemassa olevan havaintokohteensa ja tarkastella tämän tietoja
  - :heavy_check_mark: käyttöliittymä näyttää kohteelle merkityn numeerisen tiedon graafilla
  - :heavy_check_mark: kohteen tietoja voi tarkastella myös päivätasolla, jolloin käyttäjä näkee merkinnän tarkemmat tiedot
  - :heavy_check_mark: <sup>0</sup> kohteiden päiväkohtaiset tiedot sisältävät päivän keskiarvolämpötilan (ja haluttaessa min/max lämpötilat), ilmankosteuden, sademäärän, sään kuvauksen (kuten selkeää, vaihtelevaa, pilvistä, puolipilvistä, sateista, ukkosta) ja vapaamuotoisen kommentin.
- :heavy_check_mark: käyttäjä voi valita havaintokohteen ja lisätä uuden säähavainnon kohteelle
  - :heavy_check_mark: syötettävien tietojen tulee vastata edellä mainittua listausta kohteiden päiväkohtaisista tiedoista
  - :heavy_check_mark:<sup>1</sup> tiedot lisätään päivätasolla, päivämäärän on oltava järkevä eikä samalle päivälle voi perustoiminnallisuuden puitteissa lisätä kuin yhden merkinnän per kohde (lisäämällä merkinnän jo olemassa olevalle päivälle vanha yliajetaan).
  - :heavy_check_mark: käyttöliittymän tulee validoida käyttäjän syöte ja tarkistaa, että syötetyt arvot ovat järkeviä (esimerkiksi lämpötila on [-60,+60]°C, suhteellinen kosteus 0...100 %RH, sademäärä >= 0 mm)
  - :heavy_check_mark: sään kuvaus valitaan esityötettyjen vaihtoehtojen joukosta (esimerkkejä mainittu edellä)
  - :heavy_check_mark:<sup>2</sup> kommentti voi olla tyhjä tai rajoitettu esimerkiksi 100 merkkiin  
- :heavy_check_mark: käyttäjä voi kirjautua ulos

<sup>0) Tämä on toteutettu vaihtoehtoisella taulukkonäkymällä. Tarjolla olevat säänkuvaukset voivat hieman poiketa esimerkkinä annetuista. Min ja max - lämpötiloja ei ole tällä hetkellä saatavilla.</sup>
<sup>1) Sovellus ei tällä hetkellä validoi löytyykö tietylle tietyllä ajanhetkellä jo mittausta, vaan sovellus näyttää vain toisen mahdollisista duplikaatti-merkinnöistä; </sup>
<sup>2) 100 merkin rajoitusta ei toistaiseksi ole</sup>

## Käyttöliittymäluonnos
Sovelluksen avatuessa käyttöliittymä aukeaa kirjautumissivulle, jossa käyttäjä voi joko kirjautua olemassa olevalla tunnuksella sisään tai luoda uuden tunnuksen. Kirjautumisen jälkeen avautuu niin sanottu päänäkymä, jossa listataan kirjautuneen käyttäjän kohteet ja näytetään valitun kohteen yksityiskohtaisemmat havaintotiedot. Näkymästä on mahdollisuus myös siirtyä luomaan uusi kohde tai uusi havaintomerkintä valitulle kohteelle. Käyttöliittymään olisi tarkoitus toteuttaa pudotusvalikko, jonka takaa löytyy toiminnot esimerkiksi käyttäjätietojen hallintaan ja uloskirjautumiseen. Käyttöliittymään on mahdollisuus toteuttaa palauteviestejä syötteiden yhteyteen, jotta käyttäjä tietää onko syöte ollut validi vai ei (ja miksi, jos ei).
![UI-luonnos](./kayttoliittyma/UI-luonnos.jpg)

Kommentti: Huomaa, että käyttöliittymäluonnoksessa nimi on vielä ollut "Weather Diary", joka on sittemmin vaihtunut nimeksi "Weather Observation Journal".

## Jatkokehitysideoita
- edistyneempi tiedon hallinta
  - säähavaintojen import/export-toiminto, jonka avulla käyttäjä voi viedä/tuoda järjestelmään/järjestelmästä havaintoja tietyssä formaatissa esimerkiksi csv-tiedostona
  - graafien vienti-toiminto
  - generoitavat kohdetason raportit (esimerkiksi eri kausien vertailu, eri kohteiden vertailu, minimi- ja maksimilämpötilat, keskiarvot jne.)
  - olemassaolevan tiedon muokkaaminen
- edistyneempi käyttäjähallinta ja tiedon jakaminen käyttäjien kesken
  - erilaiset käyttäjäroolit - erityisesti admin-tason käyttäjä-rooli, jonka avulla voi tarkastella, muokata tai jopa poistaa kaikkien käyttäjien tietoja ja oikeuksia 
  - mahdollisuus jakaa kohdeoikeuksia käyttäjien/ryhmien kesken
  - eritasoiset käyttäjäoikeudet saattavat edellyttää myös salasanan käyttöönottoa
- mahdollisuus käyttöliittymän kustomoiduille näkymille
  - esimerkiksi kielen vaihtaminen, tiedon sijoittelu ja näyttäminen/piilottaminen
- kohteiden, käyttäjien ja havaintojen lisätiedot
  - mahdollisuus lisätä muuta materiaalia kuten valokuvia havaintojen tueksi
  - mahdollisuus lisätä merkintöjä tarkemmin kuin päivätasolla
  - mahdollisuus lisätä havaintokohteille lisätietoja kuten osoitetietoja, koordinaatteja ja muita huomioita
  - mahdollisuus lisätä käyttäjäkohtaista tietoa pelkän käyttäjänimen lisäksi
- edistyneempi tiedon visuaalinen esitystapa
  - mahdollisuus muuttaa graafin skaalaa ja zoomata
  - mahdollisuus muuttaa näytettäviä sarjoja
  - kohteiden näyttäminen kartalla (edellyttää koordinaattitietojen lisäämistä)
- kohdelistauksessa merkintöjen määrän ja mahdollisen muun kohdetiedon maininta
- mahdollisuus synkronoida tietoja todellisen sääaseman avointen tietojen kanssa ja lisätä kohteille ennustetieto
- interaktiiviset käyttöohjeet
