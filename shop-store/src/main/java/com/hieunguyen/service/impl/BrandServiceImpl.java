package com.hieunguyen.service.impl;

import com.hieunguyen.exception.ResourceNotFoundException;
import com.hieunguyen.model.Brand;
import com.hieunguyen.repository.BrandRepository;
import com.hieunguyen.service.BrandService;
import com.hieunguyen.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    @Transactional
    public Brand updateBrand(Long id, Brand brand) {
        Brand existingBrand = getBrandById(id);
        existingBrand.setName(brand.getName());
        existingBrand.setDescription(brand.getDescription());
        existingBrand.setLogoUrl(brand.getLogoUrl());
        existingBrand.setStatus(brand.isStatus());
        return brandRepository.save(existingBrand);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        Brand brand = getBrandById(id);
        brandRepository.delete(brand);
    }

    @Override
    @Transactional
    public void deleteBrandLogo(Long id) throws IOException {
        Brand brand = getBrandById(id);
        if (brand.getLogoUrl() != null) {
            String filename = extractFilenameFromUrl(brand.getLogoUrl());
            fileStorageService.deleteFile("brands/" + filename);
            brand.setLogoUrl(null);
            brandRepository.save(brand);
        }
    }

    @Override
    public Brand getBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public String uploadBrandImage(MultipartFile file) throws IOException {
        // Lưu file trong module "brands"
        String storedPath = fileStorageService.storeFile(file, "brands");
        // Lấy filename từ storedPath
        String filename = storedPath.substring(storedPath.lastIndexOf("/") + 1);
        // Trả về URL file
        return fileStorageService.getFileUrl(filename, "brands");
    }

    private String extractFilenameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
