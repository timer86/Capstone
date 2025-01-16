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


public class ArtistServiceTest {
    private TrackDAO trackDAO;
    private ArtistDAO artistDao;
    private ArtistService artistService;

    @BeforeEach

    public void setUp() {
        //create the Mock per DAO
        trackDAO =Mockito.mock(TrackDAO.class);
        artistDao = Mockito.mock(ArtistDAO.class);

        //inject the mock
        artistService = new ArtistService(trackDAO, artistDao);
    }

    @Test
    public void testCreateArtist_Success() {
        String artistId = "A001";
        String name = "System of a Down";
        String genre = "Rock";
        List<String> trackIds = List.of("T001");

        // Mock of track exist
        when(trackDAO.getTrackById("T001")).thenReturn(new Track("T001", 2001,"Rock", "Toxicity", "Toxicity"));

        // Mock of Artist DAO with log
        when(artistDao.createArtist(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test of method
        Artist createdArtist = artistService.createArtist(artistId, name, genre, trackIds);

        // controls
        assertNotNull(createdArtist, "The created Artist should not be null");
        assertEquals(artistId, createdArtist.getId());
        assertEquals(name, createdArtist.getName());
        assertEquals(genre, createdArtist.getGenre());

        // control that the moks methods are correctly called
          verify(artistDao).createArtist(any(Artist.class));
        verify(trackDAO).getTrackById("T001");
    }

    @Test
    public void testCreateArtist_FailsifTrack() {
        String artistId = "A002";
        String name = "Madonna";
        String genre = "Pop";
        List<String> trackIds = List.of("T999");

        //Mock: Track don't exist
        when(trackDAO.getTrackById("T999")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.createArtist(artistId, name, genre,trackIds)
        );

        //Verify the message
        assertEquals("The Track with this ID: T999 does not exist", exception.getMessage());
        verifyNoInteractions(artistDao);
    }

}
