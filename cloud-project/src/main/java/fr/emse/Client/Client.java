package fr.emse.Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

public class Client {

  public static void main(String[] args) {
    Region region = Region.US_EAST_1;

    String queueURL = "https://sqs.us-east-1.amazonaws.com/986682050097/myqueue3434415666";
    String bucketName = "mybucket32555562";
    String path = Path.of("D:\\cours\\2A\\informatique\\cloud_computing\\group project\\data").toString();
    String filename = "01-10-2022-store10.csv";
    
    // Upload file to s3
    S3Client s3 = S3Client.builder().region(region).build();

    // Creates the bucket if it doesn't exist yet
    ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder()
          .build();
      ListBucketsResponse listBucketResponse = s3.listBuckets(listBucketsRequest);

      if ((listBucketResponse.hasBuckets()) && (listBucketResponse.buckets()
          .stream().noneMatch(x -> x.name().equals(bucketName)))) {

        CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
            .bucket(bucketName).build();

        s3.createBucket(bucketRequest);
      }

    PutObjectRequest putOb = PutObjectRequest.builder().bucket(bucketName)
        .key(filename).build();
    s3.putObject(putOb,
        RequestBody.fromBytes(getObjectFile(path + File.separator + filename)));
    
    sendNotification(queueURL, filename);
  }


  private static void sendNotification(String queueURL, String fileName) {
    Region region = Region.US_EAST_1;

    // Send sqs notification
    SqsClient sqsClient = SqsClient.builder().region(region).build();

    SendMessageRequest sendRequest = SendMessageRequest.builder().queueUrl(queueURL)
        .messageBody(fileName).build();

    SendMessageResponse sqsResponse = sqsClient.sendMessage(sendRequest);

    System.out.println(
        sqsResponse.messageId() + " Message sent. Status is " + sqsResponse.sdkHttpResponse().statusCode());

  }


  // Converts file to bytes
  private static byte[] getObjectFile(String filePath) {

    FileInputStream fileInputStream = null;
    byte[] bytesArray = null;

    try {
      File file = new File(filePath);
      bytesArray = new byte[(int) file.length()];
      fileInputStream = new FileInputStream(file);
      fileInputStream.read(bytesArray);

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fileInputStream != null) {
        try {
          fileInputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return bytesArray;
  }

}
