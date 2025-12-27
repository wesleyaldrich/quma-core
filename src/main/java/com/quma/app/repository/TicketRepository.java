package com.quma.app.repository;

import com.quma.app.common.dto.TicketsByBookingDateDto;
import com.quma.app.entity.Ticket;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {

    @Aggregation(pipeline = {
            "{ $match: { valid: true, customerNo: ?0 } }",
            "{ $project: { bookingDate: { $dateToString: { format: '%Y-%m-%d', date: '$bookingDate' } }, ticket: '$$ROOT' } }",
            "{ $group: { _id: '$bookingDate', tickets: { $push: '$ticket' } } }",
            "{ $project: { bookingDate: '$_id', tickets: 1, _id: 0 } }",
            "{ $sort: { bookingDate: 1 } }"
    })
    List<TicketsByBookingDateDto> findGroupedByBookingDate(String customerNo);

    /* Write custom queries here if needed. */

}
