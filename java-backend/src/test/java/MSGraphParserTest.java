import nils.todo.services.MSGraphParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MSGraphParserTest {
    MSGraphParser msGraphParser = new MSGraphParser();

    // extractIdFromJson tests
    // Null test
    @Test
    void extractIdFromJSON_nullDisplayNameTest() {
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
        assertNull(msGraphParser.extractIdFromJSON(null, responseBody));
    }

    @Test
    void extractIdFromJSON_nullResponseBodyTest() {
        assertNull(msGraphParser.extractIdFromJSON("Tasks", null));
    }
    // Invalid test
    @Test
    void extractIdFromJSON_invalidTest() {
        String responseBody = """
                {
                    "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#users('3a2bc284-f11c-4676-a9e1-6310eea60f26')/todo/lists",
                    "value": [
                        {
                        hello I am horribly written JSON
                        pls excuse me
                            "@odata.etag": "W/\\"1MMsE9Md1kSV33nxgvF8/wAABYwDhg==\\"",
                            "displayName": "Tasks",
                            "isOwner": true,
                            "isShared": false,
                            "wellknownListName": "defaultList",
                            "id": invalidjson 
                        }
                    ]
                }
                """;
        assertThrows(RuntimeException.class, () -> msGraphParser.extractIdFromJSON("Tasks", responseBody));
    }
    // Correct test
    @Test
    void extractIdFromJSON_correctTest() {
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
        assertEquals("validID", msGraphParser.extractIdFromJSON("Tasks", responseBody));
    }
    // Not Found test
    @Test
    void extractIdFromJSON_notFoundTest() {
        String responseBody = """
                {
                    "@odata.context": "https://graph.microsoft.com/v1.0/$metadata#users('3a2bc284-f11c-4676-a9e1-6310eea60f26')/todo/lists",
                    "value": [
                        {
                            "@odata.etag": "W/\\"1MMsE9Md1kSV33nxgvF8/wAABYwDhg==\\"",
                            "displayName": "Not_Tasks",
                            "isOwner": true,
                            "isShared": false,
                            "wellknownListName": "defaultList",
                            "id": "validID"
                        }
                    ]
                }
                """;
        assertNull(msGraphParser.extractIdFromJSON("Tasks", responseBody));

    }

    // extractTasksFromJSON tests
    // Null test
    @Test
    void extractTasksFromJSON_nullTest() {
        assertEquals(List.of(), msGraphParser.extractTasksFromJSON(null));

    }
    // Invalid test
    @Test
    void extractTasksFromJSON_invalidTest() {
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
                            "tit
                             // Message was cut off here
                """;
        assertThrows(RuntimeException.class, () -> msGraphParser.extractTasksFromJSON(responseBody));

    }
    // Correct test
    @Test
    void extractTasksFromJSON_correctTest() {
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
        assertEquals(List.of("test 1", "test 2"), msGraphParser.extractTasksFromJSON(responseBody));

    }
}
