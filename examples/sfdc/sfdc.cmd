# LOADS A SALESFORCE ACCOUNT DATA

# CREATE A NEW PROJECT
CreateProject(name="SFDC");

# LOAD THE ACCOUNT
LoadSfdc(configFile="examples/sfdc/account.xml",username="mh@gooddata.blank",password="8uEx6ddzkrP0xxkXRmW7w4KI8Ir1etTNFp",query="SELECT Id, Name FROM Account");

# GENERATE THE ACCOUNT MAQL
GenerateMaql(maqlFile="examples/sfdc/account.maql");

# EXECUTE THE ACCOUNT MAQL
ExecuteMaql(maqlFile="examples/sfdc/account.maql");

# TRANSFER THE ACCOUNT DATA
TransferLastSnapshot(incremental="true");

# GENERATE THE OPPORTUNITY CONFIG
#GenerateSfdcConfig(name="Opportunity", configFile="examples/sfdc/opportunity.xml",username="mh@gooddata.blank",password="8uEx6ddzkrP0xxkXRmW7w4KI8Ir1etTNFp",query="SELECT Id, AccountId, IsWon, IsClosed, CloseDate, StageName, CreatedDate, ExpectedRevenue, Amount FROM Opportunity");

# LOAD THE OPPORTUNITY
LoadSfdc(configFile="examples/sfdc/opportunity.xml",username="mh@gooddata.blank",password="8uEx6ddzkrP0xxkXRmW7w4KI8Ir1etTNFp",query="SELECT Id, AccountId, IsWon, IsClosed, CloseDate, StageName, CreatedDate, ExpectedRevenue, Amount FROM Opportunity");

# GENERATE THE OPPORTUNITY MAQL
GenerateMaql(maqlFile="examples/sfdc/opportunity.maql");

# EXECUTE THE OPPORTUNITY MAQL
ExecuteMaql(maqlFile="examples/sfdc/opportunity.maql");

# TRANSFER THE OPPORTUNITY DATA
TransferLastSnapshot(incremental="true");