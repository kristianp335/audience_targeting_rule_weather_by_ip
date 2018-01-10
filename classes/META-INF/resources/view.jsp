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

<aui:fieldset>
	<aui:select name="weather" value="<%= weather %>">
		<aui:option label="sunny" value="sunny" />
		<aui:option label="clouds" value="clouds" />
		<aui:option label="mist" value="mist" />
		<aui:option label="snow" value="snow" />
	</aui:select>
</aui:fieldset>