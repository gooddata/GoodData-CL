// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SecurityToken.java

package com.gooddata.liveid;

import java.util.Date;
import javax.xml.soap.SOAPBody;

public class SecurityToken
{

    SecurityToken()
    {
    }

    SecurityToken(String aBinarySecurityToken, Date anIssueDate, Date anExpireDate)
    {
        fBinarySecurityToken = aBinarySecurityToken;
        fIssueDate = anIssueDate;
        fExpireDate = anExpireDate;
    }

    public boolean isValid()
    {
        return fBinarySecurityToken != null;
    }

    public String getBinarySecurityToken()
    {
        return fBinarySecurityToken;
    }

    public Date getIssueDate()
    {
        return fIssueDate;
    }

    public Date getExpireDate()
    {
        return fExpireDate;
    }

    public SOAPBody getSOAPBody()
    {
        return fSOAPBody;
    }

    void setBinarySecurityToken(String aBinarySecurityToken)
    {
        fBinarySecurityToken = aBinarySecurityToken;
    }

    void setExpireDate(Date anExpireDate)
    {
        fExpireDate = anExpireDate;
    }

    void setIssueDate(Date anIssueDate)
    {
        fIssueDate = anIssueDate;
    }

    void setSOAPBody(SOAPBody aSOAPBody)
    {
        fSOAPBody = aSOAPBody;
    }

    protected String fBinarySecurityToken;
    protected Date fIssueDate;
    protected Date fExpireDate;
    protected SOAPBody fSOAPBody;
}
