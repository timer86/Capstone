package DAO;

import Track.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class inMemoryTrackDAO implements TrackDAO{
//a
    private final Map<String, Track> trackMap = new HashMap<>();

    @Override
    public Track createTrack(Track track) {
        if (trackMap.containsKey(track.getId())) {
            throw new IllegalArgumentException("Track already exists" + track.getId() + "already Exist");
        }
        trackMap.put(track.getId(), track);
        return track;
    }

    @Override
    public Track getTrackById(String id) {
        return trackMap.get(id);
    }

    @Override
    public List<Track> getAllTracks() {
        return new ArrayList<>(trackMap.values());
    }

    @Override
    public Track updateTrack(Track track){
        if (!trackMap.containsKey(track.getId())) {
            throw new IllegalArgumentException("Track does not exists" + track.getId() + "already Exist");
        }
        trackMap.put(track.getId(), track);
        return track;
    }

    public boolean deleteTrack(String id){
        Track removed = trackMap.remove(id);
        return removed != null;
    }
}
