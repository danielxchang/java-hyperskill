package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.*;
import java.util.List;

@RestController
public class TheaterController {
    private final int rows = 9;
    private final int columns = 9;
    private final Theater theater = new Theater(this.rows, this.columns);

    private final ConcurrentMap<String, String> seatMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, String> tokenMap = new ConcurrentHashMap<>();

    private final TheaterStatistics statistics;

    public TheaterController() {
        List<Seat> seats = this.theater.getAvailable_seats();
        for (Seat s: seats) {
            String seatKey = Seat.constructSeatKey(s.getRow(), s.getColumn());
            seatMap.put(seatKey, "");
        }
        statistics = new TheaterStatistics(this.rows * this.columns);
    }

    @GetMapping("/seats")
    public Theater getTheater() {
        return this.theater;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseTicket(@RequestBody Seat seat) {
        String seatKey = Seat.constructSeatKey(seat.getRow(), seat.getColumn());
        if (!this.seatMap.containsKey(seatKey)) {
            return new ResponseEntity<>(Map.of("error", "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }

        if (!this.seatMap.get(seatKey).isEmpty()) {
            return new ResponseEntity<>(Map.of("error",  "The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
        }

        String token = new TicketToken().getToken();
        this.seatMap.replace(seatKey, token);
        this.tokenMap.put(token, seatKey);
        this.statistics.buyTicket(seat);
        return new ResponseEntity<>(Map.of("token", token, "ticket", seat), HttpStatus.OK);
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(@RequestBody TicketToken returnToken) {
        String token = returnToken.getToken();
        if (!this.tokenMap.containsKey(token)) {
            return new ResponseEntity<>(Map.of("error", "Wrong token!"), HttpStatus.BAD_REQUEST);
        }

        String seatKey = this.tokenMap.get(token);
        String[] splitKey = seatKey.split(":");
        Seat seat = theater.getSeat(Integer.parseInt(splitKey[0]), Integer.parseInt(splitKey[1]));
        this.tokenMap.remove(token);
        this.seatMap.replace(seatKey, "");
        this.statistics.returnTicket(seat);
        return new ResponseEntity<>(Map.of("returned_ticket", seat), HttpStatus.OK);
    }

    @PostMapping("/stats")
    public ResponseEntity<?> getStatistics(@RequestParam(required = false) String password) {
        if (!"super_secret".equals(password)) {
            return new ResponseEntity<>(Map.of("error", "The password is wrong!"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(this.statistics, HttpStatus.OK);
    }
}
