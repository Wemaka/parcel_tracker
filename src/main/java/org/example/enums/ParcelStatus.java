package org.example.enums;

public enum ParcelStatus {
	CREATED,
	IN_TRANSIT,
	ARRIVED_AT_PICKUP_POINT,
	DELIVERED,
	;

	public boolean isValidTransition(ParcelStatus newStatus) {
		return switch (this) {
			case CREATED -> newStatus == IN_TRANSIT;
			case IN_TRANSIT -> newStatus == ARRIVED_AT_PICKUP_POINT;
			case ARRIVED_AT_PICKUP_POINT -> newStatus == DELIVERED;
			default -> false;
		};
	}
}
