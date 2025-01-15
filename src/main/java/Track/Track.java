package Track;

import Artist.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class represent a Sing Track with reference to artista and other data fc update 15/01/2025 15:16
 */

public class Track {
    /**
     * Variables:
     * id : track unique id
     * artistid: track unique artist id
     * genre: genre song
     * year: year song
     * album: name of the album
     * title: track title
     */
    private String id;
    //private String artistId;
    private String genre;
    private int year;
    private String album;
    private String title;
    private List<String> artistIds;

    public Track(String id, int year, String genre, String album, String title) {
        this.id = id;
       // this.artistId = artistId;
        this.year = year;
        this.genre = genre;
        this.album = album;
        this.title = title;
        this.artistIds = new ArrayList<>();
    }


    //Starting getter Method

   public String getId() {
        return id;
   }


    public void  addArtistID(String artistId) {
        if (!this.artistIds.contains(artistId)) {
            this.artistIds.add(artistId);
        }
    }

    public void removeArtistID(String artistId) {
        if (!this.artistIds.contains(artistId)) {
            this.artistIds.remove(artistId);
        }

    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }
    public String getAlbum() {
        return album;
    }
    public String getTitle() {
        return title;
    }

    public List<String> getArtistIds() {
        return artistIds;
    }

    public void setArtistIds(List<String> artistIds) {
        this.artistIds = artistIds;
    }

    //Starting Setter Method
    public void setId(String id) {
        this.id = id;
    }


    public void setGenre(String genre) {
        this.genre = genre;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Track { " +
                "id='" + id + "', " +
                "artistIds='" + artistIds + "', " +  // se hai una lista
                "year=" + year + ", " +
                "genre='" + genre + "', " +
                "album='" + album + "', " +
                "title='" + title + "' }";
    }




}
