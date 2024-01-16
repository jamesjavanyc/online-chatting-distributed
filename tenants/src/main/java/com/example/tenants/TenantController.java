package com.example.tenants;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TenantController {
    private final TenantRepository tenantRepository;

    @PostMapping("/tenant")
    public void saveTenant(@RequestBody Tenant tenant){
        this.tenantRepository.save(tenant);
    }

    @GetMapping("/tenant")
    public Tenant saveTenant(@RequestParam("id") Long id){
        return this.tenantRepository.findById(id).orElseThrow(() -> new RuntimeException("Tenant not found"));
    }
}
