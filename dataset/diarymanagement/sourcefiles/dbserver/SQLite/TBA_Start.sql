.echo off
-- create tables
.read TBA_Create.sql

-- drop all triggers
.echo on 
-- ////////// Errors are exected here //////////
.echo off
.read TBA_DROP_Trigger.sql
.echo on 
-- /////////////////////////////////////////////
.echo off

-- load Trigger
.read TBA_Trigger.sql


-- Run Test on Constraints
.echo on
.read Test.sql

