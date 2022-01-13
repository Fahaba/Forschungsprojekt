package jmaster.artwork;

public class Painting extends jmaster.artwork.Art {

    public Painting(String name, String artistName, int year, ArtType type) {
        super(name, artistName, year, type);
    }

    @Override
    public String info() {
        return "The " + artworkType + " named " + artworkName +
                " was created by " + artistName +
                " in " + creationYear + ".";
    }
}
