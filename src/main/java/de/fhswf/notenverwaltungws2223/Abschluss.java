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

	public boolean kolloquiumBestanden() {
		if (notenKolloquium.size() == 0) {
			return false;
		}
		for (Note note : notenKolloquium) {
			if (note.getErgebnis() < 4.0F) {
				return true;
			}
		}
		return false;
	}

	public boolean bachelorBestanden() {
		if (noteBachelor.size() == 0) {
			return false;
		}
		for (Note note : noteBachelor) {
			if (note.getErgebnis() < 4.0F) {
				return true;
			}
		}
		return false;
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

			// note kann nur eingefügt werden, wenn entweder keine noten eingefügt worden sind, oder die letzte note 5.0F ist
			if (noteBachelor.size() == 0 || noteBachelor.get(noteBachelor.size() - 1).getErgebnis() == 5.0F) {
				noteBachelor.add(note);
			} else {
				throw new IllegalArgumentException("Die letzte Note muss 5.0F sein");
			}
			// noteBachelor.add(note);
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

			// note kann nur eingefügt werden, wenn entweder keine noten eingefügt worden sind, oder die letzte note 5.0F ist
			if (notenKolloquium.size() == 0 || notenKolloquium.get(notenKolloquium.size() - 1).getErgebnis() == 5.0F) {
				notenKolloquium.add(note);
			} else {
				throw new IllegalArgumentException("Die letzte Note muss 5.0F sein");
			}
			// notenKolloquium.add(note);
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