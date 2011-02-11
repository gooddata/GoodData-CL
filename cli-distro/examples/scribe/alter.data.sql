UPDATE opportunity SET actualclosedate = estimatedclosedate
go
UPDATE opportunity SET estimatedclosedate = DATEADD(DAY,637,estimatedclosedate)
go
UPDATE opportunity SET createdon = DATEADD(DAY,637,createdon)
go
