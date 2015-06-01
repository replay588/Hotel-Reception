package com.replay588.hotelreception.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Entity implementation class for Entity: Customer
 * @author replay588
 */
@Entity
@Table(name = "res_men_customer", uniqueConstraints = {
        @UniqueConstraint(columnNames = "CUSTOMER_ID")
})
public class Customer implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID", unique = true, nullable = false)
    private long customerId;

    @Column(name = "NAME",  unique = false, nullable = false)
    private String name;

    public Customer(@NotNull String name) throws IllegalArgumentException{
            this.name = name;
    }

    public Customer() {
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name){
            this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
