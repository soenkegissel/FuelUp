package sk.piskula.fuelup.screens.detailfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import sk.piskula.fuelup.R;
import sk.piskula.fuelup.adapters.ListFillUpsAdapter;
import sk.piskula.fuelup.business.FillUpService;
import sk.piskula.fuelup.entity.FillUp;
import sk.piskula.fuelup.entity.Vehicle;
import sk.piskula.fuelup.loaders.FillUpLoader;
import sk.piskula.fuelup.screens.edit.AddFillUpActivity;
import sk.piskula.fuelup.screens.edit.EditFillUpActivity;

import static android.app.Activity.RESULT_OK;
import static sk.piskula.fuelup.screens.VehicleTabbedDetailActivity.VEHICLE_TO_FRAGMENT;
import static sk.piskula.fuelup.screens.edit.AddFillUpActivity.EXTRA_CAR;
import static sk.piskula.fuelup.screens.edit.EditFillUpActivity.EXTRA_FILLUP;

/**
 * @author Ondrej Oravcok
 * @author Martin Styk
 * @version 21.6.2017
 */
public class FillUpsListFragment extends Fragment implements ListFillUpsAdapter.Callback, View.OnClickListener,
        LoaderManager.LoaderCallbacks<List<FillUp>> {

    private static final String TAG = "FillUpsListFragment";
    public static final int FILLUP_ACTION_REQUEST_CODE = 32;

    private Vehicle vehicle;
    private List<FillUp> data;
    private ListFillUpsAdapter adapter;

    private RecyclerView recyclerView;
    private ProgressBar loadingBar;
    private TextView emptyList;

    private CollapsingToolbarLayout appBarLayout;
    private FloatingActionButton addButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_fillups_list, container, false);

        Bundle args = getArguments();
        vehicle = args.getParcelable(VEHICLE_TO_FRAGMENT);

        appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
        appBarLayout.setTitle(getResources().getString(R.string.title_fillUps));

        loadingBar = view.findViewById(R.id.fill_ups_list_loading);
        emptyList = view.findViewById(R.id.fill_ups_list_empty);

        addButton = getActivity().findViewById(R.id.fab_add);
        addButton.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.fill_ups_list);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation()));

        if (adapter == null)
            adapter = new ListFillUpsAdapter(this);

        recyclerView.setAdapter(adapter);

        getLoaderManager().initLoader(FillUpLoader.ID, args, this);

        return view;
    }

    @Override
    public Loader<List<FillUp>> onCreateLoader(int id, Bundle args) {
        Vehicle vehicle = args.getParcelable(VEHICLE_TO_FRAGMENT);
        long vehicleId = vehicle.getId();
        return new FillUpLoader(getActivity(), vehicleId, new FillUpService(getActivity()));
    }

    @Override
    public void onLoadFinished(Loader<List<FillUp>> loader, List<FillUp> data) {
        this.data = data;
        adapter.dataChange(data);
        loadingBar.setVisibility(View.GONE);
        if (data.isEmpty()) {
            emptyList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<FillUp>> loader) {
        if (!data.isEmpty())
            data.clear();
    }

    @Override
    public void onItemClick(View v, FillUp fillUp, int position) {
        Intent i = new Intent(getActivity(), EditFillUpActivity.class);
        i.putExtra(EXTRA_FILLUP, fillUp);
        startActivityForResult(i, FILLUP_ACTION_REQUEST_CODE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == addButton.getId()) {
            Intent i = new Intent(getActivity(), AddFillUpActivity.class);
            i.putExtra(EXTRA_CAR, vehicle);
            startActivityForResult(i, FILLUP_ACTION_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILLUP_ACTION_REQUEST_CODE && resultCode == RESULT_OK)
            getLoaderManager().getLoader(FillUpLoader.ID).onContentChanged();
    }

}
