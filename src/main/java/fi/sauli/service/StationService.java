package fi.sauli.service;

import fi.sauli.entity.Station;
import fi.sauli.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StationService {

    private final StationRepository stationRepository;

    // --- Konstruktor ---
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    // Hakee kaikki
    public List<Station> findAll() {
        return stationRepository.findAll();
    }

    // Tallentaa uuden / päivittää vanhan
    public void save(Station station) {
        stationRepository.save(station);
    }

    // Poistaa
    public void delete(Station station) {
        stationRepository.delete(station);
    }

    // Hakee yksittäisen id:n perusteella
    public Optional<Station> findById(Long id) {
        return stationRepository.findById(id);
    }

}


