// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LogonManager.java

package com.gooddata.liveid;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import javax.xml.soap.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Referenced classes of package com.jp.windows.live:
//            LogonManagerException, SecurityToken

public class LogonManager
{


       public static String loadText(String aResourceName, Class aBaseClass)
       {
           String result = null;
           try
           {
               InputStream inputStream = aBaseClass.getResourceAsStream(aResourceName);
               result = loadText(inputStream);
           }
           catch(Exception ex)
           {
               System.err.println((new StringBuilder()).append("Can't load resource ").append(aResourceName).toString());
           }
           return result;
       }

       public static String loadText(InputStream anInputStream) throws IOException
       {
           StringBuffer result = new StringBuffer();
           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(anInputStream));
           do
           {
               String line = bufferedReader.readLine();
               if(line == null)
                   break;
               result.append(line);
           } while(true);
           bufferedReader.close();
           return result.toString();
       }


    public LogonManager()
    {
        fURL = "https://dev.login.live.com/wstlogin.srf";
        REQUEST_FORMAT = new MessageFormat(loadText("Login.xml", com.gooddata.liveid.LogonManager.class));
    }

    public LogonManager(String anURL)
    {
        fURL = "https://dev.login.live.com/wstlogin.srf";
        REQUEST_FORMAT = new MessageFormat(loadText("Login.xml", com.gooddata.liveid.LogonManager.class));
        fURL = anURL;
    }

    protected Date parseDate(String aDate)
    {
        Calendar result = null;
        String split[] = aDate.split("[TZ]");
        if(split.length == 2)
        {
            String date[] = split[0].split("-");
            if(date.length == 3)
            {
                result = Calendar.getInstance();
                result.set(1, Integer.parseInt(date[0]));
                result.set(2, Integer.parseInt(date[1]) - 1);
                result.set(5, Integer.parseInt(date[2]));
                String time[] = split[1].split(":");
                if(time.length == 3)
                {
                    result.set(11, Integer.parseInt(time[0]));
                    result.set(12, Integer.parseInt(time[1]));
                    result.set(13, Integer.parseInt(time[2]));
                }
                result.add(14, result.get(15));
            }
        }
        return result == null ? null : result.getTime();
    }

    protected void fill(SecurityToken aSecurityToken, Node aNode)
    {
        if("BinarySecurityToken".equalsIgnoreCase(aNode.getLocalName()))
        {
            NodeList children = aNode.getChildNodes();
            if(children.getLength() > 0)
                aSecurityToken.setBinarySecurityToken(children.item(0).getNodeValue());
        } else
        if("Created".equalsIgnoreCase(aNode.getLocalName()))
        {
            NodeList children = aNode.getChildNodes();
            if(children.getLength() > 0)
                aSecurityToken.setIssueDate(parseDate(children.item(0).getNodeValue()));
        } else
        if("Expires".equalsIgnoreCase(aNode.getLocalName()))
        {
            NodeList children = aNode.getChildNodes();
            if(children.getLength() > 0)
                aSecurityToken.setExpireDate(parseDate(children.item(0).getNodeValue()));
        } else
        if(aNode.hasChildNodes())
        {
            NodeList list = aNode.getChildNodes();
            for(int i = 0; i < list.getLength(); i++)
                fill(aSecurityToken, list.item(i));

        }
    }

    public SecurityToken logon(String anEndpointReference, String aUserName, String aPassword)
        throws LogonManagerException, IOException, SOAPException
    {
        return logon(anEndpointReference, aUserName, aPassword, "");
    }

    public SecurityToken logon(String anEndpointReference, String aUserName, String aPassword, String anApplicationID)
        throws LogonManagerException, SOAPException, IOException
    {
        SecurityToken result;
        SOAPMessage message;
        SOAPConnection conn = null;

        try {
            result = null;
            String request = REQUEST_FORMAT.format(((Object) (new Object[] {
                fURL, aUserName, aPassword, anEndpointReference
            })));
            message = MessageFactory.newInstance().createMessage(new MimeHeaders(), new ByteArrayInputStream(request.getBytes()));
            conn = SOAPConnectionFactory.newInstance().createConnection();
            SOAPMessage response = conn.call(message, fURL);
            SOAPBody body = response.getSOAPBody();
            if(body.hasFault())
                throw new LogonManagerException(body.getFault());
            result = new SecurityToken();
            result.setSOAPBody(body);
            for(Iterator it = body.getChildElements(); it.hasNext(); fill(result, (Node)it.next()));
            return result;
        }
        finally {
            if (conn!=null)
                conn.close();
        }
    }

    public static void main(String args[])
    {
        if(args.length > 1)
            try
            {
                SecurityToken securityToken = (new LogonManager()).logon("live.com", args[0], args[1]);
                System.out.println("Logon succeeded!");
                System.out.println("Passport Token: " + securityToken.getBinarySecurityToken());
                System.out.println("Issue Date:     " + securityToken.getIssueDate());
                System.out.println("Expire Date:    " + securityToken.getExpireDate());
            }
            catch(LogonManagerException lm)
            {
                System.out.println("Logon failed: " + lm.getDetailedMessage());
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        System.exit(0);
    }

    protected String fURL;
    protected MessageFormat REQUEST_FORMAT;
    protected static final String SOAP_NODE_SECURITY_TOKEN = "BinarySecurityToken";
    protected static final String SOAP_NODE_CREATED = "Created";
    protected static final String SOAP_NODE_EXPIRES = "Expires";
}
