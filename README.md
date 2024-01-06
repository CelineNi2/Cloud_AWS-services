# Instructions

To run the project, you can either follow the instructions on the demonstration video or the steps that are written below: 

1. On AWS, create a bucket, a SQS queue, and a lambda function
2. In your java code Client, Worker, lambdaWorker and Consolidator, replace the defined variables bucketName and queueURL by the ones you just created (this step has been skipped in the video since it was already setup; note that the 2 variables aren't present in every file, sometimes there is only one of them)
3. On AWS, add your SQS queue as a triggering event of your lambda function
4. On AWS, replace the code of your lambda function by your "cloud-project/target/cloud-project-1.0-SNAPSHOT-jar-with-dependencies.jar" file from the target folder
5. On AWS, edit which function of your project is used when the function is triggered (the handleRequest function from lambdaWorker)
6. Open your Client.java file, change the path and filename variables to the ones you want to upload and run the main function
The file you just uploaded will be taken care by the SQS que and lambda function you just set up, creating or updating the sum up file for the date
7. When all the files or the day have been uploaded (and treated by the lambda function), you can either run the Consolidator main function on you machine (after modifying the filename to the date you want to analyse) or run it on an Amazon EC2 instance (note that the first solution is way easier to set up)
8. For the second option, you have to create an EC2 instance and run the following command to download java
```sh
sudo dnf install java-17-amazon-corretto
sudo dnf install java-17-amazon-corretto-devel
```
9. Then you have to transfer your "cloud-project/target/cloud-project-1.0-SNAPSHOT-jar-with-dependencies.jar" file to the virtual machine as well as your .aws folder (on Windows, that folder is located in the user/USERNAME directory). For that, you have to send data to your virtual michine using the SFTP protocol as described [here](https://ci.mines-stetienne.fr/cps2/course/cloud/labs/01-aws-ec2.html)
10. Finally, you can launch the following command on your virtual machine : 
```sh
java -cp cloud-project-1.0-SNAPSHOT-jar-with-dependencies.jar fr.emse.Consolidator.Consolidator
```

