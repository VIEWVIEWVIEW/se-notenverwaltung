package de.fhswf.notenverwaltungws2223;

import java.util.*;

public class Abschluss {

	private List<Note> noteBachelor;
	private List<Note> notenKolloquium;
	public int ectsKolloquium = 0;
	public int ectsBachelor = 0;

	private int maximalAnzahlNotenBachelor = 2;
	private int maximalAnzahlNotenKolloquium = 2;

	Abschluss(int ectsKolloquium, int ectsBachelor) {
		ectsBachelor = ectsBachelor;
		ectsKolloquium = ectsKolloquium;
		noteBachelor = new ArrayList<Note>();
		notenKolloquium = new ArrayList<Note>();
	}

	public float getNotendurchschnitt() {
		// TODO - implement Abschluss.getNotendurchschnitt
		throw new UnsupportedOperationException();
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