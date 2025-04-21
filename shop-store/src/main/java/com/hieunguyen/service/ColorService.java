package com.hieunguyen.service;

import com.hieunguyen.model.Color;
import java.util.List;

public interface ColorService {
    Color createColor(Color color);
    Color updateColor(Long id, Color color);
    void deleteColor(Long id);
    Color getColorById(Long id);
    List<Color> getAllColors();
}
