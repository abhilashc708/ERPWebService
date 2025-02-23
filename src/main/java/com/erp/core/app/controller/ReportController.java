package com.erp.core.app.controller;

import com.erp.core.app.dto.ReportDto;
import com.erp.core.app.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/admin")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping(value="/report",produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportDto> getReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        ReportDto report = reportService.getReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

}
