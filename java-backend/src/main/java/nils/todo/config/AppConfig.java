package nils.todo.config;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.net.MalformedURLException;

@Configuration
public class AppConfig {

    /**
     * Bean for the AuthConfigLoader
     * @return New instance of AuthConfigLoader
     */
    @Bean
    public AuthConfigLoader authConfigLoader() {
        System.out.println("AuthConfigLoader instantiated!");
        return new AuthConfigLoader();
    }

    /**
     * Bean for instantiating a ConfidentialClientApplication. It adds the data from the authDTO as credentials
     * @param loader Loads the AuthDTO file
     * @return New instance of ConfidentialClientApplication
     * @throws MalformedURLException URL added as authority does not adhere to URL syntax
     */
    @Bean
    public ConfidentialClientApplication confidentialClientApplication(AuthConfigLoader loader) throws MalformedURLException {
        System.out.println("ConfidentialClientApplication instantiated!");
        AuthDTO authDTO = loader.readConfig();
        return ConfidentialClientApplication.builder(
                        authDTO.clientId(),
                        ClientCredentialFactory.createFromSecret(authDTO.clientSecret()))
                .authority("https://login.microsoftonline.com/consumers")
                .build();
    }

    /**
     * Bean for instantiating a RestClient. It ensures the baseUrl is included
     * @return a RestClient object
     */
    @Bean
    public RestClient restClient() {
        System.out.println("RestClient instantiated!");
        return RestClient.builder()
                .baseUrl("https://graph.microsoft.com/v1.0")
                .build();
    }
}
