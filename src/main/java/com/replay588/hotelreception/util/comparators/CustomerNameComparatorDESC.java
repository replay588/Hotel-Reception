package com.replay588.hotelreception.util.comparators;

import com.replay588.hotelreception.entity.Reservation;

import java.util.Comparator;

/**
 * CustomerNameComparatorDESC
 * Sort the names of customers in DESC order
 * @author replay588
 */
public class CustomerNameComparatorDESC implements Comparator<Reservation> {

    @Override
    public int compare(Reservation o1, Reservation o2) {
        int ret = o1.getCustomerName().compareTo(o2.getCustomerName());

        if (ret < 0)
            return 1;
        if (ret > 0)
            return -1;
        else
            return 0;
    }
}
