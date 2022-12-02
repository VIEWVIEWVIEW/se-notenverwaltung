package de.fhswf.notenverwaltungws2223;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class Main extends Application {
    private ObservableList<Modul> modules = FXCollections.observableArrayList();
    ListView<Modul> listView = new ListView<Modul>(modules);

    private Modul selectedItem;

    private SimpleDoubleProperty gewichteterModulNotendurchschnitt = new SimpleDoubleProperty();
    private SimpleIntegerProperty ectsPunkte = new SimpleIntegerProperty();

    static private Abschluss abschluss = new Abschluss();

    // Berechne den neuen gewichteten durchschnitt (ECTS * letzte Note) / ECTS aller Module
    private void berechneNeuenSchnitt() {
        double summe = 0;
        double ectsSumme = 0;

        for (Modul modul : modules) {
            if (modul.isBestanden()) {
                summe += modul.ects * modul.noten.get(modul.noten.size() - 1).getErgebnis();
                ectsSumme += modul.ects;
            }
        }
        double neuerSchnitt = summe / ectsSumme;
        System.out.println(summe + " / " + ectsSumme + "=" + neuerSchnitt);
        gewichteterModulNotendurchschnitt.set(summe / ectsSumme);
        ectsPunkte.set((int) ectsSumme);
    }
    public void loadStateModule() {
        try {
            FileInputStream fos = new FileInputStream("module");
            ObjectInputStream oos = new ObjectInputStream(fos);
            // https://stackoverflow.com/a/34795127/5692336
            ArrayList<Modul> tmp = (ArrayList<Modul>) oos.readObject();

            for (Modul modul : tmp) {
                modules.add(modul);
                System.out.println(modul.getClass().getSimpleName());
            }
            oos.close();
            berechneNeuenSchnitt();
        } catch (FileNotFoundException | ClassNotFoundException e) {
            // Looks like we don't have a "module" file yet, which most likely means that this is the first time the app is run
            modules = FXCollections.observableArrayList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadStateAbschluss() {
        try {
            FileInputStream fos = new FileInputStream("abschluss");
            ObjectInputStream oos = new ObjectInputStream(fos);
            // https://stackoverflow.com/a/34795127/5692336
            Abschluss tmp = (Abschluss) oos.readObject();
            abschluss = tmp;
            oos.close();
            berechneNeuenSchnitt();
        } catch (FileNotFoundException | ClassNotFoundException e) {
            // Looks like we don't have a "abschluss" file yet, which most likely means that this is the first time the app is run
            System.out.println("Create new Abschluss");
            abschluss = new Abschluss();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void saveStateAbschluss() {
        try {
            FileOutputStream fos = new FileOutputStream("abschluss");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(abschluss);
            oos.close();
        }  catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveStateModule() {
        // Serialize our current state
        try {
            FileOutputStream fos = new FileOutputStream("module");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            // https://stackoverflow.com/a/34795127/5692336
            oos.writeObject(new ArrayList<Modul>(modules));
            oos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void alert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText("Ungültige Eingabe");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    public void modulBearbeiten(Modul modul) {
        Dialog dialog = new Dialog();
        dialog.setTitle("Modul bearbeiten");
        dialog.setHeaderText("Modul bearbeiten");

        ButtonType speichern = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(speichern, ButtonType.CANCEL);

        var ectsLabel = new Label("ECTS");
        var ectsField = new TextField();
        ectsField.setText(String.valueOf(modul.ects));

        var semesterLabel = new Label("Semester:");
        var semesterField = new TextField();
        semesterField.setText(String.valueOf(selectedItem.semester));
        var nameLabel = new Label("Name:");
        var nameField = new Label();
        nameField.setText(selectedItem.name);

        var grid = new GridPane();
        grid.add(semesterLabel, 1, 1);
        grid.add(semesterField, 2, 1);
        grid.add(nameLabel, 1, 2);
        grid.add(nameField, 2, 2);
        grid.add(ectsLabel, 1, 3);
        grid.add(ectsField, 2, 3);

        for (int i = 0; i < 3; i++) {
            var noteLabel = new Label("Note " + (i + 1) + ":");
            var noteField = new TextField();

            if (selectedItem.noten.size() > i) {
                noteField.setText(String.valueOf(selectedItem.noten.get(i).getErgebnis()));
            }

            grid.add(noteLabel, 1, i + 5);
            grid.add(noteField, 2, i + 5);
        }

        dialog.getDialogPane().setContent(grid);

        // dialog setOnCloseRequest for speichern
        dialog.setOnCloseRequest(event -> {

            if (dialog.getResult() == speichern) {
                try {
                    // save the new values
                    selectedItem.ects = Integer.parseInt(ectsField.getText());
                    selectedItem.semester = Integer.parseInt(semesterField.getText());
                    selectedItem.name = nameField.getText();

                    for (int i = 0; i < 3; i++) {
                        String neueNote = ((TextField) grid.getChildren().get(i * 2 + 7)).getText();
                        if (selectedItem.noten.size() > i) {
                            Note note = selectedItem.noten.get(i);
                            note.setErgebnis(Float.parseFloat(neueNote));
                        } else if (!neueNote.isEmpty()){
                            selectedItem.noteHinzufuegen(new Note(
                                    Float.parseFloat(neueNote)
                            ));
                        }
                        berechneNeuenSchnitt();
                    }
                } catch (IllegalArgumentException e) {
                    alert(e);
                }
            }
        });

        dialog.showAndWait();
    }


    public void neuesPflichtmodulHinzufuegen() {
        var dialog = new Dialog();
        dialog.setTitle("Neues Pflichtmodul hinzufügen");
        dialog.setHeaderText("Modul hinzufügen");

        // Set the button types.
        ButtonType speichern = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(speichern, ButtonType.CANCEL);

        // Create the semester and name labels and fields.
        var semesterLabel = new Label("Semester:");
        var semesterField = new TextField();

        var nameLabel = new Label("Name:");
        var nameField = new TextField();


        var ectsLabel = new Label("ECTS:");
        var ectsField = new TextField();

        var grid = new GridPane();
        grid.add(semesterLabel, 1, 1);
        grid.add(semesterField, 2, 1);
        grid.add(nameLabel, 1, 2);
        grid.add(nameField, 2, 2);

        grid.add(ectsLabel, 1, 3);
        grid.add(ectsField, 2, 3);

        dialog.setOnCloseRequest(event -> {
            if (dialog.getResult() == speichern) {
                // Save the data
                try {
                    int semester = Integer.parseInt(semesterField.getText());
                    String name = nameField.getText();
                    int ects = Integer.parseInt(ectsField.getText());

                    Pflichtmodul newModule = new Pflichtmodul(ects, semester, name);
                    System.out.println(newModule.name);

                    // Replica check
                    modules.forEach(modul -> {
                        if (modul.name.equalsIgnoreCase(newModule.name)) {
                            throw new IllegalArgumentException("Modul bereits vorhanden");
                        }
                    });
                    modules.add(newModule);
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fehler");
                    alert.setHeaderText("Ungültige Eingabe");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
            listView.refresh();
        });


        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();
    }
    public void neuesWahlpflichtmodulHinzufuegen() {
        var dialog = new Dialog();
        dialog.setTitle("Neues Wahlpflichtmodul hinzufügen");
        dialog.setHeaderText("Modul hinzufügen");

        // Set the button types.
        ButtonType speichern = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(speichern, ButtonType.CANCEL);

        // Create the semester and name labels and fields.
        var semesterLabel = new Label("Semester:");
        var semesterField = new TextField();

        var nameLabel = new Label("Name:");
        var nameDropdown = new ComboBox<String>();
        // populate name dropdown with "Wahlpflichtfach string" enumeration


        for (Wahlpflichtfach fach : Wahlpflichtfach.values()) {
            nameDropdown.getItems().add(fach.toString());
        }

        var ectsLabel = new Label("ECTS:");
        var ectsField = new TextField();

        var grid = new GridPane();
        grid.add(semesterLabel, 1, 1);
        grid.add(semesterField, 2, 1);
        grid.add(nameLabel, 1, 2);
        grid.add(nameDropdown, 2, 2);

        grid.add(ectsLabel, 1, 3);
        grid.add(ectsField, 2, 3);

        dialog.setOnCloseRequest(event -> {
            if (dialog.getResult() == speichern) {
                // Save the data
                try {
                    int semester = Integer.parseInt(semesterField.getText());
                    String name = nameDropdown.getValue();
                    int ects = Integer.parseInt(ectsField.getText());

                    Wahlpflichtmodul newModule = new Wahlpflichtmodul(ects, semester, Wahlpflichtfach.valueOf(name));
                    System.out.println(Wahlpflichtfach.valueOf(name));

                    // Replica check
                    modules.forEach(modul -> {
                        if (modul.name.equalsIgnoreCase(newModule.name)) {
                            throw new IllegalArgumentException("Modul bereits vorhanden");
                        }
                    });
                    modules.add(newModule);
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fehler");
                    alert.setHeaderText("Ungültige Eingabe");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
            listView.refresh();
        });


        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();
    }


    @Override
    public void start(Stage stage) throws IOException {
        this.gewichteterModulNotendurchschnitt.set(0);
        loadStateModule();
        loadStateAbschluss();

        // save state on close
        stage.setOnCloseRequest(event -> {
            saveStateModule();
            saveStateAbschluss();
        });

        // toolbar
        var modulSchnittLabel = new Label("Modulschnitt: ");

        var modulSchnitt = new Label();
        modulSchnitt.textProperty().bind(gewichteterModulNotendurchschnitt.asString("%.2f"));

        var anzahlAllerEctsLabel = new Label("ECTS: ");
        var anzahlAllerEcts = new Label();
        anzahlAllerEcts.textProperty().bind(ectsPunkte.asString());
        var labelVbox = new VBox(new HBox(modulSchnittLabel, modulSchnitt), new HBox(anzahlAllerEctsLabel, anzahlAllerEcts));


        var nurOffeneFächerAnzeigen = new CheckBox("Nur offene Fächer anzeigen");
        var neuesWahlpflichtmodulHinzufügen = new Button("Neues Wahlpflichtmodul hinzufügen");
        var neuesPflichtmodulHinzufügen = new Button("Neues Pflichtmodul hinzufügen");
        var öffneAbschlussfenster = new Button("Abschlussfenster öffnen");
        var editModul = new Button("Modul bearbeiten");
        var toolbar = new ToolBar(labelVbox, nurOffeneFächerAnzeigen, neuesWahlpflichtmodulHinzufügen, neuesPflichtmodulHinzufügen, editModul, öffneAbschlussfenster);

        // add new wahlpflichtmodul
        neuesWahlpflichtmodulHinzufügen.setOnMouseClicked(event -> {
            neuesWahlpflichtmodulHinzufuegen();
            berechneNeuenSchnitt();
        });

        // add new pflichtmodul
        neuesPflichtmodulHinzufügen.setOnMouseClicked(event -> {
            neuesPflichtmodulHinzufuegen();
            berechneNeuenSchnitt();
        });

        // öffne Abschlussfenster
        öffneAbschlussfenster.setOnMouseClicked(event -> {
            abschlussFenster();
        });

        // Filter list view based on checkbox
        nurOffeneFächerAnzeigen.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                listView.setItems(modules.filtered(modul -> !modul.isBestanden()));
            } else {
                listView.setItems(modules);
            }
        });

        // edit modul
        editModul.setOnMouseClicked(event -> {
            if (selectedItem != null) {
                modulBearbeiten(selectedItem);
            }
        });

        // Cell Factory
        listView.setCellFactory(param -> new ListCell<Modul>() {
            @Override
            protected void updateItem(Modul item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.name == null) {
                    setText(null);
                } else {
                    setText(item.name);
                }
            }
        });

        // select item in list view
        listView.setOnMouseClicked(event -> {
            // Use ListView's getSelected Item
            this.selectedItem = listView.getSelectionModel().getSelectedItem();
            if (this.selectedItem != null) {
                editModul.setDisable(false);
            } else {
                editModul.setDisable(true);
            }
        });

        Scene scene = new Scene(new VBox(listView, toolbar));
        stage.setScene(scene);
        stage.show();


    }

    public static void abschlussFenster() {
        var dialog = new Dialog();

        dialog.setTitle("Abschlussfenster");

        ButtonType speichern = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(speichern, ButtonType.CANCEL);

        var grid = new GridPane();
        var label1 = new Label("Kolloquium: ");
        grid.add(label1, 1, 1);
        var kLabelEcts = new Label("ECTS: ");
        grid.add(kLabelEcts, 1, 2);
        var kEcts = new TextField();
        grid.add(kEcts, 2, 2);

        var kLabelNote1 = new Label("Note 1: ");
        grid.add(kLabelNote1, 1, 3);
        var kNote1 = new TextField();
        grid.add(kNote1, 2, 3);

        var kLabelNote2 = new Label("Note 2: ");
        grid.add(kLabelNote2, 1, 4);
        var kNote2 = new TextField();
        grid.add(kNote2, 2, 4);


        var label2 = new Label("Bachelorarbeit: ");
        grid.add(label2, 1, 5);

        var bLabelEcts = new Label("ECTS: ");
        grid.add(bLabelEcts, 1, 6);
        var bEcts = new TextField();
        grid.add(bEcts, 2, 6);
        var bLabelNote1 = new Label("Note 1: ");
        grid.add(bLabelNote1, 1, 7);
        var bNote1 = new TextField();
        grid.add(bNote1, 2, 7);
        var bLabelNote2 = new Label("Note 2: ");
        grid.add(bLabelNote2, 1, 8);
        var bNote2 = new TextField();
        grid.add(bNote2, 2, 8);


        // set abschluss ects texts
        kEcts.setText(abschluss.ectsKolloquium.toString());
        bEcts.setText(abschluss.ectsBachelor.toString());

        // set abschluss note texts
        for (int i = 0; i < abschluss.notenKolloquium.size(); i++) {
            if (i == 0) {
                kNote1.setText(abschluss.notenKolloquium.get(i).toString());
            } else if (i == 1) {
                kNote2.setText(abschluss.notenKolloquium.get(i).toString());
            }
        }

        for (int i = 0; i < abschluss.noteBachelor.size(); i++) {
            if (i == 0) {
                bNote1.setText(abschluss.noteBachelor.get(i).toString());
            } else if (i == 1) {
                bNote2.setText(abschluss.noteBachelor.get(i).toString());
            }
        }

        dialog.setOnCloseRequest(event -> {

            if (dialog.getResult() == speichern) {
                try {
                    // save the new values
                    abschluss.ectsKolloquium = Integer.parseInt(kEcts.getText());
                    abschluss.ectsBachelor = Integer.parseInt(bEcts.getText());


                    /**
                    for (int i = 0; i < 2; i++) {
                        String neueNote = ((TextField) grid.getChildren().get(i * 2 + 7)).getText();
                        if (selectedItem.noten.size() > i) {
                            Note note = selectedItem.noten.get(i);
                            note.setErgebnis(Float.parseFloat(neueNote));
                        } else if (!neueNote.isEmpty()){
                            selectedItem.noteHinzufuegen(new Note(
                                    Float.parseFloat(neueNote)
                            ));
                        }
                    }*/

/**
                    if (abschluss.notenKolloquium.size() > 0) {
                        Note note = abschluss.notenKolloquium.get(0);
                        note.setErgebnis(Float.parseFloat(kNote1.getText()));
                    } else if (!kNote1.getText().isEmpty()) {
                        abschluss.noteKolloquiumHinzufuegen(new Note(
                                Float.parseFloat(kNote1.getText())
                        ));
                    }
**/



                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        });



        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}