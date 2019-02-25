package com.ticketmgr.rohit.repository;

import com.ticketmgr.rohit.model.Seat;
import com.ticketmgr.rohit.model.SeatHold;
import com.ticketmgr.rohit.model.SeatReserved;

import java.util.List;

public interface RepositoryVenue {

    int getAvailableSeats();

    List<Seat> getSeatsAvailable(int numOfSeats);

    void saveSeatHold(SeatHold seatHold);

    SeatHold findSeatHold(int seatHoldId);

    void addSeats(List<Seat> seats);

    void saveBooking(SeatReserved seatReserved);


}
