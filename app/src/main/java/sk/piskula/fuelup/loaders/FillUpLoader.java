package sk.piskula.fuelup.loaders;

/**
 * Created by Martin Styk on 20.06.2017.
 */

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sk.piskula.fuelup.data.DatabaseHelper;
import sk.piskula.fuelup.entity.FillUp;


/**
 * Loader async task FillUps
 * <p>
 * Created by Martin Styk on 15.06.2017.
 */
public class FillUpLoader extends AsyncTaskLoader<List<FillUp>> {
    private static final String TAG = FillUpLoader.class.getSimpleName();
    public static final int ID = 1;

    private DatabaseHelper databaseHelper = null;

    private List<FillUp> items;
    private long vehicleId;
    private Dao<FillUp, Integer> dao;

    public FillUpLoader(Context context, long vehicleId, Dao<FillUp, Integer> dao) {
        super(context);
        this.vehicleId = vehicleId;
        this.dao = dao;
    }

    /**
     * This is where the bulk of our work is done.  This function is
     * called in a background thread and should generate a new set of
     * data to be published by the loader.
     */
    @Override
    public List<FillUp> loadInBackground() {
        try {
            //TODO remove this is just for debugging purposes
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            return dao.queryBuilder().where().eq("vehicle_id", vehicleId).query();
        } catch (SQLException e) {
            Log.e(TAG, "Error getting expenses from DB for vehicle id " + vehicleId, e);
            return new ArrayList<>();
        }
    }


    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override
    public void deliverResult(List<FillUp> newItems) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (items != null) {
                onReleaseResources(items);
            }
        }
        List<FillUp> oldItems = items;
        items = newItems;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(items);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldItems != null) {
            onReleaseResources(oldItems);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (items != null) {
            deliverResult(items);
        }

        if (takeContentChanged() || items == null) {
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(List<FillUp> fillUps) {
        super.onCanceled(fillUps);

        onReleaseResources(fillUps);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (items != null) {
            onReleaseResources(items);
            items = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(List<FillUp> fillUps) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }

}


