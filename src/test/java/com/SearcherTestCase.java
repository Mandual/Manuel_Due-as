package com;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearcherTestCase {

    private final Searcher searcher = new Searcher();


    @Test
    @DisplayName("searchWord: la palabra existe en la lista")
    void testSearchWord_wordExists() {
        List<String> lista = Arrays.asList("hola", "adios", "buenos", "dias");

        boolean resultado = searcher.searchWord("buenos", lista);

        assertTrue(resultado, "Debería encontrar 'buenos' en la lista");
    }

    @Test
    @DisplayName("searchWord: la palabra no existe en la lista")
    void testSearchWord_wordDoesNotExist() {
        List<String> lista = Arrays.asList("hola", "adios", "buenos", "dias");

        boolean resultado = searcher.searchWord("gracias", lista);

        assertFalse(resultado, "'gracias' no está en la lista");
    }


    @Test
    @DisplayName("getWordByIndex: un índice válido devuelve la palabra correcta")
    void testGetWordByIndex_validIndex() {
        List<String> lista = Arrays.asList("uno", "dos", "tres");

        String palabra = searcher.getWordByIndex(lista, 1);

        assertEquals("dos", palabra);
    }

    @Test
    @DisplayName("getWordByIndex: índice negativo devuelve null")
    void testGetWordByIndex_negativeIndex() {
        List<String> lista = Arrays.asList("uno", "dos", "tres");

        String palabra = searcher.getWordByIndex(lista, -1);

        assertNull(palabra);
    }

    @Test
    @DisplayName("getWordByIndex: índice demasiado grande devuelve null")
    void testGetWordByIndex_indexTooLarge() {
        List<String> lista = Arrays.asList("uno", "dos", "tres");

        String palabra = searcher.getWordByIndex(lista, 10);

        assertNull(palabra);
    }


    @Test
    @DisplayName("searchByPrefix: devuelve las palabras que empiezan con el prefijo")
    void testSearchByPrefix_matchesFound() {
        List<String> lista = Arrays.asList("casa", "carro", "camino", "perro");

        List<String> resultado = searcher.searchByPrefix("ca", lista);

        assertEquals(3, resultado.size());
        assertTrue(resultado.contains("casa"));
        assertTrue(resultado.contains("carro"));
        assertTrue(resultado.contains("camino"));
    }

    @Test
    @DisplayName("searchByPrefix: no incluye palabras que no empiecen con el prefijo")
    void testSearchByPrefix_noFalsePositives() {
        List<String> lista = Arrays.asList("casa", "carro", "perro", "gato");

        List<String> resultado = searcher.searchByPrefix("ca", lista);

        assertFalse(resultado.contains("perro"));
        assertFalse(resultado.contains("gato"));
    }


    @Test
    @DisplayName("filterByKeyword: devuelve todos los elementos que contienen la palabra clave")
    void testFilterByKeyword_matchesFound() {
        List<String> lista = Arrays.asList("manzana verde", "pera verde", "uva morada");

        List<String> resultado = searcher.filterByKeyword("verde", lista);

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains("manzana verde"));
        assertTrue(resultado.contains("pera verde"));
    }

    @Test
    @DisplayName("filterByKeyword: no devuelve ninguno si la palabra clave no existe")
    void testFilterByKeyword_noMatches() {
        List<String> lista = Arrays.asList("manzana verde", "pera verde", "uva morada");

        List<String> resultado = searcher.filterByKeyword("azul", lista);

        assertTrue(resultado.isEmpty());
    }


    @Test
    @DisplayName("searchExactPhrase: encuentra la frase cuando es el primer elemento")
    void testSearchExactPhrase_firstElement() {
        List<String> lista = Arrays.asList("buenos dias", "adios", "hola mundo");

        boolean resultado = searcher.searchExactPhrase("buenos dias", lista);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("searchExactPhrase: BUG original - la frase existe pero no es el primer elemento")
    void testSearchExactPhrase_notFirstElement() {
        // Con la implementación original, el "else return false" dentro del bucle
        // hacía que el método comparase solo el primer elemento de la lista:
        // si no coincidía, devolvía false inmediatamente sin mirar el resto.
        // Tras corregir Searcher.java (quitando el else y dejando que el bucle
        // siga), este test pasa correctamente.
        List<String> lista = Arrays.asList("adios", "hola mundo", "buenos dias");

        boolean resultado = searcher.searchExactPhrase("buenos dias", lista);

        assertTrue(resultado, "La frase está en la lista, aunque no en la primera posición");
    }

    @Test
    @DisplayName("searchExactPhrase: la frase no existe en ninguna posición")
    void testSearchExactPhrase_notFound() {
        List<String> lista = Arrays.asList("adios", "hola mundo", "buenos dias");

        boolean resultado = searcher.searchExactPhrase("no existe", lista);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("searchExactPhrase: lista vacía siempre devuelve false")
    void testSearchExactPhrase_emptyList() {
        List<String> lista = new ArrayList<>();

        boolean resultado = searcher.searchExactPhrase("cualquier cosa", lista);

        assertFalse(resultado);
    }
}
