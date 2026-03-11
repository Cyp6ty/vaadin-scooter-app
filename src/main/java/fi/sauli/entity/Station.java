package fi.sauli.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 5 kpl validoitavia kenttiä
    @NotBlank
    private String name;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    @NotNull
    @Min(1)
    @Max(50)
    private Integer capacity;

    @NotBlank
    private String area;

    // --- RELAATIOT ---

    // 1:M - Yhdellä stationilla voi olla monta scooteria
    // KÄÄNTEINEN PUOLI
    @OneToMany(mappedBy = "station")
    private List<Scooter> scooters = new ArrayList<>();

    // -- Konstruktorit ---
    public Station() {
    }

    public Station(String name, String city, String address,
                   Integer capacity, String area) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.capacity = capacity;
        this.area = area;
    }

    // GET / SET
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCapacity() {
        return capacity;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }

    public List<Scooter> getScooters() {
        return scooters;
    }
    public void setScooters(List<Scooter> scooters) {
        this.scooters = scooters;
    }

}
