package com.hieunguyen.service.impl;

import com.hieunguyen.config.JwtProvider;
import com.hieunguyen.entity.Address;
import com.hieunguyen.entity.Seller;
import com.hieunguyen.exception.SellerException;
import com.hieunguyen.repository.AddressRepository;
import com.hieunguyen.repository.SellerRepository;
import com.hieunguyen.service.SellerService;
import com.hieunguyen.utils.AccountStatus;
import com.hieunguyen.utils.USER_ROLE;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    @Override
    public Seller getSellerProfile(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);

        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {
        Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());
        if (sellerExist != null){
            throw new Exception("Seller already exist, used different email.");
        }
        Address address = addressRepository.save(seller.getPickupAddress());

        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(seller.getPassword());
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(address);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
        return sellerRepository.findById(id).orElseThrow(()-> new SellerException("Seller not found with id: " + id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null){
            throw new Exception("Seller not found ...");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSeller(AccountStatus status) {
        return sellerRepository.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {
        Seller existingSeller = this.getSellerById(id);

        if (seller.getSellerName() != null){
            existingSeller.setSellerName(seller.getSellerName());
        }
        if (seller.getMobile() != null){
            existingSeller.setMobile(seller.getMobile());
        }
        if (seller.getEmail() != null){
            existingSeller.setEmail(seller.getEmail());
        }
        if (seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null){
            existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
        }
        if (seller.getBankDetails() != null
                && seller.getBankDetails().getAccountHolderName() != null
                &&seller.getBankDetails().getIfsCode() !=null
                && seller.getBankDetails().getAccountNumber() != null
        ){
            existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
            existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
            existingSeller.getBankDetails().setIfsCode(seller.getBankDetails().getIfsCode());
        }
        if (seller.getPickupAddress() != null
                &&seller.getPickupAddress().getAddress() != null
                && seller.getPickupAddress().getMobile() != null
                && seller.getPickupAddress().getCity() != null
                && seller.getPickupAddress().getState() != null
        ){
            existingSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
            existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            existingSeller.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
        }

        if (existingSeller.getGSTIN() != null){
            existingSeller.setGSTIN(seller.getGSTIN());
        }

        return sellerRepository.save(existingSeller);

    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        Seller seller = getSellerById(id);

        sellerRepository.delete(seller);

    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);

        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long id, AccountStatus status) throws Exception {
        Seller seller = getSellerById(id);
        seller.setAccountStatus(status);

        return sellerRepository.save(seller);
    }
}
