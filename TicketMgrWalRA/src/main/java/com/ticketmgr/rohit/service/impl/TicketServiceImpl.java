package com.ticketmgr.rohit.service.impl;

import com.ticketmgr.rohit.Utility.Utilities;
import com.ticketmgr.rohit.model.Seat;
import com.ticketmgr.rohit.model.SeatHold;
import com.ticketmgr.rohit.model.SeatReserved;
import com.ticketmgr.rohit.model.SeatStatus;
import com.ticketmgr.rohit.repository.RepositoryVenue;
import com.ticketmgr.rohit.repository.impl.RepositoryImpl;
import com.ticketmgr.rohit.service.TicketService;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

@Service
public class TicketServiceImpl implements TicketService {

    int timeOut = 40;

    @Autowired
    RepositoryVenue repositoryVenue;

    public TicketServiceImpl(RepositoryImpl repositoryVenue) {
        this.repositoryVenue = repositoryVenue;
    }

    @Override
    public int numSeatsAvailable() {
        return repositoryVenue.getAvailableSeats();
    }

    @Override
    public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail) {

        List<Seat> seatlst = new ArrayList<>();

        if (numSeats > numSeatsAvailable()){
            System.out.println("Number of Seats requested are not available, Please try with a lesser number");
            return null;
        }else{
            String primaryId = Utilities.createID();
            SeatHold seatHold = new SeatHold(primaryId, customerEmail, numSeats);

            // Get Seats that are available
            // @Todo ask for number for seats, Email ID.
            // either hold all of the seats or some of them.

            seatlst = repositoryVenue.getSeatsAvailable(numSeats);
            if(seatlst!=null){
                /*for(int i=0; i<seatlst.size();i++){
                    seatlst.get(i).setStatus(SeatStatus.HELD);
                }*/
                repositoryVenue.addSeats(seatlst);
            }

            // Save Seats in repo
            repositoryVenue.saveSeatHold(seatHold);
            seatHold.setSeatsLst(seatlst);

            return seatHold;

        }

    }

    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {

        {
            String uniqueID = "";
            SeatHold seatHold = repositoryVenue.findSeatHold(seatHoldId);

            if (seatHold != null && customerEmail.equals(seatHold.getCustomerEmail())) {

                for (Seat seat : seatHold.getSeatsLst()) {
                    seat.setStatus(SeatStatus.HELD);
                }

                SeatReserved seatReserved = new SeatReserved(seatHold);
                repositoryVenue.saveBooking(seatReserved);
                uniqueID = seatReserved.getId();

            }

            return uniqueID;
        }
    }






}
