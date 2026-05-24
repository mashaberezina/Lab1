import model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieLibraryTest {

    @Mock
    private MovieStorage storage;

    @Mock
    private NotificationService notifier;

    @InjectMocks
    private MovieLibrary library;

    private Movie testMovie;

    @BeforeEach
    void setUp() {
        testMovie = new Movie("Тестовый фильм", "Тестовый режиссёр", 2020, 120, "Драма");
    }

    @Test
    void testAddMovie_Success() {
        when(storage.exists(testMovie.getId())).thenReturn(false);

        library.addMovie(testMovie);

        verify(storage).save(testMovie);
        verify(notifier).notify(anyString());
    }

    @Test
    void testAddMovie_Null_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> library.addMovie(null));
    }

    @Test
    void testAddMovie_Duplicate_ThrowsException() {
        when(storage.exists(testMovie.getId())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> library.addMovie(testMovie));
    }

    @Test
    void testRemoveMovie_Success() {
        when(storage.exists(testMovie.getId())).thenReturn(true);

        library.removeMovie(testMovie.getId());

        verify(storage).delete(testMovie.getId());
    }

    @Test
    void testRemoveMovie_NotFound_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(storage.exists(id)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> library.removeMovie(id));
    }

    @Test
    void testFindByDirector_ReturnsMatches() {
        when(storage.getAll()).thenReturn(List.of(testMovie));

        List<Movie> result = library.findByDirector("режиссёр");

        assertEquals(1, result.size());
    }

    @Test
    void testFindByGenre_ReturnsMatches() {
        when(storage.getAll()).thenReturn(List.of(testMovie));

        List<Movie> result = library.findByGenre("Драма");

        assertEquals(1, result.size());
    }

    @Test
    void testMarkAsWatched_Success() {
        when(storage.findById(testMovie.getId())).thenReturn(testMovie);

        library.markAsWatched(testMovie.getId(), 9);

        assertTrue(testMovie.isWatched());
        assertEquals(9, testMovie.getUserRating());
        verify(storage).save(testMovie);
    }

    @Test
    void testGetUnwatchedMovies_ReturnsOnlyNotWatched() {
        Movie watched = new Movie("Просмотрен", "Реж", 2000, 120, "Драма");
        watched.markAsWatched(8);

        when(storage.getAll()).thenReturn(Arrays.asList(watched, testMovie));

        List<Movie> result = library.getUnwatchedMovies();

        assertEquals(1, result.size());
        assertEquals("Тестовый фильм", result.get(0).getTitle());
    }

    @Test
    void testGetAverageRating_ReturnsCorrectAverage() {
        Movie m1 = new Movie("1", "A", 2000, 120, "Драма");
        Movie m2 = new Movie("2", "B", 2001, 130, "Боевик");
        m1.markAsWatched(8);
        m2.markAsWatched(6);

        when(storage.getAll()).thenReturn(Arrays.asList(m1, m2));

        assertEquals(7.0, library.getAverageRating());
    }

    @Test
    void testGetAverageRating_NoWatched_ReturnsZero() {
        when(storage.getAll()).thenReturn(List.of(testMovie));

        assertEquals(0.0, library.getAverageRating());
    }
}