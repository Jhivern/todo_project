package controllerTests;

import nils.todo.controllers.AuthController;
import nils.todo.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    AuthService authService = mock(AuthService.class);

    AuthController authController = new AuthController(authService);

    // /login tests
    // Incorrect URL
    @Test
    void loginIncorrectURLTest() {
        when(authService.getAuthorizationUrl()).thenReturn(null);
        doThrow(new IllegalArgumentException()).when(authService).launchBrowser(any());
        assertThrows(RuntimeException.class, () -> authController.login());
    }

    // Browser failure
    @Test
    void loginBrowserFailureTest() {
        when(authService.getAuthorizationUrl()).thenReturn("validUrl");
        doThrow(new RuntimeException()).when(authService).launchBrowser(anyString());
        assertThrows(RuntimeException.class, () -> authController.login());
    }

    // Valid test
    @Test
    void loginCorrectTest() {
        when(authService.getAuthorizationUrl()).thenReturn("validUrl");
        doNothing().when(authService).launchBrowser(anyString());

        assertDoesNotThrow(() -> authController.login());
    }

    // /redirect tests
    // Invalid code test
    @Test
    void invalidCodeTest() {
        doThrow(RuntimeException.class).when(authService).acquireTokenFromCode(any());
        when(authService.hasValidToken()).thenReturn(false);
        assertEquals(HttpStatusCode.valueOf(400), authController.redirect(null).getStatusCode());
    }

    // Token not valid
    @Test
    void invalidTokenTest() {
        doNothing().when(authService).acquireTokenFromCode(any());
        when(authService.hasValidToken()).thenReturn(false);
        assertEquals(HttpStatusCode.valueOf(400), authController.redirect(null).getStatusCode());
    }

    // Valid test
    @Test
    void validTest() {
        doNothing().when(authService).acquireTokenFromCode(any());
        when(authService.hasValidToken()).thenReturn(true);
        assertEquals(HttpStatusCode.valueOf(200), authController.redirect(null).getStatusCode());
    }

    // /status tests
    // Token not valid
    @Test
    void statusNotValidTest() {
        when(authService.hasValidToken()).thenReturn(true);
        assertEquals(HttpStatusCode.valueOf(200), authController.checkAuthStatus().getStatusCode());
    }

    // Token valid
    @Test
    void statusValidTest() {
        when(authService.hasValidToken()).thenReturn(false);
        assertEquals(HttpStatusCode.valueOf(401), authController.checkAuthStatus().getStatusCode());
    }
}
