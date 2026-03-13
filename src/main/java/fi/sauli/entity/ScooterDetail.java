package fi.sauli.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Entity
public class ScooterDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 5 kpl validoitavia kenttiä
    @NotBlank
    private String firmwareVersion;

    @NotNull
    @Min(1)
    @Max(50)
    private Integer maxSpeed;

    @NotNull
    @PastOrPresent
    private LocalDate lastInspectionDate;

    @Column(unique = true)
    @NotBlank
    private String qrCode;

    @NotNull
    @Positive
    private Double weight;


    // --- RELAATIOT ---

    // 1:1 - Yhdellä ScooterDetaililla on yksi scooter
    // OMISTAVA PUOLI
    @OneToOne
    @JoinColumn(name = "scooter_id", unique = true)
    private Scooter scooter;

    // -- Konstruktorit ---
    public ScooterDetail() {
    }

    public ScooterDetail(String firmwareVersion, Integer maxSpeed,
                         LocalDate lastInspectionDate,String qrCode, Double weight) {
        this.firmwareVersion = firmwareVersion;
        this.maxSpeed = maxSpeed;
        this.lastInspectionDate = lastInspectionDate;
        this.qrCode = qrCode;
        this.weight = weight;
    }

    // GET / SET
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }
    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }
    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public LocalDate getLastInspectionDate() {
        return lastInspectionDate;
    }
    public void setLastInspectionDate(LocalDate lastInspectionDate) {
        this.lastInspectionDate = lastInspectionDate;
    }

    public String getQrCode() {
        return qrCode;
    }
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Double getWeight() {
        return weight;
    }
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Scooter getScooter() {
        return scooter;
    }
    public void setScooter(Scooter scooter) {
        this.scooter = scooter;
    }

}
