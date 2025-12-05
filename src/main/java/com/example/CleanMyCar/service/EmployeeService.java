package com.example.CleanMyCar.service;

import com.example.CleanMyCar.model.Employee;
import com.example.CleanMyCar.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repo;

    public EmployeeService(EmployeeRepository repo) {
        this.repo = repo;
    }

    public Employee createEmployee(String fullName, String mobile, String pin) {
        Employee e = new Employee(fullName, mobile, pin);
        return repo.save(e);
    }

    public List<Employee> getAll() {
        return repo.findAll();
    }

    public Employee getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void deactivate(Long id) {
        Employee e = getById(id);
        if (e != null) {
            e.setStatus("INACTIVE");
            repo.save(e);
        }
    }

    // Simple PIN login (no JWT for now)
    public Employee login(String mobile, String pin) {
        Optional<Employee> opt = repo.findByMobileNumber(mobile);
        if (opt.isEmpty()) return null;
        Employee e = opt.get();
        if (!"ACTIVE".equalsIgnoreCase(e.getStatus())) return null;
        if (!e.getPin().equals(pin)) return null;
        return e;
    }
}
