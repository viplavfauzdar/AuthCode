# SSO Implicit & Client Credentials flow sample application

## Synopsis

This is a sample application to demonstrate using implicit & client credentials grant type to access a secure resource using pivotal SSO (site minder) to authenticate and obtain a token 


##  SSO config

Create SSO service in pivotal of the type **javascript single page app** aka **implicit** grant type. 

If you want the API to be accessed by other applications then add another SSO service for **service to service app** which is grant type **client credentials**. Make sure to add openid and uaa.resource to scopes.