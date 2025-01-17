/**
 * Added Francesco Cao 17/01/2025 15:43

 */

package Service;

import DAO.TrackDAO;
import DAO.ArtistDAO;
import Artist.Artist;
import Track.Track;
import Track.MusicGenres;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import java.util.ArrayList;


public class TrackServiceTest {
    private TrackDAO trackDAO;
    private ArtistDAO artistDao;
    private TrackService trackService;

    @BeforeEach

    public void setUp() {
        //create the Mock per DAO
        trackDAO =Mockito.mock(TrackDAO.class);
        artistDao = Mockito.mock(ArtistDAO.class);

        //inject the mock
        trackService = new TrackService(trackDAO, artistDao);
    }


    @Test
    public void testGetGenreByTrackId_Success() {
        // Mock del track DAO
        Track mockTrack = new Track("T001", 2023, "Rock", "Rock Album", "Best Song", List.of("A001"));
        when(trackDAO.getTrackById("T001")).thenReturn(mockTrack);

        // Metodo da testare
        String genre = trackService.getGenreByTrackId("T001");

        // Verifica
        assertEquals("Rock", genre);
        verify(trackDAO).getTrackById("T001");
    }


    @Test
    public void testCreateTrack_Success() {
        // Dati di esempio
        int year = 2022;
        String id = "T001";
        String genre = "Rock";
        String album = "Rock Album";
        String title = "Rock Song";
        List<String> artistIds = List.of("A001");

        // Mock dell'artista esistente
        when(artistDao.getArtistById("A001")).thenReturn(new Artist("A001", "Bon Jovi", "Rock",new ArrayList<>()));

        // Mock del track DAO con log
        when(trackDAO.createTrack(any(Track.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test del metodo
        Track createdTrack = trackService.createTrack(id, year, genre, album, title, artistIds);

        // Verifiche
        assertNotNull(createdTrack, "The created track should not be null");
        assertNotNull(createdTrack.getId(), "The track ID should not be null");
        assertTrue(createdTrack.getId().startsWith("T001"), "The track ID should start with 'T001_'");
        assertEquals(year, createdTrack.getYear());
        assertEquals(genre, createdTrack.getGenre());
        assertEquals(album, createdTrack.getAlbum());
        assertEquals(title, createdTrack.getTitle());
        assertEquals(artistIds, createdTrack.getArtistIds());

        // Verifica che i metodi mock siano stati chiamati
        verify(artistDao).getArtistById("A001");
        verify(trackDAO).createTrack(any(Track.class));
    }


    @Test
    public void testCreateTrack_FailsifArtist() {
        String trackId = "T002";
        int year = 2021;
        String genre = "Pop";
        String album = "Maverick";
        String title = "Hold My Hands";
        List<String> artistIds = List.of("A999");

        //Mock: Artist don't exist
        when(artistDao.getArtistById("A999")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack(trackId, year, genre, album, title, artistIds)
        );

        //Verify the message
        assertEquals("The Artist with this ID: A999 does not exist", exception.getMessage());
        verifyNoInteractions(trackDAO);
    }




    @Test
    public void testCreateTrack_WithMultipleArtists() {
        // Dati di esempio
        int year = 2022;
        String id = "T001";
        String genre = "Rock";
        String album = "Rock Album";
        String title = "Rock Song";
        List<String> artistIds = List.of("A001", "A002");

        // Mock degli artisti esistenti
        when(artistDao.getArtistById("A001")).thenReturn(new Artist("A001", "Artist 1", "Rock",new ArrayList<>()));
        when(artistDao.getArtistById("A002")).thenReturn(new Artist("A002", "Artist 2", "Rock",new ArrayList<>()));

        // Mock del track DAO
        when(trackDAO.createTrack(any(Track.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test del metodo
        Track createdTrack = trackService.createTrack(id, year, genre, album, title, artistIds);

        // Verifiche
        assertNotNull(createdTrack, "The created track should not be null");
        assertNotNull(createdTrack.getId(), "The track ID should not be null");
        System.out.println(createdTrack.getId());
        assertTrue(createdTrack.getId().startsWith("T001"), "The track ID should start with 'ROCKSONG_'");
        assertEquals(artistIds, createdTrack.getArtistIds());
        assertEquals(year, createdTrack.getYear());
        assertEquals(genre, createdTrack.getGenre());
        assertEquals(album, createdTrack.getAlbum());
        assertEquals(title, createdTrack.getTitle());

        // Verifica che ogni artista sia stato aggiornato esattamente una volta
        verify(artistDao).getArtistById("A001");
        verify(artistDao).getArtistById("A002");
        verify(artistDao, times(1)).updateArtist(argThat(artist -> artist.getId().equals("A001")));
        verify(artistDao, times(1)).updateArtist(argThat(artist -> artist.getId().equals("A002")));

        // Verifica che il track sia stato salvato
        verify(trackDAO).createTrack(any(Track.class));
    }


    @Test
    public void testCreateTrack_InvalidYear_ThrowsException() {
        // Dati non validi
        String trackId = "T003";
        int invalidYear = 1800; // Anno non valido
        String genre = "Rock";
        String album = "Classic Album";
        String title = "Classic Song";
        List<String> artistIds = List.of("A001");

        // Test del metodo e verifica che venga lanciata un'eccezione
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack(trackId, invalidYear, genre, album, title, artistIds)
        );

        // Stampa il messaggio dell'eccezione
        System.out.println("Exception message: " + exception.getMessage());

        // Verifica del messaggio di errore
        assertEquals("The Year cannot be less than 1900", exception.getMessage());
    }


    @Test
    public void testDeleteTrack_Success() {
        // Mock del DAO
        String trackId = "T001";
        when(trackDAO.deleteTrack(trackId)).thenReturn(true);

        // Test del metodo
        boolean result = trackService.deleteTrack(trackId);

        // Verifiche
        assertTrue(result);
        verify(trackDAO).deleteTrack(trackId);
    }

    @Test
    public void testDeleteTrack_Fails() {
        // Mock del DAO
        String trackId = "T999";
        when(trackDAO.deleteTrack(trackId)).thenReturn(false);

        // Test del metodo
        boolean result = trackService.deleteTrack(trackId);

        // Verifiche
        assertFalse(result);
        verify(trackDAO).deleteTrack(trackId);
    }

    @Test
    public void testUpdateTrack_Success() {
        Track track = new Track("T001", 2022, "Rock", "Rock Album", "Rock Song", List.of("A001"));
        when(trackDAO.updateTrack(track)).thenReturn(track);

        Track updatedTrack = trackService.updateTrack(track);

        assertNotNull(updatedTrack);
        assertEquals("T001", updatedTrack.getId());
    }

    @Test
    public void testGetAllTracks_EmptyList() {
        when(trackDAO.getAllTracks()).thenReturn(List.of());

        // Test del metodo
        List<Track> allTracks = trackService.getAllTracks();

        // Verifica
        assertNotNull(allTracks);
        assertTrue(allTracks.isEmpty());
    }

    @Test
    public void testGetAllTracks_WithTracks() {
        List<Track> tracks = List.of(
                new Track("T001", 2022, "Rock", "Album 1", "Song 1", List.of("A001")),
                new Track("T002", 2021, "Pop", "Album 2", "Song 2", List.of("A002"))
        );
        when(trackDAO.getAllTracks()).thenReturn(tracks);

        List<Track> allTracks = trackService.getAllTracks();

        assertNotNull(allTracks);
        assertEquals(2, allTracks.size());
    }

    @Test
    public void testCreateTrack_ArgumentCaptor() {
        // Dati di esempio
        int year = 2022;
        String id = "T001";
        String genre = "Rock";
        String album = "Rock Album";
        String title = "Rock Song";
        List<String> artistIds = List.of("A001");

        // Mock degli artisti esistenti
        when(artistDao.getArtistById("A001")).thenReturn(new Artist("A001", "Bon Jovi", "Rock", new ArrayList<>()));

        // Mock del track DAO
        when(trackDAO.createTrack(any(Track.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test del metodo
        trackService.createTrack(id,year, genre, album, title, artistIds);

        // Cattura l'argomento passato al DAO
        ArgumentCaptor<Track> trackCaptor = ArgumentCaptor.forClass(Track.class);
        verify(trackDAO).createTrack(trackCaptor.capture());

        // Verifiche
        Track capturedTrack = trackCaptor.getValue();
        assertNotNull(capturedTrack, "The captured track should not be null");
        assertNotNull(capturedTrack.getId(), "The track ID should not be null");
        assertTrue(capturedTrack.getId().startsWith("T001"), "The track ID should start with 'ROCKSONG_'");
        assertEquals(year, capturedTrack.getYear());
        assertEquals(genre, capturedTrack.getGenre());
        assertEquals(album, capturedTrack.getAlbum());
        assertEquals(title, capturedTrack.getTitle());
        assertEquals(artistIds, capturedTrack.getArtistIds());
    }

    @Test
    public void testGetArtistById_Success() {
        // Dati di esempio
        String artistId = "A001";
        Artist mockArtist = new Artist(artistId, "Bon Jovi", "Rock", new ArrayList<>());
        when(artistDao.getArtistById(artistId)).thenReturn(mockArtist);

        // Test del metodo
        Artist retrievedArtist = artistDao.getArtistById(artistId);

        // Verifiche
        assertNotNull(retrievedArtist, "The retrieved artist should not be null");
        assertEquals(artistId, retrievedArtist.getId());
        assertEquals("Bon Jovi", retrievedArtist.getName());
        assertEquals("Rock", retrievedArtist.getGenre());
    }

    @Test
    public void testGetTrackById_Success() {
        String trackId = "T001";
        Track mockTrack = new Track(trackId, 2022, "Rock", "Rock Album", "Rock Song", List.of("A001"));
        when(trackDAO.getTrackById(trackId)).thenReturn(mockTrack);

        Track retrievedTrack = trackService.getTrackbyId(trackId);

        assertNotNull(retrievedTrack);
        assertEquals(trackId, retrievedTrack.getId());
        assertEquals("Rock", retrievedTrack.getGenre());
        assertEquals("Rock Song", retrievedTrack.getTitle());
    }

    @Test
    public void testGetTracksByArtistId_Success() {
        when(trackDAO.getAllTracks()).thenReturn(List.of(
                new Track("T001", 2023, "Rock", "Album 1", "Song 1", List.of("A001", "A002")),
                new Track("T002", 2024, "Rock", "Album 2", "Song 2", List.of("A001"))
        ));

        List<Track> tracks = trackService.getTracksByArtistID("A001");

        assertNotNull(tracks);
        assertEquals(2, tracks.size());
        assertTrue(tracks.stream().allMatch(track -> track.getArtistIds().contains("A001")));
    }

    @Test
    public void testCreateTrack_YearBefore1900_ThrowsException() {
        String trackId = "T003";
        int invalidYear = 1800; // Valore prima del 1900
        String genre = "Rock";
        String album = "Album 1";
        String title = "Title 1";
        List<String> artistIds = List.of("A001");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack(trackId, invalidYear, genre, album, title, artistIds)
        );

        assertEquals("The Year cannot be less than 1900", exception.getMessage());
    }

    @Test
    public void testCreateTrack_YearInFuture_ThrowsException() {
        String trackId = "T004";
        int futureYear = 3000; // Valore futuro
        String genre = "Pop";
        String album = "Album 2";
        String title = "Title 2";
        List<String> artistIds = List.of("A002");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack(trackId, futureYear, genre, album, title, artistIds)
        );

        assertEquals("The Year cannot be in the Future", exception.getMessage());
    }





    @Test
    public void testCreateTrack_AutoGeneratedId() {
        // Mock dell'artista esistente
        when(artistDao.getArtistById("A001")).thenReturn(new Artist("A001", "Artist 1", "Rock", new ArrayList<>()));

        // Mock del DAO delle tracce
        when(trackDAO.createTrack(any(Track.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Metodo da testare
        Track createdTrack = trackService.createTrack(null, 2023, "Rock", "Rock Album", "Best Song Ever", List.of("A001"));

        // Verifica che l'ID sia stato generato correttamente
        System.out.println(createdTrack.getId());
        assertNotNull(createdTrack.getId(), "The track ID should not be null");
        assertTrue(createdTrack.getId().startsWith("BESTSONGEVER_"), "The track ID should start with 'BESTSONGEVER_'");

        // Debug opzionale
        System.out.println("Generated Track ID: " + createdTrack.getId());
    }




    @Test
    public void testArtistTrackRelation_Success() {
        // Mock delle tracce e degli artisti
        ArtistService artistService = new ArtistService(trackDAO, artistDao);

        // Genera ID per la traccia e l'artista
        String generatedTrackId = "SONG1_1234567890"; // Puoi generare dinamicamente l'ID
        String generatedArtistId = "SYSTEMOFADOWN_1234567890";

        // Mock delle tracce e degli artisti con ID generati
        when(trackDAO.getTrackById(generatedTrackId)).thenReturn(new Track(generatedTrackId, 2023, "Rock", "Album 1", "Song 1", new ArrayList<>()));
        when(artistDao.getArtistById(generatedArtistId)).thenReturn(new Artist(generatedArtistId, "System of a Down", "Rock", new ArrayList<>()));

        // Configura il mock del DAO per creare artisti
        when(artistDao.createArtist(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Configura il mock del DAO per creare tracce
        when(trackDAO.createTrack(any(Track.class))).thenAnswer(invocation -> {
            Track track = invocation.getArgument(0);
            track.setArtistIds(new ArrayList<>(track.getArtistIds()));
            return track;
        });

        // Metodo da testare
        Artist createdArtist = artistService.createArtist(null, "System of a Down", "Rock", List.of(generatedTrackId));
        Track createdTrack = trackService.createTrack(null, 2023, "Rock", "Album 1", "Song 1", List.of(generatedArtistId));

        // Verifica la relazione
        assertNotNull(createdTrack, "The created track should not be null");
        assertNotNull(createdArtist, "The created artist should not be null");
        assertTrue(createdTrack.getArtistIds().contains(generatedArtistId), "The track should contain the artist ID");
        assertTrue(createdArtist.getTrackIds().contains(generatedTrackId), "The artist should contain the track ID");
    }
//1



    @Test
    public void testFindTrackByTitle_Success() {
        // Mock delle tracce
        TrackService trackService = new TrackService(trackDAO, artistDao);

        when(trackDAO.getAllTracks()).thenReturn(List.of(
                new Track("T001", 2023, "Rock", "Album 1", "Chop Suey", List.of("A001")),
                new Track("T002", 2023, "Rock", "Album 2", "Toxicity", List.of("A001"))
        ));

        // Metodo da testare
        Track track = trackService.getTrackByTitle("Chop Suey");

        // Verifiche
        assertNotNull(track);
        assertEquals("Chop Suey", track.getTitle());
        assertEquals("Rock", track.getGenre());
    }

}
