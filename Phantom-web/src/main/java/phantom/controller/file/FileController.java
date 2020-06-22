package phantom.controller.file;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import phantom.aop.request.annotation.RepeatLock;
import phantom.common.UString;
import phantom.constant.UserType;
import phantom.io.UFile;
import phantom.mvc.data.Result;
import phantom.mvc.data.Result.Value;
import phantom.web.security.RequireUser;

@Slf4j
@RepeatLock
@RestController
@RequestMapping(value = "/file", name = "文件上传控制器")
public class FileController {

	@SuppressWarnings("unchecked")
	@RequireUser({ UserType.ADMIN, UserType.NORMAL })
	@PostMapping(value = "/image", name = "图片上传接口")
	public Value<String> uploadImage(@RequestParam("image") MultipartFile image) {
		try {
			if (image == null) {
				return Result.errorRequest("image is null");
			}
			String fileName = String.valueOf(System.currentTimeMillis()) + "_" + image.getOriginalFilename();
			image.transferTo(new File(UString.concat(UFile.PATH, "/", fileName)));
			return Result.success(fileName);
		} catch (Exception e) {
			log.error("uploadImage:{}", e);
			return Result.fail();
		}
	}

}
