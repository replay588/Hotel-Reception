package com.replay588.hotelreception.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

/**
 * Entity implementation class for Entity: Reservation
 * @author replay588
 */
@Entity
@Table(name = "res_men_reservation")
public class Reservation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "RESERVATION_ID")
	private long reservationId;


	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;

	@OneToOne
	@JoinColumn(name = "HOTELROOM_ID")
	private HotelRoom hotelRoom;

	@Column(name = "CHECK_IN", nullable = false)
	private LocalDate checkIn;

	@Column(name = "CHECK_OUT", nullable = false)
	private LocalDate checkOut;

	@Column(name = "NUM_OF_ADULTS")
	private int numOfAdults;

	@Column(name = "NUM_OF_CHILDREN")
	private int numOfChildren;

	@Column(name = "PRICE")
	private double price;

	private boolean approved;

	public Reservation(Customer customer, HotelRoom hotelRoom, LocalDate checkIn, LocalDate checkOut, int numOfAdults, int numOfChildren) {
		this.customer = customer;
		this.hotelRoom = hotelRoom;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.numOfAdults = numOfAdults;
		this.numOfChildren = numOfChildren;
		this.approved = false;
		this.price = getCountOfFullPrice();
	}

	public Reservation() {
	}

	public HotelRoom getHotelRoom() {
		return hotelRoom;
	}

	public Customer getCustomer() {
		return customer;
	}

	public long getReservationId() {
		return reservationId;
	}


	public String getCustomerName() {
		return customer.getName();
	}

	public LocalDate getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(LocalDate checkIn) {
		this.checkIn = checkIn;
	}

	public LocalDate getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(LocalDate checkOut) {
		this.checkOut = checkOut;
	}

	public int getNumOfAdults() {
		return numOfAdults;
	}

	public void setNumOfAdults(int numOfAdults) {
		this.numOfAdults = numOfAdults;
	}

	public int getNumOfChildren() {
		return numOfChildren;
	}

	public void setNumOfChildren(int numOfChildren) {
		this.numOfChildren = numOfChildren;
	}

	public Double getPrice() {
		return price;
	}

	/**
	 * Calculate full reservation cost
	 * @return reservation price
	 */
	public Double getCountOfFullPrice() {
		Double result = null;
		Double priseForOneDay = hotelRoom.getPriceForOneDay();
		Period period = getCheckIn().until(getCheckOut());
		Integer numOfDays = period.getDays();
		result = priseForOneDay * numOfDays;
		return result ;
	}

	public Integer getHotelRoomNumber() {
		return hotelRoom.getNumber();
	}

	public boolean isApproved() {
		return approved;
	}

	public Reservation setApproved(boolean approved) {
		this.approved = approved;
		return this;
	}
}
