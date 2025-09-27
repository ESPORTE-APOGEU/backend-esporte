package com.esporte.myapp.service;

import com.esporte.myapp.dto.ReportRequest;
import com.esporte.myapp.entity.Report;
import com.esporte.myapp.entity.User;
import com.esporte.myapp.repository.EventRepository;
import com.esporte.myapp.repository.ReportRepository;
import com.esporte.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public User getUserReferenceById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public Report createReport(String reporterId, ReportRequest req) {
        User reporter = getUserReferenceById(reporterId);

        Report r = new Report();
        r.setReporter(reporter);

    if (req.reportedUserId() != null && !req.reportedUserId().isBlank()) {
        // If reported user id is provided, require that the user exists; otherwise return 400
        User reported = userRepository.findById(req.reportedUserId()).orElseThrow(
            () -> new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST, "Reported user not found")
        );
        r.setReportedUser(reported);
    }

        try {
            Report.Type t = Report.Type.valueOf(req.type());
            r.setType(t);
        } catch (Exception ex) {
            r.setType(Report.Type.OUTROS);
        }

        r.setDescription(req.description());

        return reportRepository.save(r);
    }
}
