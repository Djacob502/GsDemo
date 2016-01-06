package com.ecolumbia.gsdemo;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class InitializationFragment extends Fragment {

    // Create variables for the text views.
    private TextView tvDroneType;
    private TextView tvPermissionResult;
    private TextView tvPermissionResultDescription;
    private TextView tvConnectionDescription;

    public InitializationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_initialization, container, false);
        // Initialization the TextViews
        tvDroneType = (TextView) v.findViewById(R.id.tvDroneType);
        tvPermissionResult = (TextView) v.findViewById(R.id.tvPermissionResult);
        tvPermissionResultDescription = (TextView) v.findViewById(R.id.tvPermissionResultDescription);
        tvConnectionDescription = (TextView) v.findViewById(R.id.tvConnectionDescription);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Populate the text views with the bundle obtain from getArguments()
        tvDroneType.setText("Drone Type: " + getArguments().getString(getString(R.string.TAG_DRONE_MODEL_ID)));
        tvPermissionResult.setText("Permission Result: " + getArguments().getInt(getString(R.string.TAG_PERMISSION_RESULT), -999));
        tvPermissionResultDescription.setText("Permission Result Description: " + getArguments().getString(getString(R.string.TAG_PERMISSION_RESULT_DESCRIPTION)));
        tvConnectionDescription.setText("Connection Success: " + getArguments().getString(getString(R.string.TAG_CONNECTION_SUCCESS_DESCRIPTION)));
    }

}
