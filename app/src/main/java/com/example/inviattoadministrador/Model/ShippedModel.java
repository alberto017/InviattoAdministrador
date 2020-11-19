package com.example.inviattoadministrador.Model;

public class ShippedModel {
    private String Name;
    private String Password;
    private String Image;
    private String Phone;
    private String Address;

    public ShippedModel() {
    }

    public ShippedModel(String name, String password, String phone, String address) {
        Name = name;
        Password = password;
        Phone = phone;
        Address = address;
    }

    public ShippedModel(String name, String password, String image, String phone, String address) {
        Name = name;
        Password = password;
        Image = image;
        Phone = phone;
        Address = address;
    }//ShippedModel

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

}//ShippedModel
