package DAO;
import DAO.TrackDAO;
import Track.Track;

import java.util.*;
//16:04 arto
public interface TrackDAO {
    Track createTrack(Track track);
    Track getTrackById(String id);
    List<Track> getAllTracks();
    List<Track> getTracksByArtist(String artistId);
    List<Track> getTrackByTitle(String title);
    List<Track> getTracksByGenre(String genre);
    List<Track> getTracksByYear(int year);
    List<Track> getTrackByAlbum(String album);
    Track updateTrack(Track track);
    boolean deleteTrack(String id);
}



