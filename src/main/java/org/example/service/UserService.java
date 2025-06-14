package org.example.service;

import org.example.enums.Permission;
import org.example.enums.Role;

import java.util.HashMap;
import java.util.Map;

public class UserService {
	private final Map<String, Role> users = new HashMap<>();

	public UserService() {}

	public void addUser(String username, Role role) {
		users.put(username, role);
	}

	public void addUser(String username) {
		addUser(username, Role.CLIENT);
	}

	public boolean hasRole(String username, Role role) {
		if (!users.containsKey(username)) return false;

		return users.get(username).equals(role);
	}

	public boolean hasPermission(String username, Permission permission) {
		if (!users.containsKey(username)) return false;

		return users.get(username).hasPermission(permission);
	}
}
