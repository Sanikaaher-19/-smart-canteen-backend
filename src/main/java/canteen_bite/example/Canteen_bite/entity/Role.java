package canteen_bite.example.Canteen_bite.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

//Enums are special classes that can only contain a fixed set of constants.
public enum Role {
    ADMIN,
    STAFF,
    STUDENT,
    CUSTOMER, 
    KITCHEN_STAFF;

     @JsonCreator
    public static Role fromString(String key) {
        return key == null ? null : Role.valueOf(key.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }

    public boolean equalsIgnoreCase(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'equalsIgnoreCase'");
    }
    
}
