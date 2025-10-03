package com.example.agenda;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.api.ApiClient;
import com.example.agenda.api.ContactoService;
import com.example.agenda.models.Contacto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactFormActivity extends AppCompatActivity {

    private EditText etName, etPhone, etCategoryId;
    private ContactoService service;
    private Long idEdicion = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etCategoryId = findViewById(R.id.etCategoryId);
        Button btnSave = findViewById(R.id.btnSave);

        service = ApiClient.getInstance().create(ContactoService.class);

        if (getIntent() != null && getIntent().hasExtra("id")) {
            idEdicion = getIntent().getLongExtra("id", 0);
            etName.setText(getIntent().getStringExtra("nombre"));
            etPhone.setText(getIntent().getStringExtra("telefono"));
            etCategoryId.setText(String.valueOf(getIntent().getIntExtra("idCategoria", 0)));
        }

        btnSave.setOnClickListener(v -> guardar());
    }

    private void guardar() {
        String nombre = etName.getText().toString().trim();
        String telefono = etPhone.getText().toString().trim();
        String cat = etCategoryId.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(telefono) || TextUtils.isEmpty(cat)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int idCategoria = Integer.parseInt(cat);
        Contacto body = new Contacto();
        body.setNombre(nombre);
        body.setTelefono(telefono);
        body.setIdCategoria(idCategoria);

        Callback<Contacto> cb = new Callback<Contacto>() {
            @Override
            public void onResponse(Call<Contacto> call, Response<Contacto> response) {
                if (response.isSuccessful()) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ContactFormActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contacto> call, Throwable t) {
                Toast.makeText(ContactFormActivity.this, "Fallo: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        if (idEdicion == null) {
            service.crear(body).enqueue(cb);
        } else {
            service.actualizar(idEdicion, body).enqueue(cb);
        }
    }
}
