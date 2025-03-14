package a311.college.dto.college;

import lombok.Data;

@Data
public class AddCommentDTO {
    private Long userId;

    private Integer schoolId;

    private String comment;
}
