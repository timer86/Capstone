package DAO;

import java.util.*;
import Artist.Artist;

public class ArtistDAOImp implements ArtistDAO{
    //list is working as a database
    private List<Artist> artists;


    public ArtistDAOImp() {
        artists = new ArrayList<>();
        artists.add(new Artist("1", "Ligabue","POP"));
        artists.add(new Artist("2", "Bon-Jovi","ROCK"));
        artists.add(new Artist("3", "Madonna","POP",));
    }
    @Override
    public List<Artist> getAllArtist() {
        return artists;
    }
    @Override
    public Artist getArtistnameById(int id) {
        return artists.get(id);
    }
    @Override
    public void saveArtist(Artist artist) {
        artists.add(artist);
    }
    @Override
    public void deleteArtist(Artist artist) {
        artists.remove(artist);
    }
}