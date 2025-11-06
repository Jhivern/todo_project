package nils.todo.components;

import nils.todo.facades.AuthFacade;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {
    AuthFacade authFacade;

    public StartupRunner(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    @Override
    public void run(String... args) throws Exception {
        authFacade.startLoginFlow();
    }
}
