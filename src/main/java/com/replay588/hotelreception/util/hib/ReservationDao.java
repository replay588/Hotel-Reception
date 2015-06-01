package com.replay588.hotelreception.util.hib;

import com.replay588.hotelreception.entity.Customer;
import com.replay588.hotelreception.entity.HotelRoom;
import com.replay588.hotelreception.entity.Reservation;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Interface ReservationDao
 * @author replay588
 */
public interface ReservationDao {

    Long saveOrUpdateReservation(@NotNull Reservation reservation);

    Long saveOrUpdateCustomer(@NotNull Customer customer);

    Long saveOrUpdateHotelRoom(@NotNull HotelRoom hotelRoom);

    Customer getCustomer(int customerId);

    HotelRoom getHotelRoom(int hotelRoomId);

    boolean deleteReservation(Long reservationId);

    boolean deleteCustomer(Long customerId);

    List<Reservation> getAllReservations();

    List<Customer> getAllCustomers();

    List<HotelRoom> getAllHotelRooms();
}
