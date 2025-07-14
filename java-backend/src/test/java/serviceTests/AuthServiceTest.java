package serviceTests;

import com.microsoft.aad.msal4j.AuthorizationCodeParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAccount;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import nils.todo.config.AuthConfigLoader;
import nils.todo.config.AuthDTO;
import nils.todo.services.AuthService;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceTest {
    AuthConfigLoader authConfigLoader = mock(AuthConfigLoader.class);
    ConfidentialClientApplication app = mock(ConfidentialClientApplication.class);
    AuthService authService = new AuthService(authConfigLoader, app);

    // hasValidToken() tests
    // No accounts found
    @Test
    void hasValidToken_noAccountTest() {
        // .join would have thrown a NullPointerException
        when(app.getAccounts()).thenThrow(NullPointerException.class);
        assertFalse(authService.hasValidToken());
    }

    // Exception thrown during acquireTokenSilently
    @Test
    void hasValidToken_resultExceptionTest() throws MalformedURLException {
        // Setup
        IAccount mockAccount = mock(IAccount.class);
        Set<IAccount> accountSet = Set.of(mockAccount);
        CompletableFuture<Set<IAccount>> futureSet = CompletableFuture.completedFuture(accountSet);

        // Let getAccounts() return a mocked set
        when(app.getAccounts()).thenReturn(futureSet);

        // Simulate the token acquisition failing
        when(app.acquireTokenSilently(any()))
                .thenAnswer(invocation -> { throw new RuntimeException("Token failure"); });

        // Assert
        assertFalse(authService.hasValidToken());
    }

    // Result is null
    @Test
    void hasValidToken_resultNullTest() throws MalformedURLException {
        // Setup
        IAccount mockAccount = mock(IAccount.class);
        Set<IAccount> accountSet = Set.of(mockAccount);
        CompletableFuture<Set<IAccount>> futureSet = CompletableFuture.completedFuture(accountSet);

        // Let getAccounts() return a mocked set
        when(app.getAccounts()).thenReturn(futureSet);

        // Let acquireTokenSilently return an authentication result
        IAuthenticationResult result = mock(IAuthenticationResult.class);
        CompletableFuture<IAuthenticationResult> futureResult = CompletableFuture.completedFuture(result);

        // Let the result be null
        when(app.acquireTokenSilently(any())).thenReturn(futureResult);
        when(result.accessToken()).thenReturn(null);

        // Assert
        assertFalse(authService.hasValidToken());
    }

    // valid example
    @Test
    void hasValidToken_validTest() throws MalformedURLException {
        // Setup
        IAccount mockAccount = mock(IAccount.class);
        Set<IAccount> accountSet = Set.of(mockAccount);
        CompletableFuture<Set<IAccount>> futureSet = CompletableFuture.completedFuture(accountSet);

        // Let getAccounts() return a mocked set
        when(app.getAccounts()).thenReturn(futureSet);

        // Let acquireTokenSilently return an authentication result
        IAuthenticationResult result = mock(IAuthenticationResult.class);
        CompletableFuture<IAuthenticationResult> futureResult = CompletableFuture.completedFuture(result);

        // Let the result be null
        when(app.acquireTokenSilently(any())).thenReturn(futureResult);
        when(result.accessToken()).thenReturn("validToken");

        // Assert
        assertTrue(authService.hasValidToken());
    }

    // getAccessToken() tests
    @Test
    void getAccessToken_noAccountTest() {
        // .join would have thrown a NullPointerException
        when(app.getAccounts()).thenThrow(NullPointerException.class);
        assertThrows(RuntimeException.class, () -> authService.getAccessToken());
    }

    // Exception thrown during acquireTokenSilently
    @Test
    void getAccessToken_resultExceptionTest() throws MalformedURLException {
        // Setup
        IAccount mockAccount = mock(IAccount.class);
        Set<IAccount> accountSet = Set.of(mockAccount);
        CompletableFuture<Set<IAccount>> futureSet = CompletableFuture.completedFuture(accountSet);

        // Let getAccounts() return a mocked set
        when(app.getAccounts()).thenReturn(futureSet);

        // Simulate the token acquisition failing
        when(app.acquireTokenSilently(any()))
                .thenAnswer(invocation -> { throw new RuntimeException("Token failure"); });

        // Assert
        assertThrows(RuntimeException.class, () -> authService.getAccessToken());
    }

    // valid example
    @Test
    void getAccessToken_validTest() throws MalformedURLException {
        // Setup
        IAccount mockAccount = mock(IAccount.class);
        Set<IAccount> accountSet = Set.of(mockAccount);
        CompletableFuture<Set<IAccount>> futureSet = CompletableFuture.completedFuture(accountSet);

        // Let getAccounts() return a mocked set
        when(app.getAccounts()).thenReturn(futureSet);

        // Let acquireTokenSilently return an authentication result
        IAuthenticationResult result = mock(IAuthenticationResult.class);
        CompletableFuture<IAuthenticationResult> futureResult = CompletableFuture.completedFuture(result);

        // Let the result be null
        when(app.acquireTokenSilently(any())).thenReturn(futureResult);
        when(result.accessToken()).thenReturn("validToken");

        // Assert
        assertEquals("validToken", authService.getAccessToken());
    }

    // getAuthorizationUrl() tests
    @Test
    void getAuthorizationUrl_validTest() throws MalformedURLException {
        // Create authDTO
        String clientId = "FAKE-CLIENT-ID";
        String redirectUri = "http://localhost:8080/taskApi/auth/redirect";
        String scopes = "User.Read+Tasks.ReadWrite+offline_access";
        AuthDTO authDto = new AuthDTO(clientId, "FAKE-CLIENT-SECRET", "FAKE-TENANT-ID");

        String expectedUrl = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize"
                + "?response_type=code"
                + "&client_id=FAKE-CLIENT-ID"
                + "&scope=" + scopes
                + "&redirect_uri=" + URI.create(redirectUri).toASCIIString()
                + "&response_mode=query";

        // Mock authDTO behavior
        when(authConfigLoader.getAuthDTO()).thenReturn(authDto);

        // Stub MSAL to return the constructed URL as a URI
        when(app.getAuthorizationRequestUrl(any())).thenReturn(URI.create(expectedUrl).toURL());

        // Assert
        assertEquals(expectedUrl, authService.getAuthorizationUrl());
    }

    // launchBrowser() tests
    // Null test
    @Test
    void launchBrowser_nullTest() {
        assertThrows(IllegalArgumentException.class, () -> authService.launchBrowser(null));
    }

    // Empty Url test
    @Test
    void launchBrowser_emptyTest() {
        assertThrows(IllegalArgumentException.class, () -> authService.launchBrowser(""));
    }

    // acquireTokenFromCode() tests
    // invalid code test
    @Test
    void acquireTokenFromCode_invalidTest() throws ExecutionException, InterruptedException {
        // Setup
        CompletableFuture<IAuthenticationResult> futureMock = mock(CompletableFuture.class);
        when(futureMock.get()).thenThrow(new RuntimeException());
        when(app.acquireToken(any(AuthorizationCodeParameters.class))).thenReturn(futureMock);

        // Assert
        assertThrows(RuntimeException.class, () -> authService.acquireTokenFromCode(null));
    }

    // .get() interrupted test
    @Test
    void acquireTokenFromCode_interruptedTest() throws ExecutionException, InterruptedException {
        // Setup
        CompletableFuture<IAuthenticationResult> futureMock = mock(CompletableFuture.class);
        when(futureMock.get()).thenThrow(new InterruptedException());
        when(app.acquireToken(any(AuthorizationCodeParameters.class))).thenReturn(futureMock);

        // Assert
        assertThrows(RuntimeException.class, () -> authService.acquireTokenFromCode(""));
    }

}
