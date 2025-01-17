/**
 * Added Francesco Cao 17/01/2025 15:43

 */

package DAO;

import Track.Track;

import java.util.*;
import java.util.stream.Collectors;

public class inMemoryTrackDAO implements TrackDAO{
//arto
    private final List<Track> tracks = new ArrayList<>();
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

    @Override
    public List<Track> getTracksbyArtistID(String artistID){
        return tracks.stream()
                .filter(track -> track.getArtistIds().contains(artistID))
                .collect(Collectors.toList());

    }

    @Override

    public List<Track> getTrackByTitle(String title){
        return tracks.stream()
                .filter(track -> Objects.equals(track.getTitle(), title))
                .collect(Collectors.toList());
    }

    @Override
    public List<Track> getTrackByID(String id){
        return tracks.stream()
                .filter(track-> Objects.equals(track.getId(), id))
                .collect(Collectors.toList());
    }

    @Override
    public List<Track>  getTracksByYear(int year){
        return tracks.stream()
                .filter(track -> Objects.equals(track.getYear(),year))
                .collect(Collectors.toList());
    }

    @Override
    public List<Track> getTracksbyAlbum(String Album){
        return tracks.stream()
                .filter(track -> Objects.equals(track.getAlbum(), Album))
                .collect(Collectors.toList());
    }

    public List<Track> getTracksByGenre(String genre){
        return tracks.stream()
                .filter(track -> Objects.equals(track.getGenre(), genre))
                .collect(Collectors.toList());
    }

}
