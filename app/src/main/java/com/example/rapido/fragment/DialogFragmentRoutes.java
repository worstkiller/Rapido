package com.example.rapido.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.rapido.R;
import com.example.rapido.activity.MapsActivity;
import com.example.rapido.controller.RouteRecyclerViewAdapter;
import com.example.rapido.interfaces.ClickListener;
import com.example.rapido.model.RouteInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.rapido.util.JsonUtil.addRoutes;

/**
 * Created by Khushhal on 6/10/2017.
 */

public class DialogFragmentRoutes extends AppCompatDialogFragment implements ClickListener {
    public final static String TAG = DialogFragmentRoutes.class.getCanonicalName();
    @BindView(R.id.rvRoutes)
    RecyclerView rvRoutes;
    Unbinder unbinder;
    @BindView(R.id.tvSource)
    TextView tvSource;
    @BindView(R.id.tvDestination)
    TextView tvDestination;
    @BindView(R.id.btClose)
    AppCompatButton btClose;
    private String json, end_address, start_address;
    private List<RouteInfo> routeInfoList = new ArrayList<>();
    private RouteRecyclerViewAdapter routeRecyclerViewAdapter;
    JsonObject jsonObjectDirections;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        json = getArguments().getString(MapsActivity.TAG);
        initMembers();
    }

    private void initMembers() {
        try {
            jsonObjectDirections = new JsonParser().parse(json).getAsJsonObject();
            JsonObject child = jsonObjectDirections.get("routes").getAsJsonArray().get(0).getAsJsonObject();
            end_address = child.get("legs").getAsJsonArray().get(0).getAsJsonObject().get("end_address").getAsString();
            start_address = child.get("legs").getAsJsonArray().get(0).getAsJsonObject().get("start_address").getAsString();
            routeInfoList.addAll(addRoutes(jsonObjectDirections));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            dismiss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_routes, container, false);
        unbinder = ButterKnife.bind(this, view);
        bindRecyclerView();
        bindViewToData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    private void bindViewToData() {
        tvDestination.setText(end_address);
        tvSource.setText(start_address);
    }

    private void bindRecyclerView() {
        routeRecyclerViewAdapter = new RouteRecyclerViewAdapter(routeInfoList, this);
        rvRoutes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRoutes.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvRoutes.setHasFixedSize(true);
        rvRoutes.setAdapter(routeRecyclerViewAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(int position) {
        //handle the click event
        Bundle bundle = new Bundle();
        bundle.putString(TAG, jsonObjectDirections.get("routes").getAsJsonArray().get(position).getAsJsonObject().toString());
        DirectionsBottomSheet directionsBottomSheet = new DirectionsBottomSheet();
        directionsBottomSheet.setArguments(bundle);
        directionsBottomSheet.show(getFragmentManager(), TAG);
    }

    @OnClick(R.id.btClose)
    public void onViewClicked() {
        dismiss();
    }
}
