import org.example.enums.ParcelStatus;
import org.example.enums.Permission;
import org.example.enums.Role;
import org.example.model.Parcel;
import org.example.model.StatusHistory;
import org.example.service.ParcelService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ParcelServiceTest {
	private ParcelService parcelService;
	private UserService userService;

	@BeforeEach
	void setUp() {
		userService = new UserService();
		userService.addUser("operator", Role.OPERATOR);
		userService.addUser("courier", Role.COURIER);
		userService.addUser("client");
		userService.addUser("client2");

		parcelService = new ParcelService(userService);
	}

	@Test
	void createParcel_ShouldCreateParcel() {
		Parcel parcel = parcelService.createParcel(
				"client", "sender", "receiver", "destination", "operator");

		assertNotNull(parcel);
		assertEquals("client", parcel.getOwner());
		assertEquals("sender", parcel.getSender());
		assertEquals("receiver", parcel.getReceiver());
		assertEquals("destination", parcel.getDestination());
		assertEquals(ParcelStatus.CREATED, parcel.getStatus());
		assertNotNull(parcel.getTrackingNumber());
		assertEquals(10, parcel.getTrackingNumber().length());
	}

	@Test
	void createParcel_ShouldThrowSecurityException() {
		assertThrows(SecurityException.class, () ->
				parcelService.createParcel(
						"client", "sender", "receiver", "destination", "client")
		);
	}

	@Test
	void addTrackingNumber_ShouldAddNewTracking() {
		Parcel parcel = parcelService.addTrackingNumber("TEST123456", "client");

		assertNotNull(parcel);
		assertEquals("TEST123456", parcel.getTrackingNumber());
	}

	@Test
	void addTrackingNumber_ShouldThrowSecurityExceptionWithoutPermission() {
		assertThrows(SecurityException.class, () ->
				parcelService.addTrackingNumber("TEST123456", "courier")
		);
	}

	@Test
	void addTrackingNumber_ShouldThrowExceptionDuplicateTrackingNumber() {
		parcelService.addTrackingNumber("DUPLICATE", "client");

		assertThrows(IllegalArgumentException.class, () ->
				parcelService.addTrackingNumber("DUPLICATE", "client")
		);
	}

	@Test
	void updateStatus_ShouldUpdateStatus() {
		Parcel parcel = parcelService.createParcel("client", "s", "r", "d", "operator");
		parcelService.updateStatus(parcel.getTrackingNumber(), ParcelStatus.IN_TRANSIT, "courier");

		assertEquals("courier", parcel.getCurrentCourier());
		assertEquals(2, parcel.getHistory().size());
		assertEquals(ParcelStatus.IN_TRANSIT, parcel.getStatus());
	}

	@Test
	void updateStatus_ShouldThrowExceptionInvalidTransition() {
		Parcel parcel = parcelService.createParcel("client", "s", "r", "d", "operator");

		assertThrows(IllegalStateException.class, () ->
				parcelService.updateStatus(parcel.getTrackingNumber(), ParcelStatus.DELIVERED, "operator")
		);
	}

	@Test
	void updateStatus_ShouldThrowExceptionWithoutPermission() {
		Parcel parcel = parcelService.createParcel("client", "s", "r", "d", "operator");

		assertThrows(SecurityException.class, () ->
				parcelService.updateStatus(parcel.getTrackingNumber(), ParcelStatus.IN_TRANSIT,
						"client")
		);
	}

	@Test
	void getStatusHistory_ShouldReturnHistoryForOwner() {
		Parcel parcel = parcelService.createParcel("client", "s", "r", "d", "operator");
		List<StatusHistory> history = parcelService.getStatusHistory("client", parcel.getTrackingNumber());

		assertEquals(1, history.size());
		assertEquals(ParcelStatus.CREATED, history.get(0).getStatus());
	}

	@Test
	void getStatusHistory_ShouldReturnHistoryForNotOwner() {
		Parcel parcel = parcelService.createParcel("client", "s", "r", "d", "operator");

		List<StatusHistory> history = parcelService.getStatusHistory("operator", parcel.getTrackingNumber());

		assertEquals(1, history.size());
		assertEquals(ParcelStatus.CREATED, history.get(0).getStatus());
	}

	@Test
	void getStatusHistory_ShouldThrowSecurityException() {
		Parcel parcel = parcelService.createParcel("client", "s", "r", "d", "operator");

		assertThrows(SecurityException.class, () ->
				parcelService.getStatusHistory("client2", parcel.getTrackingNumber())
		);
	}

	@Test
	void getTrackingNumbers_ShouldReturnAvailableTrackNumber() {
		parcelService.createParcel("client", "s", "r", "d", "operator");
		parcelService.createParcel("client", "a", "b", "c", "operator");
		parcelService.createParcel("client2", "s", "r", "d", "operator");

		List<String> trackingNumbersClient = parcelService.getTrackingNumbers("client");
		List<String> trackingNumbersClient2 = parcelService.getTrackingNumbers("client2");
		List<String> trackingNumbersOperator = parcelService.getTrackingNumbers("operator");

		assertEquals(2, trackingNumbersClient.size());
		assertEquals(1, trackingNumbersClient2.size());
		assertEquals(3, trackingNumbersOperator.size());
	}

	@Test
	void getTrackingNumbers_ShouldThrowExceptionWithoutPermission() {
		parcelService.createParcel("client", "s", "r", "d", "operator");

		assertThrows(SecurityException.class, () ->
				parcelService.getTrackingNumbers("courier")
		);
	}

	@Test
	void getPackageByNumber_ExistingPackage_ShouldReturnPackage() {
		Parcel parcel = parcelService.createParcel("client", "s", "r", "d", "operator");
		Parcel found = parcelService.getPackageByNumber(parcel.getTrackingNumber());

		assertSame(parcel, found);
	}
}
