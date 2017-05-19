package com.example.pavilion.proyectomoviles.Services;

/**
 * Created by Pavilion on 18/5/2017.
 */
public class DeficientModel {
    private String name;
    private String description;
    private float R;
    private float G;
    private float B;
    private float A;

    public DeficientModel(String name, String description, float r, float g, float b, float a) {
        this.name = name;
        this.description = description;
        R = r;
        G = g;
        B = b;
        A = a;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getR() {
        return R;
    }

    public void setR(float r) {
        R = r;
    }

    public float getG() {
        return G;
    }

    public void setG(float g) {
        G = g;
    }

    public float getB() {
        return B;
    }

    public void setB(float b) {
        B = b;
    }

    public float getA() {
        return A;
    }

    public void setA(float a) {
        A = a;
    }
}
