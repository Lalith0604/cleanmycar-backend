package com.example.CleanMyCar.controller;

import com.example.CleanMyCar.dto.AdminStatsDto;
import com.example.CleanMyCar.model.Customer;
import com.example.CleanMyCar.model.PaymentRecord;
import com.example.CleanMyCar.model.Subscription;
import com.example.CleanMyCar.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** GET /api/admin/customers?page=0&size=20 */
    @GetMapping("/customers")
    public ResponseEntity<Page<Customer>> getCustomers(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "20") int size) {
        Pageable p = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.getAllCustomers(p));
    }

    /** GET /api/admin/subscriptions?page=0&size=20&status=ACTIVE */
    @GetMapping("/subscriptions")
    public ResponseEntity<Page<Subscription>> getSubscriptions(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "20") int size,
                                                               @RequestParam(required = false) String status) {
        Pageable p = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.getSubscriptions(p, status));
    }

    /** GET /api/admin/payments?page=0&size=20 */
    @GetMapping("/payments")
    public ResponseEntity<Page<PaymentRecord>> getPayments(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "20") int size) {
        Pageable p = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.getPayments(p));
    }

    /** GET /api/admin/stats */
    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDto> getStats() {
        AdminStatsDto stats = adminService.getStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/export/customers")
    public ResponseEntity<byte[]> exportCustomers() {
        String csv = adminService.exportCustomersCsv();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=customers.csv")
                .header("Content-Type", "text/csv")
                .body(csv.getBytes());
    }

    @GetMapping("/export/subscriptions")
    public ResponseEntity<byte[]> exportSubscriptions() {
        String csv = adminService.exportSubscriptionsCsv();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=subscriptions.csv")
                .header("Content-Type", "text/csv")
                .body(csv.getBytes());
    }

    @GetMapping("/export/payments")
    public ResponseEntity<byte[]> exportPayments() {
        String csv = adminService.exportPaymentsCsv();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=payments.csv")
                .header("Content-Type", "text/csv")
                .body(csv.getBytes());
    }




}

