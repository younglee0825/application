package carrot.app.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CommentDTO {
	private int cnum;
	private String ctext;
	private String cdatetime;
	private int post_num;
	private int user_num;
	private String unick;
	private String commentdesc;
	private byte[] profile;
}
