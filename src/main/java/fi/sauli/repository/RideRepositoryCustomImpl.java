package fi.sauli.repository;


import fi.sauli.entity.Ride;
import fi.sauli.entity.Scooter;
import fi.sauli.entity.Station;
import fi.sauli.filter.RideFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RideRepositoryCustomImpl implements RideRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Ride> searchRides(RideFilter filter) {
        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder(); // Rakentaa ehdot
        CriteriaQuery<Ride> cQuery = cBuilder.createQuery(Ride.class); // Itse kysely
        Root<Ride> ride = cQuery.from(Ride.class); // Viittaus entiteettiin

        // JOIN ehdot
        Join<Ride, Scooter> scooterJoin = ride.join("scooter");
        Join<Ride, Station> startStationJoin = ride.join("startStation");

        // Dynaamiset ehdot
        List<Predicate> predicates = new ArrayList<>();

        // STATUS
        // status annettu -> suodatus sen mukaan
        // status tyhjä -> ei lisätä ehtoa
        if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
            predicates.add(cBuilder.equal(ride.get("status"), filter.getStatus()));
        }


        // PÄIVÄMÄÄRÄ
        // annettu vain alkupäivä -> hakee siitä eteenpäin
        // vain loppupäivä -> hakee siihen asti
        // molemmat -> tulos niiden välistä
        // ei mitään -> ei lisää ehtoja

        // alkupäivä
        if (filter.getFromDate() != null) {
            LocalDateTime fromDateTime = filter.getFromDate().atStartOfDay();
            predicates.add(cBuilder.greaterThanOrEqualTo(
                    ride.get("startTime"),
                    fromDateTime
            ));
        }
        // loppupäivä
        if (filter.getToDate() != null) {
            LocalDateTime toDateTime = filter.getToDate().atTime(23, 59, 59);
            predicates.add(cBuilder.lessThanOrEqualTo(
                    ride.get("startTime"),
                    toDateTime
            ));
        }


        // LÄHTÖASEMA (JOIN)
        // hakee ajot joiden lähtöaseman id = valittu asema
        if (filter.getStartStation() != null) {
            predicates.add(cBuilder.equal(
                    startStationJoin.get("id"),
                    filter.getStartStation().getId()
            ));
        }


        // HAKUSANA (LIKE + OR)
        // Rakentaa:  LOWER(model) LIKE '%hakusana%' OR LOWER(serialNumber) LIKE '%hakusana%'
        // ja lisää sen ehdoksi muiden joukkoon

        /* (X OR Y AND Z)
        Kun tämä yhdistyy muihin:
        - status
        - päivämäärä
        - station

        lopullinen rakenne on automaattisesti:
        (model LIKE ? OR serial LIKE ?)
        AND status = ?
        AND startTime BETWEEN ...
        AND startStation = ?
        */
        if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
            String pattern = "%" + filter.getKeyword().toLowerCase() + "%";

            Predicate model = cBuilder.like(
                    cBuilder.lower(scooterJoin.get("model")),
                    pattern
            );

            Predicate serialNumber = cBuilder.like(
                    cBuilder.lower(scooterJoin.get("serialNumber")),
                    pattern
            );

            Predicate startStationName = cBuilder.like(
                    cBuilder.lower(startStationJoin.get("name")),
                    pattern
            );

            Join<Ride, Station> endStationJoin = ride.join("endStation");
            Predicate endStationName = cBuilder.like(
                    cBuilder.lower(endStationJoin.get("name")),
                    pattern
            );

            // (model LIKE ? OR serialNumber LIKE ? OR station LIKE ?)
            Predicate orCondition = cBuilder.or(
                    model,
                    serialNumber,
                    startStationName,
                    endStationName
            );

            predicates.add(orCondition);
        }

        // QUERY rakennus + predikaattien lisäys
        cQuery.select(ride);
        cQuery.where(predicates.toArray(new Predicate[0]));


        // LAJITTELU
        // jos mitään ei anneta -> oletus: uusimmat ajot ensin
        if (filter.getSortBy() != null) {
            switch (filter.getSortBy()) {
                case "startTimeAsc":
                    cQuery.orderBy(cBuilder.asc(ride.get("startTime")));
                    break;
                case "startTimeDesc":
                    cQuery.orderBy(cBuilder.desc(ride.get("startTime")));
                    break;
                case "priceAsc":
                    cQuery.orderBy(cBuilder.asc(ride.get("price")));
                    break;
                case "priceDesc":
                    cQuery.orderBy(cBuilder.desc(ride.get("price")));
                    break;
                default:
                    cQuery.orderBy(cBuilder.desc(ride.get("startTime")));
            }
        } else {
            // oletus
            cQuery.orderBy(cBuilder.desc(ride.get("startTime")));
        }

        return entityManager.createQuery(cQuery).getResultList();
    }
}
