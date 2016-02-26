package com.tayo.dfw.tm4.iotbbg;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Random;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.iotdata.AWSIotDataClient;
import com.amazonaws.services.iotdata.model.GetThingShadowRequest;
import com.amazonaws.services.iotdata.model.GetThingShadowResult;
import com.amazonaws.services.iotdata.model.UpdateThingShadowRequest;
import com.amazonaws.services.iotdata.model.UpdateThingShadowResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class BBGShadowUpdater 
{
	public static final String BLUE_INPUT_STRING = "{\"state\":{\r\n" + 
			"  \"desired\": {\r\n" + 
			"    \"red\": 0,\r\n" + 
			"    \"green\": 0,\r\n" + 
			"    \"blue\": 255,\r\n" + 
			"    \"value\": \"24.19\"\r\n" + 
			"  }\r\n" + 
			"}}";
	
	public static final String RED_INPUT_STRING = "{\"state\":{\r\n" + 
			"  \"desired\": {\r\n" + 
			"    \"red\": 255,\r\n" + 
			"    \"green\": 0,\r\n" + 
			"    \"blue\": 0,\r\n" + 
			"    \"value\": \"24.19\"\r\n" + 
			"  }\r\n" + 
			"}}";
	
	public static final String GREEN_INPUT_STRING = "{\"state\":{\r\n" + 
			"  \"desired\": {\r\n" + 
			"    \"red\": 0,\r\n" + 
			"    \"green\": 255,\r\n" + 
			"    \"blue\": 0,\r\n" + 
			"    \"value\": \"24.19\"\r\n" + 
			"  }\r\n" + 
			"}}";
	
	
	public static final String BBG1_THING = "BBG1-temperature";
	
	public void handleRequest (Object input, Context context) throws Exception 
	{
		LambdaLogger logger = context.getLogger();		
		logger.log("received : " + input.toString()); // No input for now, we will run with CRON and randomize the inputs
		AWSIotDataClient iotDataClient = new AWSIotDataClient(new EnvironmentVariableCredentialsProvider());	
		GetThingShadowRequest getThingShadowRequest = new GetThingShadowRequest().withThingName(BBG1_THING);
		GetThingShadowResult gtsr = iotDataClient.getThingShadow(getThingShadowRequest);			
		logger.log("Obtained the thing shadow successfully :"  + gtsr.getPayload().toString());
		UpdateThingShadowRequest updateTSR = new UpdateThingShadowRequest();
		//updateTSR.setRequestCredentials(new EnvironmentVariableCredentialsProvider());
		updateTSR.setThingName(BBG1_THING);
		
		//Randomize the input 
		Random r = new Random();
		int number = r.nextInt(8)+1;
		
		try
		{
			if(0 <= number && number <= 2 )
			{
				logger.log("RED ALERT  Received:"  + RED_INPUT_STRING );
				updateTSR.setPayload(ByteBuffer.wrap(RED_INPUT_STRING.getBytes("utf-8")));
			}
			else if(3 <= number && number <= 5)
			{
				logger.log("BLUE ALERT  Received:"  + BLUE_INPUT_STRING );
				updateTSR.setPayload(ByteBuffer.wrap(BLUE_INPUT_STRING.getBytes("utf-8")));
			}
			else
			{
				logger.log("GREEN ALERT  Received:"  + GREEN_INPUT_STRING );
				updateTSR.setPayload(ByteBuffer.wrap(GREEN_INPUT_STRING.getBytes("utf-8")));
			}
			
		}
		catch(UnsupportedEncodingException ues)
		{
			ues.printStackTrace();
			throw new Exception(ues.getMessage());
		}	
		
	
		UpdateThingShadowResult updateTSResult = iotDataClient.updateThingShadow(updateTSR);
		
		
		logger.log("Thing Shadow Updated successfully :"  + updateTSResult.toString());
		logger.log("Update Results from Thing Shadow :" + updateTSResult.getPayload());
					
	
		} 
		
	

}
