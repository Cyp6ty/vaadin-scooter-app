package fi.sauli.service;

import fi.sauli.entity.Scooter;
import fi.sauli.repository.ScooterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScooterService {

    private final ScooterRepository scooterRepository;

    // --- Konstruktor ---
    public ScooterService(ScooterRepository scooterRepository) {
        this.scooterRepository = scooterRepository;
    }

    // Hakee kaikki
    public List<Scooter> findAll() {
        return scooterRepository.findAll();
    }

    // Tallentaa uuden / päivittää vanhan
    public void save(Scooter scooter) {
        scooterRepository.save(scooter);
    }

    // Poistaa
    public void delete(Scooter scooter) {
        scooterRepository.delete(scooter);
    }

    // Hakee yksittäisen id:n perusteella
    public Optional<Scooter> findById(Long id) {
        return scooterRepository.findById(id);
    }

}
