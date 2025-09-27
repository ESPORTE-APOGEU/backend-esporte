package com.esporte.myapp.controller;

import com.esporte.myapp.dto.ReportRequest;
import com.esporte.myapp.entity.Report;
import com.esporte.myapp.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Void> createReport(@RequestBody ReportRequest req,
                                             @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt,
                                             org.springframework.security.core.Authentication authentication) {

        String clerkId = (jwt != null) ? jwt.getSubject() : null;
        if (clerkId == null && authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof String s) clerkId = s; else clerkId = authentication.getName();
        }

        if (clerkId == null) {
            return ResponseEntity.status(401).build();
        }

        reportService.createReport(clerkId, req);
        return ResponseEntity.noContent().build();
    }

}
