package Artist;

import Track.Track;

import java.util.ArrayList;
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
    private String idTrack;
    private String name;
    private String genre;
    private List<Track> tracks = new ArrayList<Track>();

    public Artist(String id, String idTrack, String name, String genre,Track track) {
        this.id = id;
        this.idTrack = idTrack;
        this.name = name;
        this.genre = genre;
        this.tracks.add(track);
    }



    /** Starting getter Method **/
    public String getId() {
        return id;
    }
    public String getIdTrack() {
        return idTrack;
    }
    public String getName() {
        return name;
    }
    public String getGenre() {
        return genre;
    }



    /** Starting setter Method **/
    public void setId(String id) {
        this.id = id;
    }
    public void setIdTrack(String idTrack) {
        this.idTrack = idTrack;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public void AddTrack(Track track) {
        this.tracks.add(track);
    }
    public void RemoveTrack(Track track) {
        this.tracks.remove(track);
    }

    @Override
    public String toString() {
        return "Artist(" +
                "id" + id +
                "artist_id :" + name +
                "genre :" + genre;
    }


    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
