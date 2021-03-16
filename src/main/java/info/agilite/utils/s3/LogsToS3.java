package info.agilite.utils.s3;

import java.io.IOException;
import java.time.LocalDate;

import com.amazonaws.services.s3.AmazonS3;

import info.agilite.utils.DateUtils;
import info.agilite.utils.StringUtils;

public class LogsToS3 {

	public static void enviarLog(AmazonS3 s3, String rastreamento, String pid, String schema, String sistema) throws IOException {
		String data = DateUtils.formatDate(LocalDate.now(), "yyMMdd");
		String key = StringUtils.concat("LOGS/", data, "/", schema, "/", sistema, "/", pid, ".log");
		S3Utils.putString(s3, "agilite-files", key, rastreamento);
	}
}
