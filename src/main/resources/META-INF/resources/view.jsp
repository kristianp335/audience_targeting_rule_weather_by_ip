<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>

<%@ page import="java.util.Map" %>

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
Map<String, Object> context = (Map<String, Object>)request.getAttribute("context");

String weather = (String)context.get("weather");
%>
<p>This is a <b>custom</b> Audience Targetting rule that gets the user's IP and then caluculates the user's logitude and latitude, which in turn calls a weather API...
<aui:fieldset>
	<aui:select name="weather" value="<%= weather %>">
		<aui:option label="Sunny" value="sunny" />
		<aui:option label="Cloudy" value="cloudy" />
		<aui:option label="Misty" value="misty" />
		<aui:option label="Snowy" value="snowy" />
		<aui:option label="Rainy" value="rainy" />
		<aui:option label="Windy" value="windy" />
	</aui:select>
</aui:fieldset>