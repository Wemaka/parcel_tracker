package org.example.service;

import org.example.enums.ParcelStatus;
import org.example.enums.Permission;
import org.example.enums.Role;
import org.example.model.Parcel;
import org.example.model.StatusHistory;

import java.util.*;
import java.util.stream.Collectors;

public class ParcelService {
	private final Map<String, Parcel> parcels = new HashMap<>();
	private final Set<String> trackingNumbers = new HashSet<>();
	private final UserService userService;

	public ParcelService(UserService userService) {
		this.userService = userService;
	}

	public Parcel createParcel(String owner, String sender, String receiver,
	                           String destination, String operator) {
		if (!userService.hasPermission(operator, Permission.CREATE_PACKAGE)) {
			throw new SecurityException("У вас нет разрешения на создание посылок");
		}

		String trackingNumber;
		do {
			trackingNumber = generateTrackingNumber();
		} while (trackingNumbers.contains(trackingNumber));

		trackingNumbers.add(trackingNumber);

		Parcel pkg = new Parcel(sender, receiver, destination);
		pkg.setOwner(owner);
		pkg.setTrackingNumber(trackingNumber);
		parcels.put(trackingNumber, pkg);

		return pkg;
	}
	public Parcel addTrackingNumber(String trackingNumber, String username) {
		if (!userService.hasPermission(username, Permission.ADD_TRACKING)) {
			throw new SecurityException("У вас нет разрешение на добавление трек-номера.");
		}

		if (parcels.containsKey(trackingNumber)) {
			throw new IllegalArgumentException("Трек-номер уже существует");
		}

		Parcel pkg = new Parcel("Unknown", "Unknown", "Unknown");
		pkg.setTrackingNumber(trackingNumber);
		pkg.setStatus(ParcelStatus.CREATED);
		parcels.put(trackingNumber, pkg);
		trackingNumbers.add(trackingNumber);

		return pkg;
	}

	public Parcel updateStatus(String trackingNumber, ParcelStatus newStatus, String courier) {
		if (!userService.hasPermission(courier, Permission.UPDATE_STATUS)) {
			throw new SecurityException("У вас нет разрешения обновлять статус");
		}

		Parcel pkg = getPackageByNumber(trackingNumber);

		if (!pkg.getStatus().isValidTransition(newStatus)) {
			throw new IllegalStateException("Недопустимый переход статуса");
		}

		pkg.setStatus(newStatus);
		pkg.setCurrentCourier(courier);
		pkg.getHistory().add(new StatusHistory(newStatus));

		return pkg;
	}

	public List<StatusHistory> getStatusHistory(String username, String trackingNumber) {
		Parcel pkg = getPackageByNumber(trackingNumber);

		boolean isOwnerParcel =	userService.hasRole(username, Role.CLIENT) && pkg.getOwner().equals(username);
		boolean isPermission =
				!userService.hasRole(username, Role.CLIENT) && userService.hasPermission(username, Permission.VIEW_HISTORY);

		if (isOwnerParcel || isPermission) {
			return Collections.unmodifiableList(pkg.getHistory());
		} else {
			throw new SecurityException("У вас нет доступа к истории посылки");
		}
	}

	public List<String> getTrackingNumbers(String username) {
		if (!userService.hasPermission(username, Permission.LIST_TRACKINGS)) {
			throw new SecurityException("У вас нет доступа к трек-номерам.");
		}

		if (!userService.hasRole(username, Role.CLIENT)) {
			return new ArrayList<>(trackingNumbers);
		} else {
			return parcels.values().stream()
					.filter(p -> p.getOwner().equals(username))
					.map(Parcel::getTrackingNumber)
					.collect(Collectors.toList());
		}
	}

	public Parcel getPackageByNumber(String trackingNumber) {
		Parcel pkg = parcels.get(trackingNumber);

		if (pkg == null) {
			throw new IllegalArgumentException("Посылка не найдена");
		}

		return pkg;
	}

	private String generateTrackingNumber() {
		String letters = "ABCDEFGHJKLMNPQRSTUVWXYZ";
		String numbers = "0123456789";
		Random random = new Random();

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 3; i++) {
			sb.append(letters.charAt(random.nextInt(letters.length())));
		}

		for (int i = 0; i < 7; i++) {
			sb.append(numbers.charAt(random.nextInt(numbers.length())));
		}

		return sb.toString();
	}


	@Override
	public String toString() {
		return "ParcelService{" +
				"parcels=" + parcels +
				", trackingNumbers=" + trackingNumbers +
				'}';
	}
}
