package Artist;

import Track.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class represent a Sing Artist with reference to Track and other data AG update 15/01/2025 15:11
 */

public class Artist {
    /**
     * Variables:
     * id : Artist unique id
     * name: Artist name
     * genre: Artist genre
     * idTrack: List of all TrackId for the Artist
     */

    private String id;
    private String name;
    private String genre;
    private List<String> idTrack;

    public Artist(String id,String name, String genre) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.idTrack = new ArrayList<>();
    }


/** Starting getter Method **/
    public String getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public String getName() {
        return name;
    }
    public List<String> getTrackIds() {
        return idTrack;
    }



/** Starting setter Method **/

    public void setId(String id) {
    this.id = id;
    }

    public void setGenre(String genre) {
    this.genre = genre;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrackIds(List<String> idTrack) {
        this.idTrack = idTrack;
    }


/** Starting Add - Remove Method **/

    public void  addIdTrack(String idTrack) {
        if (!this.idTrack.contains(idTrack)) {
            this.idTrack.add(idTrack);
        }
    }

    public void removeIdTrack(String idTrack)
    {
        if (!this.idTrack.contains(idTrack)) {
            this.idTrack.remove(idTrack);
        }
    }


    @Override
    public String toString() {
        return "Track { " +
                "id='" + id + "', " +
                "name='" + name + "', " +  // se hai una lista
                "genre='" + genre + "', ";
    }




}