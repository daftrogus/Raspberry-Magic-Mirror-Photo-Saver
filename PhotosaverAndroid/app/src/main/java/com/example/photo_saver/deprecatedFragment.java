package com.example.photo_saver;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class deprecatedFragment extends ProfilingFragment {

    EditText text_nama;
    Button button_lanjut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle      savedInstanceState) {
        View v = inflater.inflate(R.layout.deprecated,container,false);
        button_lanjut = v.findViewById(R.id.start_take);
        text_nama = v.findViewById(R.id.nama_anak_baru);

        button_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString("key",text_nama.getText().toString());
                Intent intent = new Intent(getActivity(),TakecamActivity.class);
                intent.putExtra("key",text_nama.getText().toString());
                startActivity(intent);
            }
        });

        return inflater.inflate(R.layout.deprecated,container,false);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        }
    }