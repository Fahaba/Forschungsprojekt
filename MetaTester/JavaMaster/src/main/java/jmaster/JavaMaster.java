package jmaster;

import jmaster.artwork.Art;
import jmaster.artwork.ArtType;
import jmaster.artwork.Painting;
import jmaster.artwork.Sculpture;

import java.util.ArrayList;

public class JavaMaster {

    final String name;
    public ArrayList<Art> art = new ArrayList<>();

    JavaMaster(String name) {
        this.name = name;
    }

    public void createPainting(String artworkName, int year){
        Painting painting = new Painting(artworkName, this.name, year , ArtType.PAINTING);
        System.out.println(name + " create a new painting.");
        art.add(painting);
    }

    public void createSculpture(String artworkName, int year, int sculptureSize){
        Sculpture sculpture = new Sculpture(artworkName, this.name, year , ArtType.SCULPTURE,sculptureSize);
        System.out.println(name + " create a new Sculpture.");
        art.add(sculpture);
    }

    public void printArt() {
        for (int i = 0; i < art.size(); i++) {
            System.out.println(art.get(i).info());
        }
    }

    static String greetings() {
        return "Greetings form the Java Arts Association!";
    }
}
