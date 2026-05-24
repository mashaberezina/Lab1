package model;

import java.util.UUID;

public class Movie {
    private final UUID id;
    private final String title;
    private final String director;
    private final int releaseYear;
    private final int durationMinutes;
    private final String genre;
    private boolean isWatched;
    private Integer userRating;

    public Movie(String title, String director, int releaseYear,
                 int durationMinutes, String genre) {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Название фильма не может быть пустым");
        if (director == null || director.isBlank())
            throw new IllegalArgumentException("Имя режиссёра не может быть пустым");
        if (releaseYear < 1888 || releaseYear > java.time.Year.now().getValue() + 2)
            throw new IllegalArgumentException("Некорректный год выпуска");
        if (durationMinutes <= 0)
            throw new IllegalArgumentException("Длительность должна быть положительной");
        if (genre == null || genre.isBlank())
            throw new IllegalArgumentException("Жанр не может быть пустым");

        this.id = UUID.randomUUID();
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.durationMinutes = durationMinutes;
        this.genre = genre;
        this.isWatched = false;
        this.userRating = null;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDirector() { return director; }
    public int getReleaseYear() { return releaseYear; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getGenre() { return genre; }
    public boolean isWatched() { return isWatched; }
    public Integer getUserRating() { return userRating; }

    public void markAsWatched(int rating) {
        if (rating < 1 || rating > 10)
            throw new IllegalArgumentException("Рейтинг должен быть от 1 до 10");
        this.isWatched = true;
        this.userRating = rating;
    }

    public void markAsUnwatched() {
        this.isWatched = false;
        this.userRating = null;
    }

    @Override
    public String toString() {
        return String.format("%s (%d) - %s | %d мин | %s%s",
                title, releaseYear, director, durationMinutes, genre,
                isWatched ? " ✅" : " ⏳");
    }
}