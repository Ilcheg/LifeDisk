package ru.netology.lifedisk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MySQLContainer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:/application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LifeDiskIntegrationTest {

    @LocalServerPort
    private Integer port;

    private static RestTemplate restTemplate = new RestTemplate();

    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest").withDatabaseName("life_disk");

    private static String jwt = "";
    private static String fileName = "testfile";
    private static String fileJsonBody = "{\"fileName\": \"filetest\"}";

    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
        mySQLContainer.close();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Test
    @Order(1)
    public void login() throws JsonProcessingException {
        String credentialsJsonBody = "{\"login\": \"TestUser\", \"password\": 1234}";

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/login",
                HttpMethod.POST,
                getJsonRequestEntity(jwt, credentialsJsonBody),
                String.class
        );
        assertEquals(200, response.getStatusCodeValue());
        JsonNode jsonNode = parseResponseBody(response);
        assertTrue(jsonNode.has("auth-token"));
        assertFalse(jsonNode.get("auth-token").asText().isEmpty());
        jwt = jsonNode.get("auth-token").asText();
    }

    @Test
    @Order(2)
    public void uploadFile() throws IOException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("auth-token", "Bearer " + jwt);
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        Resource resource = new ClassPathResource("testfile.txt");
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(resource.getFile()));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, requestHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/file?filename={filename}",
                HttpMethod.POST,
                requestEntity,
                String.class,
                getParam("filename", fileName)
        );
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @Order(3)
    void getAllFileUser() throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/list?limit={limit}",
                HttpMethod.GET,
                getRequestEntity(jwt),
                String.class,
                getParam("limit", "3")
        );

        JsonNode fileNode = parseResponseBody(response).get(0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testfile", fileNode.get("filename").asText());
        assertEquals(18, fileNode.get("size").asInt());
    }

    @Test
    @Order(4)
    void downloadFile() {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/file?filename={filename}",
                HttpMethod.GET,
                getRequestEntity(jwt),
                String.class,
                getParam("filename", fileName)
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("HI! It's testfile!", response.getBody());
    }

    @Test
    @Order(5)
    public void editFileName() {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/file?filename={filename}",
                HttpMethod.PUT,
                getJsonRequestEntity(jwt, fileJsonBody),
                String.class,
                getParam("filename", fileName)
        );
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @Order(6)
    public void deleteFile() {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/file?filename={filename}",
                HttpMethod.DELETE,
                getRequestEntity(jwt),
                String.class,
                getParam("filename", fileName)
        );
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @Order(7)
    public void logout() {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/logout",
                HttpMethod.POST,
                getRequestEntity(jwt),
                String.class
        );
        assertEquals(200, response.getStatusCodeValue());
    }

    private HttpEntity<String> getJsonRequestEntity(String jwt, String jsonBody) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("auth-token", "Bearer " + jwt);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(jsonBody, requestHeaders);
    }

    private JsonNode parseResponseBody(ResponseEntity<String> response) throws JsonProcessingException {
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode parsedBody = objectMapper.readTree(responseBody);
        return parsedBody;
    }

    private Map<String, String> getParam(String name, String value) {
        Map<String, String> params = new HashMap<>();
        params.put(name, value);
        return params;
    }

    private HttpEntity<String> getRequestEntity(String jwt) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("auth-token", "Bearer " + jwt);
        return new HttpEntity<>(requestHeaders);
    }
}
