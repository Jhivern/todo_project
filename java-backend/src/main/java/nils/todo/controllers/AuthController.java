package nils.todo.controllers;

import com.google.inject.Inject;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import nils.todo.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("taskApi/auth")
public class AuthController {
    private final AuthService authService;

    @Inject
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Initial portal for users to log in
    // !! Let this be done by MSGraphService!!
    @GetMapping("/login")
    public void login() {
        try {
            String authUrl = authService.getAuthorizationUrl();
            authService.launchBrowser(authUrl);
        }
        catch (Exception e) {
            System.err.println("Failure while creating authorization URL\n" + e.getMessage());

            throw new RuntimeException("Failure while creating authorization URL", e);
        }
    }

    /**
     * Handles redirect when user has logged in
     * @param code The authentication code to log in
     */
    @GetMapping("/redirect")
    public ResponseEntity<String> redirect(@RequestParam String code) {
        try {
            IAuthenticationResult result = authService.acquireTokenFromCode(code);
            return ResponseEntity.ok(result.accessToken());
        }
        catch (Exception e) {
            System.err.println("Azure rejected the token\n" + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Azure rejected the token");
        }
    }

    /**
     * Endpoint for the ESP32 to check if we have completed login
     * @return Status with either 200 OK or 401 UNAUTHORIZED
     */
    @GetMapping("/status")
    public ResponseEntity<Void> checkAuthStatus() {
        if (authService.hasValidToken()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }




}
