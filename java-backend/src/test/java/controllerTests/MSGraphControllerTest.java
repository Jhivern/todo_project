package controllerTests;

import nils.todo.controllers.MSGraphController;
import nils.todo.services.AuthService;
import nils.todo.services.MSGraphService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MSGraphControllerTest {
    AuthService authService = mock(AuthService.class);
    MSGraphService graphService = mock(MSGraphService.class);
    MSGraphController graphController = new MSGraphController(graphService, authService);

    // getTop2Tasks tests
    // Invalid token test
    @Test
    void invalidTokenTest() {
        when(authService.hasValidToken()).thenReturn(false);
        assertEquals(HttpStatusCode.valueOf(401), graphController.getTop2Tasks("randomName").getStatusCode());
    }

    // Null test
    @Test
    void nullTest() {
        when(authService.hasValidToken()).thenReturn(true);
//        when(graphService.).thenReturn();
        assertEquals(HttpStatusCode.valueOf(400), graphController.getTop2Tasks(null).getStatusCode());
        assertEquals(List.of("List not found..."), graphController.getTop2Tasks(null).getBody());
    }

    // Name not found test
    @Test
    void nameNotFoundTest() {
        when(authService.hasValidToken()).thenReturn(true);
        when(graphService.getTaskListID(anyString())).thenReturn(null);
//        when(graphService.).thenReturn();
        assertEquals(HttpStatusCode.valueOf(400), graphController.getTop2Tasks("name").getStatusCode());
        assertEquals(List.of("List not found..."), graphController.getTop2Tasks("name").getBody());
    }

    // Valid test
    @Test
    void validTest() {
        when(authService.hasValidToken()).thenReturn(true);
        when(graphService.getTaskListID("name")).thenReturn("validId");
        when(graphService.getTop2Tasks("validId")).thenReturn(List.of("1. Work", "2. Sleep"));
//        when(graphService.).thenReturn();
        assertEquals(HttpStatusCode.valueOf(200), graphController.getTop2Tasks("name").getStatusCode());
        assertEquals(List.of("1. Work", "2. Sleep"), graphController.getTop2Tasks("name").getBody());
    }

}
