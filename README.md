# Weather Observation Journal

Sovelluksen avulla käyttäjä voi pitää henkilökohtaista säähavaintopäiväkirjaa omilla säähavaintokohteillaan. Sovellusta voi käyttää useampi järjestelmään kirjattu käyttäjä, joilla kullakin on omat havaintokohteensa ja havaintonsa.

Sovellus toteutetaan osana Helsingin yliopiston Ohjelmistotekniikka (kevät 2020)-kurssia harjoitustyönä.

## Dokumentaatio

[Vaatimusmäärittely](https://github.com/toniramo/ot-harjoitustyo/tree/master/dokumentointi/vaatimusmaarittely.md)

[Arkkitehtuuri](https://github.com/toniramo/ot-harjoitustyo/tree/master/dokumentointi/arkkitehtuuri.md)

[Työaikakirjanpito](https://github.com/toniramo/ot-harjoitustyo/tree/master/dokumentointi/tyoaikakirjanpito.md)

[Käyttöohje](https://github.com/toniramo/ot-harjoitustyo/tree/master/dokumentointi/kayttoohje.md)

[Testausdokumentti](https://github.com/toniramo/ot-harjoitustyo/tree/master/dokumentointi/testausdokumentti.md)

## Releaset

[Viikko 5](https://github.com/toniramo/ot-harjoitustyo/releases/tag/viikko5)

[Viikko 6](https://github.com/toniramo/ot-harjoitustyo/releases/tag/viikko6)

[loppupalautus](https://github.com/toniramo/ot-harjoitustyo/releases/tag/loppupalautus)

## Komentorivitoiminnot

### Testaus
Testit suoritetaan komennolla

```
mvn test
```

Testikattavuusraportin voi luoda komennolla

```
mvn jacoco:report
```

Kattavuusraportin sisältöä pääsee tarkastelemaan selaimella avaamalla tiedoston `target/site/jacoco/index.html`

### Kääntäminen ja suoritus
Ohjelman koodi käännetään ja suoritetaan komennolla

```
mvn compile exec:java -Dexec.mainClass=woj.Main
```

### Suoritettavan jarin generointi
Komennolla

```
mvn package
```

generoituu hakemistoon `target` suoritettava jar-tiedosto *WeatherObservationJournal-1.0-SNAPSHOT.jar*

### JavaDoc

JavaDoc on mahdollista generoida komennolla

```
mvn javadoc:javadoc
```

Generoitua JavaDocia on mahdollista tarkastella selaimella sijaintiin *target/site/apidocs/* luoduilla tiedostoilla (index.html).

### Checkstyle
Tiedoston [checkstyle.xml](./WeatherObservationJournal/.checkstyle.xml) määrittelemät tarkastukset suoritetaan komennolla

```
mvn jxr:jxr checkstyle:checkstyle
```

Tarkistuksen tuloksena havaittuja mahdollisia virheilmoituksia voi tarkastella selaimella avamaalla tiedoston `target/site/checkstyle.html`



