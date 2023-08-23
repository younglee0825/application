package carrot.app.Map;

import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;


@Data
public class MapVo {
    private Integer mnum;
    private LocalDateTime mdatetime;
    private Double mdistance;
    private Time mtime;
    private Double mykcals;
    private Integer user_num;
    private String user_nick;

}

