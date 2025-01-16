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
        Track trackToSave = new Track(trackId, year, genre, album, title);
        trackToSave.setArtistIds(artistIds);
        when(trackDAO.createTrack(any(Track.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test del metodo
        Track createdTrack = trackService.createTrack(trackId, year, genre, album, title, artistIds);

        // Verifiche
        assertNotNull(createdTrack);
        assertEquals(trackId, createdTrack.getId());
        assertEquals(artistIds, createdTrack.getArtistIds());

        // Verifica che ogni artista sia stato aggiornato esattamente una volta
        verify(artistDao).getArtistById("A001");
        verify(artistDao).getArtistById("A002");
        verify(artistDao, times(1)).updateArtist(argThat(artist -> artist.getTrackIds().contains(trackId)));
        verify(artistDao, times(1)).updateArtist(argThat(artist -> artist.getId().equals("A001")));
        verify(artistDao, times(1)).updateArtist(argThat(artist -> artist.getId().equals("A002")));

        // Verifica che il track sia stato salvato
        verify(trackDAO).createTrack(any(Track.class));
    }





}
