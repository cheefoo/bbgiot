# bbgiot
BBG IOT is  an AWS Lambda function written in Java that integrates with the AWS IOT Cloud to change the color of a light sensor attached to a Beagle Bone Device.

To run this code:

1. First follow the walkthrough in the below link to upload the NodeJS code to your beagle bine green device.
http://www.seeedstudio.com/wiki/Beagle_Bone_Green_and_Grove_IoT_Starter_Kit_Powered_by_AWS

2. Clone this repository
3. Execute mvn package (make sure you have maven installed on your workstation)
http://docs.aws.amazon.com/lambda/latest/dg/lambda-java-how-to-create-deployment-package.html
4. Login to your AWS management console and navigate to the Lambda AWS console to upload the shaded jar produced from 3. above
