/**
 * Added Francesco Cao 17/01/2025 15:43

*/
package DAO;
import DAO.TrackDAO;
import Track.Track;

import java.util.*;
//16:04 arto
public interface TrackDAO {
    Track createTrack(Track track);
    Track getTrackById(String id);
    List<Track> getAllTracks();
    List<Track> getTracksbyArtistID(String artistId);
    List<Track> getTrackByID(String id);
    List<Track> getTrackByTitle(String title);
    List<Track> getTracksByGenre(String genre);

    List<Track> getTracksByYear(int year);
    List<Track> getTracksbyAlbum(String album);
    Track updateTrack(Track track);
    boolean deleteTrack(String id);
}



