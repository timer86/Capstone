package Service;
import DAO.TrackDAO;
import DAO.ArtistDAO;
import Track.Track;
import Artist.Artist;
import Track.MusicGenres;

import java.util.List;


public class ArtistService {
    private TrackDAO trackDao;
    private ArtistDAO artistDao;

    public ArtistService(TrackDAO trackDao, ArtistDAO artistDao) {
        this.trackDao = trackDao;
        this.artistDao = artistDao;
    }

    public Artist createArtist(String id, String name, String genre, List<String> trackIds ) {

        if (id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("The Genre cannot be empty");
        }

        if (name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("The Genre cannot be empty");
        }

        if (genre == null || genre.trim().isEmpty()){
            throw new IllegalArgumentException("The Genre cannot be empty");
        }

        if (!MusicGenres.ALLOWED_GENRES.contains(genre)){
            throw new IllegalArgumentException("The Genre" + genre + "Not Present, use an other one");
        }


        for (String trackId : trackIds){
            Track track = trackDao.getTrackById(trackId);
            if (track == null){
                throw new IllegalArgumentException("The Track " + track + " with this ID: " + trackId + " does not exist");
            }

        }

        Artist newArtist = new Artist(id, name, genre);
        newArtist.setTrackIds(trackIds);
        return artistDao.createArtist(newArtist);

    }
    public Artist getArtistId(String id) {
        Artist artist = artistDao.getArtistById(id);
        if (artist == null){
            throw new IllegalArgumentException("The Artist with this ID: " + id + " does not exist");
        }
        return artist;
    }

    public List<Artist> getAllArtists() {
        return artistDao.getAllArtists();
    }

    public Artist updateArtist(Artist artist) {
        return artistDao.updateArtist(artist);
    }

    public boolean deleteArtist(String id) {
        return artistDao.deleteArtist(id);
    }
}
