package service;

import model.Movie;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MovieLibrary {
    private final MovieStorage storage;
    private final NotificationService notifier;

    public MovieLibrary(MovieStorage storage, NotificationService notifier) {
        this.storage = storage;
        this.notifier = notifier;
    }

    public void addMovie(Movie movie) {
        if (movie == null) throw new IllegalArgumentException("Фильм не может быть null");
        if (storage.exists(movie.getId()))
            throw new IllegalStateException("Фильм уже существует");

        storage.save(movie);
        notifier.notify("Добавлен: " + movie.getTitle());
    }

    public void removeMovie(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID не может быть null");
        if (!storage.exists(id)) throw new IllegalStateException("Фильм не найден");

        storage.delete(id);
        notifier.notify("Удалён фильм");
    }

    public List<Movie> findByDirector(String director) {
        if (director == null || director.isBlank())
            throw new IllegalArgumentException("Режиссёр не может быть пустым");

        return storage.getAll().stream()
                .filter(m -> m.getDirector().toLowerCase().contains(director.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Movie> findByGenre(String genre) {
        if (genre == null || genre.isBlank())
            throw new IllegalArgumentException("Жанр не может быть пустым");

        return storage.getAll().stream()
                .filter(m -> m.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Movie> getMoviesByYearRange(int fromYear, int toYear) {
        if (fromYear > toYear) throw new IllegalArgumentException("Некорректный диапазон годов");

        return storage.getAll().stream()
                .filter(m -> m.getReleaseYear() >= fromYear && m.getReleaseYear() <= toYear)
                .collect(Collectors.toList());
    }

    public void markAsWatched(UUID id, int rating) {
        if (id == null) throw new IllegalArgumentException("ID не может быть null");
        Movie movie = storage.findById(id);
        if (movie == null) throw new IllegalStateException("Фильм не найден");

        movie.markAsWatched(rating);
        storage.save(movie);
        notifier.notify("Просмотрен: " + movie.getTitle() + " (рейтинг: " + rating + "/10)");
    }

    public List<Movie> getUnwatchedMovies() {
        return storage.getAll().stream().filter(m -> !m.isWatched()).collect(Collectors.toList());
    }

    public List<Movie> getWatchedMovies() {
        return storage.getAll().stream().filter(Movie::isWatched).collect(Collectors.toList());
    }

    public List<Movie> getAllMovies() {
        return storage.getAll();
    }

    public double getAverageRating() {
        return storage.getAll().stream()
                .filter(Movie::isWatched)
                .mapToInt(Movie::getUserRating)
                .average()
                .orElse(0.0);
    }

    public int getTotalMoviesCount() { return storage.getAll().size(); }
    public int getWatchedCount() { return getWatchedMovies().size(); }
    public int getUnwatchedCount() { return getUnwatchedMovies().size(); }
}