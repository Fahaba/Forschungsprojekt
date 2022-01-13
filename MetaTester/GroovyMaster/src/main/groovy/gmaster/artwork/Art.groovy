package gmaster.artwork

abstract class Art {

    final artworkName;
    final artistName;
    final creationYear;
    final artworkType;

    Art(artworkName, artistName, creationYear, type) {
        this.artworkName = artworkName
        this.artistName = artistName
        this.creationYear = creationYear
        this.artworkType = type
    }

    abstract String info()
}

enum ArtType {
    PAINTING,
    SCULPTURE,
}
