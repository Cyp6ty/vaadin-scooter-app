package fi.sauli.repository;

import fi.sauli.entity.Ride;
import fi.sauli.filter.RideFilter;

import java.util.List;

public interface RideRepositoryCustom {

    List<Ride> searchRides(RideFilter filter);

}
