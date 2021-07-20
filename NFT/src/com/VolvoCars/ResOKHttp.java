package com.VolvoCars;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.*;
import okhttp3.OkHttpClient;

public class ResOKHttp {

	public static void main(String[] args) throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder()
				  .build();
				MediaType mediaType = MediaType.parse("application/json;api-version=1.0");
				RequestBody body = RequestBody.create(mediaType, "{\n    \"sims\": [\n        {\n            \"iccid\": \"12342221118203781313\",\n            \"simType\": \"ESIM\",\n            \"mnoOrderNumber\": \"AS7hfd454\",\n            \"profiles\": [\n                {\n                    \"imsi\": \"310170500022266\",\n                    \"msisdn\": \"\",\n                    \"mno\": \"\",\n                    \"productionDate\": \"2019-03-31\",\n                    \"pin\": \"1111\",\n                    \"pin2\": \"2222\",\n                    \"puk\": \"23423\",\n                    \"puk2\": \"32353230\",\n                    \"isMigrated\": \"Yes\"\n                }\n            ]\n        }\n    ]\n}");
				Request request = new Request.Builder()
				  .url("https://api-gw-qa.vssm.volvocars.biz/simservice/api/sims")
				  .method("POST", body)
				  .addHeader("X-Request-ID", "20190410001")
				  .addHeader("Date", "18 APR 2019 12:28:07")
				  .addHeader("Vssm-api-key", "8a59a0b7e042491ba09aff88b32f2b8f")
				  .addHeader("Content-Type", "application/json;api-version=1.0")
				  .build();
				Response response = client.newCall(request).execute();
				 ResponseBody body1 = response.body();

				 System.out.println("Response Body is: " + body1.toString());
				 
				 String jsonData = response.body().string();
				 System.out.println(jsonData);
				    JSONObject Jobject = new JSONObject(jsonData);
				    System.out.println();
				    System.out.println(Jobject);
				   /* JSONArray Jarray = Jobject.getJSONArray("employees");*/

				    /*for (int i = 0; i < Jarray.length(); i++) {
				        JSONObject object     = Jarray.getJSONObject(i);
				    }*/
				}
				

	}


