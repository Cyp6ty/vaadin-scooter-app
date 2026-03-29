package fi.sauli.repository;

import fi.sauli.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

// Perii custom rajapinnan (RideRepositoryCustom)
public interface RideRepository extends JpaRepository<Ride, Long>, RideRepositoryCustom {
}
