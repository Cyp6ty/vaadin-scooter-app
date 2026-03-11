package fi.sauli.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 5 kpl validoitavia kenttiä
    @Column(unique = true)
    @NotBlank
    private String name;

    @Size(min = 10)
    private String description;

    @NotBlank
    private String category;

    @NotNull
    private Boolean active;

    @NotNull
    @Positive
    private Double installationCost;


    // --- RELAATIOT ---

    // M:N - Yksi ominaisuus voi kuulua monelle scooterille
    // KÄÄNTEINEN PUOLI
    @ManyToMany(mappedBy = "features")
    private Set<Scooter> scooters = new HashSet<>();

    // -- Konstruktorit ---
    public Feature() {
    }

    public Feature(String name, String description, String category,
                   Boolean active, Double installationCost, Set<Scooter> scooters) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.active = active;
        this.installationCost = installationCost;
        this.scooters = scooters;
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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getInstallationCost() {
        return installationCost;
    }
    public void setInstallationCost(Double installationCost) {
        this.installationCost = installationCost;
    }

    public Set<Scooter> getScooters() {
        return scooters;
    }
    public void setScooters(Set<Scooter> scooters) {
        this.scooters = scooters;
    }

}
