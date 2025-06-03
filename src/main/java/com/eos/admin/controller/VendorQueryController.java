package com.eos.admin.controller;

import com.eos.admin.dto.VendorQueryDTO;
import com.eos.admin.repository.VendorRepository;
import com.eos.admin.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candi")
@CrossOrigin("*")
public class VendorQueryController {

    // Create a logger instance
    private static final Logger logger = LoggerFactory.getLogger(VendorQueryController.class);

    @Autowired
    private QueryService queryService;
    
    @Autowired
    private VendorRepository vendorRepository;

    @PostMapping("/raisequery")
    public ResponseEntity<String> raiseQuery(@RequestBody VendorQueryDTO queryDTO) {
        try {
            // Log the received queryDTO
            logger.info("Received a new query to raise: {}", queryDTO);

            queryService.saveQuery(queryDTO);

            // Log the successful query raising
            logger.info("Query raised successfully for vendor: {}", queryDTO.getVendorEmail());

            return ResponseEntity.ok("Query raised successfully!");
        } catch (Exception e) {
            // Log the exception when the query cannot be raised
            logger.error("Failed to raise query: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to raise query: " + e.getMessage());
        }
    }

    @GetMapping("/getByVendorEmail")
    public ResponseEntity<List<VendorQueryDTO>> getQueriesByVendorEmail(@RequestParam("vendorEmail") String vendorEmail) {
        try {

            // Log the request for queries by vendor email
            logger.info("Fetching queries for vendor email: {}", vendorEmail);

            List<VendorQueryDTO> queries = queryService.getQueriesByVendorEmail(vendorEmail);

            if (queries.isEmpty()) {
                // Log if no queries are found
                logger.warn("No queries found for vendor email: {}", vendorEmail);
                return ResponseEntity.noContent().build();
            }

            // Log the number of queries found
            logger.info("Found {} queries for vendor email: {}", queries.size(), vendorEmail);

            return ResponseEntity.ok(queries);
        } catch (Exception e) {
            // Log the exception if there's an error fetching the queries
            logger.error("Error fetching queries for vendor email {}: {}", vendorEmail, e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }
}
