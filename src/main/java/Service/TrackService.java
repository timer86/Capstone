/**
 * Added Francesco Cao 17/01/2025 15:43

 */

package Service;
import DAO.TrackDAO;
import DAO.ArtistDAO;
import Track.Track;
import Artist.Artist;
import Track.MusicGenres;


import java.time.LocalDate;
import java.util.*;


public class TrackService {
    private final List<Track> tracks = new ArrayList<>();
    private TrackDAO trackDao;
    private ArtistDAO artistDao;

    public TrackService(TrackDAO trackDao, ArtistDAO artistDao) {
        this.trackDao = trackDao;
        this.artistDao = artistDao;
    }

    public Track createTrack(String id, int year, String genre, String album, String title, List<String> artistIds) {
        int currentYear = java.time.LocalDate.now().getYear();
        if (title == null || title.trim().isEmpty()){
            throw new IllegalArgumentException("The Title cannot be empty");
        }
        if (id  == null || id.trim().isEmpty()) {
            id = generateTrackId(title);
            System.out.println(id);
        }
        if (artistIds == null || artistIds.isEmpty()) {
            throw new IllegalArgumentException("Artist IDs cannot be null or empty");
        }


        if (trackDao.getTrackById(id) != null) {
            throw new IllegalArgumentException("Track with ID " + id + " already exists");
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



        List<String> uniqueArtistIds = artistIds.stream().distinct().toList();
        Map<String, Artist> artistMap = new HashMap<>();

        for (String artistId : uniqueArtistIds) {
            Artist artist = artistDao.getArtistById(artistId);

            if (artist == null) {
                // Se l'artista non esiste, chiedi all'utente di crearlo
                artist = promptUserToCreateArtist(artistId);
                artistDao.createArtist(artist);
                System.out.println("Artist with ID " + artistId + " did not exist and was created automatically.");
            }
            artistMap.put(artistId, artist);
        }
        Track newTrack = new Track (id, year, genre, album, title,artistIds);
        newTrack.setArtistIds(artistIds);

        Track savedTrack = trackDao.createTrack(newTrack);

        for (Artist artist : artistMap.values()) {
            if (!artist.getTrackIds().contains(id)) {
                artist.addIdTrack(id);
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

    public String getGenreByTrackId(String trackId) {
        Track track = trackDao.getTrackById(trackId);
        if (track == null) {
            throw new IllegalArgumentException("Track with ID " + trackId + " does not exist");
        }
        return track.getGenre();
    }

    public int getYearByTrackId(String trackId) {
        Track track = trackDao.getTrackById(trackId);
        if (track == null) {
            throw new IllegalArgumentException("Track with ID " + trackId + " does not exist");
        }
        return track.getYear();
    }

    public String getAlbumTrackId(String trackId) {
        Track track = trackDao.getTrackById(trackId);
        if (track == null) {
            throw new IllegalArgumentException("Track with ID " + trackId + " does not exist");
        }
        return track.getAlbum();
    }

    private Artist promptUserToCreateArtist(String artistId) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Artist with ID " + artistId + " does not exist.");
        System.out.print("Enter the name for the new artist: ");
        String name = scanner.nextLine();

        System.out.print("Enter the genre for the new artist: ");
        String genre = scanner.nextLine();

        if (!MusicGenres.ALLOWED_GENRES.contains(genre)) {
            throw new IllegalArgumentException("The Genre " + genre + " is not allowed. Allowed genres: " + MusicGenres.ALLOWED_GENRES);
        }

        // Crea automaticamente l'ID se non specificato
        if (artistId == null || artistId.trim().isEmpty()) {
            artistId = generateArtistId(name);
        }

        // Crea l'artista
        return new Artist(artistId, name, genre, new ArrayList<>());
    }

    public List<Artist> getArtistsByTrackId(String trackId) {
        // Recupera la traccia dall'ID
        Track track = trackDao.getTrackById(trackId);
        if (track == null) {
            throw new IllegalArgumentException("Track with ID " + trackId + " does not exist");
        }

        // Ottieni gli ID degli artisti dalla traccia
        List<String> artistIds = track.getArtistIds();

        // Recupera gli artisti utilizzando i loro ID
        List<Artist> artists = new ArrayList<>();
        for (String artistId : artistIds) {
            Artist artist = artistDao.getArtistById(artistId);
            if (artist != null) {
                artists.add(artist);
            } else {
                throw new IllegalArgumentException("Artist with ID " + artistId + " does not exist");
            }
        }

        return artists;
    }



    public List<Track> getTracksByArtistID(String artistId) {
        if (artistId == null || artistId.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist ID cannot be null or empty");
        }
        return trackDao.getAllTracks().stream()
                .filter(track -> track.getArtistIds().contains(artistId))
                .toList();
    }

    public List<Track> getTrackById(String id) {
        // Recupera le tracce dal DAO
        List<Track> tracks = trackDao.getTrackByID(id);
        if (tracks == null || tracks.isEmpty()) {
            throw new IllegalArgumentException("No tracks found with ID: " + id);
        }
        return tracks;
    }

    private String generateArtistId(String name) {
        // Genera un ID unico basato sul nome e sul timestamp
        return name.replaceAll("\\s+", "").toUpperCase() + "_" + System.currentTimeMillis();
    }




    private String generateTrackId(String name) {
        // Genera un ID unico basato sul nome e sul timestamp
        return name.replaceAll("\\s+", "").toUpperCase() + "_" + System.currentTimeMillis();
    }
    public Track getSingleTrackbyId(String id) {
        Track track = trackDao.getTrackById(id);
        if (track == null){
            throw new IllegalArgumentException("The Track with this ID: " + id + " does not exist");
        }
        return track;
    }


    public List<Track> getTracksByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be null or empty");
        }
        return trackDao.getTracksByGenre(genre);
    }

    public List<Track> getTracksByYear(int year) {
        if (year < 1900 || year > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Year must be between 1900 and " + LocalDate.now().getYear());
        }
        return trackDao.getTracksByYear(year);
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
