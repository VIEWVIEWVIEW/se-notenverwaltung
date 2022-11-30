package de.fhswf.notenverwaltungws2223;

public enum Wahlpflichtfach {
	EinfuehrungMachineLearning("EML"),
	NaturalLanguageProcessing("NLP"),
	DeepLearning("DL"),
	Skriptsprachen("SCS");

	public final String name;

	Wahlpflichtfach(String name) {
		this.name = name;
	}
}