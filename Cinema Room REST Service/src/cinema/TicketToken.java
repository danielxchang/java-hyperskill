package cinema;

import java.util.UUID;

public class TicketToken {
    private final String token;

    public TicketToken() {
        this.token = UUID.randomUUID().toString();
    }

    public TicketToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
