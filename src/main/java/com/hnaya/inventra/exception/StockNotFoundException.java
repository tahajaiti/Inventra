package com.hnaya.inventra.exception;

public class StockNotFoundException extends RuntimeException{
    public StockNotFoundException (String message) {
        super(message);
    }
}
