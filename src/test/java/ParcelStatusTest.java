import org.example.enums.ParcelStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParcelStatusTest {
	@Test
	void isValidTransition_ShouldAllowCorrectTransitions() {
		assertTrue(ParcelStatus.CREATED.isValidTransition(ParcelStatus.IN_TRANSIT));
		assertTrue(ParcelStatus.IN_TRANSIT.isValidTransition(ParcelStatus.ARRIVED_AT_PICKUP_POINT));
		assertTrue(ParcelStatus.ARRIVED_AT_PICKUP_POINT.isValidTransition(ParcelStatus.DELIVERED));
	}

	@Test
	void isValidTransition_ShouldPreventIncorrectTransitions() {
		assertFalse(ParcelStatus.CREATED.isValidTransition(ParcelStatus.DELIVERED));
		assertFalse(ParcelStatus.IN_TRANSIT.isValidTransition(ParcelStatus.CREATED));
		assertFalse(ParcelStatus.DELIVERED.isValidTransition(ParcelStatus.IN_TRANSIT));
	}
}
