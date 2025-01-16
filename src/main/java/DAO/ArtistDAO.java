package DAO;

import Artist.Artist;
import java.util.List;

public interface ArtistDAO {
        List<Artist> getAllArtist();
        Artist getArtistnameById(String id);
        void saveArtist(Artist artist);
        void deleteArtist(Artist artist);
    }

