package com.example.wilsonf.firebaseapp;

import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WilsonF on 25/04/2017.
 */

public class Cliente
{



    public String nombre;
    public String Apellido;
    public int Edad;





    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nombre", nombre);
        result.put("Apellido", Apellido);
        result.put("Edad", Edad);
        return result;
    }
}