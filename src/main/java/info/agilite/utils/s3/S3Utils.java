package info.agilite.utils.s3;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.amazonaws.services.s3.AmazonS3;

public class S3Utils {
	private static final String BUCKET_NAME = "info.agilite.api";
	
	public static void putString(AmazonS3 s3, S3Type type, String key, String data) throws IOException {
		s3.putObject(BUCKET_NAME, type.toString() + "/" + key, data);
	}
	
	public static void putBytes(AmazonS3 s3, S3Type type, String key, byte[] data) throws IOException {
		s3.putObject(BUCKET_NAME, type.toString() + "/" + key, new ByteArrayInputStream(data), null);
	}
}
