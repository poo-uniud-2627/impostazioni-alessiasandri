// Pong — Programmazione Orientata agli Oggetti e Laboratori, Udine, a.a. 2026-27
// Consegna 2: le impostazioni. Scadenza: domenica 1 novembre 2026.
package pong;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Le impostazioni di una partita di Pong: quanti punti servono per vincere, con
 * quale scarto
 * minimo, a che velocità va la palla. È un record (lezione 4): una volta
 * create, non cambiano.
 *
 * IL PEZZO MANCANTE: il costruttore che rifiuta i valori sbagliati e il metodo
 * leggi.
 * Ogni metodo ha il suo contratto, nel commento sopra. I test ufficiali
 * (cartella test)
 * controllano i contratti.
 */
public record Impostazioni(int puntiPerVincere, int scartoMinimo, int velocitaPalla) {

    /**
     * Le impostazioni standard: 11 punti per vincere, scarto minimo 2, velocità
     * della palla 5.
     */
    public static final Impostazioni STANDARD = new Impostazioni(11, 2, 5);

    /**
     * Il costruttore (lezione 7: un record che nasce giusto).
     * Promette: se i tre valori sono nei loro intervalli, le impostazioni con quei
     * valori.
     * Lancia IllegalArgumentException se puntiPerVincere non è fra 1 e 99, se
     * scartoMinimo
     * non è fra 1 e 10, o se velocitaPalla non è fra 1 e 20 (estremi compresi). Il
     * messaggio
     * dice quale valore è sbagliato e perché.
     */
    public Impostazioni {
        // codice scritto da me
        if ((puntiPerVincere < 1) || (puntiPerVincere > 99)) {
            throw new IllegalArgumentException(
                    "puntiPerVincere è: " + puntiPerVincere + ". Deve essere compreso tra 1 e 99");
        }
        if ((scartoMinimo < 1) || (scartoMinimo > 10)) {
            throw new IllegalArgumentException(
                    "scartoMinimo è: " + scartoMinimo + ". Deve essere compreso tra 1 e 10");
        }
        if ((velocitaPalla < 1) || (velocitaPalla > 20)) {
            throw new IllegalArgumentException(
                    "velocitaPalla è: " + velocitaPalla + ". Deve essere compreso tra 1 e 20");
        }
    }

    /**
     * Legge le impostazioni dalle righe di un file, una impostazione per riga.
     * Pretende: righe non è null e non contiene null.
     * Promette:
     * - le righe vuote (o di soli spazi) e quelle che, tolti gli spazi iniziali,
     * cominciano
     * con # sono ignorate;
     * - ogni altra riga è nella forma chiave = valore , con spazi qualsiasi attorno
     * alla chiave,
     * al segno = e al valore; le chiavi sono puntiPerVincere, scartoMinimo,
     * velocitaPalla;
     * - un'impostazione che nelle righe non c'è prende il valore standard;
     * - il risultato sono le impostazioni lette.
     * Lancia IllegalArgumentException, con un messaggio che comincia con «riga N: »
     * (N è il numero
     * della riga, contando da 1 tutte le righe, anche quelle vuote e i commenti),
     * se in una riga:
     * 1. manca il segno = ;
     * 2. la chiave non è una delle tre;
     * 3. il valore non è un numero intero;
     * 4. la stessa chiave c'era già in una riga precedente;
     * 5. il valore è fuori dal suo intervallo (gli intervalli del costruttore).
     * Se ci sono errori in più righe, il messaggio è quello della riga più in alto.
     */
    public static Impostazioni leggi(List<String> righe) {
        // codice scritto da me
        boolean puntiGiaVista = false;
        boolean scartoGiaVista = false;
        boolean velocitaGiaVista = false;
        int punti = 11;
        int scarto = 2;
        int velocita = 5;

        for (int i = 0; i < righe.size(); i++) {
            String riga = righe.get(i).trim();
            if (riga.startsWith("#") || (riga.isEmpty())) {
                continue;
            }

            int posizioneUguale = riga.indexOf('=');
            if (posizioneUguale == -1) {
                throw new IllegalArgumentException(
                        "riga " + (i + 1) + ": manca il segno '='.");
            }

            String chiave = riga.substring(0, posizioneUguale).trim();
            String valore = riga.substring(posizioneUguale + 1).trim();
            if ((!chiave.equals("puntiPerVincere")) && (!chiave.equals("scartoMinimo"))
                    && (!chiave.equals("velocitaPalla"))) {
                throw new IllegalArgumentException(
                        "riga " + (i + 1) + ": chiave non riconosciuta.");
            }

            int numero;
            try {
                numero = Integer.parseInt(valore);
            } catch (NumberFormatException e) {
                throw new NumberFormatException(
                        "riga " + (i + 1) + ": il valore indicato non è un intero.");
            }

            if (chiave.equals("puntiPerVincere")) {
                if (puntiGiaVista) {
                    throw new IllegalArgumentException("riga " + (i + 1) + ": chiave già inizializzata");
                } else {
                    puntiGiaVista = true;
                    if ((numero < 1) || (numero > 99)) {
                        throw new IllegalArgumentException("riga " + (i + 1) + ": valore fuori dall'intervallo consentito.");
                    }
                    punti = numero;
                }
            }
            if (chiave.equals("scartoMinimo")) {
                if (scartoGiaVista) {
                    throw new IllegalArgumentException("riga " + (i + 1) + ": chiave già inizializzata");
                } else {
                    scartoGiaVista = true;
                    if ((numero < 1) || (numero > 10)) {
                        throw new IllegalArgumentException("riga " + (i + 1) + ": valore fuori dall'intervallo consentito.");
                    }
                    scarto = numero;
                }
            }
            if (chiave.equals("velocitaPalla")) {
                if (velocitaGiaVista) {
                    throw new IllegalArgumentException("riga " + (i + 1) + ": chiave già inizializzata");
                } else {
                    velocitaGiaVista = true;
                    if ((numero < 1) || (numero > 20)) {
                        throw new IllegalArgumentException("riga " + (i + 1) + ": valore fuori dall'intervallo consentito.");
                    }
                    velocita = numero;
                }
            }
        }
        return new Impostazioni(punti, scarto, velocita);
    }

    /**
     * Legge le impostazioni da un file di testo.
     * Promette: come leggi, con le righe del file.
     * Lancia IOException se il file non si può leggere (per esempio non esiste):
     * non la cattura,
     * la lascia a chi chiama. Lancia IllegalArgumentException come leggi.
     */
    public static Impostazioni daFile(Path file) throws IOException {
        return leggi(Files.readAllLines(file));
    }
}
