package fi.sauli.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 5 kpl validoitavia kenttiä
    @NotNull
    @PastOrPresent
    private LocalDateTime startTime;

    @NotNull
    @PastOrPresent
    private LocalDateTime endTime;

    @NotNull
    @Positive
    private Double distanceKm;

    @NotNull
    @Positive
    private Double price;

    @NotBlank
    private String status;


    // --- RELAATIOT ---

    /* Yksi Ride = yksi ajo
       yhdellä ajolla on:
        - yksi skootteri
        - yksi lähtöasema
        - yksi pääteasema
    JA
     - yhdellä skootterilla voi olla monta ajoa
     - yhdellä asemalla voi olla monta alkavaa ajoa
     - yhdellä asemalla voi olla monta päättyvää ajoa
    */

    // N:1 - Monta ajoa voi kuulua yhdelle scooterille
    // OMISTAVA PUOLI
    // Ride -> Scooter
    @NotNull
    @ManyToOne
    @JoinColumn(name = "scooter_id")
    private Scooter scooter;

    // N:1 - Monta ajoa voi alkaa samalta asemalta
    // OMISTAVA PUOLI
    // Ride -> startStation
    @NotNull
    @ManyToOne
    @JoinColumn(name = "start_station_id")
    private Station startStation;

    // N:1 - Yhdellä stationilla voi olla monta
    // OMISTAVA PUOLI
    // Ride -> endStation
    @NotNull
    @ManyToOne
    @JoinColumn(name = "end_station_id")
    private Station endStation;


    // -- Konstruktorit ---
    public Ride() {
    }

    public Ride(LocalDateTime startTime, LocalDateTime endTime, Double distanceKm, Double price,
                String status, Scooter scooter, Station startStation, Station endStation) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.distanceKm = distanceKm;
        this.price = price;
        this.status = status;
        this.scooter = scooter;
        this.startStation = startStation;
        this.endStation = endStation;
    }

    // GET / SET
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }
    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Scooter getScooter() {
        return scooter;
    }
    public void setScooter(Scooter scooter) {
        this.scooter = scooter;
    }

    public Station getStartStation() {
        return startStation;
    }
    public void setStartStation(Station startStation) {
        this.startStation = startStation;
    }

    public Station getEndStation() {
        return endStation;
    }
    public void setEndStation(Station endStation) {
        this.endStation = endStation;
    }
}
