package gmaster.artwork

class Painting extends Art{

    Painting(String name, String artistName, int year, ArtType artType) {
        super(name, artistName, year, artType);
    }

    @Override
    String info() {
        return "The $artworkType named $artworkName" +
                " was created by $artistName" +
                " in $creationYear"
    }
}
