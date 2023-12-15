package model;


import java.sql.Blob;

public class PaymentMethod {
    private String type; //Virtual account or e-wallet
    private String paymentName;
    private Blob image;
}
