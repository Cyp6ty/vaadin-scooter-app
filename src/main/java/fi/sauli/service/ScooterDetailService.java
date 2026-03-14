package fi.sauli.service;

import fi.sauli.entity.ScooterDetail;
import fi.sauli.repository.ScooterDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScooterDetailService {

    private final ScooterDetailRepository scooterDetailRepository;

    // --- Konstruktor
    public ScooterDetailService(ScooterDetailRepository scooterDetailRepository) {
        this.scooterDetailRepository = scooterDetailRepository;
    }

    // Hakee kaikki
    public List<ScooterDetail> findAll() {
        return scooterDetailRepository.findAll();
    }

    // Tallentaa / päivittää vanhan
    public void save(ScooterDetail scooterDetail) {
        scooterDetailRepository.save(scooterDetail);
    }

    // Poistaa
    public void delete(ScooterDetail scooterDetail) {
        scooterDetailRepository.delete(scooterDetail);
    }

    // Hakee yksittäisen id:n perusteella
    public Optional<ScooterDetail> findById(Long id) {
        return scooterDetailRepository.findById(id);
    }
}
