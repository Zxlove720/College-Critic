package a311.college.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class PhoneLoginDTO implements Serializable {
    public String phone;

    public String code;
}
