package de.fhswf.notenverwaltungws2223;

import java.io.Serializable;
import java.util.*;

public class Abschluss implements Serializable {

	public List<Note> noteBachelor;
	public List<Note> notenKolloquium;
	public Integer ectsKolloquium = 0;
	public Integer ectsBachelor = 0;

	private Integer maximalAnzahlNotenBachelor = 2;
	private Integer maximalAnzahlNotenKolloquium = 2;

	Abschluss() {
		//ectsBachelor = ectsBachelor;
		//ectsKolloquium = ectsKolloquium;
		noteBachelor = new ArrayList<Note>();
		notenKolloquium = new ArrayList<Note>();
	}

	public float getNotendurchschnitt() {
		// TODO - implement Abschluss.getNotendurchschnitt
		Note bachelorNote = noteBachelor.get(noteBachelor.size() - 1);
		Note kolloquiumNote = notenKolloquium.get(notenKolloquium.size() - 1);
		float notendurchschnitt = (bachelorNote.getErgebnis() * ectsBachelor) + (kolloquiumNote.getErgebnis() * ectsKolloquium) / (ectsBachelor + ectsKolloquium);
		return notendurchschnitt;
	}

	/**
	 * 
	 * @param note
	 */
	public void noteBachelorHinzufuegen(Note note) {
		if (noteBachelor.size() < maximalAnzahlNotenBachelor) {
			noteBachelor.add(note);
		} else {
			throw new IllegalArgumentException("Maximal 2 Noten pro Bachelor");
		}
	}

	/**
	 * 
	 * @param note
	 */
	public void noteKolloquiumHinzufuegen(Note note) {
		if (notenKolloquium.size() < maximalAnzahlNotenKolloquium) {
			notenKolloquium.add(note);
		} else {
			throw new IllegalArgumentException("Maximal 2 Noten pro Kolloquium");
		}
	}

	public void letzteBachelornoteEntfernen() {
		if (noteBachelor.size() > 0) {
			noteBachelor.remove(noteBachelor.size() - 1);
		} else {
			throw new IllegalArgumentException("Keine Noten vorhanden");
		}
	}

	public void letzteKolloquiumnoteEntfernen() {
		if (notenKolloquium.size() > 0) {
			notenKolloquium.remove(notenKolloquium.size() - 1);
		} else {
			throw new IllegalArgumentException("Keine Noten vorhanden");
		}
	}

}