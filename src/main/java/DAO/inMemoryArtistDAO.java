/**
 * Added Francesco Cao 17/01/2025 15:43

 */
package DAO;

import Artist.Artist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class inMemoryArtistDAO implements ArtistDAO{
    private final Map<String, Artist> artistMap = new HashMap<>();

    @Override
    public Artist createArtist(Artist artist) {
        if (artistMap.containsKey(artist.getId())) {
            throw new IllegalArgumentException("Artist already exists " + artist.getId() + " already Exist");
        }
        artistMap.put(artist.getId(), artist);
        return artist;
    }

    @Override
    public Artist getArtistById(String id) {
        return artistMap.get(id);
    }

    @Override
    public List<Artist> getAllArtists() {
        return new ArrayList<>(artistMap.values());
    }

    @Override
    public Artist updateArtist(Artist artist){
        if (!artistMap.containsKey(artist.getId())) {
            throw new IllegalArgumentException("Artist does not exists - " + artist.getId());
        }
        artistMap.put(artist.getId(), artist);
        return artist;
    }

    public boolean deleteArtist(String id){
        Artist removed = artistMap.remove(id);
        return removed != null;
    }
}
