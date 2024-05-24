package ru.yandex.practicum.filmorate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FilmControllerTest {

    private MockMvc mockMvc;
    private FilmController filmController;

    @Before
    public void setUp() {
        filmController = Mockito.mock(FilmController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
    }

    @Test
    public void testAddFilm() throws Exception {
        Film film = new Film();
        film.setId(1);
        film.setName("testFilm");
        film.setDescription("testDescription");
        film.setReleaseDate(LocalDate.now().minusYears(20));
        film.setDuration(120);

        Mockito.when(filmController.addFilm(Mockito.any(Film.class))).thenReturn(film);

        mockMvc.perform(post("/films")
                        .content("{\"id\":1,\"name\":\"testFilm\",\"description\":\"testDescription\",\"releaseDate\":\"2004-05-23\",\"duration\":120}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(film.getId()));
    }

    @Test
    public void testGetFilms() throws Exception {
        Film film = new Film();
        film.setId(1);
        film.setName("testFilm");
        film.setDescription("testDescription");
        film.setReleaseDate(LocalDate.now().minusYears(20));
        film.setDuration(120);
        Mockito.when(filmController.getFilms()).thenReturn(List.of(film));
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(film.getId()));
    }

    @Test
    public void testUpdateFilm() throws Exception {
        Film film = new Film();
        film.setId(1);
        film.setName("testFilm");
        film.setDescription("testDescription");
        film.setReleaseDate(LocalDate.now().minusYears(20));
        film.setDuration(120);

        Mockito.when(filmController.updateFilm(Mockito.any(Film.class))).thenReturn(film);

        mockMvc.perform(put("/films")
                        .content("{\"id\":1,\"name\":\"testFilm\",\"description\":\"testDescription\",\"releaseDate\":\"2004-05-23\",\"duration\":120}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(film.getId()));
    }

    @Test
    public void testValidateFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("testFilm");
        film.setDescription("testDescription");
        film.setReleaseDate(LocalDate.now().minusYears(20));
        film.setDuration(120);
        Mockito.when(filmController.validateFilm(Mockito.any(Film.class))).thenReturn(true);
        boolean result = filmController.validateFilm(film);
        assert (result);
    }
}
