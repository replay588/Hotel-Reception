package com.replay588.hotelreception.entity;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Entity implementation class for Entity: HotelRoom
 * @author replay588
 */
@Entity
@Table(name = "res_men_hotelroom", uniqueConstraints = {
        @UniqueConstraint(columnNames = "HOTELROOM_ID"),
        @UniqueConstraint(columnNames = "NUMBER")
})
public class HotelRoom implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOTELROOM_ID", unique = true, nullable = false)
    private long hotelRoomId;

    @Column(name = "NUMBER", unique = true, nullable = false)
    private int number;

    @Column(name = "AVAILABILITY")
    private boolean availability;

    @Column(name = "NUM_OF_ROOMS")
    private int numOfRooms;

    @Column(name = "PRICE_FOR_ONE_DAY")
    private double priceForOneDay;

    @OneToOne(mappedBy = "hotelRoom")
    private Reservation reservation;

    public HotelRoom(int number, int numOfRooms, double priceForOneDay) {
        this.number = number;
        this.numOfRooms = numOfRooms;
        this.priceForOneDay = priceForOneDay;
        this.availability = true;
    }

    public HotelRoom() {
    }

    public Long getHotelRoomId() {
        return hotelRoomId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isAvailability() {
        return availability;
    }

    /**
     * Set availability for hotelRoom
     * @param availability
     * @return hotelRoom
     */
    public HotelRoom setAvailability(boolean availability) {
        this.availability = availability;
        return this;
    }

    public int getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public double getPriceForOneDay() {
        return priceForOneDay;
    }

    public void setPriceForOneDay(double price) {
        this.priceForOneDay = price;
    }

}
