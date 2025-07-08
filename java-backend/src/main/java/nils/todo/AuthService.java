package nils.todo;

import com.google.inject.Singleton;
import com.microsoft.aad.msal4j.*;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

@Singleton
public class AuthService {
    private final AuthDTO authDTO;

    //
    private final ConfidentialClientApplication app;

    /**
     *  Constructor for AuthService
     * @param authConfigLoader Controls initial access to the config file
     */
    public AuthService(AuthConfigLoader authConfigLoader) {
        this.authDTO = authConfigLoader.loadConfig();
        try {
            app = ConfidentialClientApplication.builder(authDTO.clientId(),
                            ClientCredentialFactory.createFromSecret(authDTO.clientSecret()))
                    .authority("https://login.microsoftonline.com/" + authDTO.tenantId())
                    .build();
        }
        // MalformedURLException thrown when .authority() URL has invalid syntax
        // Replace this with a non-breaking solution
        catch(MalformedURLException e) {
            throw new RuntimeException("Invalid authority URL: " + e.getMessage(), e);
        }
    }

    public boolean hasValidToken() {
        try {
            SilentParameters silentParameters = SilentParameters
                    .builder(Set.of("User.Read", "Tasks.ReadWrite", "offline_access"))
                    .build();

            IAuthenticationResult result = app.acquireTokenSilently(silentParameters).join();

            return result.accessToken() != null;
        } catch (Exception e) {
            // No cached token exists, or it's expired with no refresh
            return false;
        }
    }

    /**
     * Gets the authorization URL the user has to navigate to
     * @return Authorization URL as a String
     */
    public String getAuthorizationUrl() {
        AuthorizationRequestUrlParameters parameters =
                AuthorizationRequestUrlParameters
                        .builder("http://localhost:8080/taskApi/auth/redirect",
                                Set.of("User.Read", "Tasks.ReadWrite", "offline_access"))
                        .responseMode(ResponseMode.QUERY)
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

        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            throw new UnsupportedOperationException("Platform does not support desktop browsing");
        }

        try {
            URI uri = new URI(url);
            Desktop.getDesktop().browse(uri);
        }
        catch (IOException | UnsupportedOperationException e) {
            throw new RuntimeException("Failure while opening browser", e);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException("URL contains invalid syntax: " + url, e);
        }
    }

    /**
     * Gets the token from MSAL by giving it the code from Azure
     * @param code The code gotten from MS login
     * @return The authentication token (wrapped in a class)
     */
    public IAuthenticationResult acquireTokenFromCode(String code) {
        try {
            AuthorizationCodeParameters params = AuthorizationCodeParameters
                    .builder(code, new URI("http://localhost:8080/taskApi/auth/redirect"))
                    .build();

            return app.acquireToken(params).get();
        }
        catch(Exception e) {
            throw new RuntimeException("Code not verified by Azure", e);
        }
    }
}
