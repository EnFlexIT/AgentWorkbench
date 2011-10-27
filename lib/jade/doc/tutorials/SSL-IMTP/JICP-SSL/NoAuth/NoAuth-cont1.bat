
rem set LEAP=..\..\..\..\..\add-ons\leap\j2se\lib\JadeLeap.jar
set LEAP=..\..\..\..\..\add-ons\leap\j2se\classes

java  -cp %LEAP%  jade.Boot -container -nomtp -port 5500 -icps jade.imtp.leap.JICP.JICPSPeer

pause


