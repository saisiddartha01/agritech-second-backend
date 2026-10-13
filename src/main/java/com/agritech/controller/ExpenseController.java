package com.agritech.controller;

import com.agritech.model.Expense;
import com.agritech.repository.ExpenseRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseRepository repo;

    public ExpenseController(ExpenseRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Expense> all() {
        return repo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Expense create(@Valid @RequestBody Expense e) {
        e.setId(null);
        return repo.save(e);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found");
        }
        repo.deleteById(id);
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        List<Expense> list = repo.findAll();
        double total = list.stream().mapToDouble(Expense::getAmount).sum();
        Map<String, Double> byCategory = new TreeMap<>();
        list.forEach(e -> byCategory.merge(e.getCategory(), e.getAmount(), Double::sum));
        return Map.of("total", total, "count", list.size(), "byCategory", byCategory);
    }
}
