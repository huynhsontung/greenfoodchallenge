package com.ecoone.mindfulmealplanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPhotoFragment extends Fragment {

    private TextView textView;
    public AddPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);

        textView = view.findViewById(R.id.test_text_view);
        String message = getArguments().getString("message");
        textView.setText(message);
        return view;
    }

}
