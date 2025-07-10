package nils.todo.config;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.net.MalformedURLException;

@Configuration
public class AppConfig {

    @Bean
    public AuthConfigLoader authConfigLoader() {
        System.out.println("AuthConfigLoader instantiated!");
        return new AuthConfigLoader();
    }

    @Bean
    public ConfidentialClientApplication confidentialClientApplication(AuthConfigLoader loader) throws MalformedURLException {
        System.out.println("ConfidentialClientApplication instantiated!");
        AuthDTO authDTO = loader.readConfig();
        return ConfidentialClientApplication.builder(
                        authDTO.clientId(),
                        ClientCredentialFactory.createFromSecret(authDTO.clientSecret()))
                .authority("https://login.microsoftonline.com/consumers" //+ authDTO.tenantId()
                         )
                .build();
    }

    @Bean
    public RestClient restClient() {
        System.out.println("RestClient instantiated!");
        return RestClient.builder()
                .baseUrl("https://graph.microsoft.com/v1.0")
                .build();
    }
}
