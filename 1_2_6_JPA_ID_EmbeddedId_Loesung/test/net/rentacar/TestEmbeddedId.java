package net.rentacar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import net.rentacar.model.Booking;
import net.rentacar.model.BookingId;

public class TestEmbeddedId extends AbstractJPATestCase {

	@Override
	public void setUp() throws Exception {
		BookingId id = new BookingId("RENT-2026-001", "BER-01");
		Booking booking = new Booking(id, "Max Mustermann", 249.50);
		manager.persist(booking);
		manager.flush();
		manager.clear();
	}

	@Test
	public void testFindWithEmbeddedId() {
		BookingId searchId = new BookingId("RENT-2026-001", "BER-01");
		Booking found = manager.find(Booking.class, searchId);
		assertNotNull(found);
		assertEquals("Max Mustermann", found.getCustomerName());
		assertEquals(249.50, found.getTotalAmount());
	}
}
