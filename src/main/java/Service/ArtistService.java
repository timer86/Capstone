/**
 * Added Francesco Cao 17/01/2025 15:43

 */

package Service;

import DAO.TrackDAO;
import DAO.ArtistDAO;
import Track.Track;
import Artist.Artist;

import Track.MusicGenres;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;

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
            id = generateArtistId(name);
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

        if (artistDao.getArtistById(id) != null) {
            throw new IllegalArgumentException("Artist with ID " + id + " already exists");
        }

        if (trackIds == null || trackIds.isEmpty()) {
            throw new IllegalArgumentException("Track IDs cannot be null or empty");
        }

        Set<String> uniqueTrackIds = new HashSet<>(trackIds);

        // Verifica che tutte le tracce esistano
        for (String trackId : uniqueTrackIds) {
            Track track = trackDao.getTrackById(trackId);
            if (track == null) {
                throw new IllegalArgumentException("The Track with ID: " + trackId + " does not exist");
            }
        }

        // Crea il nuovo artista
        Artist newArtist = new Artist(id, name, genre,trackIds);
        newArtist.setTrackIds(List.copyOf(uniqueTrackIds));

        // Salva l'artista nel DAO
        Artist savedArtist = artistDao.createArtist(newArtist);

        // Aggiorna le tracce per aggiungere l'artista appena creato
        for (String trackId : uniqueTrackIds) {
            Track track = trackDao.getTrackById(trackId);
            if (track != null && !track.getArtistIds().contains(id)) {
                track.addArtistID(id); // Aggiunge l'ID dell'artista alla traccia
                trackDao.updateTrack(track); // Salva la traccia aggiornata
            }
        }

        return savedArtist;
    }

    private String generateArtistId(String name) {
        // Genera un ID unico basato sul nome e sul timestamp
        return name.replaceAll("\\s+", "").toUpperCase() + "_" + System.currentTimeMillis();
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


    public Artist getArtistByName(String artistName) {
        if (artistName == null || artistName.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist name cannot be null or empty");
        }
        return artistDao.getAllArtists().stream()
                .filter(artist -> artist.getId().equalsIgnoreCase(artistName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Artist with name " + artistName + " does not exist"));
    }


    public String getGenreById(String artistId) {
        Artist artist = artistDao.getArtistById(artistId);
        if (artist == null) {
            throw new IllegalArgumentException("Artist with ID " + artistId + " does not exist");
        }
        return artist.getGenre();
    }

    public List<Track> getTracksByArtistId(String artistId) {
        Artist artist = artistDao.getArtistById(artistId);
        if (artist == null) {
            throw new IllegalArgumentException("Artist with ID " + artistId + " does not exist");
        }

        List<String> trackIds = artist.getTrackIds();
        List<Track> tracks = new ArrayList<>();

        for (String trackId : trackIds) {
            Track track = trackDao.getTrackById(trackId);
            if (track != null) {
                tracks.add(track);
            }
        }

        if (tracks.isEmpty()) {
            throw new IllegalArgumentException("No tracks found for artist with ID " + artistId);
        }

        return tracks;
    }

    public Artist getArtistDetailsById(String artistId) {
        Artist artist = artistDao.getArtistById(artistId);
        if (artist == null) {
            throw new IllegalArgumentException("Artist with ID " + artistId + " does not exist");
        }
        return artist;
    }




}

