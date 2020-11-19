package com.example.inviattoadministrador.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import info.hoang8f.widget.FButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.inviattoadministrador.R;


public class AdministrarUsuariosFragment extends Fragment {

    private FrameLayout flAdministrarUsuarios;
    private TextView lblTitleAdministrarUsuarios;
    private FButton btnAdministrarClientes;
    private FButton btnAdministrarRestauranteros;
    private FButton btnAdministrarRepartidores;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_administrar_usuarios, container, false);
        //Enlazar controladores
        flAdministrarUsuarios = view.findViewById(R.id.flAdministrarUsuarios);
        lblTitleAdministrarUsuarios = view.findViewById(R.id.lblTitleAdministrarUsuarios);
        btnAdministrarClientes = view.findViewById(R.id.btnAdministrarClientes);
        btnAdministrarRestauranteros = view.findViewById(R.id.btnAdministrarRestauranteros);
        btnAdministrarRepartidores = view.findViewById(R.id.btnAdministrarRepartidores);

        //Asignacion de la fuente
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NABILA.TTF");
        lblTitleAdministrarUsuarios.setTypeface(typeface);

        return view;
    }//onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAdministrarClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }//onClick
        });

        btnAdministrarRestauranteros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }//onClick
        });

        btnAdministrarRepartidores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFrament(new RepartidorListaFragment());
            }//onClick
        });

    }//onViewCreated

    //Llamo fragment
    private void setFrament(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(flAdministrarUsuarios.getId(), fragment);
        fragmentTransaction.commit();
    }//setFrament

}//AdministrarUsuariosFragment