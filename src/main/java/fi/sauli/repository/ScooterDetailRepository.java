package fi.sauli.repository;

import fi.sauli.entity.ScooterDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScooterDetailRepository extends JpaRepository<ScooterDetail, Long> {
}