package com.ecoone.mindfulmealplanner.setup;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;

import java.util.Objects;

// Fragments declarations
public class GreetingFragment extends Fragment {
    public static GreetingFragment newInstance() {
        return new GreetingFragment();
    }

    private static final String CLASSTAG = "(GreetingFragment)";
    private static final String TAG = "testActivity";

    InitialSetupViewModel mViewModel;
    TextView greetingText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(InitialSetupViewModel.class);
        View view = inflater.inflate(R.layout.fragment_initial_setup_greeting, container, false);
        greetingText = view.findViewById(R.id.greeting_text);
        greetingText.setText("Greetings!");

        mViewModel.getDisplayName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String newDisplayName) {
                mViewModel.localUser.displayName = newDisplayName;
                greetingText.setText(getString(R.string.greeting_user,newDisplayName));
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
