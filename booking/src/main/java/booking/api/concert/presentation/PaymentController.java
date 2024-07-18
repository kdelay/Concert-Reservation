package booking.api.concert.presentation;

import booking.api.concert.application.ConcertFacade;
import booking.api.concert.domain.ConcertSeat;
import booking.api.concert.presentation.request.PayRequest;
import booking.api.concert.presentation.response.PayResponse;
import booking.support.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final ConcertFacade concertFacade;

    @Authorization
    @PostMapping
    public PayResponse pay(@RequestBody PayRequest request) {
        ConcertSeat concertSeat = concertFacade.pay(request.concertSeatId(), request.reservationId());
        return new PayResponse(concertSeat.getSeatNumber());
    }
}
