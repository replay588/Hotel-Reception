package com.replay588.hotelreception.util.hib;

import com.replay588.hotelreception.entity.Customer;
import com.replay588.hotelreception.entity.HotelRoom;
import com.replay588.hotelreception.entity.Reservation;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * ReservationDao implementation
 * @see com.replay588.hotelreception.util.hib.ReservationDao
 * @author replay588
 */
public class ReservationDaoImpl implements ReservationDao{

    /**
     * Save or Update reservation
     * @param reservation
     * @return reservationId of saved reservation
     */
    @Override
    public Long saveOrUpdateReservation(@NotNull Reservation reservation) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            session.saveOrUpdate(reservation);
            tx.commit();
        }catch (HibernateException ex) {
            if (tx != null) tx.rollback();
            throw  ex;
        }finally {
            if (session != null) session.close();
        }
        return reservation.getReservationId();
    }

    /**
     * Save or Update customer
     * @param customer
     * @return customerId of saved customer
     */
    @Override
    public Long saveOrUpdateCustomer(@NotNull Customer customer) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            session.saveOrUpdate(customer);
            tx.commit();
        }catch (HibernateException ex) {
            if (tx != null) tx.rollback();
            throw  ex;
        }finally {
            if (session != null) session.close();
        }
        return customer.getCustomerId();
    }

    /**
     * Save or Update hotelRoom
     * @param hotelRoom
     * @return hotelRoomId of saved hotelRoom
     */
    @Override
    public Long saveOrUpdateHotelRoom(@NotNull HotelRoom hotelRoom) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            session.saveOrUpdate(hotelRoom);
            tx.commit();
        }catch (HibernateException ex) {
            if (tx != null) tx.rollback();
            throw  ex;
        }finally {
            if (session != null) session.close();
        }
        return hotelRoom.getHotelRoomId();
    }

    /**
     * Get Customer by customerId
     * @param customerId
     * @return customer
     */
    @Override
    public Customer getCustomer(int customerId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        Customer customer;

        try{
            tx = session.beginTransaction();
            customer = (Customer) session.get(Customer.class, customerId);
            tx.commit();
        }catch (HibernateException ex) {
            if (tx != null) tx.rollback();
            throw  ex;
        }finally {
            if (session != null) session.close();
        }
        return customer;
    }

    /**
     * Get HotelRoom by hotelRoomId
     * @param hotelRoomId
     * @return hotelRoom
     */
    @Override
    public HotelRoom getHotelRoom(int hotelRoomId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        HotelRoom hotelRoom;

        try{
            tx = session.beginTransaction();
            hotelRoom = (HotelRoom) session.get(HotelRoom.class,hotelRoomId);
            tx.commit();
        }catch (HibernateException ex) {
            if (tx != null) tx.rollback();
            throw  ex;
        }finally {
            if (session != null) session.close();
        }
        return hotelRoom;
    }

    /**
     * Delete reservation by reservationId
     * @param reservationId
     * @return true/false
     */
    @Override
    public boolean deleteReservation(Long reservationId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        Boolean result = false;
        Reservation reservation;

        try{
            tx = session.beginTransaction();
            reservation = (Reservation) session.get(Reservation.class, reservationId);
            session.delete(reservation);
            result = true;
            tx.commit();
        }catch (HibernateException ex) {
            if (tx != null) tx.rollback();
            throw  ex;
        }finally {
            if (session != null) session.close();
        }
        return result;
    }

    /**
     * Delete reservation by customerId
     * @param customerId
     * @return true/false
     */
    @Override
    public boolean deleteCustomer(Long customerId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        Boolean result = false;
        Customer customer;

        try{
            tx = session.beginTransaction();
            customer = (Customer) session.get(Customer.class, customerId);
            session.delete(customer);
            result = true;
            tx.commit();
        }catch (HibernateException ex) {
            if (tx != null) tx.rollback();
            throw  ex;
        }finally {
            if (session != null) session.close();
        }
        return result;
    }

    /**
     * Load all Reservations from Database
     * @return List<Reservation>
     */
    @Override
    public List<Reservation> getAllReservations() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        List result = null;
        try{
            tx = session.beginTransaction();
            result = session.createQuery("FROM Reservation ").list();
            tx.commit();
        }catch (HibernateException ex) {
            if (tx != null) tx.rollback();
            throw ex;
        }finally {
            if (session != null) session.close();
        }
        return (List<Reservation>) result;
    }

    /**
     * Load all Customers from Database
     * @return List<Customer>
     */
    @Override
    public List<Customer> getAllCustomers() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        List result = null;
        try{
            tx = session.beginTransaction();
            result = session.createQuery("FROM Customer ").list();
            tx.commit();
        }catch (HibernateException ex) {
            if (tx != null) tx.rollback();
            throw ex;
        }finally {
            if (session != null) session.close();
        }
        return (List<Customer>) result;
    }

    /**
     * Load all HotelRooms from Database
     * @return List<HotelRoom>
     */
    @Override
    public List<HotelRoom> getAllHotelRooms() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        List result = null;
        try{
            tx = session.beginTransaction();
            result = session.createQuery("FROM HotelRoom ").list();
            tx.commit();
        }catch (HibernateException ex) {
            if (tx != null) tx.rollback();
            throw ex;
        }finally {
            if (session != null) session.close();
        }
        return (List<HotelRoom>) result;
    }
}
