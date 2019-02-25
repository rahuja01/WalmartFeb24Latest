package com.ticketmgr.rohit.model;

public class SeatReserved {

    private String id;
    private SeatHold seatHold;

    public SeatReserved(SeatHold seatHold) {
        this.seatHold = seatHold;
    }

    public SeatHold getSeatHold() {
        return seatHold;
    }

    public void setSeatHold(SeatHold seatHold) {
        this.seatHold = seatHold;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
