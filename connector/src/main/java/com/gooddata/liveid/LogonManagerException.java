// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LogonManagerException.java

package com.gooddata.liveid;

import java.util.Iterator;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LogonManagerException extends SOAPException
{

    LogonManagerException(SOAPFault aFault)
    {
        super(aFault.getFaultString());
        try
        {
            for(Iterator it = aFault.getChildElements(); it.hasNext(); fill((Node)it.next()));
        }
        catch(Exception ex) { }
    }

    LogonManagerException(String aMessage)
    {
        super(aMessage);
    }

    public SOAPFault getSOAPFault()
    {
        return fSOAPFault;
    }

    public String getErrorCode()
    {
        return fErrorCode;
    }

    public String getErrorText()
    {
        return fErrorText;
    }

    public String getDetailedMessage()
    {
        return getMessage() + "." + " Code: " + (fErrorCode == null ? "N/A" : fErrorCode) + ". Error: " + (fErrorText == null ? "N/A" : fErrorText);
    }

    protected void fill(Node aNode)
    {
        if(aNode.getParentNode().getLocalName().equalsIgnoreCase("internalerror"))
        {
            if("code".equalsIgnoreCase(aNode.getLocalName()))
            {
                NodeList children = aNode.getChildNodes();
                if(children.getLength() > 0)
                    fErrorCode = children.item(0).getNodeValue();
            } else
            if("text".equalsIgnoreCase(aNode.getLocalName()))
            {
                NodeList children = aNode.getChildNodes();
                if(children.getLength() > 0)
                    fErrorText = children.item(0).getNodeValue();
            }
        } else
        if(aNode.hasChildNodes())
        {
            NodeList list = aNode.getChildNodes();
            for(int i = 0; i < list.getLength(); i++)
                fill(list.item(i));

        }
    }

    protected SOAPFault fSOAPFault;
    protected String fErrorCode;
    protected String fErrorText;
    protected static final String NODE_INTERNAL_ERROR = "internalerror";
    protected static final String NODE_CODE = "code";
    protected static final String NODE_TEXT = "text";
}
