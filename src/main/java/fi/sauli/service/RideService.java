package fi.sauli.service;

import fi.sauli.entity.Ride;
import fi.sauli.repository.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RideService {

    private final RideRepository rideRepository;

    // --- Konstruktor ---
    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    // Hakee kaikki
    public List<Ride> findAll() {
        return rideRepository.findAll();
    }

    // Tallentaa uuden / päivittää vanhan
    public void save(Ride ride) {
        rideRepository.save(ride);
    }

    // Poistaa
    public void delete(Ride ride) {
        rideRepository.delete(ride);
    }

    // Hakee yksittäisen id:n perusteella
    public Optional<Ride> findById(Long id) {
        return rideRepository.findById(id);
    }
}
