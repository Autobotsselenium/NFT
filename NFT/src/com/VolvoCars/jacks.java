package com.VolvoCars;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class jacks {

	public static void main(String[] args) throws IOException, JsonMappingException, IOException {
		Map<String,String> profiles = new HashMap<String,String>();
		 Map<String,String> sims = new HashMap<String,String>();
		 sims.put("iccid.", Login_Volvo121212121.randomdigit(19));
		 sims.put("simType.", "eSIM");
		 sims.put("mnoOrderNumber", "DATG-80168619_200630");
		 profiles.put("imsi", "262064017781755");
		 profiles.put("msisdn", "8614506745324");
		 profiles.put("pin", "1234");
		 profiles.put("pin2", "08029014");
		 profiles.put("puk", "05807010");
		 profiles.put("puk2", "36783275");
		 profiles.put("mnoStatus", "mnoStatus");
		 
		 ObjectMapper mapper = new ObjectMapper();
		 ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		 
		 writer.writeValue(new File("C:\\Users\\jgnanara\\Downloads\\TestUNFT\\TestUNFT\\test-output\\ws.json"), sims);
		 System.out.println("--Done--");

	}

}
