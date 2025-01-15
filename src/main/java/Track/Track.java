package Track;

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

    public Track(String id, String artistId, int year, String genre, String album, String title) {
        this.id = id;
        this.artistId = artistId;
        this.year = year;
        this.genre = genre;
        this.album = album;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getArtistId() {
        return artistId;
    }
    public String getGenre() {

    }


}
