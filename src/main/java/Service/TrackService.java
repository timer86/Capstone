package Service;
import DAO.TrackDAO;
import DAO.ArtistDAO;
import Track.Track;
import Artist.Artist;
import Track.MusicGenres;

import java.util.List;


public class TrackService {
    private TrackDAO trackDao;
    private ArtistDAO artistDao;

    public TrackService(TrackDAO trackDao, ArtistDAO artistDao) {
        this.trackDao = trackDao;
        this.artistDao = artistDao;
    }

    public Track createTrack(String id, int year, String genre, String album, String title, List<String> artistIds ) {
        int currentYear = java.time.LocalDate.now().getYear();

        if (id == null || id.trim().isEmpty()){
            throw new IllegalArgumentException("Track id cannot be null or empty");
        }

        if (year < 1900){
           throw new IllegalArgumentException("The Year cannot be less than 1900");
        }
        if (year > currentYear){
            throw new IllegalArgumentException("The Year cannot be in the Future");
        }

        if (genre == null || genre.trim().isEmpty()){
            throw new IllegalArgumentException("The Genre cannot be empty");
        }

        if (!MusicGenres.ALLOWED_GENRES.contains(genre)){
            throw new IllegalArgumentException("The Genre" + genre + "Not Present, use an other one");
        }

        if (title == null || title.trim().isEmpty()){
            throw new IllegalArgumentException("The Title cannot be empty");
        }

        for (String artistId : artistIds){
            Artist artist = artistDao.getArtistById(artistId);
            if (artist == null){
                throw new IllegalArgumentException("The Artist with this ID: " + artistId + " does not exist");
            }

        }

        Track newTrack = new Track(id, year, genre, album,title);
        newTrack.setArtistIds(artistIds);
        return trackDao.createTrack(newTrack);

    }
    public Track getTrackbyId(String id) {
        Track track = trackDao.getTrackById(id);
        if (track == null){
            throw new IllegalArgumentException("The Track with this ID: " + id + " does not exist");
        }
        return track;
    }

    public List<Track> getAllTracks() {
        return trackDao.getAllTracks();
    }

    public Track updateTrack(Track track) {
        return trackDao.updateTrack(track);
    }

    public boolean deleteTrack(String id) {
        return trackDao.deleteTrack(id);
    }
}
