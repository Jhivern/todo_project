package serviceTests;

import nils.todo.services.AuthService;
import nils.todo.services.MSGraphParser;
import nils.todo.services.MSGraphService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MSGraphServiceTest {
    AuthService authService = mock(AuthService.class);
    RestClient client = mock(RestClient.class);
    MSGraphParser msGraphParser = mock(MSGraphParser.class);
    MSGraphService graphService = new MSGraphService(authService, client, msGraphParser);

    // getTaskListID() tests
    // Null test
    @Test
    void getTaskListID_nullTest() {
        // Mock client such that it does not contain the name requested
        String responseBody = """
                {
                    "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#users('3a2bc284-f11c-4676-a9e1-6310eea60f26')/todo/lists",
                    "value": [
                        {
                            "@odata.etag": "W/\\"1MMsE9Md1kSV33nxgvF8/wAABYwDhg==\\"",
                            "displayName": "Tasks",
                            "isOwner": true,
                            "isShared": false,
                            "wellknownListName": "defaultList",
                            "id": "AAMkADY1YmE3N2FhLWEwMzQtNDNkMC04Mzg3LTczMTdiMjk2NzRhMAAuAAAAAADfsy0XtCMZS5XonZkyBLu6AQDUwywT0x3WRJXfefGC8Xz-AAAAAAESAAA="
                        }
                    ]
                }
                """;

        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Chain mocks for the full HTTP flow
        when(client.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(responseBody);

        // Assert
        assertNull(graphService.getTaskListID(null));

    }
    // Not found test
    @Test
    void getTaskListID_notFoundTest() {
        // Mock client such that it does not contain the name requested
        String responseBody = """
                {
                    "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#users('3a2bc284-f11c-4676-a9e1-6310eea60f26')/todo/lists",
                    "value": [
                        {
                            "@odata.etag": "W/\\"1MMsE9Md1kSV33nxgvF8/wAABYwDhg==\\"",
                            "displayName": "Tasks",
                            "isOwner": true,
                            "isShared": false,
                            "wellknownListName": "defaultList",
                            "id": "validID"
                        }
                    ]
                }
                """;

        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Chain mocks for the full HTTP flow
        when(client.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(responseBody);

        // Assert
        assertNull(graphService.getTaskListID("doesNotExist"));
    }

    // Found test
    @Test
    void getTaskListID_foundTest() {
        // Response which contains the requested list
        String responseBody = """
                {
                    "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#users('3a2bc284-f11c-4676-a9e1-6310eea60f26')/todo/lists",
                    "value": [
                        {
                            "@odata.etag": "W/\\"1MMsE9Md1kSV33nxgvF8/wAABYwDhg==\\"",
                            "displayName": "Tasks",
                            "isOwner": true,
                            "isShared": false,
                            "wellknownListName": "defaultList",
                            "id": "validID"
                        }
                    ]
                }
                """;

        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Chain mocks for the full HTTP flow
        when(client.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(responseBody);
        when(msGraphParser.extractIdFromJSON("Tasks", responseBody)).thenReturn("validID");

        // Assert
        assertEquals("validID", graphService.getTaskListID("Tasks"));

    }

    // No lists in response
    @Test
    void getTaskListID_noListsTest() {
        // Response which has no lists
        String responseBody = """
                {
                    "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#users('3a2bc284-f11c-4676-a9e1-6310eea60f26')/todo/lists",
                    "value": []
                }
                """;

        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Chain mocks for the full HTTP flow
        when(client.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(responseBody);

        // Assert
        assertNull(graphService.getTaskListID("Tasks"));
    }

    // Multiple lists in response
    @Test
    void getTaskListID_multipleListsTest() {
        // Response which has multiple lists
        String responseBody = """
                {
                    "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#users('3a2bc284-f11c-4676-a9e1-6310eea60f26')/todo/lists",
                    "value": [
                        {
                            "@odata.etag": "W/\\"1MMsE9Md1kSV33nxgvF8/wAABYwDhg==\\"",
                            "displayName": "Tasks",
                            "isOwner": true,
                            "isShared": false,
                            "wellknownListName": "defaultList",
                            "id": "validId"
                        },
                        {
                            "@odata.etag": "W/\\"1MMsE9Md1kSV33nxgvF8/wAABYwDhg==\\"",
                            "displayName": "My Day",
                            "isOwner": true,
                            "isShared": false,
                            "wellknownListName": "defaultList",
                            "id": "validId2"
                        },
                        {
                            "@odata.etag": "W/\\"1MMsE9Md1kSV33nxgvF8/wAABYwDhg==\\"",
                            "displayName": "Fun things",
                            "isOwner": true,
                            "isShared": false,
                            "wellknownListName": "defaultList",
                            "id": "validId3"
                        }
                    ]
                }
                """;

        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Chain mocks for the full HTTP flow
        when(client.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(responseBody);
        when(msGraphParser.extractIdFromJSON("My Day", responseBody)).thenReturn("validId2");

        // Assert
        assertEquals("validId2", graphService.getTaskListID("My Day"));
    }

    // Invalid JSON response
    @Test
    void getTaskListID_invalidJsonTest() {
        // Response which contains invalid JSON
        String responseBody = "InvalidJSON";

        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Chain mocks for the full HTTP flow
        when(client.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(responseBody);
        when(msGraphParser.extractIdFromJSON("Tasks", responseBody)).thenThrow(RuntimeException.class);

        // Assert
        assertThrows(RuntimeException.class, () -> graphService.getTaskListID("Tasks"));

    }

    // getTop2Tasks() tests
    // Null test
    @Test
    void getTop2Tasks_nullTest() {
        // Response which contains multiple tasks
        String responseBody = """
                {
                    "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#users('user')/todo/lists('id')/tasks",
                    "@microsoft.graph.tips": "Use $select to choose only the properties your app needs, as this can lead to performance improvements. For example: GET me/todo/lists('<key>')/tasks?$select=body,bodyLastModifiedDateTime",
                    "value": [
                        {
                            "@odata.etag": "tag",
                            "importance": "normal",
                            "isReminderOn": false,
                            "status": "notStarted",
                            "title": "test 1",
                            "createdDateTime": "time",
                            "lastModifiedDateTime": "time",
                            "hasAttachments": false,
                            "categories": [],
                            "id": "id",
                            "body": {
                                "content": "",
                                "contentType": "text"
                            }
                        },
                        {
                            "@odata.etag": "tag",
                            "importance": "normal",
                            "isReminderOn": false,
                            "status": "notStarted",
                            "title": "test 2",
                            "createdDateTime": "time",
                            "lastModifiedDateTime": "time",
                            "hasAttachments": false,
                            "categories": [],
                            "id": "id",
                            "body": {
                                "content": "",
                                "contentType": "text"
                            }
                        },
                        {
                            "@odata.etag": "tag",
                            "importance": "normal",
                            "isReminderOn": false,
                            "status": "notStarted",
                            "title": "test 3",
                            "createdDateTime": "time",
                            "lastModifiedDateTime": "time",
                            "hasAttachments": false,
                            "categories": [],
                            "id": "id",
                            "body": {
                                "content": "",
                                "contentType": "text"
                            }
                        }
                    ]
                }
                """;

        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Chain mocks for the full HTTP flow
        when(client.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(responseBody);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> graphService.getTop2Tasks(null));
    }

    // No tasks in the list
    @Test
    void getTop2Tasks_noTasksTest() {
        // Response which contains no tasks
        String responseBody = """
                {
                    "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#users('user')/todo/lists('id')/tasks",
                    "@microsoft.graph.tips": "Use $select to choose only the properties your app needs, as this can lead to performance improvements. For example: GET me/todo/lists('<key>')/tasks?$select=body,bodyLastModifiedDateTime",
                    "value": []
                }
                """;

        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Chain mocks for the full HTTP flow
        when(client.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(responseBody);

        // Assert
        assertEquals(List.of(), graphService.getTop2Tasks("validId"));
    }


    // Not found test
    @Test
    void getTop2Tasks_invalidIdTest() {
        // Response which returns an error
        String responseBody = """
                {
                    "error": {
                        "code": "invalidRequest",
                        "message": "Invalid request",
                        "innerError": {
                            "code": "ErrorInvalidIdMalformed",
                            "date": "date",
                            "request-id": "id",
                            "client-request-id": "id"
                        }
                    }
                }
                """;

        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Chain mocks for the full HTTP flow
        when(client.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(responseBody);
        when(msGraphParser.extractTasksFromJSON(responseBody)).thenThrow(RuntimeException.class);

        // Assert
        assertThrows(RuntimeException.class, () -> graphService.getTop2Tasks("validId"));
    }

    // Found test
    @Test
    void getTop2Tasks_foundTest() {
        // Response which contains multiple tasks
        String responseBody = """
                {
                    "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#users('user')/todo/lists('id')/tasks",
                    "@microsoft.graph.tips": "Use $select to choose only the properties your app needs, as this can lead to performance improvements. For example: GET me/todo/lists('<key>')/tasks?$select=body,bodyLastModifiedDateTime",
                    "value": [
                        {
                            "@odata.etag": "tag",
                            "importance": "normal",
                            "isReminderOn": false,
                            "status": "notStarted",
                            "title": "test 1",
                            "createdDateTime": "time",
                            "lastModifiedDateTime": "time",
                            "hasAttachments": false,
                            "categories": [],
                            "id": "id",
                            "body": {
                                "content": "",
                                "contentType": "text"
                            }
                        },
                        {
                            "@odata.etag": "tag",
                            "importance": "normal",
                            "isReminderOn": false,
                            "status": "notStarted",
                            "title": "test 2",
                            "createdDateTime": "time",
                            "lastModifiedDateTime": "time",
                            "hasAttachments": false,
                            "categories": [],
                            "id": "id",
                            "body": {
                                "content": "",
                                "contentType": "text"
                            }
                        },
                        {
                            "@odata.etag": "tag",
                            "importance": "normal",
                            "isReminderOn": false,
                            "status": "notStarted",
                            "title": "test 3",
                            "createdDateTime": "time",
                            "lastModifiedDateTime": "time",
                            "hasAttachments": false,
                            "categories": [],
                            "id": "id",
                            "body": {
                                "content": "",
                                "contentType": "text"
                            }
                        }
                    ]
                }
                """;

        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Chain mocks for the full HTTP flow
        when(client.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(responseBody);
        when(msGraphParser.extractTasksFromJSON(responseBody)).thenReturn(List.of("test 1", "test 2"));

        // Assert
        assertEquals(List.of("test 1", "test 2"), graphService.getTop2Tasks("validId"));
    }

    // Incorrect JSON test
    @Test
    void getTop2Tasks_invalidJsonTest() {
        // Response which contains invalid JSON
        String responseBody = "InvalidJSON";

        RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        // Chain mocks for the full HTTP flow
        when(client.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.header(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn(responseBody);
        when(msGraphParser.extractTasksFromJSON(responseBody)).thenThrow(RuntimeException.class);

        // Assert
        assertThrows(RuntimeException.class, () -> graphService.getTop2Tasks("validId"));
    }

}
