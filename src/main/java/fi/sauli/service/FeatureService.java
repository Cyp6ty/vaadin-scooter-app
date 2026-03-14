package fi.sauli.service;

import fi.sauli.entity.Feature;
import fi.sauli.repository.FeatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeatureService {

    private final FeatureRepository featureRepository;

    // --- Konstruktor ---
    public FeatureService(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    // Hakee kaikki
    public List<Feature> findAll() {
        return featureRepository.findAll();
    }

    // Tallentaa uuden / päivittää vanhan
    public void save(Feature feature) {
        featureRepository.save(feature);
    }

    // Poistaa
    public void delete (Feature feature) {
        featureRepository.delete(feature);
    }

    // Hakee yksittäisen id:n perusteella
    public Optional<Feature> findById (Long id) {
        return featureRepository.findById(id);
    }
}
