package fi.sauli.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "scooter")
public class Scooter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 5 kpl validoitavia kenttiä
    @NotBlank
    private String model;

    @Column(unique = true)
    @NotBlank
    private String serialNumber;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer batteryLevel;

    @NotBlank
    private String status;

    @NotNull @Min(2015) @Max(2026)
    private Integer manufactureYear;


    // --- RELAATIOT ---

    // M:1 - Yhdellä scooterilla on yksi station
    // Yhdellä stationilla voi olla monta scooteria
    // OMISTAVA PUOLI
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    // 1:1 - Yhdellä ScooterDetail-oliolla on yksi scooter
    // KÄÄNTEINEN PUOLI
    @OneToOne(mappedBy = "scooter")
    private ScooterDetail scooterDetail;

    // M:N - Yhdellä scooterilla voi olla monta ominaisuutta
    // OMISTAVA PUOLI
    @ManyToMany
    @JoinTable(
            name = "scooter_feature",
            joinColumns = @JoinColumn(name = "scooter_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private Set<Feature> features = new HashSet<>();


    // -- Konstruktorit ---
    public Scooter() {
    }

    public Scooter(String model, String serialNumber, Integer batteryLevel,
                   String status, Integer manufactureYear) {
        this.model = model;
        this.serialNumber = serialNumber;
        this.batteryLevel = batteryLevel;
        this.status = status;
        this.manufactureYear = manufactureYear;
    }

    // GET / SET
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }
    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getManufactureYear() {
        return manufactureYear;
    }
    public void setManufactureYear(Integer manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public Station getStation() {
        return station;
    }
    public void setStation(Station station) {
        this.station = station;
    }

    public ScooterDetail getScooterDetail() {
        return scooterDetail;
    }
    public void setScooterDetail(ScooterDetail scooterDetail) {
        this.scooterDetail = scooterDetail;
    }

    public Set<Feature> getFeatures() {
        return features;
    }
    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }
}
