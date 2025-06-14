package org.example.enums;

import java.util.Set;

public enum Role {
	OPERATOR(Set.of(
			Permission.CREATE_PACKAGE,
			Permission.ADD_TRACKING,
			Permission.UPDATE_STATUS,
			Permission.VIEW_HISTORY,
			Permission.LIST_TRACKINGS)),
	COURIER(Set.of(
			Permission.UPDATE_STATUS,
			Permission.VIEW_HISTORY)),
	CLIENT(Set.of(
			Permission.VIEW_HISTORY,
			Permission.LIST_TRACKINGS,
			Permission.ADD_TRACKING)),
	ADMIN(Set.of(Permission.values()));

	private final Set<Permission> permissions;

	Role(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public boolean hasPermission(Permission permission) {
		return permissions.contains(permission);
	}
}