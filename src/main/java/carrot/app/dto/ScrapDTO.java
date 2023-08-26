package carrot.app.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScrapDTO {
	private int scrapNum;
	private int userNum;
	private String userNick;
	private int recruitNum;
}
