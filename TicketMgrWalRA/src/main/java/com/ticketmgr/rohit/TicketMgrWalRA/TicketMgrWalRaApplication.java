package com.ticketmgr.rohit.TicketMgrWalRA;

import com.ticketmgr.rohit.Utility.EmailValidator;
import com.ticketmgr.rohit.model.SeatHold;
import com.ticketmgr.rohit.repository.impl.RepositoryImpl;
import com.ticketmgr.rohit.service.impl.TicketServiceImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.Scanner;

@SpringBootApplication
public class TicketMgrWalRaApplication {

    static TicketServiceImpl ticketService = null;
    static Scanner scanner;
    static EmailValidator emailValidator = new EmailValidator();

    public static void main(String[] args) {

        //SpringApplication.run(TicketMgrWalRaApplication.class, args);

        boolean flag = true;

        scanner = new Scanner(System.in);
        System.out.print("Enter the Total number of seats for the TicketMgt System ");
        String numOfSeats = scanner.nextLine();

        RepositoryImpl venuRep = new RepositoryImpl(Integer.parseInt(numOfSeats));
        ticketService = new TicketServiceImpl(venuRep);


            //System.out.println("Enter 1 to view total available seats OR 2 to hold seats OR 3 to allocate seats OR 4 to Quit ");
            //String choice = scanner.nextLine();
            int choiceSel = 1;

            try {
                //choiceSel = Integer.parseInt(choice);


                while(choiceSel!=0){
                    System.out.println("Enter 1 to view total available seats OR 2 to hold seats OR 3 to allocate seats OR 4 to Quit ");

                    String choice = scanner.nextLine();
                    if(choice!= "" || choice!=null){
                        choiceSel = Integer.parseInt(choice);
                    }else{
                        System.out.println("Enter 1 to view total available seats OR 2 to hold seats OR 3 to allocate seats OR 4 to Quit ");
                    }

                    if (choiceSel < 1 || choiceSel > 3) {
                        System.out.println("Incorrect Value entered");
                    }

                    if(choiceSel==1){
                        printTotalSeatsAvailable();
                    }else if(choiceSel==2){
                        holdSeats();
                        continue;
                    }else if(choiceSel==3){
                        allocateSeats();
                        continue;
                    }else if(choiceSel==4){
                        break;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            scanner.close();


    }

    public static void printTotalSeatsAvailable() {
        System.out.println("\nAvailable seats: " + ticketService.numSeatsAvailable() + "\n");
    }

    public static void holdSeats() {


        System.out.println("###### Please provide your email address for holding the tickets #######");

        String email = scanner.nextLine();
        if(!emailValidator.validate(email))
            System.out.println(" %%%%%%  Incorrect email address format, Please enter as per format, example - abc@gmail.com %%%%");
        boolean flag = emailValidator.validate(email);
        while (flag) {

            System.out.println("### Thanks your email address is validated ###");

            int numOfSeatsToHold = 0;
            while (numOfSeatsToHold == 0) {
                System.out.print("### Enter the number of seats to hold ### ");
                String SeatsToHold = scanner.nextLine();

                try {
                    SeatHold seatHold = ticketService.findAndHoldSeats(Integer.parseInt(SeatsToHold), email);
                    if (seatHold == null) {
                        numOfSeatsToHold = 0;
                        continue;
                    }else{
                        numOfSeatsToHold = Integer.parseInt(SeatsToHold);
                    }

                    System.out.println("\n Seat hold id is " + seatHold.getId() + " with email "
                            + seatHold.getCustomerEmail() + "\n");


                } catch (Exception exception) {
                    exception.printStackTrace();
                    continue;
                }
                flag = false;
            }

        }


    }


    private static void allocateSeats() {
        System.out.println("###### Please provide your email address for holding the tickets #######");

        String email = scanner.nextLine();
        if(!emailValidator.validate(email))
            System.out.println(" %%%%%%  Incorrect email address format, Please enter as per format, example - abc@gmail.com %%%%");
        boolean flag = emailValidator.validate(email);
        while (flag) {

            System.out.println("### Thanks your email address is validated ###");

                int seatHoldId = -1;

                while (seatHoldId < 0) {
                    System.out.print("Enter seat hold id: ");
                    String seatHoldIdStr = scanner.nextLine();

                    try {
                        seatHoldId = Integer.parseInt(seatHoldIdStr);
                        String bookedId = ticketService.reserveSeats(seatHoldId, email);
                        if (bookedId == null) {
                            email = null;
                            continue;
                        }else{
                            seatHoldId=0;
                        }

                        System.out.println("#### Your booking id is " + bookedId + "\n");

                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
                flag = false;

        }
    }



}
