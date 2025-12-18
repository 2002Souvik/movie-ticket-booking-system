package repository;

import model.Show;
import java.util.*;

public class ShowRepository {
    private Map<String, Show> shows;
    private Map<String, List<Show>> movieShows; // movieId -> list of shows
    
    public ShowRepository() {
        this.shows = new HashMap<>();
        this.movieShows = new HashMap<>();
    }
    
    public void addShow(Show show) {
        shows.put(show.getId(), show);
        String movieId = show.getMovie().getId();
        movieShows.computeIfAbsent(movieId, k -> new ArrayList<>()).add(show);
    }
    
    public Show getShow(String id) {
        return shows.get(id);
    }
    
    public List<Show> getShowsByMovie(String movieId) {
        return movieShows.getOrDefault(movieId, new ArrayList<>());
    }
    
    public List<Show> getAllShows() {
        return new ArrayList<>(shows.values());
    }
}