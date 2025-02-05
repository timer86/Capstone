package Artist;

import Track.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Added Francesco Cao 17/01/2025 15:43

 */
/**
 * This Class represent a Sing Artist with reference to Track and other data AG update 15/01/2025 15:19
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

    public Artist(String id,String name, String genre, List<String> idTrack) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.idTrack = new ArrayList<>(idTrack);
    }


/** Starting Get Method **/
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

    public String getIdByName(String name){
        return this.id;
    }

/** Starting Set Method **/

    public void setId(String id) {
    this.id = id;
    }

    public void setGenre(String genre) {
    this.genre = genre;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrackIds(List<String> trackIds) {
        this.idTrack = new ArrayList<>(trackIds); // Converte in lista mutabile
    }


/** Starting Add-Remove Method **/

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