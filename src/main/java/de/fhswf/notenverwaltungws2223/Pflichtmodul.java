package de.fhswf.notenverwaltungws2223;

import java.io.Serializable;
import java.util.*;


public class Pflichtmodul extends Modul implements Serializable {



	/**
	 * 
	 * @param ectsWertDesModuls Der Wert des Modules in ECTS Punkten
	 * @param startSemester Das Semester in dem das Modul beginnt
	 * @param nameDesModuls
	 */
	public Pflichtmodul(int ectsWertDesModuls, int startSemester, String nameDesModuls) {
		super(ectsWertDesModuls, startSemester);
		name = nameDesModuls;
	}
}