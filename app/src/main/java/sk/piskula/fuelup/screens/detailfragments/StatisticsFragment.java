package sk.piskula.fuelup.screens.detailfragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;

import sk.piskula.fuelup.R;
import sk.piskula.fuelup.business.StatisticsService;
import sk.piskula.fuelup.databinding.FragmentStatisticsBinding;
import sk.piskula.fuelup.entity.Vehicle;
import sk.piskula.fuelup.entity.dto.StatisticsDTO;
import sk.piskula.fuelup.loaders.StatisticsLoader;
import sk.piskula.fuelup.screens.VehicleStatisticsActivity;
import sk.piskula.fuelup.screens.VehicleTabbedDetailActivity;
import sk.piskula.fuelup.util.BigDecimalFormatter;

import static sk.piskula.fuelup.screens.VehicleTabbedDetailActivity.VEHICLE_TO_FRAGMENT;

/**
 * @author Ondrej Oravcok
 * @version 17.6.2017
 */
public class StatisticsFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<StatisticsDTO> {

    private Vehicle vehicle;

    private FragmentStatisticsBinding binding;

    private CollapsingToolbarLayout appBarLayout;
    private FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentStatisticsBinding.inflate(inflater);

        Bundle args = getArguments();
        if (args != null) {
            vehicle = args.getParcelable(VehicleTabbedDetailActivity.VEHICLE_TO_FRAGMENT);
        }

        appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
        appBarLayout.setTitle(getResources().getString(R.string.title_statistics));

        floatingActionButton = getActivity().findViewById(R.id.fab_add);
        floatingActionButton.setImageResource(android.R.drawable.ic_input_get);
        floatingActionButton.setOnClickListener(this);


        binding.setVehicle(vehicle);
        binding.setCurrency(vehicle.getCurrencySymbol(getActivity()));

        setFuelConsumption(binding);
        getLoaderManager().initLoader(StatisticsLoader.ID, args, this);

        return binding.getRoot();
    }

    private void setFuelConsumption(FragmentStatisticsBinding binding) {
        binding.fuelConsumption.setUnit("\u2113/" + vehicle.getUnit().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        floatingActionButton.setImageResource(android.R.drawable.ic_input_add);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == floatingActionButton.getId()) {
            Intent i = new Intent(getActivity(), VehicleStatisticsActivity.class);
            i.putExtra(VehicleStatisticsActivity.VEHICLE_TO_ADVANCED_STATISTICS, vehicle);
            startActivity(i);
        }
    }

    @Override
    public Loader<StatisticsDTO> onCreateLoader(int id, Bundle args) {
        Vehicle vehicle = args.getParcelable(VEHICLE_TO_FRAGMENT);
        long vehicleId = vehicle.getId();
        return new StatisticsLoader(getActivity(), new StatisticsService(getContext(), vehicleId));
    }

    @Override
    public void onLoadFinished(Loader<StatisticsDTO> loader, StatisticsDTO data) {
        DecimalFormat formatter = BigDecimalFormatter.getCommonFormat();

        binding.fuelConsumption.setValue(formatter.format(data.getAvgConsumption()));

        binding.distanceDrivenTotal.setValue(String.valueOf(data.getTotalDrivenDistance()));

        binding.fillUpsNumberTotal.setValue(String.valueOf(data.getTotalNumberFillUps()));
        binding.expensesNumberTotal.setValue(String.valueOf(data.getTotalNumberExpenses()));

        binding.priceTotal.setValue(formatter.format(data.getTotalPrice()));
        binding.priceTotalFuel.setValue(formatter.format(data.getTotalPriceFillUps()));
        binding.priceTotalExpense.setValue(formatter.format(data.getTotalPriceExpenses()));

        binding.pricePerDistanceTotal.setValue(formatter.format(data.getTotalPricePerDistance()));
        binding.pricePerDistanceFillUp.setValue(formatter.format(data.getFillUpPricePerDistance()));
        binding.pricePerDistanceExpense.setValue(formatter.format(data.getExpensePricePerDistance()));
    }

    @Override
    public void onLoaderReset(Loader<StatisticsDTO> loader) {
    }
}
