/**
 * Added Francesco Cao 17/01/2025 15:43

 */

package DAO;
import DAO.ArtistDAO;
import Artist.Artist;
import Track.MusicGenres;
import java.util.*;
//16:04 arto
public interface ArtistDAO {
    Artist createArtist(Artist artist);
    Artist getArtistById(String id);
    public String getGenreById(String id);

    List<Artist> getAllArtists();
    Artist updateArtist(Artist artist);
    boolean deleteArtist(String id);
}


