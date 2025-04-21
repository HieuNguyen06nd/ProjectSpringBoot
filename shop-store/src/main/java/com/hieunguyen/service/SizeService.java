package com.hieunguyen.service;

import com.hieunguyen.model.Size;
import java.util.List;

public interface SizeService {
    Size createSize(Size size);
    Size updateSize(Long id, Size size);
    void deleteSize(Long id);
    Size getSizeById(Long id);
    List<Size> getAllSizes();
}
