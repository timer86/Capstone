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

import java.io.ByteArrayInputStream;
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
    public void testGetGenreByTrackId_Fail() {
        Track mockTrack = new Track("T001",2023, "Rock","Rock Album", "Best Song", List.of("A001"));
        when(trackDAO.getTrackById("T001")).thenReturn(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> trackService.getGenreByTrackId("T001"));
        assertEquals("Track with ID T001 does not exist", exception.getMessage());
        verify(trackDAO).getTrackById("T001");
    }


    @Test
    public void testCreateTrack_Success() {
        // Dati di esempio
        int year = 2022;
        String id = "T001";
        String genre = "ROCK";
        String album = "Rock Album";
        String title = "Rock Song";
        List<String> artistIds = List.of("A001");

        // Mock dell'artista esistente
        when(artistDao.getArtistById("A001")).thenReturn(new Artist("A001", "Bon Jovi", "ROCK",new ArrayList<>()));

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
    public void testCreateTrack_CreatesMissingArtists() {
        String simulatedUserInput = "New Artist\nROCK\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        String trackId = "T001";
        int year = 2023;
        String genre = "ROCK";
        String album = "Rock Album";
        String title = "Best Song Ever";
        List<String> artistIds = List.of("A001", "A002");

        when(artistDao.getArtistById("A001")).thenReturn(new Artist("A001", "Existing Artist", "ROCK", new ArrayList<>()));
        when(artistDao.getArtistById("A002")).thenReturn(null);
        when(trackDAO.createTrack(any(Track.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(artistDao.createArtist(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Track createdTrack = trackService.createTrack(trackId, year, genre, album, title, artistIds);

        assertNotNull(createdTrack);
        assertTrue(createdTrack.getArtistIds().containsAll(artistIds));

        verify(artistDao).createArtist(argThat(artist -> artist.getName().equals("New Artist") && artist.getGenre().equals("ROCK")));
        verify(trackDAO).createTrack(any(Track.class));
    }










    @Test
    public void testCreateTrack_WithMultipleArtists() {
        // Dati di esempio
        int year = 2022;
        String id = "T001";
        String genre = "ROCK";
        String album = "Rock Album";
        String title = "Rock Song";
        List<String> artistIds = List.of("A001", "A002");

        // Mock degli artisti esistenti
        when(artistDao.getArtistById("A001")).thenReturn(new Artist("A001", "Artist 1", "ROCK",new ArrayList<>()));
        when(artistDao.getArtistById("A002")).thenReturn(new Artist("A002", "Artist 2", "ROCK",new ArrayList<>()));

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

    public void testUpdateTrack_Fails() {
        Track track = new Track("T001", 2022, "Rock", "Rock Album", "Rock Song", List.of("A001"));
        when(trackDAO.updateTrack(track)).thenThrow(new IllegalArgumentException("Track with ID T001 does not exist"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> trackService.updateTrack(track));
        assertEquals("Track with ID T001 does not exist", exception.getMessage());
        verify(trackDAO).updateTrack(track);
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
        String genre = "ROCK";
        String album = "Rock Album";
        String title = "Rock Song";
        List<String> artistIds = List.of("A001");

        // Mock degli artisti esistenti
        when(artistDao.getArtistById("A001")).thenReturn(new Artist("A001", "Bon Jovi", "ROCK", new ArrayList<>()));

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
    public void testGetTrackById_Success() {
        String trackId = "T001";
        Track mockTrack = new Track(trackId, 2022, "Rock", "Rock Album", "Rock Song", List.of("A001"));
        when(trackDAO.getTrackById(trackId)).thenReturn(mockTrack);

        Track retrievedTrack = trackService.getSingleTrackbyId(trackId);

        assertNotNull(retrievedTrack);
        assertEquals(trackId, retrievedTrack.getId());
        assertEquals("Rock", retrievedTrack.getGenre());
        assertEquals("Rock Song", retrievedTrack.getTitle());
    }


    @Test
    public void testGetTrackById_Fails() {
        // Configura il mock per restituire una lista vuota
        String trackId = "T001";
        when(trackDAO.getTrackByID(trackId)).thenReturn(List.of());

        // Metodo da testare
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.getTrackById(trackId)
        );

        // Verifica del messaggio
        assertEquals("No tracks found with ID: " + trackId, exception.getMessage());

        // Verifica che il mock sia stato chiamato
        verify(trackDAO).getTrackByID(trackId);
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
    public void testgetIDYear_yearEmpty(){
        Exception exception = assertThrows(IllegalArgumentException.class, () ->trackService.getYearByTrackId(null));
        assertEquals("Track with ID null does not exist",exception.getMessage());
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
        when(artistDao.getArtistById("A001")).thenReturn(new Artist("A001", "Artist 1", "ROCK", new ArrayList<>()));

        // Mock del DAO delle tracce
        when(trackDAO.createTrack(any(Track.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Metodo da testare
        Track createdTrack = trackService.createTrack(null, 2023, "ROCK", "Rock Album", "Best Song Ever", List.of("A001"));

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
        when(artistDao.getArtistById(generatedArtistId)).thenReturn(new Artist(generatedArtistId, "System of a Down", "ROCK", new ArrayList<>()));

        // Configura il mock del DAO per creare artisti
        when(artistDao.createArtist(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Configura il mock del DAO per creare tracce
        when(trackDAO.createTrack(any(Track.class))).thenAnswer(invocation -> {
            Track track = invocation.getArgument(0);
            track.setArtistIds(new ArrayList<>(track.getArtistIds()));
            return track;
        });

        // Metodo da testare
        Artist createdArtist = artistService.createArtist(null, "System of a Down", "ROCK", List.of(generatedTrackId));
        Track createdTrack = trackService.createTrack(null, 2023, "ROCK", "Album 1", "Song 1", List.of(generatedArtistId));

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
                new Track("T001", 2023, "ROCK", "Album 1", "Chop Suey", List.of("A001")),
                new Track("T002", 2023, "ROCK", "Album 2", "Toxicity", List.of("A001"))
        ));

        // Metodo da testare
        Track track = trackService.getTrackByTitle("Chop Suey");

        // Verifiche
        assertNotNull(track);
        assertEquals("Chop Suey", track.getTitle());
        assertEquals("ROCK", track.getGenre());

    }

    @Test
    public void testGetTrackByTitle_NullorEmptyTitle_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> trackService.getTrackByTitle(null));
        assertEquals("Title cannot be null or empty", exception.getMessage());
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> trackService.getTrackByTitle(" "));
        assertEquals("Title cannot be null or empty", exception1.getMessage());
    }


    @Test
    public void testGetTrackByTitle_notFound(){
        when(trackDAO.getAllTracks()).thenReturn(List.of(new Track("T001",2023,"Rock","Rock Album", "Antother Song", List.of("A001"))));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> trackService.getTrackByTitle("Best Song"));
        assertEquals("Track with title Best Song does not exist", exception.getMessage());
        verify(trackDAO, times(1)).getAllTracks();

    }



    @Test
    public void testGetArtistsByTrackId_Success() {
        // Mock della traccia esistente
        Track mockTrack = new Track("T001", 2023, "Rock", "Rock Album", "Best Song", List.of("A001", "A002"));
        when(trackDAO.getTrackById("T001")).thenReturn(mockTrack);

        // Mock degli artisti esistenti
        Artist artist1 = new Artist("A001", "Artist 1", "Rock", new ArrayList<>());
        Artist artist2 = new Artist("A002", "Artist 2", "Pop", new ArrayList<>());
        when(artistDao.getArtistById("A001")).thenReturn(artist1);
        when(artistDao.getArtistById("A002")).thenReturn(artist2);

        // Metodo da testare
        List<Artist> artists = trackService.getArtistsByTrackId("T001");

        // Verifiche
        assertNotNull(artists, "The list of artists should not be null");
        assertEquals(2, artists.size(), "The track should have 2 associated artists");
        assertTrue(artists.contains(artist1), "The list should contain Artist 1");
        assertTrue(artists.contains(artist2), "The list should contain Artist 2");

        // Verifica dei mock
        verify(trackDAO).getTrackById("T001");
        verify(artistDao).getArtistById("A001");
        verify(artistDao).getArtistById("A002");
    }

    @Test
    public void testGetArtistsByTrackId_TrackNotFound_ThrowsException() {
        // Configura il mock per restituire null per una traccia inesistente
        when(trackDAO.getTrackById("INVALID_ID")).thenReturn(null);

        // Metodo da testare e verifica che l'eccezione venga lanciata
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.getArtistsByTrackId("INVALID_ID")
        );

        // Verifica del messaggio dell'eccezione
        assertEquals("Track with ID INVALID_ID does not exist", exception.getMessage());

        // Verifica che il mock sia stato chiamato
        verify(trackDAO).getTrackById("INVALID_ID");
    }

    @Test
    public void testGetArtistsByTrackId_ArtistNotFound_ThrowsException() {
        // Mock della traccia esistente
        Track mockTrack = new Track("T001", 2023, "Rock", "Rock Album", "Best Song", List.of("A001", "INVALID_ARTIST_ID"));
        when(trackDAO.getTrackById("T001")).thenReturn(mockTrack);

        // Mock dell'artista esistente
        when(artistDao.getArtistById("A001")).thenReturn(new Artist("A001", "Artist 1", "Rock", new ArrayList<>()));

        // Configura il mock per restituire null per l'artista inesistente
        when(artistDao.getArtistById("INVALID_ARTIST_ID")).thenReturn(null);

        // Metodo da testare e verifica che l'eccezione venga lanciata
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.getArtistsByTrackId("T001")
        );

        // Verifica del messaggio dell'eccezione
        assertEquals("Artist with ID INVALID_ARTIST_ID does not exist", exception.getMessage());

        // Verifica che il mock sia stato chiamato
        verify(trackDAO).getTrackById("T001");
        verify(artistDao).getArtistById("A001");
        verify(artistDao).getArtistById("INVALID_ARTIST_ID");
    }

    @Test
    public void testGetAlbumByTrackId_Success() {
        // Mock del track DAO
        Track mockTrack = new Track("T001", 2023, "Rock", "Rock Album", "Best Song", List.of("A001"));
        when(trackDAO.getTrackById("T001")).thenReturn(mockTrack);

        // Metodo da testare
        String album = trackService.getAlbumTrackId("T001");

        // Verifica
        assertEquals("Rock Album", album);
        verify(trackDAO).getTrackById("T001");
    }

    @Test
    public void testGetAlbumByTrackId_fail(){
    String trackId = "T001";
    when(trackDAO.getTrackById(trackId)).thenReturn(null);
    Exception exception = assertThrows(IllegalArgumentException.class, () -> trackService.getAlbumTrackId(trackId));
    assertEquals("Track with ID T001 does not exist", exception.getMessage());
    verify(trackDAO).getTrackById(trackId);
    }

    @Test
    public void testGetYearByTrack_id(){
        Track mockTrack = new Track("T001",2023, "Rock","Rock Album","Best Song", List.of("A001"));
        when(trackDAO.getTrackById("T001")).thenReturn(mockTrack);
        int year = trackService.getYearByTrackId("T001");
        assertEquals(2023, year);
        verify(trackDAO).getTrackById("T001");

    }

    @Test
    public void testGetYearByTrackId_Fails() {
        // Configura il mock per restituire null (traccia non trovata)
        String trackId = "T001";
        when(trackDAO.getTrackById(trackId)).thenReturn(null);

        // Metodo da testare
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.getYearByTrackId(trackId)
        );

        // Verifica il messaggio dell'eccezione
        assertEquals("Track with ID " + trackId + " does not exist", exception.getMessage());

        // Verifica che il mock sia stato chiamato
        verify(trackDAO).getTrackById(trackId);
    }


    @Test
    public void testCreateTrack_DuplicateId_ThrowsException() {
        String trackId = "T001";
        when(trackDAO.getTrackById(trackId)).thenReturn(new Track(trackId, 2023, "Rock", "Album 1", "Song 1", List.of("A001")));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack(trackId, 2023, "Rock", "Album 1", "Song 1", List.of("A001"))
        );

        assertEquals("Track with ID T001 already exists", exception.getMessage());
        verify(trackDAO).getTrackById(trackId);
    }

    @Test
    public void testCreateTrack_NullOrEmptyTitle_ThrowsException() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack(null, 2023, "Rock", "Rock Album", null, List.of("A001"))
        );
        assertEquals("The Title cannot be empty", exception1.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack(null, 2023, "Rock", "Rock Album", "   ", List.of("A001"))
        );
        assertEquals("The Title cannot be empty", exception2.getMessage());
    }

    @Test
    public void testCreateTrack_NullOrEmptyGenre_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack(null, 2023, null, "Rock Album", "Best Song", List.of("A001"))
        );
        assertEquals("The Genre cannot be empty", exception.getMessage());
    }


    @Test
    public void testCreateTrack_NoArtists_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack(null, 2023, "Rock", "Rock Album", "Best Song", List.of())
        );
        assertEquals("Artist IDs cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testGetTrackByTitle_NotFound_ThrowsException() {
        when(trackDAO.getAllTracks()).thenReturn(List.of());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.getTrackByTitle("Non Existent")
        );
        assertEquals("Track with title Non Existent does not exist", exception.getMessage());
    }

    @Test
    public void testUpdateTrack_Fails_ThrowsException() {
        Track track = new Track("T001", 2022, "Rock", "Rock Album", "Rock Song", List.of("A001"));
        when(trackDAO.updateTrack(track)).thenThrow(new IllegalArgumentException("Track with ID T001 does not exist"));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.updateTrack(track)
        );
        assertEquals("Track with ID T001 does not exist", exception.getMessage());
    }

    @Test
    public void testCreateTrack_InvalidYearTooEarly_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack(null, 1800, "Rock", "Rock Album", "Best Song", List.of("A001"))
        );
        assertEquals("The Year cannot be less than 1900", exception.getMessage());
    }

    @Test
    public void testCreateTrack_InvalidYearInFuture_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack(null, 3000, "Rock", "Rock Album", "Best Song", List.of("A001"))
        );
        assertEquals("The Year cannot be in the Future", exception.getMessage());
    }
    @Test
    public void testCreateTrack_NullGenre_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack("T005", 2023, null, "Rock Album", "Best Song", List.of("A001"))
        );
        assertEquals("The Genre cannot be empty", exception.getMessage());
    }

    @Test
    public void testCreateTrack_CreatesArtistFromUserInput() {
        String simulatedUserInput = "New Artist\nROCK\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        String trackId = "T005";
        int year = 2023;
        String genre = "ROCK";
        String album = "New Album";
        String title = "New Song";
        List<String> artistIds = List.of("A003");

        when(artistDao.getArtistById("A003")).thenReturn(null);
        when(trackDAO.createTrack(any(Track.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(artistDao.createArtist(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Track createdTrack = trackService.createTrack(trackId, year, genre, album, title, artistIds);

        assertNotNull(createdTrack, "The created track should not be null");
        assertEquals("New Song", createdTrack.getTitle());

        verify(artistDao).createArtist(argThat(artist -> artist.getName().equals("New Artist") && artist.getGenre().equals("ROCK")));
    }

    @Test
    public void testGetTrackById_NullId_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> trackService.getSingleTrackbyId(null));
        assertEquals("The Track with this ID: null does not exist", exception.getMessage());
    }

    @Test
    public void testCreateTrack_CreatesArtistFromUserInput_Fail_InvalidGenre() {
        String simulatedUserInput = "New Artist\nInvalidGenre\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        String trackId = "T005";
        int year = 2023;
        String genre = "ROCK";
        String album = "New Album";
        String title = "New Song";
        List<String> artistIds = List.of("A003");

        when(artistDao.getArtistById("A003")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack(trackId, year, genre, album, title, artistIds)
        );

        assertEquals("The Genre InvalidGenre is not allowed. Allowed genres: " + MusicGenres.ALLOWED_GENRES, exception.getMessage());

        verify(artistDao, never()).createArtist(any(Artist.class));
        verify(trackDAO, never()).createTrack(any(Track.class));
    }

    @Test
    public void testGetTrackById_NullOrEmptyId_ThrowsException() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () ->
                trackService.getSingleTrackbyId(null)
        );
        assertEquals("The Track with this ID: null does not exist", exception1.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () ->
                trackService.getSingleTrackbyId("")
        );
        assertEquals("The Track with this ID:  does not exist", exception2.getMessage());
    }
    @Test
    public void testGetTracksByGenre_Success() {
        List<Track> mockTracks = List.of(
                new Track("T001", 2023, "ROCK", "Rock Album", "Livin' on a Prayer", List.of("A001"))
        );
        when(trackDAO.getTracksByGenre("Rock")).thenReturn(mockTracks);

        List<Track> tracks = trackService.getTracksByGenre("Rock");

        assertEquals(1, tracks.size());
        assertEquals("Livin' on a Prayer", tracks.get(0).getTitle());
    }

    @Test
    public void testGetTracksByYear_Success() {
        List<Track> mockTracks = List.of(
                new Track("T001", 2023, "ROCK", "Rock Album", "Livin' on a Prayer", List.of("A001"))
        );
        when(trackDAO.getTracksByYear(2023)).thenReturn(mockTracks);

        List<Track> tracks = trackService.getTracksByYear(2023);

        assertEquals(1, tracks.size());
        assertEquals("Livin' on a Prayer", tracks.get(0).getTitle());
    }






    @Test
    public void testCreateTrack_InvalidGenre_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.createTrack("T001", 2023, "INVALID_GENRE", "Rock Album", "Song Title", List.of("A001"))
        );

        assertEquals("The GenreINVALID_GENRENot Present, use an other one", exception.getMessage());
    }


    @Test
    public void testGetTracksByGenre_NullGenre_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.getTracksByGenre(null)
        );
        assertEquals("Genre cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testGetTracksByGenre_EmptyGenre_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                trackService.getTracksByGenre(" ")
        );
        assertEquals("Genre cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testGetTracksByGenre_InvalidGenre_ReturnsEmptyList() {
        when(trackDAO.getTracksByGenre("UNKNOWN")).thenReturn(List.of());

        List<Track> tracks = trackService.getTracksByGenre("UNKNOWN");

        assertTrue(tracks.isEmpty(), "Expected empty list for invalid genre");
    }

    @Test
    public void testGetTracksByGenre_NoTracksFound() {
        when(trackDAO.getTracksByGenre("ROCK")).thenReturn(List.of());

        List<Track> tracks = trackService.getTracksByGenre("ROCK");

        assertTrue(tracks.isEmpty(), "Expected empty list for genre ROCK");
    }


    @Test
    public void testGetTracksByArtistID_Success() {
        Track track1 = new Track("T001", 2023, "ROCK", "Rock Album", "Livin' on a Prayer", List.of("A001"));
        Track track2 = new Track("T002", 2022, "POP", "Pop Album", "Someone Like You", List.of("A002"));

        when(trackDAO.getAllTracks()).thenReturn(List.of(track1, track2));

        List<Track> tracks = trackService.getTracksByArtistID("A001");

        assertFalse(tracks.isEmpty());
        assertEquals(1, tracks.size());
        assertEquals("Livin' on a Prayer", tracks.get(0).getTitle());

        verify(trackDAO, times(1)).getAllTracks();
    }


    @Test
    public void testGetAllTracks_Success() {
        List<Track> mockTracks = List.of(
                new Track("T001", 2023, "Rock", "Rock Album", "Livin' on a Prayer", List.of("A001")),
                new Track("T002", 2022, "Pop", "Pop Album", "Hello", List.of("A002"))
        );
        when(trackDAO.getAllTracks()).thenReturn(mockTracks);

        List<Track> tracks = trackService.getAllTracks();

        assertNotNull(tracks);
        assertEquals(2, tracks.size());
        assertEquals("Livin' on a Prayer", tracks.get(0).getTitle());
    }
}
