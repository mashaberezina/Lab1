package service;

import model.Movie;
import java.util.List;
import java.util.UUID;

public interface MovieStorage {
    void save(Movie movie);
    void delete(UUID id);
    Movie findById(UUID id);
    List<Movie> getAll();
    boolean exists(UUID id);
}