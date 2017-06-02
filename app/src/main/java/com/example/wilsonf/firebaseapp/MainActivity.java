package com.example.wilsonf.firebaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button botonRegister, botonAutenticar, botonCargueMasivo, botonCrearCliente , botonConsultarCliente;
    private EditText editTextEmail, editTextNombre, editTextApellido, editTextEdad;
    private EditText editTextPassword;
    private TextView textViewSingIn;
    private GridView grilla;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        botonRegister =(Button) findViewById(R.id.buttonRegister);
        botonAutenticar =(Button) findViewById(R.id.buttonAutenticar);
        botonCargueMasivo =(Button) findViewById(R.id.botonCargueMasivo);
        botonCrearCliente =(Button) findViewById(R.id.buttonCrearCliente);
        botonConsultarCliente =(Button) findViewById(R.id.buttonConsultarCliente);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword= (EditText) findViewById(R.id.editTextPassword);
        textViewSingIn= (TextView) findViewById(R.id.textViewSingin);
        editTextNombre= (EditText) findViewById(R.id.editTextNombre);
        editTextApellido= (EditText) findViewById(R.id.editTextApellido);
        editTextEdad= (EditText) findViewById(R.id.editTextEdad);


        grilla = (GridView) findViewById(R.id.grilla);

        botonRegister.setOnClickListener(this);
        botonAutenticar.setOnClickListener(this);
        botonCargueMasivo.setOnClickListener(this);
        botonCrearCliente.setOnClickListener(this);
        botonConsultarCliente.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        if(v == botonRegister)
        {
            RegisterUser();
        }
        if(v == botonAutenticar)
        {
            Autenticar();
        }
        if(v == botonCargueMasivo)
        {
            CargueMasivo();
        }
        if(v == botonCrearCliente)
        {
            CrearCliente();
        }
        if(v == botonConsultarCliente)
        {
            ConsultarClientes();
        }

    }

    private void RegisterUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        //Crear Usuario
        firebaseAuth.createUserWithEmailAndPassword(email,password);
    }

    private void Autenticar() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        //Autenticacion usuario
        firebaseAuth.signInWithEmailAndPassword(email,password);

        //Recuperar usuario
        FirebaseUser usuario = firebaseAuth.getCurrentUser();
        textViewSingIn.setText( usuario.getUid()+"|"+ usuario.getEmail());
    }

    private void CargueMasivo() {
        //Registrar Informacion BD

        Cliente cliente1 = new Cliente();
        cliente1.nombre ="Camila";
        cliente1.Apellido="Perez";
        cliente1.Edad =12;
        Cliente cliente2 = new Cliente();
        cliente2.nombre ="Daniel";
        cliente2.Apellido="Perez";
        cliente2.Edad =16;

        List<Cliente> clientes = new ArrayList<Cliente>() ;
        clientes.add(cliente1);
        clientes.add(cliente2);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        String id = firebaseAuth.getCurrentUser().getUid();

        databaseReference.child("User").child(id).setValue(clientes);
        databaseReference.child("Client").child(id).setValue(clientes);
    }

    private void CrearCliente() {


        String nombre = editTextNombre.getText().toString();
        String apellido= editTextApellido.getText().toString();;
        int edad = Integer.parseInt(editTextEdad.getText().toString());

        Cliente cliente = new Cliente();
        cliente.nombre=nombre;
        cliente.Apellido=apellido;
        cliente.Edad=edad;

        String id = firebaseAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> postValues = cliente.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        String key = databaseReference.child("Client").push().getKey();
        childUpdates.put("/Client/"+id+"/"+key, postValues);

        databaseReference.updateChildren(childUpdates);
    }

    private void ConsultarClientes() {
        //Consultar Informacon DB

        String id = firebaseAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();



        Query query = databaseReference.child("User").child(id).orderByChild("Apellido");//.equalTo("Gomez");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshots) {
                ArrayAdapter<String> adaptador = new ArrayAdapter<String>( MainActivity.this,android.R.layout.simple_list_item_1);

                for (DataSnapshot snapshot: snapshots.getChildren()) {
                    Cliente user = snapshot.getValue(Cliente.class);
                    adaptador.add(("Nombre: "+user.nombre+" \nApellido: "+ user.Apellido+ "\nEdad:"+user.Edad));
                }
                grilla.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        });
    }


}


