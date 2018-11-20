package com.ecoone.mindfulmealplanner.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.R;

public class FinishTutorialFragment extends Fragment implements View.OnClickListener {
    public static FinishTutorialFragment newInstance() {
        return new FinishTutorialFragment();
    }

    private static Button mFinishTutorialButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_finished,container,false);
        mFinishTutorialButton = view.findViewById(R.id.finish_tutorial_button);
        mFinishTutorialButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

}
