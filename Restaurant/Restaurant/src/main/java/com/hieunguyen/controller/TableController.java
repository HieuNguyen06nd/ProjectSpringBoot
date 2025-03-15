package com.hieunguyen.controller;

import com.hieunguyen.dto.response.ApiResponse;
import com.hieunguyen.entity.Tables;
import com.hieunguyen.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Tables>>> getAllTables() {
        List<Tables> tables = tableService.getAllTables();
        return ResponseEntity.ok(ApiResponse.success(tables));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Tables>> getTableById(@PathVariable Long id) {
        Tables table = tableService.getTableById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));
        return ResponseEntity.ok(ApiResponse.success(table));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Tables>> createTable(@RequestBody Tables table) {
        Tables newTable = tableService.createTable(table);
        return ResponseEntity.ok(ApiResponse.success(newTable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Tables>> updateTable(@PathVariable Long id, @RequestBody Tables table) {
        Tables updatedTable = tableService.updateTable(id, table);
        return ResponseEntity.ok(ApiResponse.success(updatedTable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.ok(ApiResponse.success("Table deleted successfully"));
    }
}
