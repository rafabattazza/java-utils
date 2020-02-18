package info.agilite.utils.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3Builder {
	public static AmazonS3 create(String awsRegion, String awsId, String awsKey) {
		BasicAWSCredentials awsCred = new BasicAWSCredentials(awsId, awsKey);
		AmazonS3 s3Cli = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.fromName(awsRegion))
				.withCredentials(new AWSStaticCredentialsProvider(awsCred))
				.build();
		return s3Cli;
	}

}
