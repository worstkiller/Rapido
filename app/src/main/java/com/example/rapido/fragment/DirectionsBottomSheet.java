package com.example.rapido.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rapido.R;
import com.example.rapido.controller.DirectionsRecyclerViewAdapter;
import com.example.rapido.model.DirectionsInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.rapido.util.JsonUtil.addDirections;

/**
 * Created by Khushhal on 6/10/2017.
 */

public class DirectionsBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.rvDirections)
    RecyclerView rvDirections;
    Unbinder unbinder;
    private List<DirectionsInfo> directionsInfoList = new ArrayList<>();
    private DirectionsRecyclerViewAdapter directionsRecyclerViewAdapter;
    private String jsonObj = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonObj = getArguments().getString(DialogFragmentRoutes.TAG);
        JsonObject jsonObject = new JsonParser().parse(jsonObj).getAsJsonObject();
        directionsInfoList.addAll(addDirections(getContext(), jsonObject));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_direction, container, false);
        unbinder = ButterKnife.bind(this, view);
        initializeMembers();
        setupRecyclerView();
        return view;
    }

    private void initializeMembers() {
        directionsRecyclerViewAdapter = new DirectionsRecyclerViewAdapter(directionsInfoList, getContext());
    }

    private void setupRecyclerView() {
        rvDirections.setHasFixedSize(true);
        rvDirections.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDirections.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvDirections.setAdapter(directionsRecyclerViewAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
