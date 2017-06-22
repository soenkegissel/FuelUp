package sk.piskula.fuelup.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.piskula.fuelup.entity.enums.DistanceUnit;

/**
 * @author Ondrej Oravcok
 * @version 16.6.2017.
 */
@DatabaseTable(tableName = "vehicles")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = -7406082437623008261L;

    private static final Map<Currency, String> customCurrencySymbols;
    static {
        customCurrencySymbols = new HashMap<>();
        customCurrencySymbols.put(Currency.getInstance("CZK"), "K\u010D");
        customCurrencySymbols.put(Currency.getInstance("PLN"), "z\u0142");
        customCurrencySymbols.put(Currency.getInstance("HUF"), "Ft");
    }

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false, unique = true)
    private String name;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private VehicleType type;

    @DatabaseField(unknownEnumName = "km")
    private DistanceUnit unit;

    @DatabaseField(persisted = false)
    private byte[] image;

    @DatabaseField(columnName = "vehicle_maker")
    private String vehicleMaker;

    @DatabaseField(columnName = "start_mileage")
    private Long startMileage;

    @DatabaseField(canBeNull = false)
    private String currency;

    //end of attributes
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

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public DistanceUnit getUnit() {
        return unit;
    }

    public void setUnit(DistanceUnit unit) {
        this.unit = unit;
    }

    public String getVehicleMaker() {
        return vehicleMaker;
    }

    public void setVehicleMaker(String vehicleMaker) {
        this.vehicleMaker = vehicleMaker;
    }

    public Bitmap getImage() {
        return DbBitmapUtility.getImage(image);
    }
    public byte[] getImageBytes() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = DbBitmapUtility.getBytes(image);
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Long getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(Long startMileage) {
        this.startMileage = startMileage;
    }

    public Currency getCurrency() {
        return Currency.getInstance(currency);
    }

    public void setCurrency(Currency currency) {
        this.currency = currency.getCurrencyCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Vehicle))
            return false;

        Vehicle other = (Vehicle) obj;
        if (name == null && other.getName() == null) return true;
        if (name == null && "".equals(other.getName())) return true;
        if ("".equals(name) && other.getName() == null) return true;

        return name != null ? name.equals(other.getName()) : other.getName() == null;
    }

    @Override
    public int hashCode(){
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString(){
        return "Vehicle{"
                + "id=" + id
                + ", name=" + name
                + ", type=" + type
                + ", unit=" + unit
                + ", vehicleMaker=" + vehicleMaker
                + ", startMileage=" + startMileage
                + ", currency=" + currency
                +"}";
    }

    private static class DbBitmapUtility {
        private static  byte[] EMPTY_BITMAP = new byte[]{1};
        // convert from bitmap to byte array
        public static byte[] getBytes(Bitmap bitmap) {

            if(bitmap == null){
                return EMPTY_BITMAP;
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();
        }

        // convert from byte array to bitmap
        public static Bitmap getImage(byte[] image) {
            if(image == null || EMPTY_BITMAP.equals(image)){
                return null;
            }

            if(image == null) return null;
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
    }

    public String getCurrencySymbol() {
        if (customCurrencySymbols.containsKey(this.getCurrency())) {
            return customCurrencySymbols.get(this.getCurrency());
        }
        return this.getCurrency().getSymbol();
    }

    public static String getCurrencySymbol(Currency currency) {
        if (customCurrencySymbols.containsKey(currency)) {
            return customCurrencySymbols.get(currency);
        }
        return currency.getSymbol();
    }

    public static List<Currency> getSupportedCurrencies(){
        List<String> currenciesStrings = Arrays.asList("EUR", "CZK", "USD", "GBP", "PLN", "HUF");
        List<Currency> currencies = new ArrayList<>();

        for (String currencyString : currenciesStrings)
            currencies.add(Currency.getInstance(currencyString));

        return currencies;
    }

}
