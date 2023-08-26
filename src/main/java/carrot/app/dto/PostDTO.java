package carrot.app.dto;

import java.sql.Blob;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PostDTO {
	private int pnum;
	private String ptext;
	private String pdatetime;
	private String photo;
	private int user_num;
	private String user_nick;
	private LocalDateTime pdatetime_modified;
	private int ord_num;
	private int level_num;
	private int depth_num;
	private Blob pphoto;
	private MultipartFile imageFile;
	private byte[] fileContent;
	private byte[] profileContent;
	private int filesize;
	private int likecnt;
	private int commentcnt;
}
