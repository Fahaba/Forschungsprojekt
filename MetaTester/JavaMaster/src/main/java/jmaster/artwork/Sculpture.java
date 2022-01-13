package jmaster.artwork;

public class Sculpture extends Art {

    final int height;

    public Sculpture(String artworkName, String artistName, int creationYear, ArtType type, int height) {
        super(artworkName, artistName, creationYear, type);
        this.height = height;
    }

    @Override
    public String info() {
        return "The " + artworkType + " named " + artworkName +
                " was created by " + artistName +
                " in " + creationYear +
                ", it has an height of " + height + "cm.";
    }
}
