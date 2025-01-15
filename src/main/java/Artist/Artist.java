package Artist;

import Track.Track;

public class Artist {
    String id;
    String name;
    String genre;
    String country;


    public Artist(String id, String name, String genre, String country, String song) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.country = country;
        //Track track = new Track(song,name);
    }

    public Artist(String id, String name, String country, String song) {
        this.id = id;
        this.name = name;
        this.country = country;
        //Track track = new Track(song,name);
    }
    //public AddArtist(String name) {

    //}

    //public getId(String name) {


    //}
}
