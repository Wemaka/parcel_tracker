package org.example.model;

import org.example.enums.ParcelStatus;

import java.util.ArrayList;
import java.util.List;

public class Parcel {
	private String owner;
	private String trackingNumber;
	private String sender;
	private String receiver;
	private String destination;
	private ParcelStatus status;
	private List<StatusHistory> history = new ArrayList<>();
	private String currentCourier;

	public Parcel(String sender, String receiver, String destination) {
		this.sender = sender;
		this.receiver = receiver;
		this.destination = destination;
		this.status = ParcelStatus.CREATED;
		this.history.add(new StatusHistory(ParcelStatus.CREATED));
	}

	public String getOwner() {
		return owner;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getDestination() {
		return destination;
	}

	public ParcelStatus getStatus() {
		return status;
	}

	public List<StatusHistory> getHistory() {
		return history;
	}

	public String getCurrentCourier() {
		return currentCourier;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setStatus(ParcelStatus status) {
		this.status = status;
	}

	public void setHistory(List<StatusHistory> history) {
		this.history = history;
	}

	public void setCurrentCourier(String currentCourier) {
		this.currentCourier = currentCourier;
	}
}
