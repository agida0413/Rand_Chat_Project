package rand.chat.web.security.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import rand.chat.web.dto.common.ResponseDTO;


public interface TokenService {
    public void addRefresh(String key ,String token);
    public void deleteRefresh(String key,String token);
    public boolean isExist(String key,String token);

}
