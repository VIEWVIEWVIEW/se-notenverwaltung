package de.fhswf.notenverwaltungws2223;

import java.io.Serializable;
import java.util.*;

public class Wahlpflichtmodul extends Modul implements Serializable {
	/**
	 * 
	 * @param ectsWertDesModuls
	 * @param startSemester
	 * @param wahlpflichtfach
	 */
	public Wahlpflichtmodul(int ectsWertDesModuls, int startSemester, Wahlpflichtfach wahlpflichtfach) {
		super(ectsWertDesModuls, startSemester);
		name = wahlpflichtfach.name;
	}
}