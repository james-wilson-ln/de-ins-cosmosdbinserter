package com.relx.dataeng;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CosmosDBFileUploader {

    private static final String COSMOSDB_ENDPOINT = "https://localhost:8081";

    private enum Parameters {DATABASE_KEY, DATABASE_NAME, CONTAINER_NAME, PARTITION_KEY, FILE_PATH}
    public static void main(String[] args) {

        if (args.length != Parameters.values().length) {
            System.out.println("Usage: java com.relx.dataeng.CosmosDBFileUploader <database key> <database name> <container name> <partition key> <file path>");
            return;
        }
        String databaseKey = args[Parameters.DATABASE_KEY.ordinal()];
        String databaseName = args[Parameters.DATABASE_NAME.ordinal()];
        String containerName = args[Parameters.CONTAINER_NAME.ordinal()];
        String partitionKey = '/' + args[Parameters.PARTITION_KEY.ordinal()];
        String filePath = args[Parameters.FILE_PATH.ordinal()];
        try {
            String fileContent = readFileContent(filePath);
            uploadFileContent(databaseKey, databaseName, containerName, partitionKey, fileContent);
            System.out.println("File content uploaded successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while uploading the file content: " + e.getMessage());
        }
    }

    private static String readFileContent(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }

    private static void uploadFileContent(String databaseKey, String databaseName, String containerName, String partitionKey, String fileContent) throws Exception {
        JsonNode node = new ObjectMapper().readTree(fileContent);
        System.out.println(node.toPrettyString());

        CosmosClient cosmosClient = new CosmosClientBuilder()
                .endpoint(COSMOSDB_ENDPOINT)
                .key(databaseKey)
                .buildClient();

        CosmosDatabase database = cosmosClient.getDatabase(databaseName);
        database.createContainerIfNotExists(containerName, partitionKey);
        CosmosContainer container = database.getContainer(containerName);

        container.upsertItem(node);
        cosmosClient.close();
    }
}
