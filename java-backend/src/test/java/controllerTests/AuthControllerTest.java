package controllerTests;

import nils.todo.controllers.AuthController;
import nils.todo.facades.AuthFacade;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    AuthFacade authFacade = mock(AuthFacade.class);

    AuthController authController = new AuthController(authFacade);

    // /login tests
    // Incorrect URL
    @Test
    void loginIncorrectURLTest() {
        when(authFacade.startLoginFlow()).thenThrow(new IllegalArgumentException());
//        doThrow(new IllegalArgumentException()).when(authFacade).launchBrowser(any());
        assertThrows(RuntimeException.class, () -> authController.login());
    }

//    // Browser failure
//    @Test
//    void loginBrowserFailureTest() {
//        when(authFacade.startLoginFlow()).thenReturn("validUrl");
//        doThrow(new RuntimeException()).when(authService).launchBrowser(anyString());
//        assertThrows(RuntimeException.class, () -> authController.login());
//    }

    // Valid test
    @Test
    void loginCorrectTest() {
        when(authFacade.startLoginFlow()).thenReturn("validUrl");
//        doNothing().when(authService).launchBrowser(anyString());

        assertDoesNotThrow(() -> authController.login());
    }

    // /redirect tests
    // Invalid code test
    @Test
    void invalidCodeTest() {
        doThrow(RuntimeException.class).when(authFacade).completeLogin(any());
        when(authFacade.hasValidToken()).thenReturn(false);
        assertEquals(HttpStatusCode.valueOf(400), authController.redirect(null).getStatusCode());
    }

    // Token not valid
    @Test
    void invalidTokenTest() {
        doNothing().when(authFacade).completeLogin(any());
        when(authFacade.hasValidToken()).thenReturn(false);
        assertEquals(HttpStatusCode.valueOf(400), authController.redirect(null).getStatusCode());
    }

    // Valid test
    @Test
    void validTest() {
        doNothing().when(authFacade).completeLogin(any());
        when(authFacade.hasValidToken()).thenReturn(true);
        assertEquals(HttpStatusCode.valueOf(200), authController.redirect(null).getStatusCode());
    }

    // /status tests
    // Token not valid
    @Test
    void statusNotValidTest() {
        when(authFacade.hasValidToken()).thenReturn(true);
        assertEquals(HttpStatusCode.valueOf(200), authController.checkAuthStatus().getStatusCode());
    }

    // Token valid
    @Test
    void statusValidTest() {
        when(authFacade.hasValidToken()).thenReturn(false);
        assertEquals(HttpStatusCode.valueOf(401), authController.checkAuthStatus().getStatusCode());
    }
}
