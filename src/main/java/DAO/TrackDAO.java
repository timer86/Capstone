package DAO;
import DAO.TrackDAO;
import Track.Track;

import java.util.*;

public interface TrackDAO {
    Track createTrack(Track track);
    Track getTrackById(String id);
    List<Track> getAllTracks();
    Track updateTrack(Track track);
    boolean deleteTrack(String id);
}



