package cinema;

public class TheaterStatistics {
    private int current_income = 0;
    private int number_of_available_seats;
    private int number_of_purchased_tickets = 0;

    public TheaterStatistics(int availableSeats) {
        this.number_of_available_seats = availableSeats;
    }

    public int getCurrent_income() {
        return this.current_income;
    }

    public int getNumber_of_available_seats() {
        return number_of_available_seats;
    }

    public int getNumber_of_purchased_tickets() {
        return number_of_purchased_tickets;
    }

    private void setCurrent_income(int current_income) {
        this.current_income = current_income;
    }

    private void setNumber_of_available_seats(int number_of_available_seats) {
        this.number_of_available_seats = number_of_available_seats;
    }

    private void setNumber_of_purchased_tickets(int number_of_purchased_tickets) {
        this.number_of_purchased_tickets = number_of_purchased_tickets;
    }

    public void buyTicket(Seat seat) {
        setCurrent_income(this.current_income + seat.getPrice());
        setNumber_of_purchased_tickets(this.number_of_purchased_tickets + 1);
        setNumber_of_available_seats(this.number_of_available_seats - 1);
    }

    public void returnTicket(Seat seat) {
        setCurrent_income(this.current_income - seat.getPrice());
        setNumber_of_purchased_tickets(this.number_of_purchased_tickets - 1);
        setNumber_of_available_seats(this.number_of_available_seats + 1);
    }
}
