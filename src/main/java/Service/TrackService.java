package Service;
import DAO.TrackDAO;
import DAO.ArtistDAO;
import Track.Track;
import Artist.Artist;

import java.util.List;


public class TrackService {
    private TrackDAO trackDao;
    private ArtistDAO artistDao;

    public TrackService(TrackDAO trackDao, ArtistDAO artistDao) {
        this.trackDao = trackDao;
        this.artistDao = artistDao;
    }

    public Track createTrack(String id, int year, String genre, String album, String title, List<String> artistIds ) {

        if (year < 1900){
           throw new IllegalArgumentException("The Year cannot be less than 1900");
        }

        if (genre == null || genre.trim().isEmpty()){
            throw new IllegalArgumentException("The Genre cannot be empty");
        }

        if (title == null || title.trim().isEmpty()){
            throw new IllegalArgumentException("The Title cannot be empty");
        }

        for (String artistId : artistIds){
            Artist artist = artistDao.getArtistnameById(artistId);
        }



    }
}
