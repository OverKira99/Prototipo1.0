package com.alejandroBel.prototipo10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private Button btnCrearCuenta;
    private EditText etName, etEmail, etPsswrd;
    private TextView tvlogin;
    FirebaseFirestore miFirestore;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        EditText etname =findViewById(R.id.et_name_register);

        EditText etEmail =findViewById(R.id.et_email_register);
      EditText etPsswrd = findViewById(R.id.et_psswrd_register);
        Button btnCrearCuenta =findViewById(R.id.btn_crear_cuenta);
        TextView tvlogin = findViewById(R.id.tv_login);
        //base de datos
        miFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene el texto de los EditText
                String nameUser = etName.getText().toString().trim();
                String emailUser = etEmail.getText().toString().trim();
                String passwordUser= etPsswrd.getText().toString().trim();

                // Verifica si algún campo está vacío
                if(nameUser.isEmpty() && emailUser.isEmpty() && passwordUser.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Complete los datos", Toast.LENGTH_SHORT).show();
                    // Si no hay campos vacíos, procede con el registro
                }else{
                    registerUser(nameUser, emailUser, passwordUser);

                }
            }
        });

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad "activity_register"
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                // Finalizar esta actividad
                finish();
            }
        });
    }

// guardar los datos en la base de datos
    private void registerUser(String nameUser, String emailUser, String passwordUser) {
        mAuth.createUserWithEmailAndPassword(emailUser,passwordUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("name", nameUser);
                map.put("email", emailUser);
                map.put("password", passwordUser);

                miFirestore.collection("User").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        Toast.makeText(RegisterActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });


    }


}