package service;

import model.Movie;
import java.util.*;

public class InMemoryMovieStorage implements MovieStorage {
    private final Map<UUID, Movie> movies = new HashMap<>();

    @Override
    public void save(Movie movie) {
        if (movie == null) throw new IllegalArgumentException("Фильм не может быть null");
        movies.put(movie.getId(), movie);
    }

    @Override
    public void delete(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID не может быть null");
        movies.remove(id);
    }

    @Override
    public Movie findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID не может быть null");
        return movies.get(id);
    }

    @Override
    public List<Movie> getAll() {
        return new ArrayList<>(movies.values());
    }

    @Override
    public boolean exists(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID не может быть null");
        return movies.containsKey(id);
    }

    public void clear() { movies.clear(); }
}