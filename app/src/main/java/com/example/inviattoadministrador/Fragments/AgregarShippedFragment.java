package com.example.inviattoadministrador.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inviattoadministrador.Common.Common;
import com.example.inviattoadministrador.Model.ShippedModel;
import com.example.inviattoadministrador.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;


public class AgregarShippedFragment extends Fragment {

    //Declaracion de variables
    private TextView lblTitle_AgregarShipped;
    private EditText edtNombre_AgregarShipped;
    private EditText edtContraseña_AgregarShipped;
    private EditText edtPhone_AgregarShipped;
    private EditText edtDireccion_AgregarShipped;
    private Button btnSelect_AgregarShipped;
    private Button btnUpload_AgregarShipped;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agregar_shipped, container, false);

        //Enlazar controladores
        lblTitle_AgregarShipped = view.findViewById(R.id.lblTitle_AgregarShipped);
        edtNombre_AgregarShipped = view.findViewById(R.id.edtNombre_AgregarShipped);
        edtContraseña_AgregarShipped = view.findViewById(R.id.edtContraseña_AgregarShipped);
        edtPhone_AgregarShipped = view.findViewById(R.id.edtPhone_AgregarShipped);
        edtDireccion_AgregarShipped = view.findViewById(R.id.edtDireccion_AgregarShipped);
        btnSelect_AgregarShipped = view.findViewById(R.id.btnSelect_AgregarShipped);
        btnUpload_AgregarShipped = view.findViewById(R.id.btnUpload_AgregarShipped);

        //Asignacion de la fuente
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NABILA.TTF");
        lblTitle_AgregarShipped.setTypeface(typeface);

        return view;
    }//onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Conexion a firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Shipped");

        btnUpload_AgregarShipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(getActivity())) {

                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Espere un momento...");
                    progressDialog.show();

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(edtPhone_AgregarShipped.getText().toString()).exists()) {
                                progressDialog.dismiss();
                                //Toast.makeText(SignUp.this, "El telefono ya existe, intente con uno nuevo...", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                ShippedModel shippedModel = new ShippedModel(edtNombre_AgregarShipped.getText().toString(), edtContraseña_AgregarShipped.getText().toString(), edtPhone_AgregarShipped.getText().toString(), edtDireccion_AgregarShipped.getText().toString());
                                databaseReference.child(edtPhone_AgregarShipped.getText().toString()).setValue(shippedModel);
                                Toast.makeText(getActivity(), "¡Registro exitoso!", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }//else
                        }//onDataChange

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }//onCancelled
                    });
                } else {
                    Toast.makeText(getActivity(), "¡Revisa tu Conexion a Internet!", Toast.LENGTH_LONG).show();
                    return;
                }//else

                btnUpload_AgregarShipped.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }//onClick
                });
            }//onClick
        });
    }//onViewCreated

/*
    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("NUEVO REPARTIDOR");
        alertDialog.setMessage("¡Introduce los datos por favor!");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_new_menu = inflater.inflate(R.layout.fragment_agregar_platillo, null);
        edtShippedName = add_new_menu.findViewById(R.id.edt);
        edtShippedPassword = add_new_menu.findViewById(R.id.edtFoodDescription);

        edtFoodPrice = add_new_menu.findViewById(R.id.edtFoodPrice);
        edtFoodDiscount = add_new_menu.findViewById(R.id.edtFoodDiscount);
        edtFoodStatus = add_new_menu.findViewById(R.id.edtFoodStatus);

        btnSelectFood = add_new_menu.findViewById(R.id.btnSelectFood);
        btnUploadFood = add_new_menu.findViewById(R.id.btnUploadFood);

        btnSelectFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }//onClick
        });

        btnUploadFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }//onClick
        });

        alertDialog.setView(add_new_menu);
        alertDialog.setIcon(R.drawable.cart);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (platilloModel != null) {
                    databaseReference.push().setValue(platilloModel);
                }//if
            }//onClick
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }//onClick
        });
        alertDialog.show();
    }//showDialog
 */


}//AgregarShippedFragment