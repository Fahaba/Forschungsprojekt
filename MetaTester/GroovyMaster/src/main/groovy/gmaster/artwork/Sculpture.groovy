package gmaster.artwork

class Sculpture extends Art {

    final height

    Sculpture(String artworkName, String artistName, int creationYear, ArtType type, int height) {
        super(artworkName, artistName, creationYear, type)
        this.height = height
    }

    @Override
    String info() {
        return "The $artworkType named $artworkName" +
                " was created by $artistName" +
                " in $creationYear" +
                ", it has an height of ${height}cm."
    }
}
