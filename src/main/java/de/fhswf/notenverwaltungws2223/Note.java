package de.fhswf.notenverwaltungws2223;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class Note implements Serializable {

	private float ergebnis = 5.0F;
	public static ArrayList<Float> erlaubteNoten = new ArrayList<>() {
		{
			add(1.0F);
			add(1.3F);
			add(1.7F);
			add(2.0F);
			add(2.3F);
			add(2.7F);
			add(3.0F);
			add(3.3F);
			add(3.7F);
			add(4.0F);
			add(5.0F);
		}
	};
	public boolean bestanden() {
		// TODO - implement Note.bestanden
		return ergebnis >= 4.0F;
	}

	/**
	 * 
	 * @param note
	 */
	public void setErgebnis(float note) throws IllegalArgumentException {
		if (note > 5.0F) {
			throw new IllegalArgumentException("Note darf nicht groesser als 5.0 sein");
		} else if (note < 1.0F) {
			throw new IllegalArgumentException("Note darf nicht kleiner als 1.0 sein");
		}

		if (!erlaubteNoten.contains(note)) {
			throw new IllegalArgumentException("Note muss eine der folgenden Werte sein: " + erlaubteNoten.toString());
		}

		ergebnis = note;
	}

	public float getErgebnis() {
		return this.ergebnis;
	}

	/**
	 * 
	 * @param ergebnis
	 */
	public Note(float ergebnis) {
		setErgebnis(ergebnis);
	}

	public Note() {
	}
}