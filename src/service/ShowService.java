package service;

import model.Movie;
import model.Show;
import model.Theater;
import repository.ShowRepository;
import java.time.LocalDateTime;
import java.util.List;

public class ShowService {
    private ShowRepository showRepository;
    
    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }
    
    public void createShow(String showId, Movie movie, Theater theater, 
                          Theater.Screen screen, LocalDateTime startTime, double price) {
        Show show = new Show(showId, movie, theater, screen, startTime, price);
        showRepository.addShow(show);
    }
    
    public List<Show> getShowsByMovie(String movieId) {
        return showRepository.getShowsByMovie(movieId);
    }
    
    public Show getShow(String showId) {
        return showRepository.getShow(showId);
    }
    
    public List<Show> getAllShows() {
        return showRepository.getAllShows();
    }
}