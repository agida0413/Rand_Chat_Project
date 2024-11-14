package rand.chat.web.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter @Setter
public class ResponseDTO<T> {

    private final int status=200;
    private final String code="SUCCESS REQUEST";
    private  T data;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp=LocalDateTime.now();

    public ResponseDTO(T data) {
        this.data = data;

    }

    public ResponseDTO() {
    }
}
