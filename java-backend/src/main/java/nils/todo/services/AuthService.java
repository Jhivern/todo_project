package nils.todo.services;

import com.google.inject.Singleton;
import com.microsoft.aad.msal4j.*;
import nils.todo.config.AuthConfigLoader;
import nils.todo.util.BrowserLauncher;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;
import java.util.Set;

@Singleton
@Service
public class AuthService {
    private final AuthConfigLoader loader;
    private final ConfidentialClientApplication app;

    /**
     * Constructor for AuthService.
     * @param authConfigLoader Controls initial access to the config file
     */
    public AuthService(AuthConfigLoader authConfigLoader, ConfidentialClientApplication app) {
        this.app = app;
        this.loader = authConfigLoader;
    }

    /**
     * Checks if MSAL has a valid token and is authenticated
     * @return Boolean if we are authenticated
     */
    public boolean hasValidToken() {
        try {
            IAccount account = app.getAccounts().join().iterator().next();
            SilentParameters silentParameters = SilentParameters
                    .builder(Set.of("User.Read", "Tasks.ReadWrite", "offline_access"), account)
                    .build();

            IAuthenticationResult result = app.acquireTokenSilently(silentParameters).join();

            return result.accessToken() != null;
        }
        catch (Exception e) {
            // No cached token exists, or it's expired with no refresh
            return false;
        }
    }

    /**
     * Direct token access for MSGraphService
     * @return The token which grants access to the MS account
     */
    public String getAccessToken() {
        try {
            IAccount account = app.getAccounts().join().iterator().next();
            SilentParameters silentParameters = SilentParameters
                    .builder(Set.of("User.Read", "Tasks.ReadWrite", "offline_access"), account)
                    .build();

            IAuthenticationResult result = app.acquireTokenSilently(silentParameters).join();

            return result.accessToken();
        }
        catch (Exception e) {
            // No cached token exists, or it's expired with no refresh
            throw new RuntimeException("Failure while acquiring token", e);
        }
    }

    /**
     * Gets the authorization URL the user has to navigate to
     * @return Authorization URL as a String
     */
    public String getAuthorizationUrl() {
        AuthorizationRequestUrlParameters parameters = AuthorizationRequestUrlParameters
                .builder("http://localhost:8080/taskApi/auth/redirect",
                        Set.of("User.Read", "Tasks.ReadWrite", "offline_access"))
                .responseMode(ResponseMode.QUERY)
                .extraQueryParameters(Map.of(
                        "response_type", "code",
                        "client_id", loader.getAuthDTO().clientId()
                ))
                .build();

        return app.getAuthorizationRequestUrl(parameters).toString();
    }

    /**
     * Opens the browser for the user for logging in
     * @param url The URL which to navigate to (authorizationUrl)
     */
    public void launchBrowser(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL must not be null or empty");
        }

        BrowserLauncher.open(url);

    }

    /**
     * Gets the token from MSAL by giving it the code from Azure
     * @param code The code gotten from MS login
     * @return The authentication token (wrapped in a class)
     */
    public void acquireTokenFromCode(String code) {
        try {
            AuthorizationCodeParameters params = AuthorizationCodeParameters
                    .builder(code, new URI("http://localhost:8080/taskApi/auth/redirect"))
                    .build();

            // Save token in MSAL cache
            app.acquireToken(params).get();
        }
        catch (Exception e) {
            throw new RuntimeException("Code not verified by Azure", e);
        }
    }
}
