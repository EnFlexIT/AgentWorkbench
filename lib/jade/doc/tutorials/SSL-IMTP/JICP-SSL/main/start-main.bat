
rem set LEAP=..\..\..\..\..\add-ons\leap\j2se\lib\JadeLeap.jar
set LEAP=..\..\..\..\..\add-ons\leap\j2se\classes

java -Djavax.net.ssl.keyStore=keystore0 -Djavax.net.ssl.keyStorePassword=mysecretpassword -Djavax.net.ssl.trustStore=mytruststore -classpath %LEAP%  jade.Boot -gui -nomtp -icps jade.imtp.leap.JICP.JICPSPeer(5500)

pause


