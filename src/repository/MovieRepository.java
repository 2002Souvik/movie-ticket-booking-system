package repository;

import model.Movie;
import java.util.*;

public class MovieRepository {
    private Map<String, Movie> movies;
    
    public MovieRepository() {
        this.movies = new HashMap<>();
    }
    
    public void addMovie(Movie movie) {
        movies.put(movie.getId(), movie);
    }
    
    public Movie getMovie(String id) {
        return movies.get(id);
    }
    
    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies.values());
    }
    
    public List<Movie> searchMovies(String keyword) {
        List<Movie> result = new ArrayList<>();
        for (Movie movie : movies.values()) {
            if (movie.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                movie.getGenre().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(movie);
            }
        }
        return result;
    }
}