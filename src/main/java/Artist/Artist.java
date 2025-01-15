package Artist;

import Track.Track;
import java.util.List;

public class Artist {
    private String id;
    private String name;
    private String genre;
    private String country;
    private List<Track> track;

    public Artist(String id, String name, String genre, String country, String song) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.country = country;
        //
        //Track track = new Track(song,name);
    }

    public interface AddTrack{
        String getTrackId();
        String getArtistId();

    }

    public Boolean IsUnique(String name){
        return true;
    }


    public String getId(String name) {
        return this.id;
    }
}
