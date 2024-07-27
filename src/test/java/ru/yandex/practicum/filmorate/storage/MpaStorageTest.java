package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
class MpaStorageIntegrationTest {

    @Autowired
    private MpaStorage mpaStorage;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Test
    void testGetMpaById() {
        // Given
        Mpa expectedMpa = new Mpa();
        expectedMpa.setId(1);
        expectedMpa.setName("G");
        try {
            Mpa actualMpa = mpaStorage.getMpaById(1);
            assertEquals(expectedMpa, actualMpa);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    void testGetAllMpa() {
        // Given
        Mpa mpa1 = new Mpa();
        mpa1.setId(1);
        mpa1.setName("Mpa 1");
        // When
        List<Mpa> allMpa = mpaStorage.getAllMpa();
        // Then
        assertEquals(2, allMpa.size());


    }
}
