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

    public Artist createArtist(String id, String name, String genre, List<String> trackIds) {
        // Validazioni dei campi
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist ID cannot be null or empty");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist name cannot be null or empty");
        }

        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist genre cannot be null or empty");
        }

        if (!MusicGenres.ALLOWED_GENRES.contains(genre)) {
            throw new IllegalArgumentException("The Genre " + genre + " is not allowed. Allowed genres: " + MusicGenres.ALLOWED_GENRES);
        }

        // Verifica che tutte le tracce esistano
        for (String trackId : trackIds) {
            Track track = trackDao.getTrackById(trackId);
            if (track == null) {
                throw new IllegalArgumentException("The Track with ID: " + trackId + " does not exist");
            }
        }

        // Crea il nuovo artista
        Artist newArtist = new Artist(id, name, genre);
        newArtist.setTrackIds(trackIds);

        // Salva l'artista nel DAO
        Artist savedArtist = artistDao.createArtist(newArtist);

        // Aggiorna le tracce per aggiungere l'artista appena creato
        for (String trackId : trackIds) {
            Track track = trackDao.getTrackById(trackId);
            if (!track.getArtistIds().contains(id)) {
                track.addArtistID(id);
                trackDao.updateTrack(track); // Salva il track aggiornato
            }
        }

        return savedArtist;
    }

    public Artist updateArtist(Artist artist) {
        // Controlla che l'artista esista
        Artist existingArtist = artistDao.getArtistById(artist.getId());
        if (existingArtist == null) {
            throw new IllegalArgumentException("Artist does not exist - " + artist.getId());
        }

        // Aggiorna l'artista nel DAO
        return artistDao.updateArtist(artist);
    }

    public List<Artist> getAllArtists() {
        return artistDao.getAllArtists();
    }

    public boolean deleteArtist(String id) {
        // Recupera l'artista prima di eliminarlo
        Artist artist = artistDao.getArtistById(id);
        if (artist == null) {
            throw new IllegalArgumentException("Artist with ID " + id + " does not exist");
        }

        // Rimuove l'artista dalle tracce associate
        for (String trackId : artist.getTrackIds()) {
            Track track = trackDao.getTrackById(trackId);
            if (track != null) {
                track.removeArtistID(id);
                trackDao.updateTrack(track); // Salva il track aggiornato
            }
        }

        // Elimina l'artista dal DAO
        return artistDao.deleteArtist(id);
    }
}

