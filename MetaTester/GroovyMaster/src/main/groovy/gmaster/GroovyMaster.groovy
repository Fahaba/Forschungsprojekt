package gmaster

import gmaster.artwork.ArtType
import gmaster.artwork.Painting
import gmaster.artwork.Sculpture

class GroovyMaster {

    final name
    def art = []

    GroovyMaster(String name) {
        this.name = name
    }

    def createPainting(artworkName, year) {
        def painting = new Painting(artworkName, this.name, year, ArtType.PAINTING)
        println "$name created a new Painting."
        art.add painting
    }

    def createSculpture(artworkName, year, sculptureHeight) {
        def sculpture = new Sculpture(artworkName, this.name, year, ArtType.SCULPTURE, sculptureHeight)
        println "$name created a new Sculpture."
        art.add sculpture
    }

    def printArt() {
        art.each {
            println it.info()
        }
    }

    static String greetings() {
        return "Greetings form the Groovy Arts Association!"
    }
}
