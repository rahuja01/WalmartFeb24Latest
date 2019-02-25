package com.ticketmgr.rohit.Utility;

import java.util.Comparator;

import com.ticketmgr.rohit.model.Seat;
import com.ticketmgr.rohit.model.SeatStatus;

public class SeatComp implements Comparator<Seat> {

	public int compare(Seat seat1, Seat seat2) {
		if(seat1 == null || seat2 == null) {
			throw new IllegalArgumentException();
		}
		
		if (seat1.getStatus() == SeatStatus.FREE && seat2.getStatus() != SeatStatus.FREE) {
			return -1;
		}else if (seat1.getStatus() == SeatStatus.FREE && seat2.getStatus() == SeatStatus.FREE){
			return 1;
		}
		
		return Integer.compare(seat1.getNumber(), seat2.getNumber());
	}

}
