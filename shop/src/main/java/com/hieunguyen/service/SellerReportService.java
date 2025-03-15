package com.hieunguyen.service;

import com.hieunguyen.entity.Seller;
import com.hieunguyen.entity.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport (Seller seller);
    SellerReport updateSellerReport (SellerReport sellerReport);

}
