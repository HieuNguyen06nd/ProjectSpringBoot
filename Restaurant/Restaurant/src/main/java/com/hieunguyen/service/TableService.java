package com.hieunguyen.service;

import com.hieunguyen.entity.Tables;
import java.util.List;
import java.util.Optional;

public interface TableService {
    List<Tables> getAllTables();
    Optional<Tables> getTableById(Long id);
    Tables createTable(Tables table);
    Tables updateTable(Long id, Tables table);
    void deleteTable(Long id);
}
