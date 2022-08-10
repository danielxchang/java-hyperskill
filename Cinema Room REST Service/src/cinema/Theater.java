package cinema;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Theater {
    private final int total_rows;
    private final int total_columns;
    private final List<Seat> available_seats = new ArrayList<Seat>();
    private final ConcurrentMap<String, Seat> seatMap = new ConcurrentHashMap<>();

    public Theater(int rows, int columns) {
        this.total_rows = rows;
        this.total_columns = columns;
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                Seat s = new Seat(i, j);
                this.available_seats.add(s);
                this.seatMap.put(Seat.constructSeatKey(s.getRow(), s.getColumn()), s);
            }
        }
    }

    public int getTotal_rows() {
        return this.total_rows;
    }

    public int getTotal_columns() {
        return this.total_columns;
    }

    public List<Seat> getAvailable_seats() {
        return this.available_seats;
    }

    public Seat getSeat(int row, int column) {
        String seatKey = Seat.constructSeatKey(row, column);
        return seatMap.get(seatKey);
    }
}
