package com.mdrsolutions.records_management.controller;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class TranscriptSseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TranscriptSseController.class);
    // Suppose you store each student's SSEEmitter in a Map keyed by studentId
    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    /**
     * Endpoint that the client connects to for SSE updates.
     * e.g., GET /transcript-status?studentId=123
     */
    @GetMapping(value = "/transcript-status", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter transcriptStatus(@RequestParam String studentId) {
        SseEmitter emitter = new SseEmitter(0L); // 0L = no timeout
        sseEmitters.put(studentId, emitter);

        // Optionally, send an initial status
        try {
            emitter.send(SseEmitter.event()
                    .name("status")
                    .data("Waiting for transcript..."));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    @PostMapping(
            value = "/uploadTranscriptAndProcess",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public void uploadTranscriptAndProcess(
            @RequestParam("file") MultipartFile file,
            @RequestParam String studentId) {
        // Log and/or process the file as needed
        LOGGER.info("Received file for studentId={}", studentId);

        // Process asynchronously and stream updates via SSE
        new Thread(() -> simulateTranscriptProcessing(studentId)).start();

    }

    /**
     * Simulate the transcript processing steps.
     */
    private void simulateTranscriptProcessing(String studentId) {
        SseEmitter emitter = sseEmitters.get(studentId);
        if (emitter == null) {
            return;
        }

        try {
            // 1. Received
            emitter.send(SseEmitter.event().name("status").data("1: Received"));
            Thread.sleep(1000);
            LOGGER.info("Received");
            // 2. Processing
            emitter.send(SseEmitter.event().name("status").data("2: Processing"));
            Thread.sleep(2000);
            LOGGER.info("Processing");
            // 3. Validating Input
            emitter.send(SseEmitter.event().name("status").data("3: Validating Input"));
            Thread.sleep(2000);
            LOGGER.info("Validating");
            // 4. Complete
            emitter.send(SseEmitter.event().name("status").data("4: Complete"));
            Thread.sleep(1000);
            LOGGER.info("Completed");
            // 5. Accepted/Rejected
            boolean accepted = true; // or some real logic
            String finalMsg = accepted ? "5: Accepted" : "5: Rejected";
            emitter.send(SseEmitter.event().name("status").data(finalMsg));
            Thread.sleep(1000);
            LOGGER.info(finalMsg);

            emitter.complete();
        } catch (IOException | InterruptedException e) {
            LOGGER.info("Closing SSE Connection but there were errors");
            emitter.completeWithError(e);
        }
    }
}


