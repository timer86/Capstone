/**
 * Added Francesco Cao 17/01/2025 15:43

 */

package Service;
import DAO.TrackDAO;
import DAO.ArtistDAO;
import Track.Track;
import Artist.Artist;
import Track.MusicGenres;


import java.util.*;

import java.util.stream.Collectors;


public class TrackService {
    private final List<Track> tracks = new ArrayList<>();
    private TrackDAO trackDao;
    private ArtistDAO artistDao;

    public TrackService(TrackDAO trackDao, ArtistDAO artistDao) {
        this.trackDao = trackDao;
        this.artistDao = artistDao;
    }

    public Track createTrack(String id, int year, String genre, String album, String title, List<String> artistIds ) {
        int currentYear = java.time.LocalDate.now().getYear();

        if (id  == null || id.trim().isEmpty()) {
            id = generateTrackId(title);
            System.out.println(id);
        }
        if (artistIds == null || artistIds.isEmpty()) {
            throw new IllegalArgumentException("Artist IDs cannot be null or empty");
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

        List<String> uniqueArtistIds = artistIds.stream().distinct().toList();
        Map<String, Artist> artistMap = new HashMap<>();
        for (String artistId : uniqueArtistIds){
            Artist artist = artistDao.getArtistById(artistId);
            if (artist == null){
                throw new IllegalArgumentException("The Artist with this ID: " + artistId + " does not exist");
            }
            artistMap.put(artistId, artist);
        }
        Track newTrack = new Track (id, year, genre, album, title,artistIds);
        newTrack.setArtistIds(artistIds);

        Track savedTrack = trackDao.createTrack(newTrack);

        for (Artist artist : artistMap.values()){
            if (!artist.getTrackIds().contains(id)){
                artist.addIdTrack(id);
                System.out.println("Updating artist: " + artist.getId());
                artistDao.updateArtist(artist);
            }
        }
        return savedTrack;
//
    }

    public Track getTrackByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        return trackDao.getAllTracks().stream()
                .filter(track -> track.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Track with title " + title + " does not exist"));
    }

    public List<Track> getTracksByArtistID(String artistId) {
        if (artistId == null || artistId.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist ID cannot be null or empty");
        }
        return trackDao.getAllTracks().stream()
                .filter(track -> track.getArtistIds().contains(artistId))
                .toList();
    }

    private String generateTrackId(String name) {
        // Genera un ID unico basato sul nome e sul timestamp
        return name.replaceAll("\\s+", "").toUpperCase() + "_" + System.currentTimeMillis();
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
