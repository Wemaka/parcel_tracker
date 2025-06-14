package org.example.model;

import org.example.enums.ParcelStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class StatusHistory {
	private final ParcelStatus status;
	private final LocalDateTime timestamp;
	private final String location;

	public StatusHistory(ParcelStatus status, LocalDateTime timestamp, String location) {
		this.status = status;
		this.timestamp = timestamp;
		this.location = location;
	}

	public StatusHistory(ParcelStatus status) {
		this(status, LocalDateTime.now(), null);
	}

	public ParcelStatus getStatus() {
		return status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getLocation() {
		return location;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StatusHistory that = (StatusHistory) o;
		return status == that.status && Objects.equals(timestamp, that.timestamp) && Objects.equals(location, that.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(status, timestamp, location);
	}

	@Override
	public String toString() {
		return "StatusHistory{" +
				"status=" + status +
				", timestamp=" + timestamp +
				", location='" + location + '\'' +
				'}';
	}
}
