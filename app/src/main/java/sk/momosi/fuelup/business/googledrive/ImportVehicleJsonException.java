package sk.momosi.fuelup.business.googledrive;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Ondrej Oravcok
 * @version 17.10.2017
 */
public class ImportVehicleJsonException extends JSONException {

    private final JSONException e;
    private final JSONObject json;

    public ImportVehicleJsonException(final JSONException e, final JSONObject json) {
        super("JSONException occurred when importing vehicle");
        this.e = e;
        this.json = json;
    }

    public JSONException getException() {
        return this.e;
    }

    @Override
    public String toString() {
        return "JSON: " + json.toString() + "\nexception: " + e.toString();
    }
}