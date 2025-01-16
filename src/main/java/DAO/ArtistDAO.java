package DAO;
import DAO.ArtistDAO;
import Artist.Artist;

import java.util.*;
//16:04 arto
public interface ArtistDAO {
    Artist createArtist(Artist artist);
    Artist getArtistById(String id);
    List<Artist> getAllArtists();
    Artist updateArtist(Artist artist);
    boolean deleteArtist(String id);
}


