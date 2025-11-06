package nils.todo.facades;

import nils.todo.services.AuthService;
import nils.todo.util.BrowserLauncher;
import org.springframework.stereotype.Service;

@Service
public class AuthFacade {

    private final AuthService authService;

    public AuthFacade(AuthService authService) {
        this.authService = authService;
    }

    public String startLoginFlow() {
        String authUrl = authService.getAuthorizationUrl();
        BrowserLauncher.open(authUrl);
        return authUrl;
    }

    public void completeLogin(String code) {
        authService.acquireTokenFromCode(code);
    }

    public String getAccessToken() {
        return authService.getAccessToken();
    }

    public boolean hasValidToken() {
        return authService.hasValidToken();
    }

}

