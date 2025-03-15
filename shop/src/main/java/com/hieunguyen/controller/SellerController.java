package com.hieunguyen.controller;

import com.hieunguyen.config.JwtProvider;
import com.hieunguyen.dto.request.LoginRequest;
import com.hieunguyen.dto.response.ApiResponse;
import com.hieunguyen.dto.response.AuthResponse;
import com.hieunguyen.entity.Seller;
import com.hieunguyen.entity.SellerReport;
import com.hieunguyen.entity.VerificationCode;
import com.hieunguyen.exception.SellerException;
import com.hieunguyen.repository.SellerReportRepository;
import com.hieunguyen.repository.VerificationCodeRepository;
import com.hieunguyen.service.AuthService;
import com.hieunguyen.service.EmailService;
import com.hieunguyen.service.SellerReportService;
import com.hieunguyen.service.SellerService;
import com.hieunguyen.utils.AccountStatus;
import com.hieunguyen.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;
    private final SellerReportService sellerReportService;



    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest request) throws Exception {

        String otp = request.getOtp();
        String email = request.getEmail();

        request.setEmail("seller_"+email);
        AuthResponse authResponse = authService.signing(request);

        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new Exception("Wrong otp ...");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception {
        Seller saveSeller = sellerService.createSeller(seller);

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "hieu nguyen login/signup otp";
        String text = "Welcome to H2, verify your account using this link ";
        String frontend_url= "http://localhost:8080/shop/verify-seller";
        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text+frontend_url);

        return new ResponseEntity<>(saveSeller, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
        Seller seller = sellerService.getSellerById(id);

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller>getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        Seller seller = sellerService.getSellerByEmail(email);

        return ResponseEntity.ok(seller);
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);

        SellerReport sellerReport = sellerReportService.getSellerReport(seller);

        return new ResponseEntity<>(sellerReport, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<Seller>>getAllSeller(@RequestParam(required = false) AccountStatus status){
        List<Seller> sellerList = sellerService.getAllSeller(status);

        return ResponseEntity.ok(sellerList);
    }

    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt,@RequestBody Seller seller ) throws Exception {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updateSeller = sellerService.updateSeller(profile.getId(), seller);

        return ResponseEntity.ok(updateSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);

        return ResponseEntity.noContent().build();
    }
}
