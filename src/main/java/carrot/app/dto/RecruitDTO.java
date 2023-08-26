package carrot.app.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RecruitDTO {
	private int rnum;
	private MultipartFile rphoto;
	private byte[] rphotos;
	private int filesize;
	private String imageData;
	private String rtitle;
	private String rtext;
	private String rdate;
	private String rtime;
	private String rdatetime;
	private int rcount;
	private String rloc;
	private String radrs;
	private String sigungu;
	private String etc;	
	private String status;
	private int userNum;
	private String userNick;
	private String week;
	private byte[] profile;
	private int scrapCount;
	private int joinCount;
}
