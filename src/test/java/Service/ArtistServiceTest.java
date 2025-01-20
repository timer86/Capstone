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
        //assertEquals(List.of("T001","T002"),createdartist.getTrackIds());
        assertTrue(createdartist.getTrackIds().contains("T001"));
        assertTrue(createdartist.getTrackIds().contains("T002"));
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
        assertEquals("A0011", createdartist1.getId());
        assertEquals("Adriano Celentano", createdartist1.getName());
        assertEquals("Neomelodic", createdartist1.getGenre());
        //assertEquals(List.of("T003", "T004"), createdartist1.getTrackIds());
        assertTrue(createdartist1.getTrackIds().contains("T003"));
        assertTrue(createdartist1.getTrackIds().contains("T004"));

        verify(trackDao).updateTrack(argThat(track -> track.getId().contains("T003") && track.getArtistIds().contains("A0011")));
        verify(trackDao).updateTrack(argThat(track -> track.getId().contains("T004") && track.getArtistIds().contains("A0011")));
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
    public void testGetArtistById_Success() {
        // Dati di esempio
        String artistId = "A001";
        Artist mockArtist = new Artist(artistId, "Bon Jovi", "Rock", new ArrayList<>());
        when(artistDAO.getArtistById(artistId)).thenReturn(mockArtist);

        // Test del metodo
        Artist retrievedArtist = artistDAO.getArtistById(artistId);

        // Verifiche
        assertNotNull(retrievedArtist, "The retrieved artist should not be null");
        assertEquals(artistId, retrievedArtist.getId());
        assertEquals("Bon Jovi", retrievedArtist.getName());
        assertEquals("Rock", retrievedArtist.getGenre());
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
        System.out.println(createdArtist.getId());
        assertNotNull(createdArtist.getId());
        assertTrue(createdArtist.getId().startsWith("A001"));
    }

    @Test
    public void testCreateArtist_DuplicateId_ThrowsException() {
        String artistId = "A001";
        when(artistDAO.getArtistById(artistId)).thenReturn(new Artist(artistId, "Existing Artist", "Rock", new ArrayList<>()));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.createArtist(artistId, "New Artist", "Rock", List.of("T001"))
        );

        assertEquals("Artist with ID A001 already exists", exception.getMessage());
        verify(artistDAO).getArtistById(artistId);
    }


    @Test
    public void testCreateArtist_TrackIdDoesNotExist_ThrowsException() {
        String trackId = "T999";
        when(trackDao.getTrackById(trackId)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.createArtist("A001", "Artist Name", "Rock", List.of(trackId))
        );

        assertEquals("The Track with ID: " + trackId + " does not exist", exception.getMessage());
    }

    @Test
    public void testGetAllArtists_Success() {
        List<Artist> mockArtists = List.of(
                new Artist("A001", "Artist 1", "Rock", new ArrayList<>()),
                new Artist("A002", "Artist 2", "Pop", new ArrayList<>())
        );
        when(artistDAO.getAllArtists()).thenReturn(mockArtists);

        List<Artist> result = artistService.getAllArtists();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("A001", result.get(0).getId());
        verify(artistDAO).getAllArtists();
    }

    @Test
    public void testDeleteArtist_ArtistDoesNotExist() {
        String artistId = "A001";
        when(artistDAO.getArtistById(artistId)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.deleteArtist(artistId)
        );

        assertEquals("Artist with ID " + artistId + " does not exist", exception.getMessage());
    }

    @Test
    public void testCreateArtist_EmptyTrackIds_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.createArtist("A001", "System of a Down", "Rock", List.of())
        );
        assertEquals("Track IDs cannot be null or empty", exception.getMessage());
    }


    @Test
    public void testGetGenreByArtistId_Success() {
        Artist mockArtist = new Artist("A001", "Queen", "Rock", List.of("T001", "T002"));
        when(artistDAO.getArtistById("A001")).thenReturn(mockArtist);

        String genre = artistService.getGenreById("A001");

        assertEquals("Rock", genre, "The genre should be Rock");
        verify(artistDAO).getArtistById("A001");
    }

    @Test
    public void testGetGenreByArtistId_Fails() {
        when(artistDAO.getArtistById("A999")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.getGenreById("A999")
        );

        assertEquals("Artist with ID A999 does not exist", exception.getMessage());
        verify(artistDAO).getArtistById("A999");
    }


    @Test
    public void testGetTracksByArtistId_Success() {
        // Mock dell'artista con tracce associate
        when(artistDAO.getArtistById("A001"))
                .thenReturn(new Artist("A001", "Bon Jovi", "Rock", List.of("T001", "T002")));

        // Mock delle tracce
        when(trackDao.getTrackById("T001"))
                .thenReturn(new Track("T001", 2023, "Rock", "Album 1", "Song 1", List.of("A001")));
        when(trackDao.getTrackById("T002"))
                .thenReturn(new Track("T002", 2022, "Rock", "Album 2", "Song 2", List.of("A001")));

        // Metodo da testare
        List<Track> tracks = artistService.getTracksByArtistId("A001");

        // Verifica
        assertNotNull(tracks, "The list of tracks should not be null");
        assertEquals(2, tracks.size(), "The number of tracks should be 2");
        assertEquals("T001", tracks.get(0).getId(), "First track ID should be T001");
        assertEquals("T002", tracks.get(1).getId(), "Second track ID should be T002");

        // Verifica chiamate ai mock
        verify(artistDAO).getArtistById("A001");
        verify(trackDao).getTrackById("T001");
        verify(trackDao).getTrackById("T002");
    }



    @Test
    public void testGetTracksByArtistId_Fails_WhenArtistNotFound() {
        when(artistDAO.getArtistById("A999")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.getTracksByArtistId("A999")
        );

        assertEquals("Artist with ID A999 does not exist", exception.getMessage());
        verify(artistDAO).getArtistById("A999");
    }

    @Test
    public void testGetTracksByArtistId_Fails_WhenNoTracksFound() {
        when(artistDAO.getArtistById("A001"))
                .thenReturn(new Artist("A001", "Bon Jovi", "Rock", List.of()));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.getTracksByArtistId("A001")
        );

        assertEquals("No tracks found for artist with ID A001", exception.getMessage());
        verify(artistDAO).getArtistById("A001");
    }

    @Test
    public void testGetArtistDetailsById_Success() {
        // Mock dell'artista esistente
        Artist mockArtist = new Artist("A001", "Bon Jovi", "Rock", List.of("T001", "T002"));
        when(artistDAO.getArtistById("A001")).thenReturn(mockArtist);

        // Metodo da testare
        Artist artist = artistService.getArtistDetailsById("A001");

        // Verifiche
        assertNotNull(artist, "The retrieved artist should not be null");
        assertEquals("A001", artist.getId());
        assertEquals("Bon Jovi", artist.getName());
        assertEquals("Rock", artist.getGenre());
        assertEquals(List.of("T001", "T002"), artist.getTrackIds());

        // Verifica che il metodo sia stato chiamato correttamente
        verify(artistDAO).getArtistById("A001");
    }

    @Test
    public void testGetArtistDetailsById_Fails() {
        // Simulazione di artista non esistente
        when(artistDAO.getArtistById("INVALID_ID")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            artistService.getArtistDetailsById("INVALID_ID");
        });

        assertEquals("Artist with ID INVALID_ID does not exist", exception.getMessage());
        verify(artistDAO).getArtistById("INVALID_ID");
    }


    @Test
    public void testFindArtistIDByTitle_Success() {
        ArtistService artistService1  = new ArtistService(trackDao, artistDAO);
        when(artistDAO.getAllArtists()).thenReturn(List.of(
                new Artist("A001","Giorgio","Rock", List.of("T001")),
                new Artist("A002", "Peppuzzo", "Metal", List.of("T002"))
        ));
        Artist artist  = artistService1.getArtistByName("Giorgio");

        assertNotNull(artist, "The retrieved artist should not be null");
        assertEquals("A001", artist.getId());

    }

    @Test
    public void testFindArtistID_NullorEmptyTitle_ThrowsException(){
        Exception exception = assertThrows(IllegalArgumentException.class, () -> artistService.getArtistByName(null));
        assertEquals("Artist name cannot be null or empty",exception.getMessage());
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> artistService.getArtistByName(" "));
        assertEquals("Artist name cannot be null or empty",exception.getMessage());

    }

    @Test
    public void testFindArtistID_notFound(){
        when(artistDAO.getAllArtists()).thenReturn(List.of(new Artist("A001","Giorgio","Rock", List.of("T001"))));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> artistService.getArtistByName("Pluffy"));
        assertEquals("Artist with name Pluffy does not exist", exception.getMessage());
        verify(artistDAO).getAllArtists();
    }

    @Test
    public void testGetArtistsByGenre_Success() {
        when(artistDAO.getAllArtists()).thenReturn(List.of(
                new Artist("A001", "Bon Jovi", "Rock", List.of("T001")),
                new Artist("A002", "Elvis Presley", "Rock", List.of("T002")),
                new Artist("A003", "Mozart", "Classical", List.of("T003"))
        ));

        List<Artist> artists = artistService.getArtistsByGenre("Rock");

        assertEquals(2, artists.size());
        assertEquals("Bon Jovi", artists.get(0).getName());
    }

    @Test
    public void testGetArtistsByGenre_NoArtistsFound() {
        when(artistDAO.getAllArtists()).thenReturn(List.of());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.getArtistsByGenre("Pop")
        );

        assertEquals("No artists found for genre: Pop", exception.getMessage());
    }

    @Test
    public void testGetArtistsByGenre_NullOrEmptyGenre_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.getArtistsByGenre(null)
        );

        assertEquals("Genre cannot be null or empty", exception.getMessage());
    }



    @Test
    public void testGetArtistsByGenre_MixedCaseGenre() {
        when(artistDAO.getAllArtists()).thenReturn(List.of(
                new Artist("A001", "Queen", "Rock", List.of("T001")),
                new Artist("A002", "Bach", "Classical", List.of("T002"))
        ));

        List<Artist> artists = artistService.getArtistsByGenre("rock");

        assertEquals(1, artists.size(), "Should return 1 artist for genre 'rock'");
        assertEquals("Queen", artists.get(0).getName());
        verify(artistDAO, times(1)).getAllArtists();
    }

    @Test
    public void testGetArtistsByGenre_CaseInsensitiveMatch() {
        when(artistDAO.getAllArtists()).thenReturn(List.of(
                new Artist("A001", "Metallica", "Metal", List.of("T001")),
                new Artist("A002", "Bach", "Classical", List.of("T002"))
        ));

        List<Artist> artists = artistService.getArtistsByGenre("METAL");

        assertEquals(1, artists.size(), "Should return 1 artist for genre 'METAL' case insensitive");
        assertEquals("Metallica", artists.get(0).getName());
        verify(artistDAO, times(1)).getAllArtists();
    }

    @Test
    public void testUpdateArtist_NotFound_ThrowsException() {
        when(artistDAO.getArtistById("A001")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.updateArtist(new Artist("A001", "New Artist", "Rock", List.of("T001")))
        );

        assertEquals("Artist does not exist - A001", exception.getMessage());
        verify(artistDAO).getArtistById("A001");
    }

    @Test
    public void testDeleteArtist_Success() {
        Artist existingArtist = new Artist("A001", "Bon Jovi", "Rock", List.of("T001"));
        when(artistDAO.getArtistById("A001")).thenReturn(existingArtist);
        when(artistDAO.deleteArtist("A001")).thenReturn(true);

        boolean result = artistService.deleteArtist("A001");

        assertTrue(result);
        verify(artistDAO).deleteArtist("A001");
    }

    @Test
    public void testDeleteArtist_NotFound_ThrowsException() {
        when(artistDAO.getArtistById("A999")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.deleteArtist("A999")
        );

        assertEquals("Artist with ID A999 does not exist", exception.getMessage());
        verify(artistDAO).getArtistById("A999");
    }

    @Test
    public void testGetTracksByArtistId_ArtistNotFound_ThrowsException() {
        when(artistDAO.getArtistById("A999")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.getTracksByArtistId("A999")
        );

        assertEquals("Artist with ID A999 does not exist", exception.getMessage());
    }


    @Test
    public void testCreateArtist_EmptyOrWhitespaceName_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.createArtist("A001", "  ", "Rock", List.of("T001"))
        );
        assertEquals("Artist name cannot be null or empty", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () ->
                artistService.createArtist("A001", "", "Rock", List.of("T001"))
        );
        assertEquals("Artist name cannot be null or empty", exception.getMessage());
    }


}