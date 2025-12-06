package com.example.CleanMyCar.controller;

import com.example.CleanMyCar.model.Plan;
import com.example.CleanMyCar.service.PlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class PlanController {
    private final PlanService planService;

        public PlanController(PlanService planService) {
                this.planService = planService;
                    }

                        @GetMapping
                            public ResponseEntity<List<Plan>> list() {
                                    return ResponseEntity.ok(planService.getAll());
                                        }

                                            @GetMapping("/{id}")
                                                public ResponseEntity<?> getById(@PathVariable Long id) {
                                                        Plan plan = planService.getById(id);
                                                                if (plan == null) return ResponseEntity.notFound().build();
                                                                        return ResponseEntity.ok(plan);
                                                                            }

                                                                                @PostMapping
                                                                                    public ResponseEntity<Plan> create(@RequestBody Plan plan) {
                                                                                            Plan created = planService.create(plan);
                                                                                                    return ResponseEntity.ok(created);
                                                                                                        }
                                                                                                        }
                                                                                                        