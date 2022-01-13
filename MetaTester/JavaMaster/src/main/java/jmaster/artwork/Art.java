package jmaster.artwork;

public abstract class Art {

    final String artworkName;
    final String artistName;
    final int creationYear;
    final ArtType artworkType;

    Art(String artworkName, String artistName, int creationYear, ArtType type) {
        this.artworkName = artworkName;
        this.artistName = artistName;
        this.creationYear = creationYear;
        this.artworkType = type;
    }

    public abstract String info();
}

