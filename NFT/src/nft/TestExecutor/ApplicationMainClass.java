package nft.TestExecutor;

import java.util.LinkedHashMap;

import org.openqa.selenium.WebDriver;


import com.VolvoCars.Car_Volvo;
import com.VolvoCars.Login_Volvo;
import com.VolvoCars.Login_Volvo121212121;
import com.VolvoCars.Reports_Volvo;
import com.VolvoCars.Restfin;
import com.VolvoCars.Sim_Volvo;
import com.VolvoCars.TCU_Volvo;

public class ApplicationMainClass {
	public WebDriver driver;
	public ApplicationMainClass obj;
	Sim_Volvo F ;
	TCU_Volvo T ;
	Car_Volvo car ;
	public boolean MAGRCode(String currentStep, LinkedHashMap<String, String> data, WebDriver driver, int count,
			Session session) throws Exception {
		this.driver = driver;
		obj = this;
		try {

			System.out.println(currentStep);
			// if(currentStep.contains(":"))
			// {
			// currentStep=currentStep.substring(0,currentStep.indexOf(":"));
			// System.out.println("currentStep - "+currentStep);
			// }else {
			// System.out.println("No colon detected!");
			// }
			if (currentStep.equalsIgnoreCase("Volvo_login")) {
				Login_Volvo d = new Login_Volvo(data, driver, count, session);
				d.Dashdownload();
			} else if (currentStep.equalsIgnoreCase("Vovlo_SIM")) {
				 F = new Sim_Volvo(data, driver, count, session);
				F.simdata();
			} else if (currentStep.equalsIgnoreCase("Vovlo_TCU")) {
				T = new TCU_Volvo(data, driver, count, session);
				T.TCU(F);
			}

			else if (currentStep.equalsIgnoreCase("Reports_login")) {
				Reports_Volvo PS = new Reports_Volvo(data, driver, count, session);
				PS.Dashdownload();
			} else if (currentStep.equalsIgnoreCase("Vovlo_Car")) {
				car = new Car_Volvo(data, driver, count, session);
			car.Carcreattion(T);
			}else if (currentStep.equalsIgnoreCase("Esim_post")) {
				Restfin restf = new Restfin(data, driver, count, session);
			restf.Post_sims(F);
			}
			else {
				return false;
			}

		} catch (Exception j) {
			j.printStackTrace();
			throw j;
		}
		return true;
	}
}
