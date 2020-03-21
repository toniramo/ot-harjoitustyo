package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void kortinSaldoAlussaOikeinToStringilla() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
    
    @Test
    public void kortinSaldoAlussaOikeinSaldoMetodilla() {
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void rahanLataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(1000);
        assertEquals("saldo: 10.10", kortti.toString());
    }
    
    @Test
    public void saldoVaheneeOikeinJosRahaaOnTarpeeksi() {
        kortti.lataaRahaa(1000);
        kortti.otaRahaa(510);
        assertEquals("saldo: 5.0", kortti.toString());
    }
    
    @Test
    public void saldoVaheneeOikeinJosRahaaTasanOtettavaMaara() {
        kortti.otaRahaa(10);
        assertEquals("saldo: 0.0", kortti.toString());
    }
    
    @Test
    public void saldoEiMuutuJosRahaaEiOleTarpeeksi() {
        kortti.otaRahaa(11);
        assertEquals("saldo: 0.10", kortti.toString());
    }
    
    @Test
    public void otaRahaaPalauttaaTrueJosRahaaTarpeeksi() {
        assertTrue(kortti.otaRahaa(9));
    }
    
    @Test
    public void otaRahaaPalauttaaFalseJosRahaaEiTarpeeksi() {
        assertFalse(kortti.otaRahaa(100));
    }
}
