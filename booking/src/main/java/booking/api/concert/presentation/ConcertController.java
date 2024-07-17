package booking.api.concert.presentation;

import booking.api.concert.application.ConcertFacade;
import booking.api.concert.domain.ConcertSchedule;
import booking.api.concert.domain.ConcertService;
import booking.api.concert.domain.Reservation;
import booking.api.concert.presentation.request.BookingSeatsRequest;
import booking.api.concert.presentation.response.BookingSeatsResponse;
import booking.api.concert.presentation.response.SearchScheduleResponse;
import booking.api.concert.presentation.response.SearchSeatsResponse;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/concert")
public class ConcertController {

    private final ConcertService concertService;
    private final ConcertFacade concertFacade;

    @GetMapping("/schedules/{concertId}")
    public SearchScheduleResponse searchSchedules(
            @RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long concertId
    ) {
        List<ConcertSchedule> concertSchedules = concertFacade.searchSchedules(token, concertId);
        return new SearchScheduleResponse(
                concertFacade.getConcertScheduleDates(concertSchedules),
                concertFacade.getConcertScheduleId(concertSchedules)
        );
    }

    @GetMapping("/seats/{concertScheduleId}")
    public List<SearchSeatsResponse> searchSeats(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable long concertScheduleId, @PathParam("concertDate") LocalDate concertDate
    ) {
        return SearchSeatsResponse.of(concertFacade.searchSeats(token, concertScheduleId, concertDate));
    }

    @PostMapping("/seats/booking")
    public BookingSeatsResponse bookingSeats(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody BookingSeatsRequest request
    ) {
        Reservation reservation = concertService.bookingSeats(token, request.userId(), request.concertScheduleId(), request.concertDate(), request.seatNumberList());
        List<BigDecimal> price = concertService.getConcertSeat(request.userId(), reservation.getConcertSeatId(), request.concertDate(), request.seatNumberList());
        return new BookingSeatsResponse(reservation.getId(), reservation.getConcertSeatId(), request.seatNumberList(), price);
    }
}
