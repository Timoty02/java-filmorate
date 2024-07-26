package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Map;

@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Map<Integer, Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public Mpa getMpaById(int id) {
        try {
            Mpa mpa = mpaStorage.getMpaById(id);
            if (mpa == null) {
                throw new NotFoundException("MPA rating with ID " + id + " not found.");
            }
            //return Map.of(mpa.getId(), mpa.getName());
            return mpa;
        } catch (Exception e) {
            throw new NotFoundException("MPA rating with ID " + id + " not found.");
        }
    }

    private void validateMpa(Mpa mpa) {
        if (mpa == null) {
            throw new ValidationException("MPA rating cannot be null.");
        }
        if (mpa.getName() == null || mpa.getName().isBlank()) {
            throw new ValidationException("MPA rating name cannot be null or blank.");
        }
    }
}
