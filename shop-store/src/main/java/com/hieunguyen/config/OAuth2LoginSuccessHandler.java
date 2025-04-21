package com.hieunguyen.config;

import com.hieunguyen.utils.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        // Lấy thông tin người dùng từ OAuth2User
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        // Lấy email làm username, bạn cũng có thể lấy các thông tin khác từ oauthUser nếu cần
        String email = oauthUser.getAttribute("email");

        // Lấy role từ OAuth2User, giả sử bạn có thể lấy role từ dữ liệu của OAuth2
        // Bạn có thể điều chỉnh phần này tùy thuộc vào cách thông tin về vai trò được cung cấp
        String role = oauthUser.getAttribute("role");  // Ví dụ: lấy role từ attribute

        // Nếu không có role, gán mặc định là "USER" (hoặc vai trò khác bạn muốn)
        if (role == null) {
            role = Role.CUSTOMER.name();  // Đảm bảo rằng giá trị này hợp lệ với enum Role
        }

        // Tạo token JWT với thông tin username và role
        String token = jwtUtil.generateToken(email, Role.valueOf(role));

        // Trả về token dưới dạng JSON cho client
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
        response.getWriter().flush();
    }
}
