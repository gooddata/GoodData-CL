IF EXISTS(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'gdc_account') DROP TABLE gdc_account;
go
CREATE TABLE gdc_account (id VARBINARY(32), sfdcid VARCHAR(255), url VARCHAR(255), name VARCHAR(255), industry VARCHAR(100), type VARCHAR(100))
go
INSERT INTO gdc_account(sfdcid, url, name, industry, type) SELECT CAST(accountid AS VARCHAR(255)) AS sfdcid, 'http://www.dynamics.com/' + CAST(accountid AS VARCHAR(50)) AS url, name, (SELECT value FROM stringmap WHERE attributename='industrycode' AND objecttypecode=1 AND attributevalue = industrycode) AS industry, (SELECT value FROM stringmap WHERE attributename='customertypecode' AND objecttypecode=1 AND attributevalue = customertypecode) AS type FROM account
go
UPDATE gdc_account SET id = HashBytes('MD5',sfdcid+'|'+name+'|'+industry+'|'+type)
go
IF EXISTS(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'gdc_user') DROP TABLE gdc_user;
go
CREATE TABLE gdc_user (id VARBINARY(32), sfdcid VARCHAR(255), name VARCHAR(255), url VARCHAR(255), email VARCHAR(255), division VARCHAR(100), department VARCHAR(100))
go
INSERT INTO gdc_user(sfdcid, name, url, email, division, department) SELECT systemuserid AS sfdcid, fullname AS name, 'http://www.dynamics.com/' + CAST(systemuserid AS VARCHAR(50)) AS url, internalemailaddress AS name, (SELECT name FROM organization WHERE organization.organizationid = systemuser.organizationid) AS division, (SELECT name FROM businessunit WHERE businessunit.businessunitid = systemuser.businessunitid) AS department FROM systemuser
go
UPDATE gdc_user SET id = HashBytes('MD5',sfdcid+'|'+name+'|'+division+'|'+department)
go
IF EXISTS(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'gdc_opportunity') DROP TABLE gdc_opportunity;
go
CREATE TABLE gdc_opportunity (id VARBINARY(32), sfdcid VARCHAR(255), status VARCHAR(20), name VARCHAR(255), url VARCHAR(255),  leadsource VARCHAR(100), type VARCHAR(100), iswon VARCHAR(10), isclosed VARCHAR(10))
go
INSERT INTO gdc_opportunity(sfdcid, status, name, url, leadsource, type, iswon, isclosed) SELECT opportunityid AS sfdcid, (SELECT value FROM stringmap WHERE attributename='statecode' AND objecttypecode=3 AND attributevalue = statecode) AS status, name, 'http://www.dynamics.com/' + CAST(opportunityid AS VARCHAR(50)) AS url, ISNULL((SELECT name FROM campaign WHERE campaign.campaignid = opportunity.campaignid),'N/A') AS leadsource, (SELECT value FROM stringmap WHERE attributename='opportunityratingcode'AND objecttypecode = 3 AND attributevalue = opportunityratingcode) AS type, (CASE statecode WHEN 1 THEN 'true' ELSE 'false' END) AS IsWon, (CASE statecode WHEN 0 THEN 'false' ELSE 'true' END) AS IsClosed  FROM opportunity
go
UPDATE gdc_opportunity SET id = HashBytes('MD5',sfdcid+'|'+name+'|'+status+'|'+leadsource+'|'+type+'|'+iswon++'|'+isclosed)
go
IF EXISTS(SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'gdc_opportunity_snapshot') DROP TABLE gdc_opportunity_snapshot;
go
CREATE TABLE gdc_opportunity_snapshot (id VARBINARY(32), snapshot_id INT, probability INT, createddate VARCHAR(20), closeddate VARCHAR(20), snapshotdate VARCHAR(20), expectedrevenue DECIMAL(15,4), opportunity_id VARBINARY(32), dayssincelastactivity INT, amount DECIMAL(15,4), account_id VARBINARY(32), user_id VARBINARY(32), stagename VARCHAR(100), stageorder INT, daystoclose INT)
go
INSERT INTO gdc_opportunity_snapshot(snapshot_id, probability, createddate, closeddate, snapshotdate, expectedrevenue, opportunity_id, dayssincelastactivity, amount, account_id, user_id, stagename, stageorder, daystoclose) SELECT DATEDIFF(DAY, 1900-01-01, CURRENT_TIMESTAMP) AS snapshot_id, closeprobability AS probability, CONVERT(VARCHAR(10),createdon, 111) AS created_date, (CASE statecode WHEN 0 THEN CONVERT(VARCHAR(10),estimatedclosedate, 111) ELSE CONVERT(VARCHAR(10),actualclosedate, 111) END) AS closedate, CONVERT(VARCHAR(10),CURRENT_TIMESTAMP, 111) AS snapshotdate, estimatedvalue AS expectedrevenue, (SELECT id FROM gdc_opportunity WHERE sfdcid = opportunityid) AS opportunityid, DATEDIFF(DAY,modifiedon,CURRENT_TIMESTAMP) AS dayssincelastactivity, actualvalue AS amount, (SELECT id FROM gdc_account WHERE sfdcid = customerid) AS accountid, (SELECT id FROM gdc_user WHERE sfdcid = ownerid) AS userid, (SELECT value FROM stringmap WHERE attributename='salesstagecode' AND objecttypecode=3 AND attributevalue = salesstagecode) AS stagename, (SELECT displayorder FROM stringmap WHERE attributename='salesstagecode' AND objecttypecode=3 AND attributevalue = salesstagecode) AS stageorder, (CASE statecode WHEN 0 THEN DATEDIFF(DAY,CURRENT_TIMESTAMP, estimatedclosedate) ELSE 0 END) AS daystoclose FROM opportunity
go
UPDATE gdc_opportunity_snapshot SET id = HashBytes('MD5',createddate+'|'+closeddate+'|'+snapshotdate+'|'+sys.fn_varbintohexstr(opportunity_id)+'|'+sys.fn_varbintohexstr(account_id)+'|'+sys.fn_varbintohexstr(user_id)+'|'+stagename)
go

