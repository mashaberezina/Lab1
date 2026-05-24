import model.Movie;
import service.*;

public class Main {
    public static void main(String[] args) {
        var storage = new InMemoryMovieStorage();
        var notifier = new ConsoleNotificationService();
        var library = new MovieLibrary(storage, notifier);

        System.out.println("=".repeat(50));
        System.out.println("КИНОТЕКА");
        System.out.println("=".repeat(50));

        // Добавляем фильмы
        var movie1 = new Movie("Начало", "Кристофер Нолан", 2010, 148, "Фантастика");
        var movie2 = new Movie("Побег из Шоушенка", "Фрэнк Дарабонт", 1994, 142, "Драма");

        library.addMovie(movie1);
        library.addMovie(movie2);

        // Отмечаем просмотр
        library.markAsWatched(movie1.getId(), 9);

        // Статистика
        System.out.println("\nСтатистика:");
        System.out.println("  Всего: " + library.getTotalMoviesCount());
        System.out.println("  Просмотрено: " + library.getWatchedCount());
        System.out.println("  Средний рейтинг: " + library.getAverageRating());
    }
}