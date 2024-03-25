package it.theatre.com;

import java.io.Serializable;

public class Ticket implements Serializable,Comparable<Ticket> {
    private int row;
    private int seat;
    private double price;
    private Person person;

    public Ticket() {
    }

    public Ticket(int row, int seat, double price, Person person) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    // Getter and setter methods for the attributes
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    // method format and display complete ticket data
    public void print() {
        System.out.println("*********************************");
        System.out.println("*       TICKET INFORMATION      *");
        System.out.println("*********************************");
        System.out.printf("* Name: %-22s *\n", person.getName());
        System.out.printf("* Surname: %-19s *\n", person.getSurname());
        System.out.printf("* Email: %-21s *\n", person.getEmail());
        System.out.printf("* Row: %-23s *\n", row);
        System.out.printf("* Seat: %-22s *\n", seat);
        System.out.printf("* Price: $%-20.2f *\n", price);
        System.out.println("*********************************");
    }

    // method to compare Ticket objects as for the cheapest price first priority
    @Override
    public int compareTo(Ticket o) {
        if (this.price < o.price) {
            return -1;
        } else if (this.price > o.price) {
            return 1;
        } else {
            return 0;
        }
    }
}

