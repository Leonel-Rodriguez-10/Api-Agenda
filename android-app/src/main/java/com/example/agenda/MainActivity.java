package com.example.agenda;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.agenda.adapters.ContactoAdapter;
import com.example.agenda.api.ApiClient;
import com.example.agenda.api.ContactoService;
import com.example.agenda.models.Contacto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ContactoAdapter.OnItemClickListener {

    private ContactoService service;
    private ContactoAdapter adapter;
    private SwipeRefreshLayout swipe;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    cargarContactos();
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = ApiClient.getInstance().create(ContactoService.class);

        RecyclerView recycler = findViewById(R.id.recycler);
        adapter = new ContactoAdapter(new ArrayList<>(), this);
        recycler.setAdapter(adapter);

        swipe = findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this::cargarContactos);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> launcher.launch(new Intent(this, ContactFormActivity.class)));

        cargarContactos();
    }

    private void cargarContactos() {
        swipe.setRefreshing(true);
        service.listar().enqueue(new Callback<List<Contacto>>() {
            @Override
            public void onResponse(Call<List<Contacto>> call, Response<List<Contacto>> response) {
                swipe.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.update(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "Error al cargar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Contacto>> call, Throwable t) {
                swipe.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Fallo: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(Contacto c) {
        Intent i = new Intent(this, ContactFormActivity.class);
        i.putExtra("id", c.getId());
        i.putExtra("nombre", c.getNombre());
        i.putExtra("telefono", c.getTelefono());
        i.putExtra("idCategoria", c.getIdCategoria());
        launcher.launch(i);
    }

    @Override
    public void onLongClick(Contacto c) {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.confirm_delete))
                .setPositiveButton(R.string.delete, (d, w) -> eliminar(c.getId()))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void eliminar(Long id) {
        service.eliminar(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    cargarContactos();
                } else {
                    Toast.makeText(MainActivity.this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fallo: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
