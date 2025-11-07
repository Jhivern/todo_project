package nils.todo.components;

import nils.todo.facades.AuthFacade;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {
    AuthFacade authFacade;

    /**
     * Constructor for StartupRunner
     * @param authFacade The authentication facade
     */
    public StartupRunner(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    /**
     * Runs code upon startup of the Spring Application. Currently, starts loginFlow
     * @param args Magic that Spring uses
     * @throws Exception Any exception
     */
    @Override
    public void run(String... args) throws Exception {
        authFacade.startLoginFlow();
    }
}
