package com.mycompany.sneaksapp.logica;

import java.util.Random;

public class Zapato 
{
    private int shoeid = generarShoeidAleatorio(); //OJO aquí
    private String Brand;
    private String Model;
    private byte size;
    private String color;
    private String ubication;
    private int price;
    private String image;
    

    
    
    public Zapato() {
        
    }

    public Zapato(String Brand, String Model, byte size, String color, String ubication, int price, String image) {
        this.shoeid = generarShoeidAleatorio();
        this.Brand = Brand;
        this.Model = Model;
        this.size = size;
        this.color = color;
        this.ubication = ubication;
        this.price = price;
        this.image = image;
    }

    public String toCSV() {
        return shoeid + "," + Brand + "," + Model + "," + size + "," + color + "," + ubication + "," + price + "," + image;
    }
    
    private int generarShoeidAleatorio() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // Genera un número entre 1000 y 9999
    }

    // Getters y Setters sin parámetros innecesarios
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getShoeid() {
        return shoeid;
    }

    public void setShoeid(int shoeid) {
        this.shoeid = shoeid;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String Brand) {
        this.Brand = Brand;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public byte getSize() {
        return size;
    }

    public void setSize(byte size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUbication() {
        return ubication;
    }

    public void setUbication(String ubication) {
        this.ubication = ubication;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
  
    
}
