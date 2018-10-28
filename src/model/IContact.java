package model;

import java.io.Serializable;
import java.security.PublicKey;

public interface IContact extends Serializable {

    String getName();
    String getIPAddr();
    PublicKey getPublicKey();
    void setPublicKey(PublicKey newPublicKey);

}