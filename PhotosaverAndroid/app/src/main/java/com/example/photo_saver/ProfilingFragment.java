package com.example.photo_saver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfilingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profiling,container,false);
        Button button_tambahAnak = (Button) view.findViewById(R.id.button_tambahAnak);

        button_tambahAnak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BeforeTakecamFragment2()).commit();
//            FragmentTransaction toFragmentCam = getFragmentManager().beginTransaction();
//            toFragmentCam.replace(R.id.fragment_container,new BeforeTakecamFragment2());
//            toFragmentCam.commit();
            }
        });

        return view;
    }
}
