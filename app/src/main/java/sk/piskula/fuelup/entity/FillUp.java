package sk.piskula.fuelup.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;

/**
 * @author Ondrej Oravcok
 * @version 17.6.2017
 */
@DatabaseTable(tableName = "fill_ups")
public class FillUp implements Serializable {

    private static final long serialVersionUID = -7406089937623011561L;

    @DatabaseField(generatedId = true)
    private Long Id;

    @DatabaseField(canBeNull = false, foreign = true)
    private Vehicle vehicle;

    @DatabaseField(columnName = "distance_from_last_fill_up")
    private Long distanceFromLastFillUp;

    @DatabaseField(canBeNull = false, columnName = "fuel_volume")
    private double fuelVolume;

    @DatabaseField(columnName = "fuel_price_per_litre")
    private BigDecimal fuelPricePerLitre;

    @DatabaseField(columnName = "fuel_price_total")
    private BigDecimal fuelPriceTotal;

    @DatabaseField(columnName = "is_full")
    private boolean isFullFillUp;

    @DatabaseField
    private Calendar date;

    @DatabaseField
    private String info;

    //end of atributes

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Long getDistanceFromLastFillUp() {
        return distanceFromLastFillUp;
    }

    public void setDistanceFromLastFillUp(Long distanceFromLastFillUp) {
        this.distanceFromLastFillUp = distanceFromLastFillUp;
    }

    public double getFuelVolume() {
        return fuelVolume;
    }

    public void setFuelVolume(double fuelVolume) {
        this.fuelVolume = fuelVolume;
    }

    public BigDecimal getFuelPricePerLitre() {
        return fuelPricePerLitre;
    }

    public void setFuelPricePerLitre(BigDecimal fuelPricePerLitre) {
        this.fuelPricePerLitre = fuelPricePerLitre;
    }

    public BigDecimal getFuelPriceTotal() {
        return fuelPriceTotal;
    }

    public void setFuelPriceTotal(BigDecimal fuelPriceTotal) {
        this.fuelPriceTotal = fuelPriceTotal;
    }

    public boolean isFullFillUp() {
        return isFullFillUp;
    }

    public void setFullFillUp(boolean fullFillUp) {
        isFullFillUp = fullFillUp;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
