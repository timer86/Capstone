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
    public void testCreateTrack_Success() {
        // Dati di esempio
        String trackId = "T001";
        int year = 2022;
        String genre = "Rock";
        String album = "Rock Album";
        String title = "Rock Song";
        List<String> artistIds = List.of("A001");

        // Mock dell'artista esistente
        when(artistDao.getArtistById("A001")).thenReturn(new Artist("A001", "Bon Jovi", "Rock"));

        // Mock del track DAO con log
        when(trackDAO.createTrack(any(Track.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test del metodo
        Track createdTrack = trackService.createTrack(trackId, year, genre, album, title, artistIds);

        // Verifiche
        assertNotNull(createdTrack, "The created track should not be null");
        assertEquals(trackId, createdTrack.getId());
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
        String trackId = "T001";
        int year = 2022;
        String genre = "Rock";
        String album = "Rock Album";
        String title = "Rock Song";
        List<String> artistIds = List.of("A001", "A002");

        // Mock degli artisti esistenti
        when(artistDao.getArtistById("A001")).thenReturn(new Artist("A001", "Artist 1", "Rock"));
        when(artistDao.getArtistById("A002")).thenReturn(new Artist("A002", "Artist 2", "Rock"));

        // Mock del track DAO
        when(trackDAO.createTrack(any(Track.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test del metodo
        Track createdTrack = trackService.createTrack(trackId, year, genre, album, title, artistIds);

        // Verifiche
        assertNotNull(createdTrack, "The created track should not be null");
        assertEquals(trackId, createdTrack.getId());
        assertEquals(artistIds, createdTrack.getArtistIds());

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
        // Dati di esempio
        Track track = new Track("T001", 2022, "Rock", "Rock Album", "Rock Song");
        when(trackDAO.updateTrack(track)).thenReturn(track);

        // Test del metodo
        Track updatedTrack = trackService.updateTrack(track);

        // Verifiche
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
                new Track("T001", 2022, "Rock", "Album 1", "Song 1"),
                new Track("T002", 2021, "Pop", "Album 2", "Song 2")
        );
        when(trackDAO.getAllTracks()).thenReturn(tracks);

        // Test del metodo
        List<Track> allTracks = trackService.getAllTracks();

        // Verifica
        assertNotNull(allTracks);
        assertEquals(2, allTracks.size());
    }

    @Test
    public void testCreateTrack_ArgumentCaptor() {
        // Dati di esempio
        String trackId = "T001";
        int year = 2022;
        String genre = "Rock";
        String album = "Rock Album";
        String title = "Rock Song";
        List<String> artistIds = List.of("A001");

        // Mock degli artisti esistenti
        when(artistDao.getArtistById("A001")).thenReturn(new Artist("A001", "Bon Jovi", "Rock"));

        // Mock del track DAO
        when(trackDAO.createTrack(any(Track.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test del metodo
        trackService.createTrack(trackId, year, genre, album, title, artistIds);

        // Cattura l'argomento passato al DAO
        ArgumentCaptor<Track> trackCaptor = ArgumentCaptor.forClass(Track.class);
        verify(trackDAO).createTrack(trackCaptor.capture());

        // Verifiche
        Track capturedTrack = trackCaptor.getValue();
        assertNotNull(capturedTrack);
        assertEquals(trackId, capturedTrack.getId());
        assertEquals(artistIds, capturedTrack.getArtistIds());
    }

    @Test
    public void testGetArtistById_Success() {
        // Dati di esempio
        String artistId = "A001";
        Artist mockArtist = new Artist(artistId, "Bon Jovi", "Rock");
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
        // Dati di esempio
        String trackId = "T001";
        Track mockTrack = new Track(trackId, 2022, "Rock", "Rock Album", "Rock Song");
        when(trackDAO.getTrackById(trackId)).thenReturn(mockTrack);

        // Test del metodo
        Track retrievedTrack = trackService.getTrackbyId(trackId);

        // Verifiche
        assertNotNull(retrievedTrack, "The retrieved track should not be null");
        assertEquals(trackId, retrievedTrack.getId());
        assertEquals("Rock", retrievedTrack.getGenre());
        assertEquals("Rock Song", retrievedTrack.getTitle());
    }

    @Test
    public void testGetTracksByArtist_Success() {
        // Dati di esempio
        String artistId = "A001";
        List<String> trackIds = List.of("T001", "T002");
        Artist mockArtist = new Artist(artistId, "Bon Jovi", "Rock");
        mockArtist.setTrackIds(trackIds);

        // Mock dell'artista e delle tracce
        when(artistDao.getArtistById(artistId)).thenReturn(mockArtist);
        when(trackDAO.getTrackById("T001")).thenReturn(new Track("T001", 2022, "Rock", "Rock Album", "Song 1"));
        when(trackDAO.getTrackById("T002")).thenReturn(new Track("T002", 2021, "Rock", "Rock Album", "Song 2"));

        // Test del metodo
        Artist artist = artistDao.getArtistById(artistId);
        List<Track> associatedTracks = artist.getTrackIds().stream()
                .map(trackDAO::getTrackById)
                .toList();

        // Verifiche
        assertNotNull(associatedTracks, "The list of associated tracks should not be null");
        assertEquals(2, associatedTracks.size());
        assertEquals("T001", associatedTracks.get(0).getId());
        assertEquals("T002", associatedTracks.get(1).getId());
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







}
