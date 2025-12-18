package service;

import model.Movie;
import repository.MovieRepository;
import java.util.List;

public class MovieService {
    private MovieRepository movieRepository;
    
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    
    public void addMovie(Movie movie) {
        movieRepository.addMovie(movie);
    }
    
    public Movie getMovie(String id) {
        return movieRepository.getMovie(id);
    }
    
    public List<Movie> getAllMovies() {
        return movieRepository.getAllMovies();
    }
    
    public List<Movie> searchMovies(String keyword) {
        return movieRepository.searchMovies(keyword);
    }
}