package carrot.app.Map;

import lombok.AllArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@EntityScan
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Map{

    @Id
    @GeneratedValue
    private Integer mnum;
    private LocalDateTime mdatetime;
    private Double mdistance;
    private Time mtime;
    private Double mykcals;
    private Integer user_num;
    private String user_nick;
}

