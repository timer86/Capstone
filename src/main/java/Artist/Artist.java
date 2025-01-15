package Artist;

import Track.Track;

public class Artist {
    String id;
    String name;
    String genre;
    String country;


    public Artist(){
    }

    public void AddArtist(String id, String name, String genre, String country, String song) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.country = country;
        //
        //Track track = new Track(song,name);
    }



    public Boolean IsUnique(String name){
        return true;
    }

    public String[] getIds(String name){
        return

    }

    public String getId(String name) {
        return this.id;

    }
}
