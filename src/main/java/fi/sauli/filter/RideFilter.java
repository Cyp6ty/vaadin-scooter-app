package fi.sauli.filter;

import fi.sauli.entity.Station;

import java.time.LocalDate;

public class RideFilter {

    private String keyword;         // LIKE-haku ja OR-ehto
    private String status;          // tavallinen lisäsuodatus
    private Station  startStation;  // JOIN relaatio hakuun (formDate ja toDate)
    private LocalDate formDate;     // JOIN
    private LocalDate toDate;       // JOIN
    private String sortBy;          // lajittelu mukaan

    // Konstruktor
    public RideFilter() {
    }


    // GET / SET
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Station getStartStation() {
        return startStation;
    }
    public void setStartStation(Station startStation) {
        this.startStation = startStation;
    }

    public LocalDate getFromDate() {
        return formDate;
    }
    public void setFromDate(LocalDate formDate) {
        this.formDate = formDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }
    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getSortBy() {
        return sortBy;
    }
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
