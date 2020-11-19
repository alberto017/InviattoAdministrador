package com.example.inviattoadministrador.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.widget.FButton;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inviattoadministrador.Common.Common;
import com.example.inviattoadministrador.Interface.IItemClickListener;
import com.example.inviattoadministrador.Model.ShippedModel;
import com.example.inviattoadministrador.R;
import com.example.inviattoadministrador.ViewHolder.ShippedViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class RepartidorListaFragment extends Fragment {

    //Inicializacion de variables
    private FrameLayout flShippedLista;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FloatingActionButton fbAddShipped;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private TextView lblCategoryID;
    private String categoryID = "";

    private RecyclerView rvShipped;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<ShippedModel, ShippedViewHolder> adapter;

    private FirebaseRecyclerAdapter<ShippedModel, ShippedViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    private MaterialSearchBar searchBar;

    //Agregar layout de menu
    private MaterialEditText lblTitle_AgregarShipped;
    private MaterialEditText edtNombre_AgregarShipped;
    private MaterialEditText edtContraseña_AgregarShipped;
    private MaterialEditText edtPhone_AgregarShipped;
    private MaterialEditText edtDireccion_AgregarShipped;
    private FButton btnSelect_AgregarShipped;
    private FButton btnUpload_AgregarShipped;

    ShippedModel shippedModel;
    Uri saveUri;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_repartidor_lista, container, false);

        flShippedLista = view.findViewById(R.id.flShippedLista);
        //flCategoriaLista = view.findViewById(R.id.flCategoriaLista);
        searchBar = view.findViewById(R.id.searchBar);
        rvShipped = view.findViewById(R.id.rvShipped);

        //Conexion e instanciacion a  firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Shipped");
        fbAddShipped = view.findViewById(R.id.fbAddShipped);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images/");

        //Cargar platillos
        rvShipped.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvShipped.setLayoutManager(layoutManager);

        cargarShipped();

        //Search
        searchBar.setHint("Inserta el repartidor");

        loadSuggest();
        searchBar.setLastSuggestions(suggestList);
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }//beforeTextChanged

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(searchBar.getText().toLowerCase())) {
                        suggest.add(search);
                    }//if
                }//for
            }//onTextChanged

            @Override
            public void afterTextChanged(Editable editable) {

            }//afterTextChanged
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //Restauramos el adaptador cuando la busqueda se cierra
                if (!enabled) {
                    rvShipped.setAdapter(adapter);
                }//if
            }//onSearchStateChanged

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //Mostramos resultado cuando la busqueda termina
                startSearch(text);
            }//onSearchConfirmed

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        fbAddShipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setFrament(new AgregarShippedFragment());
                showDialog();
            }//onClick
        });

        return view;
    }//onCreateView

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<ShippedModel, ShippedViewHolder>(
                ShippedModel.class,
                R.layout.shipper_item,
                ShippedViewHolder.class,
                databaseReference.orderByChild("Name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(ShippedViewHolder shippedViewHolder, ShippedModel shippedModel, int i) {

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Espere un momento...");
                progressDialog.show();

                shippedViewHolder.lblShipped_name.setText(shippedModel.getName());
                Picasso.with(getActivity().getBaseContext()).load(shippedModel.getImage())
                        .into(shippedViewHolder.lblShipped_image);
                shippedViewHolder.lblShipped_password.setText(shippedModel.getPassword());
                shippedViewHolder.lblShipped_phone.setText(shippedModel.getPhone());
                shippedViewHolder.lblShipped_address.setText(shippedModel.getAddress());

                progressDialog.dismiss();

                //Asignacion de la fuente
                Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NABILA.TTF");
                shippedViewHolder.lblShipped_name.setTypeface(typeface);

                final ShippedModel shipped = shippedModel;
                shippedViewHolder.setItemClickListener(new IItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getActivity(), "¡Selecciono un repartidor!", Toast.LENGTH_LONG).show();
                        /*
                        PlatilloDetalleFragment platilloDetalleFragment = new PlatilloDetalleFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("FoodID", searchAdapter.getRef(position).getKey());
                        platilloDetalleFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        //fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                        fragmentTransaction.replace(flPlatilloLista.getId(), platilloDetalleFragment);
                        fragmentTransaction.commit();
                         */
                    }//onClick
                });
            }//populateViewHolder
        };
        rvShipped.setAdapter(searchAdapter);
    }//startSearch

    private void loadSuggest() {

        databaseReference.orderByChild("menuID").equalTo(categoryID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            ShippedModel item = postSnapshot.getValue(ShippedModel.class);
                            suggestList.add(item.getName());
                        }//for
                    }//onDataChange

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }//onCancelled
                });

    }//loadSuggest


    private void cargarShipped() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Espere un momento...");
        progressDialog.show();

        adapter = new FirebaseRecyclerAdapter<ShippedModel, ShippedViewHolder>(ShippedModel.class, R.layout.shipper_item, ShippedViewHolder.class, databaseReference) {

            @Override
            protected void populateViewHolder(ShippedViewHolder shippedViewHolder, ShippedModel shippedModel, int i) {
                shippedViewHolder.lblShipped_name.setText(shippedModel.getName());
                Picasso.with(getActivity().getBaseContext()).load(shippedModel.getImage())
                        .into(shippedViewHolder.lblShipped_image);
                shippedViewHolder.lblShipped_password.setText(shippedModel.getPassword());
                shippedViewHolder.lblShipped_phone.setText(shippedModel.getPhone());
                shippedViewHolder.lblShipped_address.setText(shippedModel.getAddress());

                progressDialog.dismiss();

                //Asignacion de la fuente
                Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NABILA.TTF");
                shippedViewHolder.lblShipped_name.setTypeface(typeface);

                final ShippedModel shipped = shippedModel;
                shippedViewHolder.setItemClickListener(new IItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        /*
                        PlatilloDetalleFragment platilloDetalleFragment = new PlatilloDetalleFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("FoodID", adapter.getRef(position).getKey());
                        platilloDetalleFragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        //fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                        fragmentTransaction.replace(flPlatilloLista.getId(), platilloDetalleFragment);
                        fragmentTransaction.commit();
                         */
                    }//onClick
                });
            }//populateViewHolder
        };
        rvShipped.setAdapter(adapter);
    }//cargarPlatillo


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            saveUri = data.getData();
            //btnSelectFood.setText("¡Imagen seleccionada!");
        }//if
    }//onActivityResult


    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("NUEVO REPARTIDOR");
        alertDialog.setMessage("¡Introduce los datos por favor!");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_new_menu = inflater.inflate(R.layout.fragment_agregar_shipped, null);
        edtNombre_AgregarShipped = add_new_menu.findViewById(R.id.edtNombre_AgregarShipped);
        edtContraseña_AgregarShipped = add_new_menu.findViewById(R.id.edtContraseña_AgregarShipped);
        edtPhone_AgregarShipped = add_new_menu.findViewById(R.id.edtPhone_AgregarShipped);
        edtDireccion_AgregarShipped = add_new_menu.findViewById(R.id.edtDireccion_AgregarShipped);

        btnSelect_AgregarShipped = add_new_menu.findViewById(R.id.btnSelect_AgregarShipped);
        btnUpload_AgregarShipped = add_new_menu.findViewById(R.id.btnUpload_AgregarShipped);

        btnSelect_AgregarShipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }//onClick
        });

        btnUpload_AgregarShipped.setOnClickListener(new View.OnClickListener() {
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
                if (shippedModel != null) {
                    databaseReference.push().setValue(shippedModel);
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


    private void chooseImage() {
        Intent select = new Intent();
        select.setType("image/*");
        select.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(select, "Selecciona imagen"), Common.PICK_IMAGE_REQUEST);
    }//chooseImage

    private void uploadImage() {
        if (saveUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Subiendo...");
            progressDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "¡Carga finalizada!", Toast.LENGTH_LONG).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //shippedModel = new ShippedModel(edtCategoryName.getText().toString(), uri.toString());
                                    shippedModel = new ShippedModel();

                                    shippedModel.setName(edtNombre_AgregarShipped.getText().toString());
                                    shippedModel.setPassword(edtPhone_AgregarShipped.getText().toString());
                                    shippedModel.setPhone(edtPhone_AgregarShipped.getText().toString());
                                    shippedModel.setAddress(edtDireccion_AgregarShipped.getText().toString());
                                    //platilloModel.setMenuID(categoryID);
                                    shippedModel.setImage(uri.toString());
                                }//onSuccess
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }//onFailure
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Carga finalizada " + progress + "%");
                        }//onProgress
                    });
        }//if
    }//uploadImage

    //Llamo fragment de menu de productos
    private void setFrament(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(flShippedLista.getId(), fragment);
        fragmentTransaction.commit();
    }//setFrament

}//RepartidorListaFragment