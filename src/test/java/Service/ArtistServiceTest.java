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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;


public class ArtistServiceTest {
    private TrackDAO trackDao;
    private ArtistDAO artistDAO;
    private ArtistService artistService;

    @BeforeEach

    public void setUp(){
        trackDao = Mockito.mock(TrackDAO.class);
        artistDAO = Mockito.mock(ArtistDAO.class);
        artistService = new ArtistService(trackDao,artistDAO);
    }


    @Test
    public void testCreateArtist_InvalidName_ThrowsException(){
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.createArtist("A001",null,"Rock",List.of("T001"))
        );

        assertEquals(exception.getMessage(), "Artist name cannot be null or empty");
    }

    @Test
    public void testCreateArtist_Invalid_Genre_ThrowsException(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> artistService.createArtist("A001","System of a Down",null,List.of("T001"))
        );
        assertEquals(exception.getMessage(), "Artist genre cannot be null or empty");
    }

    @Test
    public void testGenreNotinList_ThrowsException(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> artistService.createArtist("A001","System of a Down","ARENB",List.of("T001"))
        );
        assertEquals(exception.getMessage(), "The Genre ARENB is not allowed. Allowed genres: " + MusicGenres.ALLOWED_GENRES);
        System.out.println("The Genre ARENB is not allowed. Allowed genres: " + MusicGenres.ALLOWED_GENRES);
    }

    @Test
    public void testCreateArtist_TrackDoesNotExist_ThrowsException(){
        when(trackDao.getTrackById("T001")).thenReturn(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> artistService.createArtist("A001","System of a Down","Rock",List.of("T001"))
        );
        assertEquals(exception.getMessage(), "The Track with ID: T001 does not exist");
    }

    @Test
    public void testCreateArtist_SuccessSystem(){
        // Mock of existent Track
        when(trackDao.getTrackById("T001")).thenReturn(new Track("T001",2022,"Rock","Toxicity","Chop Suey",List.of()));
        when(trackDao.getTrackById("T002")).thenReturn(new Track("T002",2023, "Rock","Steal this Album","Innervision",List.of()));
        //Mock DAO Artist
        when(artistDAO.createArtist(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //Method Test
        Artist createdartist = artistService.createArtist("A001","System of a Down","Rock",List.of("T001","T002"));
        //Verification
        assertNotNull(createdartist);
        assertEquals("A001",createdartist.getId());
        assertEquals("System of a Down",createdartist.getName());
        assertEquals("Rock",createdartist.getGenre());
        assertEquals(List.of("T001","T002"),createdartist.getTrackIds());
        verify(trackDao).updateTrack(argThat(track -> track.getId().contains("T001") &&  track.getArtistIds().contains("A001")));
        verify(trackDao).updateTrack(argThat(track -> track.getId().contains("T002") &&  track.getArtistIds().contains("A001")));
        verify(artistDAO).createArtist(any(Artist.class));





    }

    @Test
    public void testCreateArtist_SuccessCelentano() {
        // Mock of existent Track
        when(trackDao.getTrackById("T003")).thenReturn(new Track("T003", 1950, "Neomelodic", "Una Carezza in un Pugno", "Azzurro", List.of()));
        when(trackDao.getTrackById("T004")).thenReturn(new Track("T004", 2024, "Neomelodic", "Amore No", "Amore No", List.of()));
        //Mock DAO Artist
        when(artistDAO.createArtist(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //Method Test
        Artist createdartist1 = artistService.createArtist("A0011","Adriano Celentano", "Neomelodic", List.of("T003", "T004"));


        //Verification
        assertNotNull(createdartist1);
        assertEquals("A002", createdartist1.getId());
        assertEquals("Adriano Celentano", createdartist1.getName());
        assertEquals("Neomelodic", createdartist1.getGenre());
        assertEquals(List.of("T003", "T004"), createdartist1.getTrackIds());

        verify(trackDao).updateTrack(argThat(track -> track.getId().contains("T003") && track.getArtistIds().contains("A002")));
        verify(trackDao).updateTrack(argThat(track -> track.getId().contains("T004") && track.getArtistIds().contains("A002")));
        verify(artistDAO).createArtist(any(Artist.class));

    }


    @Test

    public void testDeleteArtist_success(){
        Artist existingArtist = new Artist("A001","AC/DC","Rock", new ArrayList<>());
        existingArtist.setTrackIds(List.of("T001","T002"));

        when(artistDAO.getArtistById("A001")).thenReturn(existingArtist);
        when(artistDAO.deleteArtist("A001")).thenReturn(true);

        Track mockTrack1 = new Track("T001",2023,"Rock","Steal this Album","Innervision",List.of());
        Track mockTrack2 = new Track("T002",2024,"Rock","Toxicity","Toxicity",List.of());
        when (trackDao.getTrackById("T001")).thenReturn(mockTrack1);
        when (trackDao.getTrackById("T002")).thenReturn(mockTrack2);

        boolean result = artistService.deleteArtist("A001");

        //verification
        assertTrue(result);
        verify(trackDao, times(1)).updateTrack(argThat(track -> track.getId().equals("T001") && !track.getArtistIds().contains("A001")));
        verify(trackDao, times(1)).updateTrack(argThat(track -> track.getId().equals("T002") && !track.getArtistIds().contains("A001")));
        verify(artistDAO).deleteArtist("A001");
    }


    @Test
    public void testDeleteArtist_noTracks_Success() {
        Artist existingArtist = new Artist("A001","AC/DC","Rock", new ArrayList<>());
        existingArtist.setTrackIds(List.of());
        when(artistDAO.getArtistById("A001")).thenReturn(existingArtist);
        when(artistDAO.deleteArtist("A001")).thenReturn(true);
        boolean result = artistService.deleteArtist("A001");
        assertTrue(result);
        verifyNoMoreInteractions(trackDao);
        verify(artistDAO, times(1)).deleteArtist("A001");

    }


    @Test
    public void testDeleteArtist_ArtistdoesNotExist_success(){
        when(artistDAO.getArtistById("A001")).thenReturn(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> artistService.deleteArtist("A001"));
        assertEquals(exception.getMessage(), "Artist with ID A001 does not exist");
    }


    @Test
    public void testUpdateArtist_ArtistDoesNotExist_ThrowsException(){
        when (artistDAO.getArtistById("A001")).thenReturn(null);
        Artist artistToUpdate = new Artist("A001","AC/DC","Rock",new ArrayList<>());
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.updateArtist(artistToUpdate)
        );

        assertEquals("Artist does not exist - A001", exception.getMessage());
    }

    @Test
    public void testUpdateArtist_Success() {
        Artist existingArtist = new Artist("A001","AC/DC","Rock", new ArrayList<>());
        when(artistDAO.getArtistById("A001")).thenReturn(existingArtist);
        when(artistDAO.updateArtist(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Artist updateArtist = artistService.updateArtist(new Artist("A001", "Update Artist", "Pop",new ArrayList<>()));

        assertNotNull(updateArtist);
        assertEquals("A001",updateArtist.getId());
        assertEquals("Update Artist", updateArtist.getName());
        assertEquals("Pop", updateArtist.getGenre());
        verify(artistDAO).updateArtist(any(Artist.class));
    }



    @Test
    public void testCreateArtist_AutoGeneratedId() {
        // Mock delle tracce esistenti
        when(trackDao.getTrackById("T001")).thenReturn(new Track("T001", 2023, "Rock", "Album 1", "Song 1", List.of()));
        when(trackDao.getTrackById("T002")).thenReturn(new Track("T002", 2023, "Rock", "Album 2", "Song 2", List.of()));

        // Mock del DAO degli artisti
        when(artistDAO.createArtist(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Metodo da testare
        Artist createdArtist = artistService.createArtist("A001","System of a Down", "Rock", List.of("T001", "T002"));

        // Verifica che l'ID sia generato e abbia un prefisso corretto
        assertNotNull(createdArtist.getId());
        assertTrue(createdArtist.getId().startsWith("SYSTEMOFADOWN_"));
    }

}