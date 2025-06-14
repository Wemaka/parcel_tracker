import org.example.enums.Permission;
import org.example.enums.Role;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

	private UserService userService;

	@BeforeEach
	void setUp() {
		userService = new UserService();
	}

	@Test
	void addUser_ShouldAssignCorrectRole() {
		userService.addUser("operator", Role.OPERATOR);
		userService.addUser("courier", Role.COURIER);
		userService.addUser("admin", Role.ADMIN);
		userService.addUser("client", Role.CLIENT);

		assertTrue(userService.hasRole("operator", Role.OPERATOR));
		assertTrue(userService.hasRole("courier", Role.COURIER));
		assertTrue(userService.hasRole("admin", Role.ADMIN));
		assertTrue(userService.hasRole("client", Role.CLIENT));
	}

	@Test
	void hasRole_ShouldReturnCorrectResult() {
		userService.addUser("admin", Role.ADMIN);

		assertTrue(userService.hasRole("admin", Role.ADMIN));
		assertFalse(userService.hasRole("admin", Role.CLIENT));
	}

	@Test
	void hasPermission_ShouldHaveAllPermissionsForAdmin() {
		userService.addUser("admin", Role.ADMIN);

		assertTrue(userService.hasPermission("admin", Permission.CREATE_PACKAGE));
		assertTrue(userService.hasPermission("admin", Permission.UPDATE_STATUS));
		assertTrue(userService.hasPermission("admin", Permission.VIEW_HISTORY));
		assertTrue(userService.hasPermission("admin", Permission.LIST_TRACKINGS));
		assertTrue(userService.hasPermission("admin", Permission.ADD_TRACKING));
		assertTrue(userService.hasPermission("admin", Permission.MANAGE_USERS));
	}

	@Test
	void hasPermission_ShouldHaveSpecificPermissionsForOperator() {
		userService.addUser("operator", Role.OPERATOR);

		assertTrue(userService.hasPermission("operator", Permission.CREATE_PACKAGE));
		assertTrue(userService.hasPermission("operator", Permission.UPDATE_STATUS));
		assertTrue(userService.hasPermission("operator", Permission.VIEW_HISTORY));
		assertTrue(userService.hasPermission("operator", Permission.LIST_TRACKINGS));
		assertTrue(userService.hasPermission("operator", Permission.ADD_TRACKING));
		assertFalse(userService.hasPermission("operator", Permission.MANAGE_USERS));
	}

	@Test
	void hasPermission_ShouldHaveSpecificPermissionsForCourier() {
		userService.addUser("courier", Role.COURIER);

		assertFalse(userService.hasPermission("courier", Permission.CREATE_PACKAGE));
		assertTrue(userService.hasPermission("courier", Permission.UPDATE_STATUS));
		assertTrue(userService.hasPermission("courier", Permission.VIEW_HISTORY));
		assertFalse(userService.hasPermission("courier", Permission.LIST_TRACKINGS));
		assertFalse(userService.hasPermission("courier", Permission.ADD_TRACKING));
		assertFalse(userService.hasPermission("operator", Permission.MANAGE_USERS));
	}

	@Test
	void hasPermission_ShouldHaveLimitedPermissionsForClient() {
		userService.addUser("client", Role.CLIENT);

		assertFalse(userService.hasPermission("client", Permission.CREATE_PACKAGE));
		assertFalse(userService.hasPermission("client", Permission.UPDATE_STATUS));
		assertTrue(userService.hasPermission("client", Permission.VIEW_HISTORY));
		assertTrue(userService.hasPermission("client", Permission.LIST_TRACKINGS));
		assertTrue(userService.hasPermission("client", Permission.ADD_TRACKING));
		assertFalse(userService.hasPermission("operator", Permission.MANAGE_USERS));
	}

	@Test
	void hasPermission_ShouldReturnFalseForNonExistingUser() {
		assertFalse(userService.hasPermission("ghost", Permission.CREATE_PACKAGE));
	}
}