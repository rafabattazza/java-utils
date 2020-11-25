package info.agilite.utils.s3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;


public class S3Utils {	
	public static void putString(AmazonS3 s3, String buscketName, String key, String data) throws IOException {
		s3.putObject(buscketName, key, data);
	}
	
	public static void putBytes(AmazonS3 s3, String buscketName, String key, byte[] data) throws IOException {
		s3.putObject(buscketName, key, new ByteArrayInputStream(data), null);
	}
	
	public static void putFile(AmazonS3 s3, String buscketName, String key, File data) throws IOException {
		s3.putObject(buscketName, key, new FileInputStream(data), null);
	}
	
	public static void putInputStream(AmazonS3 s3, String buscketName, String key, InputStream is, ObjectMetadata metadata) throws IOException {
		s3.putObject(buscketName, key, is, metadata);
	}
	
	
	public static List<String> listKeys(AmazonS3 s3, String buscketName, String prefix) throws IOException {
		ObjectListing list = s3.listObjects(buscketName, prefix);
		
		return list.getObjectSummaries().stream()
			.map(S3ObjectSummary::getKey)
			.filter(obj -> !obj.endsWith("/"))
			.collect(Collectors.toList());
		
	}
	
	public static S3ObjectInputStream getInputStream(AmazonS3 s3, String buscketName, String key) throws IOException {
		S3Object s3Obj = s3.getObject(buscketName, key);
		return s3Obj.getObjectContent();
	}
	
	public static byte[] getBytes(AmazonS3 s3, String buscketName, String key) throws IOException {
		try {
			S3Object s3Obj = s3.getObject(buscketName, key);
			return IOUtils.toByteArray(s3Obj.getObjectContent());
		} catch (AmazonS3Exception e) {
			if (e.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				return null;
			} else {
				throw e;
			}
		}
	}
	
	public static void deleteObject(AmazonS3 s3, String buscketName, String key) throws IOException {
		s3.deleteObject(buscketName, key);
	}
	public static void deleteObjects(AmazonS3 s3, String buscketName, List<String> keys) throws IOException {
        DeleteObjectsRequest dor = new DeleteObjectsRequest(buscketName).withKeys(keys.toArray(new String[0]));
        s3.deleteObjects(dor);
	}

}
