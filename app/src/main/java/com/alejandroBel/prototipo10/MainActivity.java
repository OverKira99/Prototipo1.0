package com.alejandroBel.prototipo10;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Auth y Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Verificar si el usuario ha iniciado sesión y actualizar la UI
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            loadUserProfile(currentUser.getUid());
        }
    }

    private void loadUserProfile(String userId) {
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Aquí puedes obtener los datos del usuario y mostrarlos en la UI
                        String name = document.getString("name");
                        String email = document.getString("email");
                        // Actualiza tu UI con los datos del usuario
                        updateUI(name, email);
                    } else {
                        Log.d("TAG", "No such document");

                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    private void updateUI(String name, String email) {
        // Actualiza la interfaz de usuario con los datos del usuario
        TextView nameTextView = findViewById(R.id.tv_name);
        TextView emailTextView = findViewById(R.id.tv_email);
        nameTextView.setText(name);
        emailTextView.setText(email);
    }
}