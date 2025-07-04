package nils.todo;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;

import java.net.MalformedURLException;

public class AuthService {
    private final AuthDTO authDTO;

    private final ConfidentialClientApplication app;

    /**
     *  Constructor for AuthService
     * @param authConfigLoader Controls initial access to config file
     */
    public AuthService(AuthConfigLoader authConfigLoader) {
        this.authDTO = authConfigLoader.getConfig();
        try {
            app = ConfidentialClientApplication.builder(authDTO.clientId(),
                            ClientCredentialFactory.createFromSecret(authDTO.clientSecret()))
                    .authority("https://login.microsoftonline.com/" + authDTO.tenantId())
                    .build();
        }
        // MalformedURLException thrown when .authority() URL has invalid syntax
        catch(MalformedURLException e) {
            throw new RuntimeException("Invalid authority URL: " + e.getMessage(), e);
        }
    }


}
