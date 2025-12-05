package com.example.CleanMyCar.service;

import com.example.CleanMyCar.model.Plan;
import com.example.CleanMyCar.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    private final PlanRepository repo;

    public PlanService(PlanRepository repo) {
        this.repo = repo;
    }

    public List<Plan> getAll() { return repo.findAll(); }

    public Plan getById(Long id) { return repo.findById(id).orElse(null); }

    public Plan create(Plan p) { return repo.save(p); }
}
