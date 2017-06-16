package com.example.pavilion.proyectomoviles.Services;

/**
 * Created by Pavilion on 18/5/2017.
 */
public class Deficient {
    private int id;
    private String name;
    private String description;
    private double R;
    private double G;
    private double B;
    private double A;
    private static Deficient instance = null;

    public Deficient() {
        id = 0;
        this.name = "";
        this.description = "";
        R = 0.0;
        G = 0.0;
        B = 0.0;
        A = 0.0;
    }

    public void setDeficient(Deficient deficient) {
        this.instance = deficient;
    }

    public void FillInformation(int id, String name, String description, double r, double g, double b, double a) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.R = r;
        this.G = g;
        this.B = b;
        this.A = a;
    }

    public static Deficient getInstance() {
        if(instance == null) {
            instance = new Deficient();
        }
        return instance;
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

    public double getR() {
        return R;
    }

    public void setR(double r) {
        R = r;
    }

    public double getG() {
        return G;
    }

    public void setG(double g) {
        G = g;
    }

    public double getB() {
        return B;
    }

    public void setB(double b) {
        B = b;
    }

    public double getA() {
        return A;
    }

    public void setA(double a) {
        A = a;
    }

    public static void setInstance(Deficient instance) {
        Deficient.instance = instance;
    }
}
