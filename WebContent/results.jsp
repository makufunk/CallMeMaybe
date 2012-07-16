<%@page import="cmm.data.UserFrontendObject"%>
<%@page import="java.util.*;"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link type="text/css" href="css/overcast/jquery-ui-1.8.21.custom.css" rel="Stylesheet" />	
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.21.custom.min.js"></script>
<script type="text/javascript">
$(function() {
	$( "#accordion" ).accordion();
});		
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">




<title>Insert title here</title>
<style>
#wait{
		font-size: large;
		line-height: 200px;
		margin-top: 50px;
		margin-bottom: 50px;
		width: 500px;
		height: 200px;
		margin-left: auto;
		margin-right: auto;
		background-color: #F7D358;
		text-align: center;
		-moz-border-radius: 15px;
		border-radius: 15px;
	}
	
body{
    background: url("images/background.jpeg") no-repeat center center fixed;
    -webkit-background-size: cover;
    -moz-background-size: cover;
    -o-background-size: cover;
    background-size: cover;
    margin:0;
    padding:0;
}
	</style>
</head>
<body>
<div id="wait">

Please wait while performing test</div>



<%
//code by marcus!!!!
List<UserFrontendObject> resultList=(List<UserFrontendObject>)request.getAttribute("resultList");
//dev code for mockup data

%>

<script>
var waitDiv= document.getElementById("wait");
waitDiv.style.display="none";

</script>
<center><img src="images/phone.png" alt="phone"/></center>

<img src="Legend.png" style="float:right;">

<div id="resContainer" style="width: 60%; margin-left: auto; margin-right: auto; margin-top: 50px;" >
	<div id="accordion">
		<%for(int i=0; i<resultList.size();i++){ %>
		<%UserFrontendObject res= resultList.get(i); %>
		<h3><a href="#"><%=res.getFirstName() %> <%= res.getLastName() %>
		
		<%if((res.getMatchLevel()>70) && res.getIsMHB()==false){%>
            <img alt="redDot" src="dotGreen.png">
        <%}%> 
        <%if(res.getMatchLevel()<=70&&res.getMatchLevel()>40&&res.getIsMHB()==false){%>
        <img alt="redDot" src="dotYellow.png">
        <%}%>
        <%if(res.getMatchLevel()<=40||res.getIsMHB()==true){%>
            <img alt="redDot" src="dotRed.png">
        <%}%>
		
		
		
		
		<%=res.getMatchLevel() %>% obsessed</a></h3>
		<div>
				<img alt="face" src="<%= res.getLinkToPic()%>">
                <p style="float: right; width: 80%; font-size: small;">
                    Common things you can talk about: 
                    <% List<String> likes= res.getCommomLikes();
                    if(likes.size()==0){%>
                        No common interests...
                    <%} 
                    else{
                        for(int j=0; j<likes.size(); j++){
                            if(!likes.get(j).equals(" "))%>
                        <%= likes.get(j) %>, 
                    <%} }%>
                    <br/>
                </p>
			
		</div>
		<% } %>	
	</div>

</div>

</body>
</html>