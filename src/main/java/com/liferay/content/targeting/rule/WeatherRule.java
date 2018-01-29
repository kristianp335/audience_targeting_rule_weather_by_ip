package com.liferay.content.targeting.rule;

import com.liferay.content.targeting.anonymous.users.model.AnonymousUser;
import com.liferay.content.targeting.api.model.BaseJSPRule;
import com.liferay.content.targeting.api.model.Rule;
import com.liferay.content.targeting.model.RuleInstance;
import com.liferay.content.targeting.rule.categories.SampleRuleCategory;
import com.liferay.content.targeting.rule.categories.SessionAttributesRuleCategory;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.geolocation.GeoLocationPoint;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;


import org.json.simple.parser.JSONParser;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = Rule.class)
public class WeatherRule extends BaseJSPRule {

	@Activate
	@Override
	public void activate() {
		super.activate();
	}

	@Deactivate
	@Override
	public void deActivate() {
		super.deActivate();
	}

	@Override
	public boolean evaluate(
			HttpServletRequest httpServletRequest, RuleInstance ruleInstance,
			AnonymousUser anonymousUser)
		throws Exception {

		// You can obtain the rule configuration from the type settings

		String ruleWeather = GetterUtil.getString(ruleInstance.getTypeSettings());
		String actualWeather = "";
		
		//Call longitude lattitude API
		
		String ipAddress = httpServletRequest.getRemoteAddr();		

		Client client = Client.create();
	    WebResource webResource = client.resource("https://ipapi.co/92.234.68.98/json");
	    ClientResponse response = webResource.get(ClientResponse.class);   
		
	    //parse JSON objects
	    
	    JSONObject jsonObject = JSONFactoryUtil.createJSONObject(response.getEntity(String.class));
	    Object latitude = jsonObject.get("latitude");
	    Object longitude = jsonObject.get("longitude");
	    
	    //Call weather API
	    
	    String apiKey = "1923b9fbe9106354dd670445a384b48d";
	    Client weatherClient = Client.create();
	    WebResource weatherWebResource = weatherClient.resource("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&APPID=" + apiKey);
	    ClientResponse weatherResponse = weatherWebResource.get(ClientResponse.class);
	    JSONObject weatherJsonObject = JSONFactoryUtil.createJSONObject(weatherResponse.getEntity(String.class));
	    int weatherCode = weatherJsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
	    if ((weatherCode >= 200 && weatherCode < 600) || weatherCode == 900 || weatherCode == 901 || weatherCode == 902) {
	    	actualWeather="rainy";
	    }
	    else
	    	if (weatherCode >= 600 && weatherCode <= 622) {
		    	actualWeather="snowy";
		    }
	    	else
	    		if (weatherCode == 800 || weatherCode == 801 ) {
	    			actualWeather="sunny";
	    		}
	    		else
	    			if (weatherCode == 802 || weatherCode == 803|| weatherCode == 804) {
	    				actualWeather="cloudy";
	    			}
	    			else
	    				if (weatherCode == 905 || weatherCode >= 956 ) {
	    					actualWeather="windy";
	    				}
	    				else
	    					if (weatherCode == 701 || weatherCode == 721 || weatherCode == 771 || weatherCode == 741) {
	    						actualWeather="misty";
	    					}
	    						
	    System.out.println("The weather is " + actualWeather);
	    System.out.println("The weather rule state is " + ruleWeather);
	    
	    boolean checkWeather = ruleWeather.equals(actualWeather);
	    System.out.println("The rule should return " + checkWeather);
	    					
		return ruleWeather.equals(actualWeather);
		
	}

	@Override
	public String getIcon() {
		return "icon-puzzle-piece";
	}

	@Override
	public String getRuleCategoryKey() {

		// Available category classes: BehaviourRuleCategory,
		// SessionAttributesRuleCategory, SocialRuleCategory and
		// UserAttributesRoleCategory

		return SessionAttributesRuleCategory.KEY;
	}

	@Override
	public String getSummary(RuleInstance ruleInstance, Locale locale) {
		return ruleInstance.getTypeSettings();
	}

	@Override
	public String processRule(
		PortletRequest portletRequest, PortletResponse portletResponse,
		String id, Map<String, String> values) {

		return values.get("weather");
	}

	@Override
	@Reference(
		target = "(osgi.web.symbolicname=weather)",
		unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

	@Override
	protected void populateContext(
		RuleInstance ruleInstance, Map<String, Object> context,
		Map<String, String> values) {

		String weather = "";

		if (!values.isEmpty()) {

			// Value from the request in case of an error

			weather = GetterUtil.getString(values.get("weather"));
		}
		else if (ruleInstance != null) {

			// Value from the stored configuration

			weather = ruleInstance.getTypeSettings();

		}

		context.put("weather", weather);
	}

	

}