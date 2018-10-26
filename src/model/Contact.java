package model;

import java.security.PublicKey;

public class Contact implements IContact {

    private String name;
    private String ipAddr;
    private PublicKey publicKey;

    public Contact(String name, String ipAddr) {
        this.name = name;
        this.ipAddr = ipAddr;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getIPAddr() {
        return this.ipAddr;
    }

    @Override
    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    @Override
    public void setPublicKey(PublicKey newPublicKey) {
        this.publicKey = newPublicKey;
    }

}
