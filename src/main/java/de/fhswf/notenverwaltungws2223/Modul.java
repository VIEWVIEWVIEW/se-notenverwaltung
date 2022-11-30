package de.fhswf.notenverwaltungws2223;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Modul implements Serializable {
    public List<Note> noten;
    public int ects = 0;
    public String name;
    public int semester = 1;

    private int maximalAnzahlNoten = 3;


    Modul (int ects, int semester) {
        // initialize noten array with three null values
        noten = new ArrayList<Note>();

        if (ects > 0) {
            this.ects = ects;
        } else {
            throw new IllegalArgumentException("ECTS Wert muss größer als 0 sein");
        }

        if (ects <= 30) {
            this.ects = ects;
        } else {
            throw new IllegalArgumentException("ECTS Wert darf nicht größer als 30 sein");
        }



    }



    public boolean isBestanden() {
        if (noten.size() == 0) {
            return false;
        }
        for (Note note : noten) {
            if (note.getErgebnis() < 4.0F) {
                return true;
            }
        }
        return false;
    }


    /**
     *
     * @param note
     */
    public void noteHinzufuegen(Note note) {
        // note kann nur eingefügt werden wenn bisher weniger als 3 noten vorhanden sind
        if (noten.size() < maximalAnzahlNoten) {

            // note kann nur eingefügt werden, wenn entweder keine noten eingefügt worden sind, oder die letzte note 5.0F ist
            if (noten.size() == 0 || noten.get(noten.size() - 1).getErgebnis() == 5.0F) {
                // note wird hinzugefügt
                noten.add(note);
            } else {
                throw new IllegalArgumentException("Die vorherige Note muss 5.0F sein");
            }

        } else {
            throw new IllegalArgumentException("Maximal " + maximalAnzahlNoten + " Noten pro Modul");
        }
    }

    public void letzteNoteEntfernen() {
        if (noten.size() > 0) {
            noten.remove(noten.size() - 1);
        } else {
            throw new IllegalArgumentException("Keine Noten vorhanden");
        }
    }
}
