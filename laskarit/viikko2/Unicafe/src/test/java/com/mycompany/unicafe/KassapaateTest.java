/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author toniramo
 */
public class KassapaateTest {

    Kassapaate kassa;
    Maksukortti kortti;
    int rahaaAlussa = 100000;
    int edullisenHinta = 240;
    int maukkaanHinta = 400;
    int rahaaKortillaAlussa = 2000;

    @Before
    public void setUp() {
        kassa = new Kassapaate();
        kortti = new Maksukortti(rahaaKortillaAlussa);
    }

    @Test
    public void alussaKassassaRahaa1000e() {
        //raha tallennettu senteissä; 1 000e = 100 000 senttiä
        tarkistaRahamaara(rahaaAlussa);
    }

    @Test
    public void alussaMyytyjaEdullisiaLounaita0() {
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void alussaMyytyjaMaukkaitaLounaita0() {
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void tasarahallaMaksetunEdullisenLounaanJalkeenRahamaaraKasvanut240senttia() {
        kassa.syoEdullisesti(edullisenHinta);
        tarkistaRahamaara(rahaaAlussa + edullisenHinta);
    }

    @Test
    public void KympillaMaksetunEdullisenLounaanJalkeenRahamaaraKasvanut240senttia() {
        kassa.syoEdullisesti(1000);
        tarkistaRahamaara(rahaaAlussa + edullisenHinta);
    }

    @Test
    public void tasarahallaMaksetunMaukkaanLounaanJalkeenRahamaaraKasvanut400senttia() {
        kassa.syoMaukkaasti(maukkaanHinta);
        tarkistaRahamaara(rahaaAlussa + maukkaanHinta);
    }

    @Test
    public void KahdellakympillaMaksetunMaukkaanLounaanJalkeenRahamaaraKasvanut400senttia() {
        kassa.syoMaukkaasti(2000);
        tarkistaRahamaara(rahaaAlussa + maukkaanHinta);
    }

    @Test
    public void vaihtoraha5eMaksullaEdullisestaLounaastaOn260senttia() {
        assertEquals(260, kassa.syoEdullisesti(500));
    }

    @Test
    public void vaihtoraha20eMaksullaMaukkaastaLounaastaOn1600senttia() {
        assertEquals(1600, kassa.syoMaukkaasti(2000));
    }

    @Test
    public void edullisenLounaanKateismyyntiKasvattaaEdullistenMaaraaYhdella() {
        kassa.syoEdullisesti(240);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void maukkaanLounaanKateismyyntiKasvattaaMaukkaittenMaaraaYhdella() {
        kassa.syoMaukkaasti(400);
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void edullisenLounaanOstoyritys1EurollaEiKasvataRahamaaraa() {
        kassa.syoEdullisesti(100);
        tarkistaRahamaara(rahaaAlussa);
    }

    @Test
    public void maukkaanLounaanOstoyritys399SentillaEiKasvataRahamaaraa() {
        kassa.syoMaukkaasti(399);
        tarkistaRahamaara(rahaaAlussa);
    }

    @Test
    public void edullisenLounaanOstoyritys239SentillaEiKasvataEdullistenMaaraa() {
        kassa.syoEdullisesti(100);
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void maukkaanLounaanOstoyritys0EurollaEiKasvataMaukkaidenMaaraa() {
        kassa.syoMaukkaasti(0);
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void edullisenLounaanOstoyritys1EurollaPalauttaa100senttia() {
        assertEquals(100, kassa.syoEdullisesti(100));
    }

    @Test
    public void maukkaanLounaanOstoyritys399SentillaPalauttaa399senttia() {
        kassa.syoMaukkaasti(399);
        assertEquals(399, kassa.syoMaukkaasti(399));
    }

    //korttiostot
    @Test
    public void edullisenLounaanKorttimyyntiVeloittaa240senttiaKortilta() {
        kassa.syoEdullisesti(kortti);
        assertEquals(rahaaKortillaAlussa - edullisenHinta, kortti.saldo());
    }

    @Test
    public void maukkaanLounaanKorttimyyntiVeloittaa400senttiaKortilta() {
        kassa.syoMaukkaasti(kortti);
        assertEquals(rahaaKortillaAlussa - maukkaanHinta, kortti.saldo());
    }

    @Test
    public void edullisenLounaanKorttimyyntiPalauttaaTrue() {
        assertTrue(kassa.syoEdullisesti(kortti));
    }

    @Test
    public void maukkaanLounaanKorttimyyntiPalauttaaTrue() {
        assertTrue(kassa.syoMaukkaasti(kortti));
    }

    @Test
    public void edullisenLounaanKorttimyyntiKasvattaaEdullistenMaaraaYhdella() {
        kassa.syoEdullisesti(kortti);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void maukkaanLounaanKorttimyyntiKasvattaaMaukkaidenMaaraaYhdella() {
        kassa.syoMaukkaasti(kortti);
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void edullisenLounaanOstoRiittamattomallaKatteellaEiMuutaKortinRahamaaraa() {
        syoKortinSaldo();
        int rahaaKortilla = kortti.saldo();
        kassa.syoEdullisesti(kortti);
        assertEquals(rahaaKortilla, kortti.saldo());
    }

    @Test
    public void maukkaanLounaanOstoRiittamattomallaKatteellaEiMuutaKortinRahamaaraa() {
        syoKortinSaldo();
        int rahaaKortilla = kortti.saldo();
        kassa.syoMaukkaasti(kortti);
        assertEquals(rahaaKortilla, kortti.saldo());
    }

    @Test
    public void edullisenLounaanOstoRiittamattomallaKatteellaEiMuutaEdullistenMaaraa() {
        syoKortinSaldo();
        int edullisia = kassa.edullisiaLounaitaMyyty();
        kassa.syoEdullisesti(kortti);
        assertEquals(edullisia, kassa.edullisiaLounaitaMyyty());
    }

    @Test
    public void maukkaanLounaanOstoRiittamattomallaKatteellaEiMuutaMaukkaittenMaaraa() {
        syoKortinSaldo();
        int maukkaita = kassa.maukkaitaLounaitaMyyty();
        kassa.syoMaukkaasti(kortti);
        assertEquals(maukkaita, kassa.maukkaitaLounaitaMyyty());
    }

    @Test
    public void edullisenLounaanOstoRiittamattomallaKatteellaEPalauttaaFalse() {
        syoKortinSaldo();
        assertFalse(kassa.syoEdullisesti(kortti));
    }

    @Test
    public void maukkaanLounaanOstoRiittamattomallaKatteellaEPalauttaaFalse() {
        syoKortinSaldo();
        assertFalse(kassa.syoMaukkaasti(kortti));
    }

    @Test
    public void edullistenLounaanKorttimyyntiEiMuutaKassanRahamaaraa() {
        kassa.syoEdullisesti(kortti);
        assertEquals(rahaaAlussa, kassa.kassassaRahaa());
    }

    @Test
    public void maukkaanLounaanKorttimyyntiEiMuutaKassanRahamaaraa() {
        kassa.syoMaukkaasti(kortti);
        assertEquals(rahaaAlussa, kassa.kassassaRahaa());
    }

    public void tarkistaRahamaara(int odotettuRahamaara) {
        assertEquals(odotettuRahamaara, kassa.kassassaRahaa());
    }

    @Test
    public void rahanLataaminenKortille1000senttiaKasvattaaKortinSaldoa1000senttia() {
        int saldonLisays = 1000;
        kassa.lataaRahaaKortille(kortti, saldonLisays);
        assertEquals(rahaaKortillaAlussa + saldonLisays, kortti.saldo());
    }

    @Test
    public void rahanLataaminenKortille2500senttiaKasvattaaKassanRahamaaraa2500senttia() {
        int saldonLisays = 2500;
        kassa.lataaRahaaKortille(kortti, saldonLisays);
        assertEquals(rahaaAlussa + saldonLisays, kassa.kassassaRahaa());
    }

    @Test
    public void negatiivisenArvonLataaminenKortilleEiMuutaKortinSaldoa() {
        int saldonLisays = -1000;
        kassa.lataaRahaaKortille(kortti, saldonLisays);
        assertEquals(rahaaKortillaAlussa, kortti.saldo());
    }

    @Test
    public void negatiivisenArvonLataaminenKortilleEiMuutaKassanRahamaaraa() {
        int saldonLisays = -999;
        kassa.lataaRahaaKortille(kortti, saldonLisays);
        assertEquals(rahaaAlussa, kassa.kassassaRahaa());
    }

    public void syoKortinSaldo() {
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoMaukkaasti(kortti);
        //nyt rahaa kortilla 1.6e, jos alussa 20e
        //maukkaastisyötyjä 4 kpl, edullisia 1 kpl
    }

}
