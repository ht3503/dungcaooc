package com.javamongo.moviebooktickets.service;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.javamongo.moviebooktickets.dto.account.LoginRegisterResponse;
import com.javamongo.moviebooktickets.dto.account.LoginRequest;
import com.javamongo.moviebooktickets.dto.account.RegisterRequest;
import com.javamongo.moviebooktickets.entity.AppUser;
import com.javamongo.moviebooktickets.repository.AppUserRepository;

@Service
public class UserService {

    @Autowired
    private AppUserRepository usersRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    
    public LoginRegisterResponse Login(LoginRequest loginRequest) {
        LoginRegisterResponse response = new LoginRegisterResponse();

        try {
            Optional<AppUser> appuser = usersRepo.findByEmail(loginRequest.getEmail());
            if (appuser.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("Tài khoản không tồn tại!");
                return response;
            }

            // Kiểm tra xem email và password có khớp không
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword()));

            var user = appuser.get();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRoles(user.getRoles());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Đăng nhập thành công!");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    public LoginRegisterResponse Register(RegisterRequest registerRequest) {
        LoginRegisterResponse response = new LoginRegisterResponse();

        try {
            // Kiểm tra mật khẩu có khớp không
            if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
                response.setStatusCode(400);
                response.setMessage("Mật khẩu không khớp!");
                return response;
            }

            // Kiểm tra xem email đã tồn tại chưa
            Optional<AppUser> appuser = usersRepo.findByEmail(registerRequest.getEmail());
            if (!appuser.isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Email đã tồn tại!");
                return response;
            }

            var ourUsers = new AppUser();
            ourUsers.setEmail(registerRequest.getEmail());
            ourUsers.setName(registerRequest.getName());
            ourUsers.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            ourUsers.setAccountBalance(1000000); // Set số dư ví mặc định là 1 triệu
            // Set role mặc định là Customer
            ourUsers.setRoles(Set.of("Customer"));
            AppUser appUser = new AppUser();
            appUser.setId(null);
            appUser = usersRepo.save(ourUsers);

            if (appUser.getId() != null) {
                response.setUser(appUser);
                response.setStatusCode(200);
                response.setMessage("Đăng kí thành công!");
            }

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }

}
