package Track;

import Artist.Artist;

import java.util.List;

/**
 * This Class represent a Sing Track with reference to artista and other data
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
    private String artistId;
    private String genre;
    private int year;
    private String album;
    private String title;
    private List<Artist> artists;

    public Track(String id, String artistId, int year, String genre, String album, String title) {
        this.id = id;
        this.artistId = artistId;
        this.year = year;
        this.genre = genre;
        this.album = album;
        this.title = title;
    }


    //Starting getter Method

   public String getId() {
        return id;
   }

    public String getArtistId() {
        return artistId;
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

    //Starting Setter Method
    public void setId(String id) {
        this.id = id;
    }
    public void setArtistId(String artistId) {
        this.artistId = artistId;
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
        return "track(" +
                "id" + id +
                "artist_id :" + artistId +
                "year : " + year +
                "genre :" + genre +
                "album :" + album +
                "title :" + title + ")";
    }



}
