package com.ticketmgr.rohit.repository.impl;

import com.ticketmgr.rohit.Utility.SeatComp;
import com.ticketmgr.rohit.model.Seat;
import com.ticketmgr.rohit.model.SeatHold;
import com.ticketmgr.rohit.model.SeatReserved;
import com.ticketmgr.rohit.model.SeatStatus;
import com.ticketmgr.rohit.repository.RepositoryVenue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class RepositoryImpl implements RepositoryVenue {


    int countSeatsAvail;
    // contains the available seats - FIFO for the customer.
    PriorityBlockingQueue<Seat> availSeats =null;

    // Map to hold and allocate the seats.
    ConcurrentHashMap<String, SeatHold> heldSeats = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, SeatReserved> allocatedSeats= new ConcurrentHashMap<>();

    ScheduledExecutorService service = null;

    public RepositoryImpl(int countSeatsAvail) {

        this.countSeatsAvail = countSeatsAvail;
        availSeats = new PriorityBlockingQueue<>(countSeatsAvail,new SeatComp());
        addingNewSeats(countSeatsAvail);

    }

    public int getAvailableSeats() {
        return countSeatsAvail;
    }

    @Override
    public List<Seat> getSeatsAvailable(int numOfSeats) {

        List<Seat> seatList = new ArrayList<>();

        while (numOfSeats > 0) {
            seatList.add(availSeats.poll());
            numOfSeats--;
        }

        return seatList;
    }

    @Override
    public void saveSeatHold(SeatHold seatHold) {

        countSeatsAvail = countSeatsAvail - seatHold.getNumSeats();
        seatHold.setId(seatHold.getId());
        heldSeats.put(seatHold.getId(), seatHold);

        new Thread(() -> {

            try {
                if (seatHold.getNumSeats() > 0 && seatHold.getSeatsLst().get(0).getStatus() == SeatStatus.HELD) {
                    for (Seat seat : seatHold.getSeatsLst()) {
                        seat.setStatus(SeatStatus.FREE);
                        availSeats.remove(seat);
                    }
                    addSeats(seatHold.getSeatsLst());
                    countSeatsAvail += seatHold.getSeatsLst().size();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }



    @Override
    public SeatHold findSeatHold(int seatHoldId) {
        SeatHold seatHold = null;

        if (heldSeats.containsKey(seatHoldId)) {
            seatHold = heldSeats.get(seatHoldId);
        }else{
            System.out.println("We dont see this ID held in our system, So we will reserve a new ID for yaa");
        }

        return seatHold;
    }

    @Override
    public void addSeats(List<Seat> seats) {
            for (Seat seat : seats) {
                //availSeats.add(seat);
                //int seatNumber = seat.getNumber();
                availSeats.remove(seat);
                seat.setStatus(SeatStatus.HELD);
                availSeats.add(seat);

                }

    }

    private void addingNewSeats(int countSeatsAvail) {
        for (int i = 0; i < countSeatsAvail; i++) {
            Seat seat = new Seat();
            seat.setNumber(i);
            seat.setStatus(SeatStatus.FREE);
            availSeats.add(seat);
        }
    }

    public void saveBooking(SeatReserved seatReserved) {

        String zeros = "000000";
        Random rnd = new Random();
        String s = Integer.toString(rnd.nextInt(0X1000000), 16);
        s = zeros.substring(s.length()) + s;

        String id = s + seatReserved.getSeatHold();

        seatReserved.setId(id);
        allocatedSeats.put(id, seatReserved);
    }

}
