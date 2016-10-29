package de.maurice144.smartdmx.service.message;


import java.io.Serializable;

public abstract class MessageBase implements Serializable {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
