package com.aga.hcp.home_control_prototype.Spark;

/**
 * Created by garygraham on 2014-10-14.
 */
public class Token {
    private String expiryDate;
    private String value;

    public Token(String value, String expiryDate){
        this.value = value;
        this.expiryDate = expiryDate;
    }

    public String getValue() {
        return value;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public boolean expiresSoon() {
        //if expires in under 1 day, return true;
        return false;
    }

    @Override
    public String toString(){
        return this.value;
    }

}
