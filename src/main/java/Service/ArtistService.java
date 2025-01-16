package Service;
import DAO.TrackDAO;
import DAO.ArtistDAO;
import Track.Track;
import Artist.Artist;
import java.util.List;

public class ArtistService {
    private TrackDAO trackDao;
    private ArtistDAO artistDao;

    public ArtistService(TrackDAO trackDao, ArtistDAO artistDao) {
        this.trackDao = trackDao;
        this.artistDao = artistDao;
    }


    public void CreateArtist(int id, String name, String genre, List<String> idTrack) {
        if (id>0) {

        }
    }
}