package com.hieunguyen.service.impl;

import com.hieunguyen.entity.Tables;
import com.hieunguyen.repository.TableRepository;
import com.hieunguyen.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;

    @Override
    public List<Tables> getAllTables() {
        return tableRepository.findAll();
    }

    @Override
    public Optional<Tables> getTableById(Long id) {
        return tableRepository.findById(id);
    }

    @Override
    public Tables createTable(Tables table) {
        return tableRepository.save(table);
    }

    @Override
    public Tables updateTable(Long id, Tables table) {
        Tables existing = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));
        existing.setCapacity(table.getCapacity());
        existing.setStatus(table.getStatus());
        return tableRepository.save(existing);
    }

    @Override
    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }
}
