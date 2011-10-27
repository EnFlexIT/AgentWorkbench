
rem set LEAP=..\..\..\..\..\add-ons\leap\j2se\lib\JadeLeap.jar
set LEAP=..\..\..\..\..\add-ons\leap\j2se\classes

java -classpath %LEAP%  jade.Boot -gui -nomtp -icps jade.imtp.leap.JICP.JICPSPeer(5500)

pause


