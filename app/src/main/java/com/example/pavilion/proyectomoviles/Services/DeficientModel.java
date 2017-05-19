package com.example.pavilion.proyectomoviles.Services;

/**
 * Created by Pavilion on 18/5/2017.
 */
public class DeficientModel {
    private String name;
    private String description;
    private int R;
    private int G;
    private int B;
    private int A;

    public DeficientModel(String name, String description, int r, int g, int b, int a) {
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

    public int getR() {
        return R;
    }

    public void setR(int r) {
        R = r;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

    public int getA() {
        return A;
    }

    public void setA(int a) {
        A = a;
    }
}
