package Artist;

import Track.Track;
import java.util.List;


/**
 * This Class represent a Sing Track with reference to artista and other data
 */

public class Artist {
    /**
     * Variables:
     * id : Artist unique id
     * name: artist name
     * genre: artist genre
     */
    private String id;
    private String name;
    private String genre;
    private List<Track> tracks;

    public Artist(String id, String name, String genre) {
        this.id = id;
        this.name = name;
        this.genre = genre;
    }


    //Starting getter Method

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }


    //Starting Setter Method
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Artist(" +
                "id" + id +
                "artist_id :" + name +
                "genre :" + genre;
    }
 //


}